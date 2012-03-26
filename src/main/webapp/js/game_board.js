/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : game_board.js
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

/**
 * The Game Board class
 * 
 * @param gameParent    An instance of game which is a parent of the toolbar
 * @param rootElement   A root element of the game board
 * @return SudokuGame_GameBoard
 */
function SudokuGame_GameBoard(gameParent, rootElement)
{
    /** An instance of game which is a parent of the toolbar */
    var _parent;
    /** A root element of the game board */
    var _$root = null;
    /** A name of a root element of the game board */
    var _rootName = null;
    /** An enabled indicator */
    var _enabled = true;
    /** A game on indicator */
    var _gamePlayed = false;
    /** An array of game fields objects */
    var _fields = new Array(81);
    
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
     * Clears errors from fields
     */
    this.clearErrors = function ()
    {
        _$root.find('.sudoku-game_field-error').removeClass('sudoku-game_field-error');
    }
    
    /**
     * Gets the parent
     * 
     * @return          The parent
     */
    this.getParent = function ()
    {
        return _parent;
    }
    
    /**
     * Removed an error from a field.
     * 
     * @param $field    A field as a JQuery object
     */
    this.removeErrorFromField = function ($field)
    {
        if ($field.parent().hasClass('sudoku-game_field-error'))
        {
            $field.parent().removeClass('sudoku-game_field-error');
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
        var state = false;

        if ($field)
        {
            var index = parseInt($field.attr('name').substr('board_field['.length), 10);

            try
            {
                self.setField(index, $field.val());
                state = true;
            }
            catch (e)
            {   
                try
                {
                    self.setField(index, null);
                    $field.val('');
                }
                catch (ignore)
                {
                    console.log(ignore);
                }
            }
            
            self.removeErrorFromField($field);
        }
        
        // check if all fields are filled in
        if (state)
        {
            // all fields are filled in
            if (self.getParent().checkEnd())
            {
                _gamePlayed = false;
            }
        }
            
        return state;
    }
    
    /**
     * Resizes fields on the game board to calculated width and height.
     */
    this.resizeBoard = function ()
    {
        var $board_table = $(_rootName + ' table');

        if ($board_table)
        {
            var width = $board_table.width();
            // width / count of rows - borders sizes in pixels
            var item_width = Math.floor(width / 9 - 10);

            $board_table.css('height', width);

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
        default:
            // update of field values
            $this.trigger('change');
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
            _$root.find('input')
                    .removeAttr('disabled')
                    .unbind('focus')
                    .css('cursor', 'text');
        }
        else
        {
            _$root.find('input')
                    .attr('disabled', true)
                    .css('cursor', 'not-allowed')
                    .blur();
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
     * Sets fields of the game by values
     * 
     * @param game          A game in a form of a string where values are separated by commas
     * @param fixed         If pushed values should be fixed
     */
    this._setFields = function (game, fixed)
    {
        var fields = game.split(',');
        
        if (fields.length != _fields.length)
        {
            throw new SudokuGame_NullPointerException('Invalid game reprezentation');
        }
        
        for (var i = 0; i < _fields.length; i++)
        {
            if (fixed === true || !_fields[i].isFixed())
            {
                // set a fixed indicator if init values are pushed
                if (fixed === true)
                {
                    _fields[i].setFixed(fields[i].length == 1);
                }
                // set value
                _fields[i].setValue(fields[i]);
            }
        }
        
        _gamePlayed = true;
        
        this.render();
    }
    
    /**
     * Sets fields of the game by init values
     * 
     * @param game          A game in a form of a string where values are separated by commas
     */
    this.setInitFields = function (game)
    {
        this._setFields(game, true);
    }
    
    /**
     * Sets fields of the game by values
     * 
     * @param game          A game in a form of a string where values are separated by commas
     */
    this.setFields = function (game)
    {
        this._setFields(game, false);
    }
    
    /**
     * Sets a value of the field
     * 
     * @param index         A number of the field
     * @param value         A new value of the field
     */
    this.setField = function (index, value)
    {
        if (index >= 0 && index < _fields.length)
        {
            if (_fields[index].isFixed())
            {
                throw new SudokuGame_IllegalStateException(
                        'A value od a fixed field can not be changed.'
                );
            }
            
            if (value == '')
            {
                value = null;
            }
            
            if (value != null && !value.match(/^[1-9]{1}$/))
            {
                throw new SudokuGame_NullPointerException('An invalid value.');
            }
            
            _fields[index].setValue(value);
            
            this.render();
        }
    }
    
    /**
     * Gets a field
     *
     * @param index         An index of the field
     * @return              A field
     */
    this.getField = function (index)
    {
        if (index >= 0 && index < _fields.length)
        {
            return _fields[index]
        }
        
        throw new SudokuGame_NullPointerException('Wrong index: ' + index);
    }
    
    /**
     * Resets unfixed fields
     */
    this.resetUnfixedFields = function ()
    {
        for (var i = 0; i < _fields.length; i++)
        {
            if (!_fields[i].isFixed())
            {
                _fields[i].setValue(null);
            }
        }
        
        this.render();
    }
    
    /**
     * Gets values of fields
     * 
     * @return              Values as a string where values are separated by commas
     */
    this.getFieldsValues = function ()
    {
        var buffer = [];
        
        for (var i = 0; i < _fields.length; i++)
        {
            buffer.push(_fields[i].getValue());
        }
        
        return buffer.join(',');
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
            
            input.val(_fields[i].getValue());
            
            if (_fields[i].isFixed())
            {
                input.attr('readonly', 'readonly');
                input.parent().addClass('sudoku-game_readonly-input');
            }
        }
    }
    
    // CONSTRUCT START /////////////////////////////////////////////////////////
    
    if (!gameParent)
    {
        throw new SudokuGame_NullPointerException('An empty parent');
    }
    
    // set parent
    _parent = gameParent;
    
    // fields init
    for (var i = 0; i < _fields.length; i++)
    {
        _fields[i] = new SudokuGame_GameBoardField(null);
    }
    // set root element
    this.setRootElement(rootElement);
    // activate input check on fields
    $(_rootName + ' input').keypress(this._digitOnlyEvent)
    // movement on fields with arrows
    $(_rootName + ' input').keyup(this._arrowMovement);
    // validator of each field
    $(_rootName + ' input').change(this._fieldValidator);
    // change size of board
    this.resizeBoard();
    // hack for this in _fieldValidator
    var self = this;
    
    // CONTRUCT END    /////////////////////////////////////////////////////////
    
    // returns an instance of the class
    return this;
}

/**
 * Prints formated lasting in form minutes:seconds
 * 
 * @param lasting       A lasting in seconds
 */
function SudokuGame_lasting(lasting)
{
    var min = Math.floor(lasting / 60);
    var sec = Math.floor(lasting % 60);
    
    return min + ':' + (sec < 10 ? '0' : '') + sec;
}

/**
 * Prints formated date in form YYYY:mm:dd H:i:s
 * 
 * @param date         A date object
 */
function SudokuGame_dateFormat(date)
{
    var Y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    var time = date.toLocaleTimeString();
    
    return Y + '/' + (m <= 10 ? '0' : '') + m + '/'
        + (d <= 10 ? '0' : '') + d + ' ' + time;
}
