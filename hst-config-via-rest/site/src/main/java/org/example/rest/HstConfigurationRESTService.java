package org.example.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.jaxrs.services.AbstractResource;

@Path("/hst/")
public class HstConfigurationRESTService extends AbstractResource {

   @GET
   @Path("/menus/")
   public HstMenusRepresentation getHstMenusConfiguration(
           @Context HttpServletRequest servletRequest,
           @Context HttpServletResponse servletResponse) {

       return new HstMenusRepresentation(
 RequestContextProvider.get().getMount("main-site").getHstSite().getSiteMenusConfiguration());
   }

   @GET
   @Path("/menus/{menuName}/")
   public HstMenuRepresentation getHstMenusConfiguration(
           @Context HttpServletRequest servletRequest,
           @Context HttpServletResponse servletResponse,
           @PathParam("menuName") String menuName) {

       return new HstMenuRepresentation(
               RequestContextProvider.get().getMount("main-site").getHstSite().getSiteMenusConfiguration().getSiteMenuConfiguration(menuName));
   }
}