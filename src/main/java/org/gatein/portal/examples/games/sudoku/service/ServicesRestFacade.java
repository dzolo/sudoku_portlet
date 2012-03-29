/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : Game.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.service;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.gatein.portal.examples.games.sudoku.controller.GamesController;
import org.gatein.portal.examples.games.sudoku.controller.ServicesController;
import org.gatein.portal.examples.games.sudoku.entity.Game;
import org.gatein.portal.examples.games.sudoku.entity.Service;
import org.gatein.portal.examples.games.sudoku.provider.publisher.PeriodicalServiceFactory;
import org.gatein.portal.examples.games.sudoku.provider.publisher.driver.PeriodicalServiceDriver;

/**
 * Services REST Facade Class.
 *
 * @author Ondřej Fibich
 */
@Path("service")
public class ServicesRestFacade
{
    /**
     * A logger
     */
    private static final Logger logger = Logger.getLogger(ServicesRestFacade.class.getName());
    
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
            logger.log(Level.WARNING, null, ex);
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    public Response edit(Service entity)
    {
        try
        {
            servicesController.edit(entity);
            return Response.ok().build();
        }
        catch (Exception ex)
        {
            logger.log(Level.WARNING, null, ex);
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Response find(@PathParam("id") Integer id)
    {
        Service service = servicesController.findService(id);
        
        if (service != null)
        {    
            return Response.ok(service).build(); 
        }
        else
        {
            return Response.status(Response.Status.NOT_FOUND).build(); 
        }
    }
    
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Service> findAll()
    {
        return servicesController.findServiceEntities();
    }

    @GET
    @Path("games/{uid}")
    @Produces({"application/xml", "application/json"})
    public List<Game> findAllGames(@PathParam("uid") String uid)
    {
        List<Service> services = servicesController.findServiceEntities();
        GamesController gameController = new GamesController();
        Game game, lastGame;
        
        try
        {
            // search for new games
            for (Service service : services)
            {
                if (service.isEnabled())
                {
                    lastGame = gameController.findLastGameOf(service);
                    game = getRemoteGame(service, lastGame);

                    if (game != null)
                    {
                        gameController.create(game);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            logger.log(Level.WARNING, null, ex);
        }
        finally
        {
            return gameController.findServicedGameEntities(uid);
        }
    }
    
    /**
     * Gets a remote game from a service
     * 
     * @param service       A remote service
     * @param lastGame      A previous game of service
     * @return 
     */
    private Game getRemoteGame(Service service, Game lastGame)
    {
        PeriodicalServiceFactory fac = new PeriodicalServiceFactory();
        PeriodicalServiceDriver d = fac.newDriverFor(service);
        
        if (d == null)
        {
            return null;
        }
        
        try
        {
            if (d.isExpirated(lastGame))
            {
                return d.getGame(lastGame);
            }
        }
        catch (IOException ex)
        {
            logger.log(Level.WARNING, "Error during getting a remote game", ex);
        }
        
        return null;
    }
    
}
