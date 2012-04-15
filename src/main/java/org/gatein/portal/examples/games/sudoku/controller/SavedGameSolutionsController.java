/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : SavedGameSolutionsController.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.controller;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.gatein.portal.examples.games.sudoku.controller.exceptions.RollbackFailureException;
import org.gatein.portal.examples.games.sudoku.entity.GameSolution;
import org.gatein.portal.examples.games.sudoku.entity.SavedGameSolution;

/**
 * The SavedGameSolutionsController creates and gets saved game solution
 * entities of user.
 *
 * @author Ondřej Fibich
 */
public class SavedGameSolutionsController extends Controller
{

    /**
     * An empty constructor. Creates entity manager factory.
     */
    public SavedGameSolutionsController()
    {
    }

    /**
     * A constructor with a given unit. Creates entity manager factory.
     * 
     * @param unitName      Name of persistence unit defined in persistence.xml
     */
    public SavedGameSolutionsController(String unitName)
    {
        super(unitName);
    }

    /**
     * Persists a service entity to the database.
     *
     * @param savedGame      A saved game to persist
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void create(SavedGameSolution savedGame) throws RollbackFailureException,Exception
    {
        EntityManager em = emf.createEntityManager();
        GameSolution gameSolution;
        
        try
        {
            em.getTransaction().begin();
            
            gameSolution = savedGame.getGameSolutionId();
            
            if (gameSolution != null)
            {
                gameSolution = em.getReference(gameSolution.getClass(),
                                               gameSolution.getId());
                
                savedGame.setGameSolutionId(gameSolution);
            }
            
            em.persist(savedGame);
            
            if (gameSolution != null)
            {
                gameSolution.getSavedGameSolutionsCollection().add(savedGame);
                em.merge(gameSolution);
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
     * Gets all saved games.
     * 
     * @return              A list of saved games
     */
    public List<SavedGameSolution> findSavedGameEntities()
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createNamedQuery("SavedGameSolution.findAll");
            
            return q.getResultList();
        }
        finally
        {
            em.close();
        }
    }
    
    /**
     * Gets saved games of a user.
     * 
     * @param uid           An identificator of a user
     * @return              A list of saved games
     */
    public List<SavedGameSolution> findSavedGameEntitiesOfUser(String uid)
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createNamedQuery("SavedGameSolution.findByUser");
            q.setParameter("uid", uid);
            
            return q.getResultList();
        }
        finally
        {
            em.close();
        }
    }

    /**
     * Finds a saved game entity with a specified id.
     * 
     * @param id            An identificator
     * @return              A found entity
     */
    public SavedGameSolution findSavedGame(Integer id)
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            return em.find(SavedGameSolution.class, id);
        }
        finally
        {
            em.close();
        }
    }
}
