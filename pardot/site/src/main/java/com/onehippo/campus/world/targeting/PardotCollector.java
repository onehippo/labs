package com.onehippo.campus.world.targeting;

import com.onehippo.cms7.targeting.collectors.AbstractCollector;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class PardotCollector extends AbstractCollector<PardotData, PardotData> {

    public static final String LOGIN_ENDPOINT = "https://pi.pardot.com/api/login/version/3";
    public static final String VISITOR_ENDPOINT = "https://pi.pardot.com/api/visitor/version/3/do/read";
    public static final String PROSPECT_ENDPOINT = "https://pi.pardot.com/api/prospect/version/3/do/read";

    private final MultiThreadedHttpConnectionManager connectionManager;
    private final HttpClient client;
    private final String cookieName;
    private final String userKey;
    private final String email;
    private final String password;

    private volatile String apiKey = null;

    public PardotCollector(String id, Node node) throws RepositoryException {
        super(id);
        this.assertEqualIds("pardot", id, PardotCollector.class.getSimpleName(), node);

        long accountId = node.getProperty("account").getLong();
        userKey = node.getProperty("user_key").getString();
        email = node.getProperty("email").getString();
        password = node.getProperty("password").getString();

        connectionManager = new MultiThreadedHttpConnectionManager();
        client = new HttpClient(connectionManager);

        cookieName = "visitor_id" + accountId;
    }

    @Override
    public PardotData getTargetingRequestData(HttpServletRequest request) {
        String referrer = request.getHeader("Referer");
        if (referrer == null || !referrer.contains("go.onehippo.com")) {
            return null;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return getPardotData(cookie.getValue());
            }
        }
        return null;
    }

    private PardotData getPardotData(final String visitorId) {
        try {
            return executeWithRetry(new Callable<PardotData>() {

                @Override
                public PardotData call() throws Exception {
                    String prospectId = getProspectId(visitorId);
                    if (prospectId != null) {
                        return getProspectData(prospectId);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private PardotData getProspectData(final String prospectId) throws IOException, PardotException {
        GetMethod getProspect = new GetMethod();
        getProspect.setURI(new URI(PROSPECT_ENDPOINT, true));
        getProspect.setQueryString(new NameValuePair[]{
            new NameValuePair("user_key", userKey),
            new NameValuePair("api_key", apiKey),
            new NameValuePair("id", prospectId),
        });

        Element root = execute(getProspect);
        Element prospectEl = root.getChild("prospect");
        String firstName = prospectEl.getChildText("first_name");
        String lastName = prospectEl.getChildText("last_name");
        List<String> lists = new ArrayList<>();
        Element listsEl = prospectEl.getChild("lists");
        for (Element subscriptionEl : listsEl.getChildren("list_subscription")) {
            Element listEl = subscriptionEl.getChild("list");
            lists.add(listEl.getChildText("name"));
        }
        return new PardotData(getId(), prospectId, firstName, lastName, lists);
    }

    private <T> T executeWithRetry(Callable<T> callable) throws Exception {
        boolean newKey = false;
        if (apiKey == null) {
            apiKey = newAPIKey();
            newKey = true;
        }
        try {
            return callable.call();
        } catch (InvalidKeyException ike) {
            if (!newKey) {
                apiKey = newAPIKey();
                return callable.call();
            }
            throw ike;
        }
    }

    private String newAPIKey() throws IOException, PardotException {
        PostMethod login = new PostMethod();
        login.setURI(new URI(LOGIN_ENDPOINT, true));
        login.setParameter("email", email);
        login.setParameter("password", password);
        login.setParameter("user_key", userKey);

        Element root = execute(login);
        return root.getChildText("api_key");
    }

    private String getProspectId(String visitorId) throws IOException, PardotException {
        GetMethod getVisitor = new GetMethod();
        getVisitor.setURI(new URI(VISITOR_ENDPOINT, true));
        getVisitor.setQueryString(new NameValuePair[]{
            new NameValuePair("user_key", userKey),
            new NameValuePair("api_key", apiKey),
            new NameValuePair("id", visitorId),
        });

        Element root = execute(getVisitor);
        Element visitor = root.getChild("visitor");
        return visitor.getChild("prospect").getChildText("id");
    }

    private Element execute(HttpMethod method) throws IOException, PardotException {
        try {
            int status = client.executeMethod(method);
            if (200 <= status && status < 300) {
                SAXBuilder builder = new SAXBuilder();
                try {
                    Document visitorData = builder.build(method.getResponseBodyAsStream());
                    Element root = visitorData.getRootElement();
                    checkReponseIsOk(root);
                    return root;
                } catch (JDOMException jde) {
                    throw new IOException(jde);
                }
            } else {
                throw new IOException("Method execution failed: " + HttpStatus.getStatusText(status));
            }
        } finally {
            method.releaseConnection();
        }
    }

    private void checkReponseIsOk(Element root) throws PardotException {
        if ("ok".equals(root.getAttributeValue("stat"))) {
            return;
        }
        Element err = root.getChild("err");
        String code = err.getAttributeValue("code");
        if ("1".equals(code)) {
            throw new InvalidKeyException(err.getText());
        } else {
            throw new PardotException(err.getText());
        }
    }

    @Override
    public boolean shouldUpdate(boolean newVisitor, boolean newVisit, PardotData pardotAggregate) {
        return true;
    }

    @Override
    public PardotData updateTargetingData(PardotData aggregate, PardotData data) {
        return data != null ? data : aggregate;
    }

    @Override
    public void dispose() {
        connectionManager.shutdown();
    }
}

class PardotException extends Exception {
    public PardotException(String message) {
        super(message);
    }
}

class InvalidKeyException extends PardotException {
    public InvalidKeyException(String message) {
        super(message);
    }
}


