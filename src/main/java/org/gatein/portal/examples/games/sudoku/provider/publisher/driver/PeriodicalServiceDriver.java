/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : AbstractPeriodicalServiceDriver.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization  : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.provider.publisher.driver;

import java.io.IOException;
import org.gatein.portal.examples.games.sudoku.entity.Game;
import org.gatein.portal.examples.games.sudoku.entity.Service;

/**
 * Period Service Driver Interface.
 * 
 * Allows obtaining of games from remote services identified by a
 * <code>Service</code> entity.
 * 
 * An instance of this interface may be obtained from the
 * <code>PeriodicalServiceFactory</code>.
 *
 * @author Ondřej Fibich
 */
public interface PeriodicalServiceDriver
{
    /**
     * Inits periodical service with a given service.
     * 
     * @throws IllegalArgumentException On an unsuported service
     * @param service 
     */
    public void init(Service service);
    
    /**
     * Checks if a new game should be obtained from the service
     * 
     * @param lastGame  The last obtained game or <code>null</code> if there is no such
     * @return          <code>true</code> on expirated, <code>false</code> otherwise
     */
    public boolean isExpirated(Game lastGame);
    
    /**
     * Obtains a new game from the service.
     * 
     * The obtained game is checked if it is not same as the previously
     * obtained game. In this case, this method do not return this game.
     * 
     * @param lastGame  The last obtained game or <code>null</code> if there is no such
     * @return          The obtained game or <code>null</code> if there is no
     *                  new game available
     * @throws IOException
     *                  On any error during obtaining from remote service
     */
    public Game getGame(Game lastGame) throws IOException;
    
}
