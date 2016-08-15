package org.example.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.hippoecm.hst.configuration.sitemenu.HstSiteMenuItemConfiguration;

@XmlRootElement(name = "menuitem")
public class HstMenuItemRepresentation {

   @XmlElement
   private String name;

   @XmlElement
   private String path;

   @XmlElement
   private String externalLink;

   @XmlElementWrapper(name = "parameters")
   @XmlElements(@XmlElement(name = "parameter"))
   private Map<String, String> parameters;

   @XmlElementWrapper(name = "menuitems")
   @XmlElements(@XmlElement(name = "menuitem"))
   private List<HstMenuItemRepresentation> menuItems = new ArrayList<>();

   public HstMenuItemRepresentation(HstSiteMenuItemConfiguration hstSiteMenuItemConfiguration) {
       this.name = hstSiteMenuItemConfiguration.getName();
       this.path = hstSiteMenuItemConfiguration.getSiteMapItemPath();
       this.externalLink = hstSiteMenuItemConfiguration.getExternalLink();
       this.parameters = hstSiteMenuItemConfiguration.getParameters();

       for (HstSiteMenuItemConfiguration child : hstSiteMenuItemConfiguration.getChildItemConfigurations()) {
           menuItems.add(new HstMenuItemRepresentation(child));
       }
   }
}