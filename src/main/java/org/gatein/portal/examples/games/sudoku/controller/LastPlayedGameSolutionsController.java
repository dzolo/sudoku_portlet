/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : SavedGamesController.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.controller;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.gatein.portal.examples.games.sudoku.controller.exceptions.RollbackFailureException;
import org.gatein.portal.examples.games.sudoku.entity.GameSolution;
import org.gatein.portal.examples.games.sudoku.entity.LastPlayedGameSolution;

/**
 * Last Played Game Solutions JPA Controller Class
 *
 * @author Ondřej Fibich
 */
public class LastPlayedGameSolutionsController extends Controller
{

    /**
     * Persists (add or update) a last played solution entity to the database.
     *
     * @param lastPlayedGameSolution      A saved game to persist
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void put(LastPlayedGameSolution lastPlayedGameSolution)
            throws RollbackFailureException,Exception
    {
        EntityManager em = emf.createEntityManager();
        GameSolution gameSolution;
        LastPlayedGameSolution lastPlayedGameSolutionOld;
        EntityTransaction ts = null;
        
        try
        {
            lastPlayedGameSolutionOld = findLastPlayedGameSolution(
                    lastPlayedGameSolution.getUserId());
            
            gameSolution = lastPlayedGameSolution.getGameSolutionId();
            
            if (gameSolution != null)
            {
                gameSolution = em.getReference(gameSolution.getClass(),
                                               gameSolution.getId());
                
                lastPlayedGameSolution.setGameSolutionId(gameSolution);
            }
            
            ts = em.getTransaction();
            ts.begin();
            
            if (lastPlayedGameSolutionOld == null)
            {
                em.persist(lastPlayedGameSolution);
            }
            else
            {
                lastPlayedGameSolutionOld.setGameSolutionId(gameSolution);
                em.merge(lastPlayedGameSolutionOld);
            }
            
            ts.commit();
                
            if (gameSolution != null)
            {
                Collection<LastPlayedGameSolution> col = gameSolution.getLastPlayedGameSolutionCollection();

                if (col.contains(lastPlayedGameSolution))
                {
                    col.remove(lastPlayedGameSolution);
                }
                
                col.add(lastPlayedGameSolution);
            }
        }
        catch (Exception ex)
        {
            if (ts != null)
            {
                try
                {
                    ts.rollback();
                }
                catch (Exception re)
                {
                    throw new RollbackFailureException(re);
                }
            }
            
            throw ex;
        }
        finally
        {
            em.close();
        }
    }

    /**
     * Finds a last played solution entity with a specified id of a user.
     * 
     * @param userId        An identificator of a user
     * @return              A found entity
     */
    public LastPlayedGameSolution findLastPlayedGameSolution(String userId)
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            return em.find(LastPlayedGameSolution.class, userId);
        }
        finally
        {
            em.close();
        }
    }
    public List<LastPlayedGameSolution> findAll()
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createQuery("SELECT object(g) FROM LastPlayedGameSolution g");
            
            return q.getResultList();
        }
        finally
        {
            em.close();
        }
    }
}
