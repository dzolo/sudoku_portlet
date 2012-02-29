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
 * Game Solution REST Facade Class
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
            gameSolution.setTimeStart(new Date());
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

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public GameSolution find(@PathParam("id") Integer id)
    {
        return gameSolutionsController.findGameSolution(id);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<GameSolution> findAll()
    {
        return gameSolutionsController.findGameSolutionEntities();
    }
    
    @POST
    @Path("check")
    @Consumes({"text/plain"})
    @Produces({"application/json"})
    public String check(String gameSolutionValues)
    {
        try
        {
            final List<Integer> incorrect = GameUtil.check(gameSolutionValues);
            
            return "{\"state\":true, \"check\": {\"valid\":"
                    + incorrect.isEmpty() + ",\"fields\":["
                    + GameUtil.join(incorrect.toArray(), ",")
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
