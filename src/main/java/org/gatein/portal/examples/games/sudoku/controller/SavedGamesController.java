/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : SavedGamesController.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.controller;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.gatein.portal.examples.games.sudoku.controller.exceptions.ForbiddenChangeOnEntityException;
import org.gatein.portal.examples.games.sudoku.controller.exceptions.NonexistentEntityException;
import org.gatein.portal.examples.games.sudoku.controller.exceptions.RollbackFailureException;
import org.gatein.portal.examples.games.sudoku.entity.GameSolution;
import org.gatein.portal.examples.games.sudoku.entity.SavedGame;

/**
 * Saved Games JPA Controller Class
 *
 * @author Ondřej Fibich
 */
public class SavedGamesController extends Controller
{

    /**
     * Persists a service entity to the database.
     *
     * @param savedGame      A saved game to persist
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void create(SavedGame savedGame) throws RollbackFailureException,Exception
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
                gameSolution.getSavedGamesCollection().add(savedGame);
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
     * Merges a saved game entity to the database.
     * 
     * @param savedGame      A service to merge
     * @throws NonexistentEntityException
     * @throws Exception 
     */
    public void edit(SavedGame savedGame) throws NonexistentEntityException,
            ForbiddenChangeOnEntityException, Exception
    {
        EntityManager em = emf.createEntityManager();
        SavedGame persistentSavedGame;
        GameSolution gameSolution;
        
        try
        {
            persistentSavedGame = em.find(SavedGame.class, savedGame.getId());
            gameSolution = savedGame.getGameSolutionId();
            
            if (!persistentSavedGame.getGameSolutionId().equals(gameSolution))
            {
                throw new ForbiddenChangeOnEntityException(
                        "Forbidden change on relatation to a game solution"
                );
            }
            
            if (gameSolution != null)
            {
                gameSolution = em.getReference(gameSolution.getClass(),
                                               gameSolution.getId());
                
                savedGame.setGameSolutionId(gameSolution);
            }
            
            em.merge(savedGame);
        }
        catch (Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            
            if (msg == null || msg.length() == 0)
            {
                if (findSavedGame(savedGame.getId()) == null)
                {
                    throw new NonexistentEntityException(
                            "The savedGames with id " + savedGame.getId() +
                            " no longer exists."
                    );
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
     * Gets all saved games.
     * 
     * @return              A list of saved games
     */
    public List<SavedGame> findSavedGameEntities()
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createNamedQuery("SavedGame.findAll");
            
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
    public List<SavedGame> findSavedGameEntitiesOfUser(String uid)
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createNamedQuery("SavedGame.findByUser");
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
    public SavedGame findSavedGame(Integer id)
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            return em.find(SavedGame.class, id);
        }
        finally
        {
            em.close();
        }
    }
}
