/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : game_board.js
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

/**
 * The Game Board class
 * 
 * @param rootElement   A root element of the game board
 * @return SudokuGame_GameBoard
 */
function SudokuGame_GameBoard(rootElement)
{
    /** A root element of the game board */
    var _$root = null;
    /** A name of a root element of the game board */
    var _rootName = null;
    /** An enabled indicator */
    var _enabled = true;
    /** A game on indicator */
    var _gamePlayed = false;
    /** An array of game fields objects */
    var _fields;
    
    /**
     * On key press event hanler. Enables just one digit in a field.
     * 
     * @param e         Key press event
     * @return          Bool value - true for accepting
     */
    this._digitOnlyEvent = function (e)
    {
        var key = e.keyCode ? e.keyCode : e.which;

        // control keys
        if ((key == null) || (key == 0) || (key == 8) || 
            (key == 9) || (key == 13) || (key == 27))
        {
            return true;
        }
        // digit
        else if ((('123456789').indexOf(String.fromCharCode(key)) > -1))
        {
            // just one digit
            return ($(this).val().length == 0);
        }

        return false;
    }
    
    /**
     * Adds an error to a field.
     * 
     * @param $field    A field as a JQuery object
     * @param focus     An indicator of a focus on the error field
     */
    this.addErrorToField = function ($field, focus)
    {
        if (!$field.parent().hasClass('sudoku-game_field-error'))
        {
            $field.parent().addClass('sudoku-game_field-error');
        }

        if (focus)
        {
            $field.focus();
        }
    }

    /**
     * Validates a content of a field.
     * 
     * @return          true if the field is valid, false otherwise 
     */
    this._fieldValidator = function()
    {
        var $field = $(this);

        if ($field)
        {
            if ($field.val().length && !$field.val().match(/^[1-9]{1}$/))
            {
                this.addErrorToField($field, false);
                return false;
            }
            else if ($field.parent().hasClass('sudoku-game_field-error'))
            {
                $field.parent().removeClass('sudoku-game_field-error');
            }
        }
        
        return true;
    }
    
    /**
     * Resizes fields on the game board to calculated width and height.
     */
    this.resizeBoard = function ()
    {
        var $board_table = $(_rootName + ' table');
        var $board_locker = $(_rootName + '-locker');

        if ($board_table)
        {
            var width = $board_table.width();
            // width / count of rows - borders sizes in pixels
            var item_width = Math.floor(width / 9 - 10);

            $board_table.css('height', width);
            $board_locker.css({
                'width'  : width,
                'height' : width
            });

            $board_table.find('td input').css({
                'fontSize' : Math.round(item_width * 2 / 3)
            });

            // fix for IE7
            if ($.browser.msie)
            {
                $board_table.find('td').css({
                    'width' : item_width
                });
            }
        }
    }
    
    /**
     * On key up event hanler. Enables an movement by arrows over gameboard fields.
     * 
     * @param e         Key press up
     */
    this._arrowMovement = function (e)
    {
        var key = e.keyCode ? e.keyCode : e.which;
        var $this = $(this);
        var index = parseInt($this.attr('name').substr('board_field['.length), 10);
        var arrow = false;

        switch (key)
        {
        case 37: // left
            arrow = true;
            if (index % 9)
                index--;
            else
                index += 8;
            break;
        case 38: // up
            arrow = true;
            index -= 9;
            break;
        case 39: // right
            arrow = true;
            if ((index + 1) % 9)
                index++;
            else
                index -= 8;
            break;
        case 40: // down
            arrow = true;
            index += 9;
            break;
        }

        if (arrow)
        {
            if (index < 0)
            {
                index += 81;    
            }
            else if (index > 80)
            {
                index -= 81;
            }

            var focusField = 'input[name="board_field[' + index + ']"]';
            $this.parent().parent().parent().find(focusField).focus();
        }
    }
    
    /**
     * Sets a state of the game board - capability to play
     * 
     * @param enabled   true for enable, false for disabled
     */
    this.setEnabled = function (enabled)
    {
        _enabled = (enabled) ? true : false;

        if (_enabled)
        {
            $(_rootName + '-locker').hide();
            $(_rootName).css('opacity', 1.0);
            $(_rootName + ' input').removeAttr('disabled');
            $(_rootName + ' input').unbind('focus');
        }
        else
        {
            $(_rootName + '-locker').show();
            $(_rootName).css('opacity', 0.5);
            $(_rootName + ' input').attr('disabled', true);
            $(_rootName + ' input').blur();
        }
    }
    
    /**
     * Gets a state of the game board - capability to play
     * 
     * @return           true for enable, false for disabled
     */
    this.isEnabled = function ()
    {
        return _enabled;
    }
    
    /**
     * Gets a state of the game board - a game is on
     * 
     * @return           true for a game is on, false for otherwise
     */
    this.isGamePlayed = function ()
    {
        return _gamePlayed;
    }
    
    /**
     * Sets a root element
     * 
     * @param rootElement   A root element of the game board
     */
    this.setRootElement = function (rootElement)
    {
        _$root = $(rootElement);
        _rootName = rootElement;
    
        if (!_$root.length)
        {
            throw new SudokuGame_NullPointerException(
                    'An invalid root element of the game board'
            );
        }
    }
    
    /**
     * Sets fields of the game
     * 
     * @param game          A game in a form of a string where values are separated by commas
     */
    this.setFields = function (game)
    {
        var fields = game.split(',');
        
        if (fields.length != 81)
        {
            throw new SudokuGame_NullPointerException('Invalid game reprezentation');
        }
        
        _fields = fields;
        _gamePlayed = true;
        
        this.render();
    }
    
    /**
     * Renders fields fo the game board
     */
    this.render = function ()
    {
        _$root.find('input').removeAttr('readonly');
        _$root.find('.sudoku-game_readonly-input').removeClass('sudoku-game_readonly-input');
        
        for (var i = 0; i < _fields.length; i++)
        {
            var input = _$root.find('input[name="board_field[' + i + ']"]');
            
            input.val(_fields[i]);
            
            if (_fields[i])
            {
                input.attr('readonly', 'readonly');
                input.parent().addClass('sudoku-game_readonly-input');
            }
        }
    }
    
    // CONSTRUCT START /////////////////////////////////////////////////////////
    
    // set root element
    this.setRootElement(rootElement);
    // activate input check on fields
    $(_rootName + ' input').keypress(this._digitOnlyEvent)
    // movement on fields with arrows
    $(_rootName + ' input').keyup(this._arrowMovement);
    // validator of each field
    $(_rootName + ' input').blur(this._fieldValidator);
    // change size of board
    this.resizeBoard();
    
    // CONTRUCT END    /////////////////////////////////////////////////////////
    
    // returns an instance of the class
    return this;
}
