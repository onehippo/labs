package org.example.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.hippoecm.hst.configuration.sitemenu.HstSiteMenuConfiguration;
import org.hippoecm.hst.configuration.sitemenu.HstSiteMenuItemConfiguration;

@XmlRootElement(name = "sitemenu")
public class HstMenuRepresentation {

   @XmlElement
   private String name;

   @XmlElementWrapper(name = "menuitems")
   @XmlElements(@XmlElement(name = "menuitem"))
   private List<HstMenuItemRepresentation> menuItems = new ArrayList<>();

   public HstMenuRepresentation(HstSiteMenuConfiguration hstSiteMenuConfiguration) {
       this.name = hstSiteMenuConfiguration.getName();
       for(HstSiteMenuItemConfiguration siteMenuItemConfiguration : hstSiteMenuConfiguration.getSiteMenuConfigurationItems()) {
           menuItems.add(new HstMenuItemRepresentation(siteMenuItemConfiguration));
       }
   }
}