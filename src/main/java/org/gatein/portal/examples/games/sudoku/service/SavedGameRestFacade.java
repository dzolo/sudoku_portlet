/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : SavedGameRestFacade.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.service;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.gatein.portal.examples.games.sudoku.controller.GameSolutionsController;
import org.gatein.portal.examples.games.sudoku.controller.SavedGameSolutionsController;
import org.gatein.portal.examples.games.sudoku.entity.SavedGameSolution;

/**
 * Saved Game REST Facade Class
 *
 * @author Ondřej Fibich
 */
@Path("saved_game")
public class SavedGameRestFacade
{
    /**
     * An instance of Game Solutions Controller
     */
    private SavedGameSolutionsController savedGamesController;

    /**
     * A construct of the class - creates an instance of the Saved Games Controller
     */
    public SavedGameRestFacade()
    {
        savedGamesController = new SavedGameSolutionsController();
    }

    @POST
    @Path("{gameSolutionId}")
    @Consumes({"application/xml", "application/json"})
    public Response create(SavedGameSolution savedGame,
                           @PathParam("gameSolutionId") Integer gameSolutionId)
    {
        GameSolutionsController gameSolutionsController;

        try
        {
            gameSolutionsController = new GameSolutionsController();
            savedGame.setGameSolutionId(gameSolutionsController.findGameSolution(gameSolutionId));
            savedGame.setSaved(new Date());
            savedGamesController.create(savedGame);
            return Response.created(URI.create(savedGame.getId().toString())).build();
        }
        catch (Exception ex)
        {
            final String name = SavedGameRestFacade.class.getName();
            Logger.getLogger(name).log(Level.WARNING, null, ex);
            return Response.notModified(ex.toString()).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Response find(@PathParam("id") Integer id)
    {
        SavedGameSolution savedGame = savedGamesController.findSavedGame(id);
        
        if (savedGame != null)
        {    
            return Response.ok(savedGame).build(); 
        }
        else
        {
            return Response.status(Response.Status.NOT_FOUND).build(); 
        }
    }

    @GET
    @Path("user/{userId}")
    @Produces({"application/xml", "application/json"})
    public List<SavedGameSolution> findAllOfUser(@PathParam("userId") String userId)
    {
        return savedGamesController.findSavedGameEntitiesOfUser(userId);
    }
    
}
