/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : GamesController.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.controller;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.gatein.portal.examples.games.sudoku.controller.exceptions.RollbackFailureException;
import org.gatein.portal.examples.games.sudoku.entity.Game;
import org.gatein.portal.examples.games.sudoku.entity.GameSolution;
import org.gatein.portal.examples.games.sudoku.entity.Service;

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
            Query q = em.createQuery("select object(o) from Game as o");
            
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
}
