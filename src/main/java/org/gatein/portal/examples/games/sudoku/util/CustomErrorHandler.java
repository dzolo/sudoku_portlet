/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : CustomErrorHandler.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Custom Error Handler Class
 *
 * @author Ondřej Fibich
 */
public class CustomErrorHandler implements ErrorHandler
{
    /**
     * Logger
     */
    private Logger logger;

    /**
     * Inits custom hanler
     * 
     * @param logger 
     */
    public CustomErrorHandler(Logger logger)
    {
        this.logger = logger;
    }
    
    public void warning(SAXParseException e) throws SAXException
    {
        logger.log(Level.WARNING, "Document validation warning", e);
    }

    public void error(SAXParseException e) throws SAXException
    {
        throw e;
    }

    public void fatalError(SAXParseException e) throws SAXException
    {
        throw e;
    }
}
