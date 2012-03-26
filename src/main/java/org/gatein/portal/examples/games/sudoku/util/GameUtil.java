/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : GameUtil.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Game Util Class
 * Provides a checker of game solution, a join method, etc.
 *
 * @author Ondřej Fibich
 */
public class GameUtil
{
    /**
     * Check a solution of game for possible mistakes.
     * Empty values are not taken as an error.
     * 
     * @param game          A game to check
     * @return              List of incorrect fields
     */
    public static List<Integer> check(String game)
    {
        return check(game, false);
    }
    
    /**
     * Check a solution of game for possible mistakes.
     * 
     * @param game          A game to check
     * @param emptyMistake  Takes an empty value as an error
     * @return              List of incorrect fields
     */
    public static List<Integer> check(String game, boolean emptyMistake)
    {
        Integer[] fields = new Integer[81];
        String[] fieldsString = game.split(",", -1);
        List<Integer> incorrect = new ArrayList<Integer>(); 
        
        if (fieldsString.length != fields.length)
        {
            throw new IllegalArgumentException(
                    "Wrong count of game values for check (" +
                    fieldsString.length + ")"
            );
        }
        
        for (int i = 0; i < fields.length; i++)
        {
            if (fieldsString[i].length() > 0)
            {
                fields[i] = Integer.valueOf(fieldsString[i]);
            }
            else
            {
                fields[i] = null;
            }
        }
        
        for (int i = 0; i < fields.length; i++)
        {
            if (fields[i] != null && !fields[i].equals(0))
            {
                if (!checkField(fields, i))
                {
                    incorrect.add(new Integer(i));
                }
            }
            else if (emptyMistake && fields[i] == null)
            {
                incorrect.add(new Integer(i));
            }
        }
        
        return incorrect;
    }

    /**
     * Joins an array of objects with a delimiter to a single string.
     * 
     * @param array         An array to join
     * @param delimiter     A delimiter sequence
     * @return              A joined string
     */
    public static String join(Object[] array, String delimiter)
    {
        StringBuilder builder = new StringBuilder();
        final String empty = "";
        
        if (array != null)
        {
            for (int i = 0; i < array.length; i++)
            {
                builder.append(array[i] != null ? array[i] : empty);

                if ((i + 1) < array.length && delimiter != null)
                {
                    builder.append(delimiter);
                }
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
    public static boolean checkField(Integer[] game, int index)
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
