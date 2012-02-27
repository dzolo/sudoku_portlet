/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : GameRestFacade.java
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
import org.gatein.portal.examples.games.sudoku.controller.GamesController;
import org.gatein.portal.examples.games.sudoku.entity.Game;
import org.gatein.portal.examples.games.sudoku.entity.datatype.GameType;
import org.gatein.portal.examples.games.sudoku.generator.GameGenerator;

/**
 * Game REST Facade Class
 *
 * @author Ondřej Fibich
 */
@Path("game")
public class GameRestFacade
{
    /**
     * An instance of Games Controller
     */
    private GamesController gamesController;

    /**
     * A construct of the class - creates an instance of the Games Controller
     */
    public GameRestFacade()
    {
        gamesController = new GamesController();
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response create(Game game)
    {
        try
        {
            game.setInitValues(GameGenerator.generate(game.getTypeDifficulty()));
            game.setInitDate(new Date());
            game.setType(GameType.GENERATED);
            game.setTypeServiceId(null);
            gamesController.create(game);
            return Response.created(URI.create(game.getId().toString())).build();
        }
        catch (Exception ex)
        {
            final String name = GameRestFacade.class.getName();
            Logger.getLogger(name).log(Level.WARNING, null, ex);
            return Response.notModified(ex.toString()).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Game find(@PathParam("id") Integer id)
    {
        return gamesController.findGame(id);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<Game> findAll()
    {
        return gamesController.findGameEntities();
    }
    
}
