/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : GameGenerator.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.gatein.portal.examples.games.sudoku.entity.Game;
import org.gatein.portal.examples.games.sudoku.entity.GameDifficulty;

/**
 * Sudoku Game Generator
 *
 * @author Ondřej Fibich
 */
public class GameGenerator
{
    
    /**
     * Generates a Sudoku game with a defined difficulty
     * 
     * @param difficulty    A difficulty level of the game 
     * @return              A generated game as a string with values separated by comma
     */
    public static Game generate(GameDifficulty difficulty)
    {
        Game game = new Game();
        Integer[] gameFields = generateRandomGame();
        unfillFields(gameFields, difficulty.getRandomUnfilledFieldCount());
        game.setInitValues(join(gameFields, ","));
        game.setTypeDificulty(difficulty);
        // @TODO: game.setType();
        return game;
    }
    
    /**
     * Generates a random game and return it.
     * 
     * @return              A generated game as one-dimensional array
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
            while (!checkField(game, index));

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
     * @param gameFields    Pre-filled fields
     * @param count         A count of unfilled fields
     */
    private static void unfillFields(Integer[] gameFields, int count)
    {
        Random r = new Random();
        
        while (count > 0)
        {
            int index = r.nextInt(81);
            
            if (gameFields[index] != null)
            {
                gameFields[index] = null;
                count--;
            }
        }
    }

    /**
     * Joins an array of objects with a delimiter to a single string.
     * 
     * @param array         An array to join
     * @param delimiter     A delimiter sequence
     * @return              A joined string
     */
    protected static String join(Object[] array, String delimiter)
    {
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < array.length; i++)
        {
            builder.append(array[i]);
            
            if ((i + 1) < array.length)
            {
                builder.append(delimiter);
            }
        }
        
        return builder.toString();
    }

    /**
     * Check if the field is valid by a value, a row, a column and a group.
     * 
     * @param game           A game witch contains the checked field
     * @param index          An index of the field in the game
     * @return               True if the field has a valid value, false otherwise
     */
    private static boolean checkField(Integer[] game, int index)
    {
        final Integer value = game[index];
        int x = index / 9;
        int y = index % 9;
        int groupX = (x / 3) * 3;
        int groupY = (y / 3) * 3;

        // checks a row and a col
        for (int i = 0; i < 9; i++)
        {
            if (value.equals(game[x * 9 + i]) && y != i)
            {
                return false;
            }

            if (value.equals(game[i * 9 + y]) && x != i)
            {
                return false;
            }
        }

        // checks a group 3x3
        for (int i = groupX; i < (groupX + 3); i++)
        {
            for (int u = groupY; u < (groupY + 3); u++)
            {
                if (value.equals(game[i * 9 + u]) && (x != i && y != u))
                {
                    return false;
                }
            }
        }

        // all checked
        return true;
    }
    
}
