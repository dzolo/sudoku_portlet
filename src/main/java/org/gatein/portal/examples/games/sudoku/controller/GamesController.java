/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : GamesController.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.gatein.portal.examples.games.sudoku.controller.exceptions.RollbackFailureException;
import org.gatein.portal.examples.games.sudoku.entity.Game;
import org.gatein.portal.examples.games.sudoku.entity.GameSolution;
import org.gatein.portal.examples.games.sudoku.entity.Service;
import org.gatein.portal.examples.games.sudoku.entity.datatype.GameType;

/**
 * Games JPA Controller Class
 *
 * @author Ondřej Fibich
 */
public class GamesController extends Controller
{

    /**
     * Persists a game entity to the database.
     * Does not save game solutions of the game.
     * 
     * @param game      A game to persist
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void create(Game game) throws RollbackFailureException, Exception
    {
        if (game.getGameSolutionsCollection() == null)
        {
            game.setGameSolutionsCollection(new ArrayList<GameSolution>());
        }
        
        EntityManager em = emf.createEntityManager();
        
        try
        {
            em.getTransaction().begin();
            
            Service typeService = game.getTypeServiceId();
            
            if (typeService != null)
            {
                typeService = em.getReference(typeService.getClass(), typeService.getId());
                game.setTypeServiceId(typeService);
            }
            
            em.persist(game);
            
            if (typeService != null)
            {
                typeService.getGamesCollection().add(game);
                em.merge(typeService);
            }
            
            em.getTransaction().commit();
        }
        catch (Exception ex)
        {
            try
            {
                em.getTransaction().rollback();
            }
            catch (Exception re)
            {
                throw new RollbackFailureException(re);
            }
            throw ex;
        }
        finally
        {
            em.close();
        }
    }

    /**
     * Gets all games.
     * 
     * @return              A list of games
     */
    public List<Game> findGameEntities()
    {
        return findGameEntities(true, -1, -1);
    }

    /**
     * Gets a limited amount of games.
     * 
     * @param maxResults    A maximum count of returned games
     * @param firstResult   An index of the first returned game
     * @return              A list of games
     */
    public List<Game> findGameEntities(int maxResults, int firstResult)
    {
        return findGameEntities(false, maxResults, firstResult);
    }

    /**
     * Gets a limited amount of games or all games.
     * 
     * @param all           An indicator of all gamens fetch.
     * @param maxResults    A maximum count of returned games
     * @param firstResult   An index of the first returned game
     * @return              A list of games
     */
    private List<Game> findGameEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createNamedQuery("Game.findAll");
            
            if (!all)
            {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            
            return q.getResultList();
        }
        finally
        {
            em.close();
        }
    }

    /**
     * Gets game entities which was gained from a remote service.
     * Gained games were not played by the user which is given by uid before.
     * 
     * @param uid           Specifies a user
     * @return              A list of games
     */
    public List<Game> findServicedGameEntities(String uid)
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createNamedQuery("Game.findAllByTypeAndNotInUser");
            q.setParameter("type", GameType.SERVICE);
            q.setParameter("uid", uid);
            
            return q.getResultList();
        }
        finally
        {
            em.close();
        }
    }

    /**
     * Gets limited count of proposal game entities
     * 
     * @param uid           An indentificator of user who requests proposals
     * @return              A list of games
     */
    public List<Game> findGameProposalEntities(String uid)
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createNamedQuery("Game.findProposals");
            q.setParameter("uid", uid);
            q.setMaxResults(50);
            
            return q.getResultList();
        }
        finally
        {
            em.close();
        }
    }
    
    /**
     * Finds at most five best solutions of a game with a given ID
     * 
     * @param id            An identificator of a game
     * @return              The best entities
     */
    public List<GameSolution> findBestSolvedGameSolutionEntities(Integer id)
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createNamedQuery("Game.findBestSolvedSolutions");
            q.setParameter("gid", id);
            q.setFirstResult(0);
            q.setMaxResults(5);
            
            return q.getResultList();
        }
        finally
        {
            em.close();
        }
    }
    
    /**
     * Finds the last obtained game of a given service
     * 
     * @param service       A service
     * @return              The last game or <code>null</code>
     */
    public Game findLastGameOf(Service service)
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createNamedQuery("Game.findLastGameOfService");
            q.setParameter("type", GameType.SERVICE);
            q.setParameter("service", service);
            q.setMaxResults(1);
            
            try
            {
                return (Game) q.getSingleResult();
            }
            catch (NoResultException ex)
            {
                return null;
            }
        }
        finally
        {
            em.close();
        }
    }
    
    /**
     * Finds at most five best solvers of games
     * 
     * @return              A list of the best solvers
     */
    public List<Map<String, Object>> findBestSolvers()
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createNamedQuery("Game.findBestSolvers");
            q.setFirstResult(0);
            q.setMaxResults(5);
            
            List<Object[]> rsp = q.getResultList();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            
            for (Object[] line : rsp)
            {
                for (int i = 2; i < line.length; i++)
                {
                    if (line[i] == null)
                        line[i] = new Long(0);
                }
                
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("userId", line[0]);
                map.put("userName", line[1]);
                map.put("c_solved", line[2]);
                map.put("s_lasting", line[3]);
                map.put("a_rating", line[3]);
                list.add(map);
            }
            
            return list;
        }
        finally
        {
            em.close();
        }
    }
    
    /**
     * Finds global statistics of all games
     * 
     * @return                A map of stats with items: a_rating, a_lasting, c_solved, c_played
     */
    public Map<String, Object> findStatistics()
    {
        Map<String, Object> stats = new HashMap<String, Object>();
        EntityManager em = emf.createEntityManager();
        GameSolutionsController gsc = new GameSolutionsController();
        
        try
        {
            /* Global stats for logged users */
            
            Query q = em.createNamedQuery("Game.findStatisticsLoggedPlayers");
            
            try
            {
                Object[] results = (Object[]) q.getSingleResult();

                for (int i = 0; i < results.length; i++)
                {
                    if (results[i] == null)
                        results[i] = new Long(0);
                }

                stats.put("a_rating", results[0]);
                stats.put("a_lasting", results[1]);
                stats.put("s_lasting", results[2]);
            }
            catch (NoResultException nre)
            {
                stats.put("a_rating", new Integer(0));
                stats.put("a_lasting", new Integer(0));
                stats.put("s_lasting", new Integer(0));
            }
            
            stats.put("c_players", new Integer(gsc.getSolverCount()));
            stats.put("c_games", new Integer(getGameCount()));
            stats.put("c_finished",  new Integer(gsc.getFinishedGameSolutionCount()));
            
            return stats;
        }
        finally
        {
            em.close();
        }
    }
    
    /**
     * Finds total statistics of the game
     * 
     * @param game            An identificator of the game
     * @return                A map of stats with items: a_rating, a_lasting, c_solved, c_played
     */
    public Map<String, Object> findStatisticsOf(Integer game)
    {
        Map<String, Object> stats = new HashMap<String, Object>();
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createNamedQuery("Game.findStatisticsOfGame");
            q.setParameter("gid", game);
            
            Object[] results = (Object[]) q.getSingleResult();
            
            for (int i = 0; i < results.length; i++)
            {
                if (results[i] == null)
                    results[i] = new Long(0);
            }
            
            stats.put("a_rating", results[0]);
            stats.put("a_lasting", results[1]);
            stats.put("c_solved", results[2]);
            stats.put("c_played", new Integer(getSolutionsCountOf(game)));
            
            return stats;
        }
        finally
        {
            em.close();
        }
    }

    /**
     * Finds a game entity with a specified id.
     * 
     * @param id            An identificator
     * @return              A found entity
     */
    public Game findGame(Integer id)
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            return em.find(Game.class, id);
        }
        finally
        {
            em.close();
        }
    }

    /**
     * Gets a count of games.
     * 
     * @return              A total count
     */
    public int getGameCount()
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createQuery("select count(o) from Game as o");
            return ((Long) q.getSingleResult()).intValue();
        }
        finally
        {
            em.close();
        }
    }

    /**
     * Gets a count of game solutions of the given game.
     * 
     * @param game          An indentificator of the game
     * @return              A total count
     */
    public int getSolutionsCountOf(Integer game)
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createNamedQuery("Game.countOfSolutionsOfGame");
            q.setParameter("gid", game);
            
            return ((Long) q.getSingleResult()).intValue();
        }
        finally
        {
            em.close();
        }
    }
}
