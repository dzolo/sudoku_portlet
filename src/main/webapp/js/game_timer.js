/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : game_timer.js
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

/**
 * The Timer class.
 * 
 * @param gameParent        An instance of game which is a parent of the toolbar
 * @param rootElement   A root element of the timer
 * @return SudokuGame_GameTimer
 */
function SudokuGame_GameTimer(gameParent, rootElement)
{
    /** Root element of the game board */
    var _$root = null;
    /** Timeout identificator */
    var _timeout_id = null;
    /** Timeout in seconds */
    var _timeout = 0;
    /** Indicator of the pause game */
    var _paused = false;
    /** An instance of game which is a parent of the toolbar */
    var _parent;
    
    /**
     * Checks if the timer is paused
     * 
     * @return          true if paused, false otherwise
     */
    this.isPaused = function ()
    {
        return _paused;
    }
    
    /**
     * Checks if the timer is started
     * 
     * @return           true if starded, false otherwise
     */
    this.isStarted = function ()
    {
        return _timeout_id != null;
    }
    
    /**
     * Sets a root element
     * 
     * @param rootElement   A root element of the timer
     */
    this.setRootElement = function (rootElement)
    {
        _$root = $(rootElement);
    
        if (!_$root.length)
        {
            throw new SudokuGame_NullPointerException(
                    "An invalid root element of the timer"
            );
        }
    }
    
    /**
     * Sets a timeout of the timer
     * 
     * @param timeout       A new timeout in seconds
     */
    this.setTimeout = function (timeout)
    {
        if (timeout < 0)
        {
            throw new SudokuGame_NullPointerException("An invalid timeout value");
        }
        
        _timeout = timeout;
    }
    
    /**
     * Gets a timeout of the timer
     * 
     * @return             A timeout in seconds
     */
    this.getTimeout = function ()
    {
        return _timeout;
    }
    
    /**
     * Starts the timer
     * 
     * @param time      Beginning [optional]
     */
    this.start = function (time)
    {
        if (!time || time < 0)
        {
            time = _timeout;
        }
        
        // disable pause
        _paused = false;
        // change the timeout
        _timeout = time;
        // render
        _$root.show();
        this.render();
        
        // a timeout hack for running of a inner method of a class
        var self = this;
        _timeout_id = setTimeout(function () {self.start(time + 1)}, 1000);
        
        // auto-update of the database each 5 seconds
        if ((_timeout + 1) % 5 == 0)
        {
            try
            {
                _parent.store();
            }
            catch (e)
            {
                alert('Can not store the current game solution to the server.\nError: server timeout');
                _parent.pause(false);
            }
        }
    }
    
    /**
     * Pauses the timer
     */
    this.pause = function ()
    {
        if (_timeout_id)
        {
            // stop counting
            clearTimeout(_timeout_id);
            _timeout_id = null;
            _paused = true;
        }
        // render
        this.render();
    }
    
    /**
     * Renders the timer
     */
    this.render = function ()
    {
        var min = Math.floor(_timeout / 60);
        var sec = _timeout % 60;

        if (sec <= 9)
        {
            sec = '0' + sec;
        }

        _$root.text(min + ':' + sec);
    }
    
    // CONSTRUCT START /////////////////////////////////////////////////////////
    
    if (!gameParent)
    {
        throw new SudokuGame_NullPointerException('An empty parent');
    }
    
    // set parent
    _parent = gameParent;
    
    this.setRootElement(rootElement);
    this.render();
    
    // CONTRUCT END    /////////////////////////////////////////////////////////
    
    // returns an instance of the class
    return this;
}
