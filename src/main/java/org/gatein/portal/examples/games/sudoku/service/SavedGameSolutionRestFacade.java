/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : SavedGameSolutionRestFacade.java
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
 * The Saved Game Solution REST Facade class encases the functionality of
 * the Saved Game Solutions Controller class.
 *
 * @author Ondřej Fibich
 */
@Path("saved_game")
public class SavedGameSolutionRestFacade
{
    /**
     * An instance of Game Solutions Controller
     */
    private SavedGameSolutionsController savedGamesController;

    /**
     * A construct of the class - creates an instance of the Saved Games Controller
     */
    public SavedGameSolutionRestFacade()
    {
        savedGamesController = new SavedGameSolutionsController();
    }

    /**
     * Persists the given saved game solution with the given parent game solution.
     * 
     * @param savedGame     A saved game solution entity
     * @param gameSolutionId An identificator of a parent game solution
     * @return              OK response on success, NOT MODIFIED on failiture
     */
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
            final String name = SavedGameSolutionRestFacade.class.getName();
            Logger.getLogger(name).log(Level.WARNING, null, ex);
            return Response.notModified(ex.toString()).build();
        }
    }

    /**
     * Finds a saved game solution by its identificator
     * 
     * @param id        An identificator of a saved game solution entity
     * @return          OK response with the entity on success, NOT FOUND on failiture
     */
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

    /**
     * Finds all unifished saved game solutions of the given user.
     * 
     * @param uid       An identificator of a user
     * @return          A list of saved game solution entities 
     */
    @GET
    @Path("user/{uid}")
    @Produces({"application/xml", "application/json"})
    public List<SavedGameSolution> findAllOfUser(@PathParam("uid") String uid)
    {
        return savedGamesController.findSavedGameEntitiesOfUser(uid);
    }
    
}
