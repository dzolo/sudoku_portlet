/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : exceptions.js
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

/**
 * The request failed exception class
 * 
 * @param message       A message of the error
 * @param cause         A cause of the error
 */
function SudokuGame_RequestFailedException(message, cause)
{
    /** A name public property */
    this.name = 'RequestFailedException';
    /** A message public property */
    this.messageInfo = message;
    /** A cause public property */
    this.cause = cause;
    
    /**
     * Tranforms the exception object to a string
     * 
     * @return          A text reprezentation of the object
     */
    this.toString = function ()
    {
        return this.messageInfo;
    }
}

/**
 * The null pointer exception class
 * 
 * @param message       A message of the error
 */
function SudokuGame_NullPointerException(message)
{
    /** A name public property */
    this.name = 'NullPointerException';
    /** A message public property */
    this.messageInfo = message;
    
    /**
     * Tranforms the exception object to a string
     * 
     * @return          A text reprezentation of the object
     */
    this.toString = function ()
    {
        return this.name + ': ' + this.messageInfo;
    }
}

/**
 * The illegal state exception class
 * 
 * @param message       A message of the error
 */
function SudokuGame_IllegalStateException(message)
{
    /** A name public property */
    this.name = 'IllegalStateException';
    /** A message public property */
    this.messageInfo = message;
    
    /**
     * Tranforms the exception object to a string
     * 
     * @return          A text reprezentation of the object
     */
    this.toString = function ()
    {
        return this.name + ': ' + this.messageInfo;
    }
}