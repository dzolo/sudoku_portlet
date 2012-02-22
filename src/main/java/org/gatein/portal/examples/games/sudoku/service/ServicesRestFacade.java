/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : Game.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.service;

import java.net.URI;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.gatein.portal.examples.games.sudoku.controller.ServicesController;
import org.gatein.portal.examples.games.sudoku.entity.Service;

/**
 * Services REST Facade Class.
 *
 * @author Onřej Fibich
 */
@Path("service")
public class ServicesRestFacade
{
    
    /**
     * An instance of the entity manager factory
     */
    private ServicesController servicesController;

    /**
     * Defines a constructor of the REST facede.
     */
    public ServicesRestFacade()
    {
        servicesController = new ServicesController();
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response create(Service entity)
    {
        try
        {
            servicesController.create(entity);
            return Response.created(URI.create(entity.getId().toString())).build();
        }
        catch (Exception ex)
        {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    public Response edit(Service entity)
    {
        try {
            servicesController.edit(entity);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id)
    {
        try {
            servicesController.destroy(id);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Service find(@PathParam("id") Integer id)
    {
        return servicesController.findService(id);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<Service> findAll()
    {
        return servicesController.findServiceEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({"application/xml", "application/json"})
    public List<Service> findRange(@PathParam("max") Integer max,
                                   @PathParam("first") Integer first)
    {
        return servicesController.findServiceEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String count()
    {
        return String.valueOf(servicesController.getServiceCount());
    }
    
}
