/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : GameSolutionRestFacade.java
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
import org.gatein.portal.examples.games.sudoku.controller.GamesController;
import org.gatein.portal.examples.games.sudoku.entity.GameSolution;
import org.gatein.portal.examples.games.sudoku.util.GameUtil;

/**
 * The Game Solution REST Facade provides the game checking capability and
 * the encased functionality of the Game Solutions Controller class.
 * 
 * @author Ondřej Fibich
 */
@Path("game_solution")
public class GameSolutionRestFacade
{
    /**
     * An instance of Game Solutions Controller
     */
    private GameSolutionsController gameSolutionsController;

    /**
     * A construct of the class - creates an instance of the Game Solutions Controller
     */
    public GameSolutionRestFacade()
    {
        gameSolutionsController = new GameSolutionsController();
    }

    /**
     * Persists the given game solution with the given parent game.
     * 
     * @param gameSolution  A game solution entity
     * @param gameId        An identificator of a parent game
     * @return              OK response on success, NOT MODIFIED on failiture
     */
    @POST
    @Path("{gameId}")
    @Consumes({"application/xml", "application/json"})
    public Response create(GameSolution gameSolution,
                           @PathParam("gameId") Integer gameId)
    {
        GamesController gamesController;

        try
        {
            gamesController = new GamesController();
            gameSolution.setGameId(gamesController.findGame(gameId));
            gameSolution.setStartTime(new Date());
            gameSolution.setFinished(false);
            gameSolution.setLasting(0);
            gameSolutionsController.create(gameSolution);
            return Response.created(URI.create(gameSolution.getId().toString())).build();
        }
        catch (Exception ex)
        {
            final String name = GameSolutionRestFacade.class.getName();
            Logger.getLogger(name).log(Level.WARNING, null, ex);
            return Response.notModified(ex.toString()).build();
        }
    }
    
    /**
     * Merges the given game solution.
     * 
     * @param gameSolution      A game solution entity
     * @return                  OK response on success, NOT MODIFIED on failiture
     */
    @PUT
    @Consumes({"application/xml", "application/json"})
    public Response edit(GameSolution gameSolution)
    {
        GameSolution merged;
        
        try
        {
            if (gameSolution.isFinished() &&
                !GameUtil.check(gameSolution.getValues(), true).isEmpty())
            {
                throw new IllegalArgumentException("The end of a game is incorrect");
            }
            
            merged = gameSolutionsController.findGameSolution(gameSolution.getId());
            
            if (merged.isFinished())
            {
                if (!gameSolution.isFinished())
                {
                    throw new IllegalStateException("Changing of finished game is not enabled");
                }
                
                merged.setRating(gameSolution.getRating());
            }
            else
            {
                merged.setFinished(gameSolution.isFinished());
                merged.setLasting(gameSolution.getLasting());
                merged.setValues(gameSolution.getValues());
            }
            
            gameSolutionsController.edit(merged);
            return Response.ok().build();
        }
        catch (Exception ex)
        {
            final String name = GameSolutionRestFacade.class.getName();
            Logger.getLogger(name).log(Level.WARNING, null, ex);
            return Response.notModified(ex.getMessage()).build();
        }
    }

    /**
     * Finds a game solution by its identificator
     * 
     * @param id        An identificator of a game solution entity
     * @return          OK response with the entity on success, NOT FOUND on failiture
     */
    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Response find(@PathParam("id") Integer id)
    {
        GameSolution gameSolution = gameSolutionsController.findGameSolution(id);
        
        if (gameSolution != null)
        {    
            return Response.ok(gameSolution).build(); 
        }
        else
        {
            return Response.status(Response.Status.NOT_FOUND).build(); 
        }
    }

    /**
     * Finds all game solutions.
     * 
     * @return          A list of game solution entities 
     */
    @GET
    @Produces({"application/xml", "application/json"})
    public List<GameSolution> findAll()
    {
        return gameSolutionsController.findGameSolutionEntities();
    }

    /**
     * Finds all unifished game solutions of the given user.
     * 
     * @param uid       An identificator of a user   
     * @return          A list of game solution entities 
     */
    @GET
    @Path("unfinished/{uid}")
    @Produces({"application/xml", "application/json"})
    public List<GameSolution> findUnfinishedOfUser(@PathParam("uid") String uid)
    {
        return gameSolutionsController.findUnfinishedGameSolutionEntitiesOfUser(uid);
    }
    
    /**
     * Checks the game solution. Checking is not increasing the checked count.
     * 
     * @see GameUtil#check(java.lang.String) 
     * @param id            An identificator of a game solution
     * @return              A JSON String with the return state of check and
     *                      with possible errors.
     */
    @POST
    @Path("check")
    @Consumes({"text/plain"})
    @Produces({"application/json"})
    public String checkWithoutNote(String gameSolutionValues)
    {
        try
        {
            List<Integer> incorrectFields = GameUtil.check(gameSolutionValues);
            
            return "{\"state\":true, \"check\": {\"valid\":"
                    + incorrectFields.isEmpty() + ",\"fields\":["
                    + GameUtil.join(incorrectFields.toArray(), ",")
                    + "]}}";
        }
        catch (Exception ex)
        {
            return "{\"state\":false,\"message\":\""
                    + ex.getMessage().replace('"', '\'')
                    + "\"}";
        }
    }
    
    /**
     * Check the game solution
     * 
     * @see GameUtil#check(java.lang.String) 
     * @param gameSolutionValues The values of fields of a game solution to check,
     *                      separated by commas
     * @param id            An identificator of a game solution
     * @return              A JSON String with the return state of check and
     *                      with possible errors.
     */
    @POST
    @Path("check/{id}")
    @Consumes({"text/plain"})
    @Produces({"application/json"})
    public String check(String gameSolutionValues, @PathParam("id") Integer id)
    {
        GameSolution gameSolution = gameSolutionsController.findGameSolution(id);
        List<Integer> incorrectFields;
        
        try
        {
            if (gameSolution == null)
            {
                throw new Exception("An incorrect identificator of the solution");
            }
            
            if (gameSolution.getCheckCount() >= GameSolution.MAX_CHECK_COUNT)
            {
                throw new Exception("You can check each game solution just 3 times");
            }
            else
            {
                gameSolution.setCheckCount(gameSolution.getCheckCount() + 1);
                gameSolutionsController.edit(gameSolution);
            }
            
            incorrectFields = GameUtil.check(gameSolutionValues);
            
            return "{\"state\":true, \"check\": {\"valid\":"
                    + incorrectFields.isEmpty() + ",\"fields\":["
                    + GameUtil.join(incorrectFields.toArray(), ",")
                    + "]}}";
        }
        catch (Exception ex)
        {
            return "{\"state\":false,\"message\":\""
                    + ex.getMessage().replace('"', '\'')
                    + "\"}";
        }
    }
    
}
