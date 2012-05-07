/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : DailysudokuComPeriodicalServiceDriver.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization  : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.provider.publisher.driver;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gatein.portal.examples.games.sudoku.entity.Game;
import org.gatein.portal.examples.games.sudoku.entity.Service;
import org.gatein.portal.examples.games.sudoku.entity.datatype.GameType;
import org.gatein.portal.examples.games.sudoku.util.GameUtil;


/**
 * A driver for daily released sudoku games from server Dailysudoku.com.
 *
 * @see http://www.dailysudoku.com/sudoku/today.shtml
 * @see http://www.dailysudoku.com/cgi-bin/sudoku/get_board.pl
 * @see PeriodicalServiceFactory
 * @author Ondřej Fibich
 */
public class DailysudokuComPeriodicalServiceDriver extends AbstractPeriodicalServiceDriver
{
    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(DailysudokuComPeriodicalServiceDriver.class.getName());
    
    /**
     * URL for which the service is capable to work with
     */
    private static final String serviceURL = "http://www.dailysudoku.com/cgi-bin/sudoku/get_board.pl";
    
    
    @Override
    protected boolean isCapableOf(Service service)
    {
        return serviceURL.equals(service.getUrl().trim());
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
     * @see http://www.dailysudoku.com/cgi-bin/sudoku/get_board.pl
     */
    public Game getGame(Game lastGame) throws IOException
    {
        ObtainedData data;
        
        try
        {
            InputStream input = new URL(serviceURL).openStream();
            Reader reader = new InputStreamReader(input, "UTF-8");
            data = new Gson().fromJson(reader, ObtainedData.class);
            reader.close();
            input.close();
        }
        catch (Exception e)
        {
            logger.log(Level.WARNING, "Cannot obtain a game", e);
            return null;
        }
        
        // game
        Game game = new Game(null, new Date(), data.getNumbersAsString(), GameType.SERVICE);
        game.setTypeDifficulty(null);
        game.setTypeServiceId(getService());
        
        // check if equal with the last game
        if (lastGame != null &&
            lastGame.getInitValues().equals(game.getInitValues()))
        {
            logger.log(Level.INFO, "Obtained game is equal to previous");
            return null;
        }
        
        return game;
    }
    
    /**
     * ObtainedData class for JSON data received friom the server.
     * Properties of the class correspondes to the JSON data.
     */
    public static class ObtainedData
    {
        private String difficulty;
        private String title;
        private String numbers;

        /**
         * An empty constructor
         */
        public ObtainedData()
        {
        }

        /**
         * Transform numbers from format without delimiters and a dot as blank
         * number to format supported by the app.
         * 
         * @return          Formated string
         */
        public String getNumbersAsString()
        {
            if (numbers != null && numbers.indexOf(",") == -1)
            {
                if (numbers.length() != 81)
                {
                    throw new IllegalArgumentException("Wrong numbers");
                }
                
                Integer[] values = new Integer[81];
                
                for (int i = 0; i < numbers.length(); i++)
                {
                    values[i] = null;
                    
                    if (numbers.charAt(i) >= '0' && numbers.charAt(i) <= '9')
                    {
                        values[i] = numbers.charAt(i) - '0';
                    }
                }
                
                numbers = GameUtil.join(values, ",");
            }
            
            return numbers;
        }
        
    }
    
}
