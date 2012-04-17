/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : SavedGamesController.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.controller;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.gatein.portal.examples.games.sudoku.controller.exceptions.RollbackFailureException;
import org.gatein.portal.examples.games.sudoku.entity.GameSolution;
import org.gatein.portal.examples.games.sudoku.entity.LastPlayedGameSolution;

/**
 * The LastPlayedGameSolutionsController modifies and gets last played game
 * solution entities of users.
 *
 * @author Ondřej Fibich
 */
public class LastPlayedGameSolutionsController extends Controller
{

    /**
     * An empty constructor. Creates entity manager factory.
     */
    public LastPlayedGameSolutionsController()
    {
    }

    /**
     * A constructor with a given unit. Creates entity manager factory.
     * 
     * @param unitName      Name of persistence unit defined in persistence.xml
     */
    public LastPlayedGameSolutionsController(String unitName)
    {
        super(unitName);
    }

    /**
     * Persists (add or update) a last played solution entity to the database.
     *
     * @param lastPlayedGameSolution      A saved game to persist
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void put(LastPlayedGameSolution lastPlayedGameSolution)
            throws RollbackFailureException, Exception
    {
        EntityManager em = emf.createEntityManager();
        GameSolution gameSolution;
        LastPlayedGameSolution lastPlayedGSOld;
        String userId = lastPlayedGameSolution.getUserId();
        EntityTransaction ts = em.getTransaction();
        
        try
        {
            ts.begin();
                
            lastPlayedGSOld = em.find(LastPlayedGameSolution.class, userId);
            
            if (lastPlayedGSOld != null)
            {
                em.remove(lastPlayedGSOld);
            }
        
            gameSolution = lastPlayedGameSolution.getGameSolutionId();
            
            if (gameSolution != null)
            {
                gameSolution = em.getReference(gameSolution.getClass(),
                                               gameSolution.getId());
                
                lastPlayedGameSolution.setGameSolutionId(gameSolution);
            }
            
            em.persist(lastPlayedGameSolution);
            
            if (gameSolution != null)
            {
                Collection<LastPlayedGameSolution> col = gameSolution.getLastPlayedGameSolutionCollection();
                col.clear();
                col.add(lastPlayedGameSolution);
            }
            
            ts.commit();
        }
        catch (Exception ex)
        {
            if (ts != null && ts.isActive())
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
    
}
