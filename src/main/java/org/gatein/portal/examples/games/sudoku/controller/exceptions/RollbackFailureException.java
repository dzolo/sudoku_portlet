/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : RollbackFailureException.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.controller.exceptions;

/**
 * Rollback Failure Exception Class.
 * This exception is thrown after an unsuccessful database roollback.
 * 
 * @author Ondřej Fibich
 */
public class RollbackFailureException extends Exception
{
    /**
     * Default error message
     */
    private final static String defaultMessage =
            "An error occurred attempting to roll back the transaction.";
    
    /**
     * Defines a constructor of the exception
     * 
     * @see Exception#Exception(java.lang.String) 
     * @param cause     An cause of the error
     */
    public RollbackFailureException(Throwable cause)
    {
        super(defaultMessage, cause);
    }

    /**
     * Defines a constructor of the exception
     * 
     * @see Exception#Exception(java.lang.String) 
     * @param message   An error message
     */
    public RollbackFailureException(String message)
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
    public RollbackFailureException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
