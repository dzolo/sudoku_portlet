/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : GameSolutionRestFacade.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.service;

import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.gatein.portal.examples.games.sudoku.controller.GameSolutionsController;
import org.gatein.portal.examples.games.sudoku.controller.LastPlayedGameSolutionsController;
import org.gatein.portal.examples.games.sudoku.entity.GameSolution;
import org.gatein.portal.examples.games.sudoku.entity.LastPlayedGameSolution;

/**
 * last Played Game Solution REST Facade Class
 *
 * @author Ondřej Fibich
 */
@Path("last_played_game_solution")
public class LastPlayedGameSolutionRestFacade
{
    /**
     * An instance of Last Played Game Solutions Controller
     */
    private LastPlayedGameSolutionsController lastPlayedGameSolutionsController;

    /**
     * A construct of the class - creates an instance of the Game Solutions Controller
     */
    public LastPlayedGameSolutionRestFacade()
    {
        lastPlayedGameSolutionsController = new LastPlayedGameSolutionsController();
    }

    @POST
    @Path("{gameSolutionId}")
    @Consumes({"application/xml", "application/json"})
    public Response put(LastPlayedGameSolution lastPlayedGameSolution,
                        @PathParam("gameSolutionId") Integer gameSolutionId)
    {
        GameSolutionsController gameSolutionsController;
        GameSolution gameSolution = null;
        
        try
        {
            gameSolutionsController = new GameSolutionsController();
            gameSolution = gameSolutionsController.findGameSolution(gameSolutionId);
            
            if (gameSolution == null)
            {
                throw new NullPointerException("The game solution was not founded");
            }
            
            if (lastPlayedGameSolution.getUserId() == null ||
                lastPlayedGameSolution.getUserId().isEmpty())
            {
                throw new NullPointerException("An empty user identificator not enabled");
            }
            
            lastPlayedGameSolution.setGameSolutionId(gameSolution);
            lastPlayedGameSolutionsController.put(lastPlayedGameSolution);
            return Response.created(URI.create(lastPlayedGameSolution.getUserId())).build();
        }
        catch (Exception ex)
        {
            final String name = LastPlayedGameSolutionRestFacade.class.getName();
            Logger.getLogger(name).log(Level.WARNING, null, ex);
            return Response.notModified(ex.toString()).build();
        }
    }

    @GET
    @Path("{uid}")
    @Produces({"application/xml", "application/json"})
    public Response find(@PathParam("uid") String userId)
    {
        LastPlayedGameSolution lastPlayedGameSolution = 
                lastPlayedGameSolutionsController.findLastPlayedGameSolution(userId);
        
        if (lastPlayedGameSolution != null)
        {    
            return Response.ok(lastPlayedGameSolution).build(); 
        }
        else
        {
            return Response.status(Response.Status.NOT_FOUND).build(); 
        }
    }
    
}
