package org.example.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.example.beans.Imageset;

@HippoEssentialsGenerated(internalName = "gogreen:bannerdocument")
@Node(jcrType = "gogreen:bannerdocument")
public class Banner extends BaseDocument {
	@HippoEssentialsGenerated(internalName = "gogreen:title")
	public String getTitle() {
		return getProperty("gogreen:title");
	}

	@HippoEssentialsGenerated(internalName = "gogreen:content")
	public HippoHtml getContent() {
		return getHippoHtml("gogreen:content");
	}

	@HippoEssentialsGenerated(internalName = "gogreen:link")
	public HippoBean getLink() {
		return getLinkedBean("gogreen:link", HippoBean.class);
	}

	@HippoEssentialsGenerated(internalName = "gogreen:image")
	public Imageset getImage() {
		return getLinkedBean("gogreen:image", Imageset.class);
	}
}
