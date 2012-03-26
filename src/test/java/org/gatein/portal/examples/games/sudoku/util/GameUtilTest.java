/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : GameUtilTest.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Collection of tests of class GameUtil.
 *
 * @author Ondřej Fibich
 * @see GameUtil
 */
public class GameUtilTest
{

    private static final String[] gamesCorrect = {
        ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,",
        ",,,,,3,,,5,,8,,2,,,,4,7,7,,,,,,,,3,4,3,,,9,,,2,,8,,7,,3,2,5,9,6,,,9,1,,6,,,4,,,3" +
        ",,,,6,5,,9,,,,,,4,,2,,,,,,7,3,,9",
        "6,2,1,7,4,3,9,8,5,3,8,5,2,6,9,1,4,7,7,9,4,8,5,1,2,6,3,4,3,6,5,9,8,7,2,1,8,1,7,4," +
        "3,2,5,9,6,2,5,9,1,7,6,8,3,4,1,7,3,9,2,4,6,5,8,9,6,8,3,1,5,4,7,2,5,4,2,6,8,7,3,1,"
    };
    
    private static final String[] gamesIncorrect = {
        ",,1,,,,,,,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,",
        ",,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,1,,,,,,",
        ",,1,,,,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
    };
    
    private static final String[] gamesFinished = {
        "6,2,1,7,4,3,9,8,5,3,8,5,2,6,9,1,4,7,7,9,4,8,5,1,2,6,3,4,3,6,5,9,8,7,2,1,8,1,7,4," +
        "3,2,5,9,6,2,5,9,1,7,6,8,3,4,1,7,3,9,2,4,6,5,8,9,6,8,3,1,5,4,7,2,5,4,2,6,8,7,3,1,9"
    };
    
    /**
     * Test of check method, of class GameUtil.
     */
    @Test
    public void testCheck_String()
    {
        System.out.println("check");
        
        for (String game : gamesCorrect)
        {
            assertEquals(true, GameUtil.check(game).isEmpty());
        }
        
        for (String game : gamesIncorrect)
        {
            assertEquals(false, GameUtil.check(game).isEmpty());
        }
        
        for (String game : gamesFinished)
        {
            assertEquals(true, GameUtil.check(game).isEmpty());
        }
    }

    /**
     * Test of check method, of class GameUtil.
     */
    @Test
    public void testCheck_String_boolean()
    {
        System.out.println("check");
        
        for (String game : gamesCorrect)
        {
            assertEquals(false, GameUtil.check(game, true).isEmpty());
        }
        
        for (String game : gamesIncorrect)
        {
            assertEquals(false, GameUtil.check(game, true).isEmpty());
        }
        
        for (String game : gamesFinished)
        {
            assertEquals(true, GameUtil.check(game, true).isEmpty());
        }
    }

    /**
     * Test of join method, of class GameUtil.
     */
    @Test
    public void testJoin()
    {
        System.out.println("join");
        
        assertEquals("Empty test", "", GameUtil.join(null, null));
        assertEquals("Empty test2", "", GameUtil.join(new Object[] {}, null));
        assertEquals("Empty test3", "", GameUtil.join(null, ","));
        assertEquals("Empty test4", "", GameUtil.join(new Object[] {}, ","));
        
        assertEquals("Mixed content, no delimiter", "1o4.5", GameUtil.join(new Object[] {1, "o", 4.5}, null));
        assertEquals("Mixed content", "1,o,4.5", GameUtil.join(new Object[] {1, "o", 4.5}, ","));
        
        assertEquals("Mixed content, a null element", "1,o,4.5,", GameUtil.join(new Object[] {1, "o", 4.5, null}, ","));
    }
}
