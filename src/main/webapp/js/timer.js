/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : timer.js
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

/**
 * The Timer class.
 * 
 * @param rootElement   A root element of the timer
 * @param startEvent    A start event function
 * @param pauseEvent    A pause event function
 * @return SudokuGame_Timer
 */
function SudokuGame_Timer(rootElement, startEvent, pauseEvent)
{
    /** Root element of the game board */
    var _$root = null;
    /** Timeout identificator */
    var _timeout_id = null;
    /** Timeout in seconds */
    var _timeout = 0;
    /** Indicator of the pause game */
    var _paused = false;
    /** Start event function */
    var _startEventFnc = null;
    /** Pause event function */
    var _pauseEventFnc = null;
    
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
     * Sets a start event function
     * 
     * @param fnc         A start event function
     */
    this.setStartEvent = function (fnc)
    {
        _startEventFnc = fnc;
    }
    
    /**
     * Sets a pause event function
     * 
     * @param fnc          A pause event function
     */
    this.setPauseEvent = function (fnc)
    {
        _pauseEventFnc = fnc;
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
        this.render();
        // trigger the start event
        if (_startEventFnc)
        {
            eval(_startEventFnc + '()');
        }
        
        // a timeout hack for running of a inner method of a class
        var self = this;
        _timeout_id = setTimeout(function () {self.start(time + 1)}, 1000);
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
        // trigger the pause event
        if (_pauseEventFnc)
        {
            eval(_pauseEventFnc + '()');
        }
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
    
    this.setRootElement(rootElement);
    this.setStartEvent(startEvent);
    this.setPauseEvent(pauseEvent);
    this.render();
    
    // CONTRUCT END    /////////////////////////////////////////////////////////
    
    // returns an instance of the class
    return this;
}
