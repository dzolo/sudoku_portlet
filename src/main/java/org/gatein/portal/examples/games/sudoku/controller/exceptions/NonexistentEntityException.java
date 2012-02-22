/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : NonexistentEntityException.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.controller.exceptions;

/**
 * Nonexistent Entity Exception Class.
 * This exception will be thrown if the accessed entity no loger exists. 
 * 
 * @author Ondřej Fibich
 */
public class NonexistentEntityException extends Exception
{
    /**
     * Defines a constructor of the exception
     * 
     * @see Exception#Exception(java.lang.String) 
     * @param message   An error message
     */
    public NonexistentEntityException(String message)
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
    public NonexistentEntityException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
