/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : AbstractPeriodicalServiceDriver.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization  : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.provider.publisher.driver;

import java.util.Date;
import org.gatein.portal.examples.games.sudoku.entity.Game;
import org.gatein.portal.examples.games.sudoku.entity.Service;

/**
 * Period Service Driver Abstract Class.
 * 
 * Allows obtaining of games from remote services identified by a
 * <code>Service</code> entity.
 * 
 * An instance of this abstract class must be obtained from the
 * <code>PeriodicalServiceFactory</code>.
 *
 * @author Ondřej Fibich
 */
public abstract class AbstractPeriodicalServiceDriver implements PeriodicalServiceDriver
{
    /**
     * Service (contains data about the service)
     */
    private Service service;
    
    /**
     * Inits periodical service with a given service.
     * 
     * @throws IllegalArgumentException On an unsuported service
     * @param service 
     */
    public void init(Service service)
    {
        if (service == null)
        {
            throw new NullPointerException("An empty service not allowed");
        }
        
        if (this.service != null)
        {
            throw new IllegalStateException("Init already called");
        }
        
        if (!isCapableOf(service))
        {
            throw new IllegalArgumentException();
        }
        
        this.service = service;
    }
    
    /**
     * Checks if a new game should be obtained from the service
     * 
     * @param lastGame  The last obtained game or <code>null</code> if there is no such
     * @return          <code>true</code> on expirated, <code>false</code> otherwise
     */
    public boolean isExpirated(Game lastGame)
    {
        if (service == null)
        {
            throw new IllegalStateException();
        }
        
        if (lastGame == null)
        {
            return true;
        }
        
        final long diff = (new Date()).getTime() - lastGame.getInitDate().getTime();
        return (diff >= service.getCheckTime());
    }

    /**
     * Gets the service
     * 
     * @return          The service
     */
    public Service getService()
    {
        if (service == null)
        {
            throw new IllegalStateException();
        }
        
        return service;
    }

    /**
     * Checks if the periodical service is capable of working with a given
     * sevice.
     * 
     * @param service   An service
     * @return          <code>true</code> if is capable, <code>false</code> otherwise
     */
    protected abstract boolean isCapableOf(Service service);
    
}
