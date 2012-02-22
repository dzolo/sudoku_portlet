/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : ForbiddenChangeOnEntity.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.controller.exceptions;

/**
 * Forbidden Change On Entity Exception Class.
 * This exception will be thrown if a forbidden change is made to an entity. 
 * 
 * @author Ondřej Fibich
 */
public class ForbiddenChangeOnEntityException extends Exception
{
    /**
     * Defines a constructor of the exception
     * 
     * @see Exception#Exception(java.lang.String) 
     * @param message   An error message
     */
    public ForbiddenChangeOnEntityException(String message)
    {
        super(message);
    }
    
    /**
     * Defines a constructor of the exception
     * 
     * @see Exception#Exception(java.lang.String, java.lang.Throwable) 
     * @param message   An error message
     * @param cause     An cause of the error
     */
    public ForbiddenChangeOnEntityException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
