package org.example.components;

import io.sphere.sdk.client.SphereAsyncHttpClientFactory;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.products.ProductData;
import io.sphere.sdk.products.ProductProjectionType;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.queries.ProductQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ExternalProductDetailComponent extends BaseHstComponent {

    public static final Logger log = LoggerFactory.getLogger(ExternalProductDetailComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        // Ensure the component loads the content and makes it available to the template
        super.doBeforeRender(request, response);

        // We will load the information we want from the URL, which is the localParameter($1)
        String identifier = request.getRequestContext().getResolvedSiteMapItem().getLocalParameter("1");
        String productId = identifier;
        if (identifier.contains("/")) {
            String[] urlParts = identifier.split("/");
            productId = urlParts[urlParts.length-1];
        }

        final SphereClientFactory factory = SphereClientFactory.of(SphereAsyncHttpClientFactory::create);
        final SphereClient client = factory.createClient(
                "hippo-gogreen", //replace with your project key
                "2ygXwjNzZyd4yvaQe7uvF7gl", //replace with your client id
                "MnUl-O5cr0_OnGIHhgG-MXQdk922Pbj7"); //replace with your client secret

        final ProductQuery query = ProductQuery.of();

        if (!productId.equals("")) {
            // Use the current, not the preview version
            ProductQuery q = query.bySku(productId, ProductProjectionType.CURRENT);

            try {
                PagedQueryResult<io.sphere.sdk.products.Product> pagedQueryResult = client.execute(q).toCompletableFuture().get();
                ProductData product = pagedQueryResult.getResults().get(0).getMasterData().getCurrent();


                // We will process the data to return to the template
                request.setAttribute("name", product.getName().get(Locale.ENGLISH));
                request.setAttribute("description", product.getDescription().get(Locale.ENGLISH));
                request.setAttribute("commerceprice", product.getMasterVariant().getPrices().get(0));
                request.setAttribute("images", product.getMasterVariant().getImages());

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
