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
 * @param appPath          A path to the web application
 * @return SudokuGame_Game
 * @example game = new SudokuGame_Game('namespace', '/sudoku');
 */
function SudokuGame_Game(namespace, appPath)
{
    /** An instance of the timer */
    var _timer;
    /** An instance of the game board */
    var _gameBoard;
    /** An instance of the game toolbard */
    var _toolbar;
    /** A namespace identificator of the portlet */
    var _namespace;
    /** A path to the web application */
    var _appPath;
    /** An identificator of the solution in the database */
    var _gameSolutionId;
    
    /**
     * Gets an ID of the game solution
     * 
     * @return          An identificator of the solution in the database
     */
    this.getGameSolutionId = function ()
    {
        return _gameSolutionId;
    }
    
    /**
     * Sets an ID of the game solution
     * 
     * @param id        A new identificator of the solution in the database
     */
    this.setGameSolutionId = function (id)
    {
        _gameSolutionId = id;
    }
    
    
    /**
     * Gets a application path
     * 
     * @return          The app path
     */
    this.getAppPath = function ()
    {
        return _appPath;
    }
    
    /**
     * Gets a namespace identificator of the portlet
     * 
     * @return          A namespace
     */
    this.getNamespace = function ()
    {
        return _namespace;
    }
    
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
     * Gets a toolbar of the game 
     * 
     * @return SudokuGame_GameToolbar
     */
    this.getToolbar = function ()
    {
        return _toolbar;
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
        _toolbar.reenableButtons();
        
        if (_timer.isPaused())
        {
            this.pause();
            _gameBoard.render();
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
     * @param obj           A game solution object
     */
    this.start = function (obj)
    {
        if (obj)
        {
            // game solution
            if (obj['gameId'] != undefined)
            {
                _gameBoard.setInitFields(obj.gameId.initValues);
                _gameBoard.setFields(obj.values);
                this.setGameSolutionId(obj.id);
            }
            // game saved
            else if (obj['gameSolutionId'] != undefined)
            {
                _gameBoard.setInitFields(obj.gameSolutionId.gameId.initValues);
                _gameBoard.setFields(obj.values);
                this.setGameSolutionId(obj.gameSolutionId.id);
            }
        }
        
        _gameBoard.setEnabled(true);
        _toolbar.setButtonsEnable(1, true);
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
        
        this.store(true);
    }
    
    /**
     * Resets the game
     */
    this.reset = function ()
    {
        this.pause();
        _gameBoard.resetUnfixedFields();
        this.start();
    }
    
    /**
     * Stores the current game to the database
     * 
     * @param force         Store also an unstarted game?
     * @return              Stored? [optional]
     * @throws SudokuGame_RequestFailedException
     * @throws SudokuGame_IllegalStateException
     */
    this.store = function (force)
    {
        if (force || this.getGameBoard().isGamePlayed())
        {
            if (force && !this.getGameBoard().isGamePlayed())
            {
                return false;
            }
            
            var data = {
                id       : this.getGameSolutionId(),
                values   : this.getGameBoard().getFieldsValues(),
                lasting  : this.getTimer().getTimeout(),
                finished : false
            };
            
            var request = new SudokuGame_Request(this.getAppPath());
            request.makePut('/game_solution', data);
            
            return true;
        }
        else
        {
            throw new new SudokuGame_IllegalStateException('Can not store game');
        }
    }
    
    // CONSTRUCT START /////////////////////////////////////////////////////////
    
    if (!namespace)
    {
        throw new SudokuGame_NullPointerException('Empty namespace');
    }
    
    if (!appPath)
    {
        throw new SudokuGame_NullPointerException('Empty application path');
    }
    
    _namespace = namespace;
    _appPath = appPath;
    _timer = new SudokuGame_Timer(this, '#' + namespace + '_footer-timer');
    _gameBoard = new SudokuGame_GameBoard('#' + namespace + '_board');
    _toolbar = new SudokuGame_GameToolbar(this);
    
    // store on close
    var self = this;
    $(window).unload(function ()
    {
        self.store(true);
    });
    
    // CONTRUCT END    /////////////////////////////////////////////////////////
    
    // returns an instance of the class
    return this;
}
