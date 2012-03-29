/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : ServicesController.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.controller;

import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.gatein.portal.examples.games.sudoku.controller.exceptions.ForbiddenChangeOnEntityException;
import org.gatein.portal.examples.games.sudoku.controller.exceptions.NonexistentEntityException;
import org.gatein.portal.examples.games.sudoku.controller.exceptions.RollbackFailureException;
import org.gatein.portal.examples.games.sudoku.entity.Service;

/**
 * Services JPA Controller Class
 *
 * @author Ondřej Fibich
 */
public class ServicesController extends Controller
{

    /**
     * Persists a service entity to the database.
     * WARNING: This method does not set related objects. 
     *
     * @param service       A service to persist
     * @throws Exception
     */
    public void create(Service service) throws Exception
    {
        EntityManager em = emf.createEntityManager();

        try
        {
            em.getTransaction().begin();
            em.persist(service);
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
     * Merges a service entity to the database.
     * Does not update related objects. 
     * 
     * @param service       An service to merge
     * @throws NonexistentEntityException
     * @throws ForbiddenChangeOnEntityException
     * @throws Exception 
     */
    public void edit(Service service) throws NonexistentEntityException, 
            ForbiddenChangeOnEntityException, Exception
    {
        EntityManager em = emf.createEntityManager();

        try
        {
            em.getTransaction().begin();
            em.merge(service);
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
     * Gets all services.
     * 
     * @return              A list of services
     */
    public List<Service> findServiceEntities()
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            Query q = em.createNamedQuery("Service.findAll");
            
            return q.getResultList();
        }
        finally
        {
            em.close();
        }
    }

    /**
     * Finds a service entity with a specified id.
     * 
     * @param id            An identificator
     * @return              A found entity
     */
    public Service findService(Integer id)
    {
        EntityManager em = emf.createEntityManager();
        
        try
        {
            return em.find(Service.class, id);
        }
        finally
        {
            em.close();
        }
    }
}
