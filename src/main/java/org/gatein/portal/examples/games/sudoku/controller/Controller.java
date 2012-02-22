/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : Controller.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.controller;

import java.io.Serializable;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Controller Class.
 * The controller class is the ancestor of all controllers.
 * It provides the entity manager connector.
 *
 * @author Ondřej Fibich
 */
public class Controller implements Serializable
{
    /**
     * An identificator of the persistence unit for connecting to the database
     */
    protected static final String PERSISTENCE_UNIT_NAME =
            "org.gatein.portal.examples.games.sudoku.1.0.mysql";

    /**
     * An instance of the entity manager factory
     */
    protected EntityManagerFactory emf = null;

    /**
     * Defines a constructor of the controller. Creates entity manager factory.
     */
    public Controller()
    {
        this.emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
}
