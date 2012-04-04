/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : ControllersTest.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.controller;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gatein.portal.examples.games.sudoku.entity.*;
import org.gatein.portal.examples.games.sudoku.entity.datatype.GameDifficulty;
import org.gatein.portal.examples.games.sudoku.entity.datatype.GameType;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * A collection of tests for controllers.
 *
 * @see GamesController
 * @see GameSolutionsController
 * @see ServicesController
 * @see SavedGamesController
 * @see LastPlayedGameSolutionsController
 */
public class ControllersTest
{
    private GamesController gamesController;
    private GameSolutionsController gameSolutionsController;
    private SavedGamesController savedGamesController;
    private LastPlayedGameSolutionsController lastPlayedGameSolutionsController;
    private ServicesController servicesController;
    
    @Before
    public void setUp()
    {
        try
        {
            gamesController = new GamesController("sudoku_db_testing");
            gameSolutionsController = new GameSolutionsController("sudoku_db_testing");
            savedGamesController = new SavedGamesController("sudoku_db_testing");
            lastPlayedGameSolutionsController = new LastPlayedGameSolutionsController("sudoku_db_testing");
            servicesController = new ServicesController("sudoku_db_testing");
        }
        catch (Exception e)
        {
            fail("Error during initialization of controllers");
        }
    }
    
    /**
     * Create a generated game and save it, create its solution, saved game and
     * last played game solution.
     */
    @Test
    public void test1()
    {
        try
        {
            Game g = new Game(null, new Date(), "empty", GameType.GENERATED);
            g.setTypeDifficulty(GameDifficulty.MODERATE);
            
            System.out.println("GamesController#create");
            gamesController.create(g);
            
            System.out.println("GamesController#");
            assertEquals(g, gamesController.findGame(g.getId()));
            assertEquals(null, gamesController.findGame(Integer.MAX_VALUE));
            System.out.println("GamesController#getGameCount");
            assertEquals(1, gamesController.getGameCount());
            System.out.println("GamesController#getSolutionsCountOf");
            assertEquals(0, gamesController.getSolutionsCountOf(g.getId()));
            System.out.println("GamesController#findGameEntities");
            assertEquals(1, gamesController.findGameEntities().size());
            
            System.out.println("GamesController#findBestSolvedGameSolutionEntities");
            gamesController.findBestSolvedGameSolutionEntities(g.getId());
            System.out.println("GamesController#findBestSolvers");
            gamesController.findBestSolvers();
            System.out.println("GamesController#findStatistics");
            gamesController.findStatistics();
            System.out.println("GamesController#findStatisticsOf");
            gamesController.findStatisticsOf(g.getId());
            System.out.println("GamesController#findGameProposalEntities");
            gamesController.findGameProposalEntities("user");
            
            GameSolution gs = new GameSolution(null, "user", null, "empty", new Date(), 150, false);
            gs.setGameId(g);
            
            System.out.println("GameSolutionsController#create");
            gameSolutionsController.create(gs);
            
            System.out.println("GameSolutionsController#findGameSolution");
            assertEquals(gs, gameSolutionsController.findGameSolution(gs.getId()));
            assertEquals(null, gameSolutionsController.findGameSolution(Integer.MAX_VALUE));
            System.out.println("GameSolutionsController#getSolverCount");
            assertEquals(0, gameSolutionsController.getSolverCount());
            System.out.println("GameSolutionsController#getFinishedGameSolutionCount");
            assertEquals(0, gameSolutionsController.getFinishedGameSolutionCount());
            System.out.println("GameSolutionsController#findGameSolutionEntities");
            assertEquals(1, gameSolutionsController.findGameSolutionEntities().size());
            
            System.out.println("GameSolutionsController#findUnfinishedGameSolutionEntitiesOfUser");
            gameSolutionsController.findUnfinishedGameSolutionEntitiesOfUser("user");
            
            SavedGame sg = new SavedGame(null, 150, "empty");
            sg.setName("a new saved game");
            sg.setSaved(new Date());
            sg.setGameSolutionId(gs);
            
            System.out.println("SavedGamesController#create");
            savedGamesController.create(sg);
            
            System.out.println("SavedGamesController#findSavedGame");
            assertEquals(sg, savedGamesController.findSavedGame(sg.getId()));
            assertEquals(null, savedGamesController.findSavedGame(Integer.MAX_VALUE));
            System.out.println("SavedGamesController#findSavedGameEntities");
            assertEquals(1, savedGamesController.findSavedGameEntities().size());
            System.out.println("SavedGamesController#findSavedGameEntitiesOfUser");
            assertEquals(1, savedGamesController.findSavedGameEntitiesOfUser("user").size());
            
            gs.setLasting(200);
            gs.setFinished(true);
            
            System.out.println("SavedGamesController#edit");
            gameSolutionsController.edit(gs);
            
            System.out.println("GameSolutionsController#getSolverCount");
            assertEquals(1, gameSolutionsController.getSolverCount());
            System.out.println("GameSolutionsController#getFinishedGameSolutionCount");
            assertEquals(1, gameSolutionsController.getFinishedGameSolutionCount());
            
            LastPlayedGameSolution lpgs = new LastPlayedGameSolution("user");
            lpgs.setGameSolutionId(gs);
            
            System.out.println("LastPlayedGameSolutionsController#put");
            lastPlayedGameSolutionsController.put(lpgs);
            lastPlayedGameSolutionsController.put(lpgs);
            
            System.out.println("LastPlayedGameSolutionsController#findLastPlayedGameSolution");
            assertEquals(null, lastPlayedGameSolutionsController.findLastPlayedGameSolution("user2"));
            assertEquals(lpgs, lastPlayedGameSolutionsController.findLastPlayedGameSolution("user"));
        }
        catch (Exception e)
        {
            Logger.getAnonymousLogger().log(Level.SEVERE, null, e);
            fail("Test failed, error: " + e.getMessage());
        }
    }
    
    /**
     * Create a service and save it and create a game of service.
     */
    @Test
    public void test2()
    {
        try
        {
            Service s = new Service(null, "service", "http://service.org", 156);
            
            System.out.println("ServicesController#create");
            servicesController.create(s);
            
            System.out.println("GamesController#findLastGameOf");
            assertEquals(null, gamesController.findLastGameOf(s));
            
            s.setEnabled(false);
            
            System.out.println("ServicesController#edit");
            servicesController.edit(s);
            
            System.out.println("ServicesController#findServiceEntities");
            assertEquals(1, servicesController.findServiceEntities().size());
            
            Game g = new Game(null, new Date(), "empty", GameType.SERVICE);
            g.setTypeServiceId(s);
            
            System.out.println("GamesController#create");
            gamesController.create(g);
            
            System.out.println("ServicesController#findService");
            assertEquals(s, servicesController.findService(s.getId()));
            assertEquals(null, servicesController.findService(Integer.MAX_VALUE));
            
            System.out.println("GamesController#findLastGameOf");
            assertEquals(g, gamesController.findLastGameOf(s));
        }
        catch (Exception e)
        {
            Logger.getAnonymousLogger().log(Level.SEVERE, null, e);
            fail("Test failed, error: " + e.getMessage());
        }
    }
    
}
