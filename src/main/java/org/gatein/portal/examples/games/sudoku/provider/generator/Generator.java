/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : Generator.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.provider.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.gatein.portal.examples.games.sudoku.entity.datatype.GameDifficulty;
import org.gatein.portal.examples.games.sudoku.util.GameUtil;

/**
 * The Generator class provides a functionality for generating sudoku games.
 *
 * @author Ondřej Fibich
 */
public class Generator
{
    /**
     * Generates an unsolved game with a defined difficulty
     * 
     * @param difficulty    A difficulty level of the game 
     * @return              A generated game as a string with values separated by comma
     */
    public static String generate(GameDifficulty difficulty)
    {
        Integer[] game = generateRandomGame();
        unfillFields(game, difficulty.getRandomUnfilledFieldCount());
        return GameUtil.join(game, ",");
    }
    
    /**
     * Generates a random solved game and return it.
     * 
     * @return              A generated game as one-dimensional array
     * @see http://en.wikipedia.org/wiki/Backtracking
     */
    private static Integer[] generateRandomGame()
    {
        Integer[] game = new Integer[81];
        List<List<Integer>> possibleValues = new ArrayList<List<Integer>>(81);
        final Integer[] allValues = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        final List<Integer> allValuesList = Arrays.asList(allValues);
        Random r = new Random();
        int index = 0, valueIndex;
        boolean nextIndex;
        
        // init possible values
        for (int i = 0; i < game.length; i++)
        {
            possibleValues.add(new ArrayList<Integer>(9));
            possibleValues.get(index).addAll(allValuesList);
        }
        
        // until all fields are filled in
        while (index < game.length)
        {
            do
            {
                // back tracking if there is not a possible value
                if (possibleValues.get(index).isEmpty())
                {
                    // re-init possible values for this index
                    possibleValues.get(index).clear();
                    possibleValues.get(index).addAll(allValuesList);
                    // erase incorect value
                    game[index] = null;
                    // disallow an iteration of the index
                    nextIndex = false;
                    // decrement the index
                    index--;
                    // return to the main while
                    break;
                }

                // allow an iteration of the index
                nextIndex = true;
                // set a next random value for possible values
                valueIndex = r.nextInt(possibleValues.get(index).size());
                game[index] = possibleValues.get(index).remove(valueIndex);
            }
            while (!GameUtil.checkField(game, index));

            // interate the index if allowed
            if (nextIndex)
            {
                index++;
            }
        }
        
        return game;
    }

    /**
     * Unfills a specified count of game fields
     * 
     * @param game          A solved game
     * @param count         A count of unfilled fields
     */
    private static void unfillFields(Integer[] game, int count)
    {
        Random r = new Random();
        
        while (count > 0)
        {
            int index = r.nextInt(81);
            
            if (game[index] != null)
            {
                game[index] = null;
                count--;
            }
        }
    }
}
