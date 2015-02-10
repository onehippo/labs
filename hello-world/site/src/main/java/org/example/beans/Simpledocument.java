package org.example.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "myhippoproject:simpledocument")
@Node(jcrType = "myhippoproject:simpledocument")
public class Simpledocument extends BaseDocument {
	@HippoEssentialsGenerated(internalName = "myhippoproject:title")
	public String getTitle() {
		return getProperty("myhippoproject:title");
	}

	@HippoEssentialsGenerated(internalName = "myhippoproject:content")
	public HippoHtml getContent() {
		return getHippoHtml("myhippoproject:content");
	}
}
