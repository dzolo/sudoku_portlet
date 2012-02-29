/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : game_board_field.js
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */


/**
 * The Game Board Field class
 * 
 * @param value         A value of the field
 * @param fixed         An indicator which tells if the field is a part of init values
 * @return SudokuGame_GameBoardField
 */
function SudokuGame_GameBoardField(value, fixed)
{
    /** A value of the field */
    var _value;
    /** An indicator if field is  part of init values */
    var _fixed;
    
    /**
     * Sets a value of the field
     * 
     * @param value         A value of the field
     */
    this.setValue = function (value)
    {
        _value = value;
    }
    
    /**
     * Gets a value of the field
     * 
     * @return              A value of the field
     */
    this.getValue = function ()
    {
        return _value;
    }
    
    /**
     * Sets an indicator if a field
     * 
     * @param fixed         An indicator
     */
    this.setFixed = function (fixed)
    {
        _fixed = fixed === true;
    }
    
    
    /**
     * Gets an indicator of a field
     * 
     * @return               An indicator
     */
    this.isFixed = function ()
    {
        return _fixed;
    }
    
    // CONSTRUCT START /////////////////////////////////////////////////////////
    
    this.setValue(value);
    this.setFixed(fixed);
    
    // CONTRUCT END    /////////////////////////////////////////////////////////
    
    // returns an instance of the class
    return this;
}
