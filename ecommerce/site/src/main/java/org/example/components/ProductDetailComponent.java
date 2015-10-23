package org.example.components;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.client.SphereAsyncHttpClientFactory;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.products.Price;
import io.sphere.sdk.products.ProductProjectionType;
import io.sphere.sdk.products.queries.ProductQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import org.example.beans.Product;
import org.hippoecm.hst.component.support.forms.FormField;
import org.hippoecm.hst.component.support.forms.FormMap;
import org.hippoecm.hst.component.support.forms.FormUtils;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class ProductDetailComponent extends EssentialsContentComponent {

    public static final Logger log = LoggerFactory.getLogger(ProductDetailComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        // Ensure the component loads the content and makes it available to the template
        super.doBeforeRender(request, response);

        // Load the product from the request attribute "document"
        Product product = (Product) request.getAttribute("document");


        final SphereClientFactory factory = SphereClientFactory.of(SphereAsyncHttpClientFactory::create);
        final SphereClient client = factory.createClient(
                "hippo-gogreen", //replace with your project key
                "2ygXwjNzZyd4yvaQe7uvF7gl", //replace with your client id
                "MnUl-O5cr0_OnGIHhgG-MXQdk922Pbj7"); //replace with your client secret

        final ProductQuery query = ProductQuery.of();

        // Get the SKU from the product document in Hippo CMS
        String sku = product.getProductID();
        if (sku != null) {
            // Use the current, not the preview version
            ProductQuery q = query.bySku(sku, ProductProjectionType.CURRENT);

            try {
                PagedQueryResult<io.sphere.sdk.products.Product> pagedQueryResult = client.execute(q).toCompletableFuture().get();
                // Price is saved in masterdata in the first price object
                List<Price> prices = pagedQueryResult.getResults().get(0).getMasterData().getCurrent().getMasterVariant().getPrices();

                // We will use the first one
                Price price = prices.get(0);
                request.setAttribute("commerceprice", price);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Check if there is a form available
        FormMap map = new FormMap();
        FormUtils.populate(request, map);

        // Get the result
        String result = map.getMessage().get("result");
        if (result != null) {
            request.setAttribute("result", result);
        }

    }

    @Override
    public void doAction(HstRequest request, HstResponse response) throws HstComponentException {
        super.doAction(request, response);

        // Load the data from the form
        FormMap map = new FormMap(request, new String[]{"sku"});

        // Load the SKU from the field
        String sku = map.getField("sku").getValue();

        final SphereClientFactory factory = SphereClientFactory.of(SphereAsyncHttpClientFactory::create);
        final SphereClient client = factory.createClient(
                "hippo-gogreen", //replace with your project key
                "2ygXwjNzZyd4yvaQe7uvF7gl", //replace with your client id
                "MnUl-O5cr0_OnGIHhgG-MXQdk922Pbj7"); //replace with your client secret

        // Set currency and country
        CurrencyUnit eur = Monetary.getCurrency("EUR");
        CountryCode nl = CountryCode.getByCode("NL");

        final CartDraft cartDraft = CartDraft.of(eur).withCountry(nl);

        // Right now, we will create a cart (even if it exists)
        CartCreateCommand cartCreateCommand = CartCreateCommand.of(cartDraft);

        // Execute creation
        CompletionStage<Cart> cartCompletionStage = client.execute(cartCreateCommand);

        // Set the right values
        Cart cart = null;
        try {
            cart = cartCompletionStage.toCompletableFuture().get();
            map.addMessage("result", "Product added to the cart!");
        } catch (InterruptedException e) {
            map.addMessage("result", "Error occured: " + e.getMessage());
            e.printStackTrace();
        } catch (ExecutionException e) {
            map.addMessage("result", "Error occured: " + e.getMessage());
            e.printStackTrace();
        }

        // Persist the result to the component
        FormUtils.persistFormMap(request, response, map, null);
    }
}
