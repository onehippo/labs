package org.example.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import java.util.List;
import org.example.beans.Imageset;

@HippoEssentialsGenerated(internalName = "gogreen:product")
@Node(jcrType = "gogreen:product")
public class Product extends BaseDocument {
	@HippoEssentialsGenerated(internalName = "gogreen:title")
	public String getTitle() {
		return getProperty("gogreen:title");
	}

	@HippoEssentialsGenerated(internalName = "gogreen:price")
	public Double getPrice() {
		return getProperty("gogreen:price");
	}

	@HippoEssentialsGenerated(internalName = "gogreen:introduction")
	public String getIntroduction() {
		return getProperty("gogreen:introduction");
	}

	@HippoEssentialsGenerated(internalName = "gogreen:description")
	public HippoHtml getDescription() {
		return getHippoHtml("gogreen:description");
	}

	@HippoEssentialsGenerated(internalName = "gogreen:images")
	public List<Imageset> getImages() {
		return getLinkedBeans("gogreen:images", Imageset.class);
	}

	@HippoEssentialsGenerated(internalName = "gogreen:productID")
	public String getProductID() {
		return getProperty("gogreen:productID");
	}
}
