/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : Controller.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * The Controller class is an ancestor of all controllers.
 * It provides the entity manager connector.
 *
 * @author Ondřej Fibich
 */
public class Controller implements Serializable
{
    /**
     * An identificator of the persistence unit for connecting to the database
     */
    protected static final String PERSISTENCE_DEFAULT_UNIT_NAME = "sudoku_db";

    /**
     * A map singleton of instances of entity manager factories
     */
    protected static Map<String, EntityManagerFactory> EMFs = 
            new HashMap<String, EntityManagerFactory>();
    
    /**
     * Name of persistence unit defined in persistence.xml
     */
    protected String unitName;
    
    /**
     * An instance of the entity manager factory
     */
    protected EntityManagerFactory emf;

    /**
     * Defines a constructor of the controller. Creates entity manager factory.
     */
    public Controller()
    {
        this(PERSISTENCE_DEFAULT_UNIT_NAME);
    }
    
    /**
     * Defines a constructor of the controller. Creates entity manager factory.
     * 
     * @param unitName      Name of persistence unit defined in persistence.xml
     */
    public Controller(String unitName)
    {
        this.unitName = unitName;
        this.emf = getEntityManagerFactoryFor(unitName);
    }
    
    /**
     * Gets the entity manager factory for the given unit name.
     * The method returns a singleton instance for each unique unit name.
     * 
     * @param unitName      Name of persistence unit defined in persistence.xml
     * @return              The entity manager factory
     */
    protected static EntityManagerFactory getEntityManagerFactoryFor(String unitName)
    {
        if (!EMFs.containsKey(unitName))
        {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(unitName);
            EMFs.put(unitName, emf);
        }
        
        return EMFs.get(unitName);
    }
}
