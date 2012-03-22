/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : AbstractPeriodicalServiceDriver.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization  : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.provider.publisher.driver;

import java.io.IOException;
import java.util.Date;
import org.gatein.portal.examples.games.sudoku.entity.Game;
import org.gatein.portal.examples.games.sudoku.entity.Service;
import org.gatein.portal.examples.games.sudoku.entity.datatype.GameType;
import org.gatein.portal.examples.games.sudoku.util.GameUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A driver for daily released sudoku games in server Rahan.wz.cz
 *
 * @author Ondřej Fibich
 * @see http://rahan.wz.cz/daily_sudoku.php
 */
public class RahanWzCzPeriodiclServiceDriver extends AbstractPeriodicalServiceDriver
{
    /**
     * URL for which the service is capable to work with
     */
    private static final String serviceURL = "http://rahan.wz.cz/daily_sudoku.php";

    @Override
    protected boolean isCapableOf(Service service)
    {
        try
        {
            return serviceURL.equals(service.getUrl().trim());
        }
        catch (NullPointerException ex)
        {
            return false;
        }
    }

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
     * @see http://rahan.wz.cz/daily_sudoku.php
     */
    public Game getGame(Game lastGame) throws IOException
    {
        // download a web page        
        Document doc = Jsoup.connect(getService().getUrl()).get();
        Elements inputs = doc.select("table tr td input");
        
        Integer[] values = new Integer[81];
        int count = 0;
        
        // get data
        for (Element input : inputs)
        {
            final String name = input.attributes().get("name");
            
            if (name.matches("^s[0-9]{2}$"))
            {
                if (input.hasClass("cislo_vklad"))
                {
                    count++;
                }
                else if (input.hasClass("cislo_pevne"))
                {
                    try
                    {
                        final int index = Integer.parseInt(name.substring(1)) - 1;
                        
                        if (index < 0 || index >= 81)
                        {
                            throw new ArrayIndexOutOfBoundsException("Invalid name of a remote field");
                        }
                        
                        if (values[index] != null)
                        {
                            throw new Exception("A set value cannot be re-set");
                        }
                        
                        values[index] = Integer.parseInt(input.attributes().get("value"));
                        
                        if (values[index] <= 0 || values[index] > 9)
                        {
                            throw new Exception("A wrong value: " + values[index]);
                        }
                        
                        count++;
                    }
                    catch (Exception ex)
                    {
                        throw new IOException("Invalid content obtained", ex);
                    }
                }
            }
        }
        
        if (count != 81)
        {
            throw new IOException("Uncompleted content obtained");
        }
        
        // game
        Game game = new Game(null, new Date(), GameUtil.join(values, ","), GameType.SERVICE);
        game.setTypeDifficulty(null);
        game.setTypeServiceId(getService());
        
        // check if equal with the last game
        if (lastGame != null &&
            lastGame.getInitValues().equals(game.getInitValues()))
        {
            return null;
        }
        
        return game;
    }
    
}