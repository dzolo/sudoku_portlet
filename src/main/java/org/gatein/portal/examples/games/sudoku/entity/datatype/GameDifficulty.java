/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : GameDifficulty.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.entity.datatype;

import java.util.Random;

/**
 * Game Difficulty Class
 * Specifies levels of a game difficulty
 *
 * @author Ondřej Fibich
 */
public enum GameDifficulty
{
    EASY(15, 25),
    MODERATE(30, 45),
    HARD(50, 55),
    EXPERT(60, 63);
    
    /**
     * A minimal count of unfilled filels in this difficulty level 
     */
    private int fromUnfilledFields;
    
    /**
     * A maximal count of unfilled fields in this difficulty level 
     */
    private int toUnfilledFields;
    
    /**
     * A construct, specifies counts of unfilled fields 
     * 
     * @param fromUnfilledFields    A minimal count
     * @param toUnfilledFields      A maximal count
     */
    private GameDifficulty(int fromUnfilledFields, int toUnfilledFields)
    {
        if (fromUnfilledFields > toUnfilledFields)
        {
            throw new IllegalArgumentException("Invalid volues of parameters");
        }
        
        this.fromUnfilledFields = fromUnfilledFields;
        this.toUnfilledFields = toUnfilledFields;
    }

    /**
     * Gets a minimal count of unfilled filels in this difficulty level
     * 
     * @return                      A minimal count
     */
    public int getFromUnfilledFieldCount()
    {
        return fromUnfilledFields;
    }

    /**
     * Gets a maximal count of unfilled filels in this difficulty level
     * 
     * @return                      A maximal count
     */
    public int getToUnfilledFieldCount()
    {
        return toUnfilledFields;
    }
    
    /**
     * 
     * 
     * @return 
     */
    public int getRandomUnfilledFieldCount()
    {
        Random r = new Random();
        return r.nextInt(toUnfilledFields - fromUnfilledFields + 1) + fromUnfilledFields;
    }
    
}
