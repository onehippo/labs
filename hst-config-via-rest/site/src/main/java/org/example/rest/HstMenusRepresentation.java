package org.example.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.hippoecm.hst.configuration.sitemenu.HstSiteMenuConfiguration;
import org.hippoecm.hst.configuration.sitemenu.HstSiteMenusConfiguration;

@XmlRootElement(name = "hstMenus")
public class HstMenusRepresentation {

   @XmlElementWrapper(name = "sitemenus")
   @XmlElements(@XmlElement(name = "sitemenu"))
   private List<HstMenuRepresentation> siteMenus = new ArrayList<>();

   public HstMenusRepresentation(HstSiteMenusConfiguration hstMenusConfiguration) {
       for(HstSiteMenuConfiguration hstSiteMenuConfiguration : hstMenusConfiguration.getSiteMenuConfigurations().values()){
           siteMenus.add(new HstMenuRepresentation(hstSiteMenuConfiguration));
       }
   }
}