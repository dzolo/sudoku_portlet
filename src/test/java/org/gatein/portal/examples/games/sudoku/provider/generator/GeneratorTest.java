/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : GeneratorTest.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.provider.generator;

import org.gatein.portal.examples.games.sudoku.entity.datatype.GameDifficulty;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Collection of tests of class Generator.
 *
 * @author Ondřej Fibich
 * @see Generator
 */
public class GeneratorTest
{
    /**
     * Test of generate method, of class Generator.
     */
    @Test
    public void testGenerate()
    {
        System.out.println("generate");
        GameDifficulty difficulty = GameDifficulty.EASY;
        String result = Generator.generate(difficulty);
        assertEquals("Generate test", true, result.matches("^([1-9]?,){80}[0-9]?$"));
    }
}
