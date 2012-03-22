/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : PeriodicalServiceFactory.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization  : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.provider.publisher;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.gatein.portal.examples.games.sudoku.entity.Service;
import org.gatein.portal.examples.games.sudoku.provider.publisher.driver.PeriodicalServiceDriver;
import org.gatein.portal.examples.games.sudoku.util.CustomErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Periodical Service Factory Class.
 * 
 * Obtains an instance of the periodical services.
 *
 * @author Ondřej Fibich
 */
public class PeriodicalServiceFactory
{
    /**
     * A defauld path to the configuration (relative to /WEB-INF/classes)
     */
    private static final String pathConfig = "../sudoku-portlet-periodical-service-drivers.xml";
    
    /**
     * A logger
     */
    private static final Logger logger = Logger.getLogger(PeriodicalServiceFactory.class.getName());
    
    /**
     * Contains all factorable classes
     */
    private List<Class> driverClasses;

    /**
     * Inits the factory by the default configuration
     */
    public PeriodicalServiceFactory()
    {
        this(pathConfig);
    }
    
    /**
     * Inits the factory by a configuration file given by a relative path
     * 
     * @param relativePath  A relative path to configuration (inside the WAR)
     */
    public PeriodicalServiceFactory(String relativePath)
    {
        this(loadConfig(relativePath));
    }
    
    /**
     * Inits the factory by a list of drivers
     * 
     * @param drivers       A list of names of driver classes  
     */
    public PeriodicalServiceFactory(List<String> drivers)
    {
        if (drivers == null)
        {
            throw new NullPointerException("No drivers available for factory");
        }
        
        driverClasses = new ArrayList<Class>();
        
        for (String className : drivers)
        {
            try
            {
                driverClasses.add(Class.forName(className));
            }
            catch (ClassNotFoundException ex)
            {
                logger.log(Level.SEVERE, "Driver class not founded, check config", ex);
            }
        }
    }
    
    /**
     * Gets a new instance of the periodical service driver which is capable
     * to work with a given service.
     * 
     * @param service       A service
     * @return              An instance or <code>null</code> is there is no
     *                      periodical service capable.
     */
    public PeriodicalServiceDriver newDriverFor(Service service)
    {
        PeriodicalServiceDriver ps;
        
        for (Class c : driverClasses)
        {
            try
            {
                ps = (PeriodicalServiceDriver) c.newInstance();
                ps.init(service);
                return ps;
            }
            catch (ClassCastException ex)
            {
                logger.log(Level.SEVERE, "Driver class is not of propper type", ex);
            }
            catch(InstantiationException ex)
            {
                logger.log(Level.SEVERE, "Driver class must be concrete", ex);
            }
            catch(IllegalAccessException ex)
            {
                logger.log(Level.SEVERE, "Driver class must have a no-arg constructor", ex);
            }
            catch (IllegalArgumentException ignore)
            {
            }
        }
        
        return null;
    }
    
    /**
     * Loads configuration from a XML config file on a given path
     * 
     * @param relativePath  A relative path to the configuration
     * @return              A list of class names available as drivers or
     *                      <code>null</code> on any error.
     */
    private static List<String> loadConfig(String relativePath)
    {
        List<String> drivers = new ArrayList<String>();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream file = cl.getResourceAsStream(relativePath);
        
        try
        {
            // load a factory
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setValidating(true);

            // load a builder
            DocumentBuilder builder = fac.newDocumentBuilder();
            builder.setErrorHandler(new CustomErrorHandler(logger));

            // load a document
            Document doc = builder.parse(file);
            NodeList driverNodes = doc.getElementsByTagName("driver");

            for (int i = 0; i < driverNodes.getLength(); i++)
            {
                NodeList skinNodeCildrens = driverNodes.item(i).getChildNodes();

                for (int u = 0; u < skinNodeCildrens.getLength(); u++)
                {
                    Node skinChildNode = skinNodeCildrens.item(u);

                    if ("class".equals(skinChildNode.getNodeName()))
                    {
                        final String content = skinChildNode.getTextContent();

                        if (content != null && !content.isEmpty())
                        {
                            drivers.add(content);
                        }
                        
                        break;
                    }
                }
            }
            
            return drivers;
        }
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Unable to load drivers for periodical services", ex);
            return null;
        }
    }
    
}
