/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : game.js
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

/**
 * The Game class
 * 
 * @param namespace        A namespace identificator of the portlet
 * @return SudokuGame_Game
 * @example game = new SudokuGame_Game({timerEl:'#elT',gameBoardEl:'#elGB'});
 */
function SudokuGame_Game(namespace)
{
    /** An instance of the timer */
    var _timer;
    /** An instance of the game board */
    var _gameBoard;
    /** A namespace identificator of the portlet */
    var _namespace;
    
    /**
     * Gets a timer of the game 
     * 
     * @return SudokuGame_Timer
     */
    this.getTimer = function ()
    {
        return _timer;
    }
    
    /**
     * Gets a game board of the game 
     * 
     * @return SudokuGame_GameBoard
     */
    this.getGameBoard = function ()
    {
        return _gameBoard;
    }
    
    /**
     * Reload root elements of the timer and the game board
     */
    this.reloadRootElements = function ()
    {
        _timer.setRootElement('#' + _namespace + '_footer-timer');
        _gameBoard.setRootElement('#' + _namespace + '_board');
    }
    
    /**
     * Inits an instantized game
     */
    this.init = function ()
    {
        this.reloadRootElements();
        _gameBoard.resizeBoard();
        
        if (_timer.isPaused())
        {
            this.pause();
        }
        else if (!_timer.isStarted())
        {
            if (_gameBoard.isGamePlayed())
            {
                this.start();
            }
            else
            {
                _gameBoard.setEnabled(false);
                $('#' + _namespace + '_footer-pause').hide();
                $('#' + _namespace + '_footer-play').hide();
                $('#' + _namespace + '_footer-timer').hide();
            }
        }
        else
        {
            _gameBoard.render();
        }
    }
    
    /**
     * Starts the game
     * 
     * @param initValues    Init values of the game [optional]
     */
    this.start = function (initValues)
    {
        if (initValues)
        {
            _gameBoard.setFields(initValues);
        }
        
        _gameBoard.setEnabled(true);
        _timer.start();
        $('#' + _namespace + '_footer-pause').show();
        $('#' + _namespace + '_footer-play').hide();
    }
    
    /**
     * Pauses the game
     */
    this.pause = function ()
    {
        _gameBoard.setEnabled(false);
        _timer.pause();
        $('#' + _namespace + '_footer-play').show();
        $('#' + _namespace + '_footer-pause').hide();
    }
    
    // CONSTRUCT START /////////////////////////////////////////////////////////
    
    if (!namespace)
    {
        throw new SudokuGame_NullPointerException('Empty namespace');
    }
    
    _namespace = namespace;
    _timer = new SudokuGame_Timer('#' + namespace + '_footer-timer');
    _gameBoard = new SudokuGame_GameBoard('#' + namespace + '_board');
    
    // CONTRUCT END    /////////////////////////////////////////////////////////
    
    // returns an instance of the class
    return this;
}
