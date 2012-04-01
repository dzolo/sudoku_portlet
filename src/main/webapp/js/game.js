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
    /** An identificator of the game in the database */
    var _game;
    /** An identificator of the solution in the database */
    var _gameSolutionId;
    /** An indicator of finished game */
    var _finished = false;
    
    /**
     * Gets the game
     * 
     * @return          The game
     */
    this.getGame = function ()
    {
        return _game;
    }
    
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
     * 
     * @param loadLastPlayedSolution   An indicator of loading last played game [optional]
     */
    this.init = function (loadLastPlayedSolution)
    {
        this.reloadRootElements();
        _gameBoard.init();
        _toolbar.reenableButtons();
        
        if (_timer.isPaused())
        {
            if (!_finished)
            {
                this.pause();
            }
            else
            {
                _gameBoard.setEnabled(false);
                $('#' + _namespace + '_footer-pause').hide();
                $('#' + _namespace + '_footer-play').hide();
                $('#' + _namespace + '_footer-timer').hide();
            }
            
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
                if (loadLastPlayedSolution)
                {
                    var request = new SudokuGame_Request(_appPath);

                    try
                    {
                        var data = request.makeGet('/last_played_game_solution/' + SudokuGame_userId);
                        
                        if (data.gameSolutionId.finished > 0)
                        {
                            this.getGameBoard().setInitFields(data.gameSolutionId.gameId.initValues);
                            this.getGameBoard().setFields(data.gameSolutionId.values, false);
                            this.getGameBoard().setGamePlayed(false);
                            this.setGameSolutionId(data.gameSolutionId.id);
                            _game = data.gameSolutionId.gameId;
                            _finished = true;
                        }
                        else
                        {
                            this.start(data);
                            return;
                        }
                    }
                    catch (e)
                    {
                        if (!(e.cause && e.cause == 'Not Found'))
                        {
                            alert('Error during loading of a previous played game. Error: ' + e);
                        }
                    }
                }
                
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
                _game = obj.gameId;
                this.setGameSolutionId(obj.id);
            }
            // game saved
            else if (obj['gameSolutionId'] != undefined)
            {
                _gameBoard.setInitFields(obj.gameSolutionId.gameId.initValues);
                _gameBoard.setFields(obj.gameSolutionId.values);
                _game = obj.gameSolutionId.gameId;
                this.setGameSolutionId(obj.gameSolutionId.id);
                this.getTimer().setTimeout(obj.gameSolutionId.lasting);
            }
            
            _gameBoard.clearErrors();
            
            // change the last played game for a user
            if (SudokuGame_userId != null)
            {
                (new SudokuGame_Request(_appPath)).makePost(
                    '/last_played_game_solution/' + _gameSolutionId, {
                        'userId' : SudokuGame_userId
                    }
                );
            }
        }
        
        _finished = false;
        _gameBoard.setEnabled(true);
        _toolbar.setButtonsEnable(1, true);
        _timer.start();
        
        $('#' + _namespace + '_footer-pause').show();
        $('#' + _namespace + '_footer-play').hide();
        
        // reload statisctics
        SudokuGame_loadStatistics(this.getNamespace());
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
        _gameBoard.clearErrors();
        this.start();
    }
    
    /**
     * Stores the current game to the database
     * 
     * @param force         Store also an unstarted game?
     * @param end           Store the end of a game?
     * @param rating        A rating of a game
     * @return              Stored? [optional]
     * @throws SudokuGame_RequestFailedException
     * @throws SudokuGame_IllegalStateException
     */
    this.store = function (force, end, rating)
    {
        if (force || this.getGameBoard().isGamePlayed())
        {            
            if (force && !this.getGameBoard().isGamePlayed() && rating == undefined)
            {
                return false;
            }
            
            var data = {
                id       : this.getGameSolutionId(),
                values   : this.getGameBoard().getFieldsValues(),
                lasting  : this.getTimer().getTimeout(),
                finished : (end === true)
            };
            
            if (rating != undefined)
            {
                data.rating = rating;
            }
            
            var request = new SudokuGame_Request(this.getAppPath());
            request.makePut('/game_solution', data);
            
            return true;
        }
        else
        {
            throw new SudokuGame_IllegalStateException('Can not store game');
        }
    }
    
    /**
     * Check if the game was solved
     * 
     * @return              The game is solved?
     */
    this.checkEnd = function ()
    {
        // check if all fields are filled in, end if not
        for (var i = 0; i < 81; i++)
        {
            if (!this.getGameBoard().getField(i).hasValue())
            {
                return false;
            }
        }
        
        // pause the game
        this.pause();
        
        // check the solution
        var request = new SudokuGame_Request(this.getAppPath());
        var game = this.getGameBoard().getFieldsValues();

        try
        {
            var respObj = request.makePostText('/game_solution/check', game);

            if (!respObj.state)
            {
                throw respObj.message;
            }

            // an invalid solution
            if (!respObj.check.valid)
            {
                this.start();
            }
            // a correct solution!
            else
            {
                this._end();
                return true;
            }
        }
        catch (e)
        {
            alert('Can not check the current game.\nError: ' + e);
            this.start();
        }
        
        return false;
    }
    
    /**
     * Ends the solved game
     */
    this._end = function ()
    {
        var $dialog = $('#' + self.getNamespace() + '_dialog-end');
            
        try
        {
            // disable buttons
            $('#' + _namespace + '_footer-play').hide();
            this.getToolbar().setButtonsEnable(0, true);
            this.getToolbar().setButtonsEnable(1, false);
            _finished = true;
            
            // store the solution
            this.store(true, true);
            
            // build the end dialog
            $dialog.html(
                $('<p>').append(
                    $('<b>').html('Congradulations! You succesfully solved this game. ')
                ).append(
                    $('<span>').html('It tooks you ' + this.getTimer().getTimeout() + ' seconds.')
                )
            ).append($('<p>').html('Did you enjoy this game? Please, rate it.'));
            
            // init a close action of the dialog
            $dialog.dialog().bind('dialogclose', function(e)
            {
                $(this).unbind('dialogclose');                
                $(this).html('');
                // show statistics of the game
                $('#' + self.getNamespace() + '_footer-statistics').trigger('click');
            });
                
            // rating
            $dialog.append($('<div>').raty({
                starOn   : this.getAppPath() + '/images/icons/star-on.png',
                starOff  : this.getAppPath() + '/images/icons/star-off.png',
                click    : function ()
                {
                    var rating = $(this).raty('score');
                    
                    if (rating != null && rating <= 5 && rating > 0)
                    {
                        $(this).raty('readOnly', true);
                        
                        try
                        {
                            self.store(true, true, rating);
                        }
                        catch (ignore)
                        {
                        }
                    }
                    
                    $dialog.dialog('close');
                    
                    // reload statistics
                    SudokuGame_loadStatistics(self.getNamespace());
                }
            }));
            
            // open
            $dialog.dialog('open');
        }
        catch (e)
        {
            alert('Can not end the game. Error: ' + e);
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
    
    var self = this;
    
    _namespace = namespace;
    _appPath = appPath;
    _timer = new SudokuGame_Timer(this, '#' + namespace + '_footer-timer');
    _gameBoard = new SudokuGame_GameBoard(this, '#' + namespace + '_board');
    _toolbar = new SudokuGame_GameToolbar(this);
    
    // store on close
    $(window).unload(function ()
    {
        self.store(true);
    });
    
    // CONTRUCT END    /////////////////////////////////////////////////////////
    
    // returns an instance of the class
    return this;
}
