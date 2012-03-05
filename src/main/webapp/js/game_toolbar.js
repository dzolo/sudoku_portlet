/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : game_toolbar.js
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

/**
 * The Game Toolbar class
 * 
 * @param gameParent        An instance of game which is a parent of the toolbar
 * @return SudokuGame_GameToolbar
 */
function SudokuGame_GameToolbar(gameParent)
{
    /** An instance of game which is a parent of the toolbar */
    var _parent;
    /** Items of the toolbar */
    var _buttons;
    
    /**
     * Gets a button by an ID
     * 
     * @param buttonId      An ID of button (an index of the _items object)
     */ 
    this.getButton = function (buttonId)
    {
        return $('#' + _parent.getNamespace() + '_' + buttonId);
    }
    
    /**
     * Enables buttons of the toolbar
     * 
     * @param group         Specifies buttons for an enable action:
     *                      0 (all) 1 (game related) 2 (not related to a game)
     * @param enable        Enabled/disable indicator
     */
    this.setButtonsEnable = function (group, enable)
    {
        switch (parseInt(group, 10))
        {
            // game related
            case 1:
                
                for (var i in _buttons)
                {
                    if (_buttons[i].gameRelated)
                    {
                        this.setButtonEnable(_buttons[i].id, enable);
                    }
                }
                
                break;
            // not related to a game
            case 2:
                
                for (i in _buttons)
                {
                    if (!_buttons[i].gameRelated)
                    {
                        this.setButtonEnable(_buttons[i].id, enable);
                    }
                }
                
                break;
            // all
            default:
                
                for (i in _buttons)
                {
                    this.setButtonEnable(_buttons[i].id, enable);
                }
        }
    }
    
    /**
     * Enables button
     * 
     * @param buttonId      An ID of button (an index of the _items object)
     * @param enable        Enabled/disable indicator
     */
    this.setButtonEnable = function (buttonId, enable)
    {
        var $btn = this.getButton(buttonId);
        
        if ($btn.length)
        {
            $btn.unbind('click');
            
            if (enable)
            {
                $btn.click(this._actionHandler);
                
                if ($btn.hasClass('sudoku-game_button-disabled'))
                {
                    $btn.removeClass('sudoku-game_button-disabled');
                }
            }
            else
            {
                $btn.click(this._actionVoidHandler);
                
                if (!$btn.hasClass('sudoku-game_button-disabled'))
                {
                    $btn.addClass('sudoku-game_button-disabled');
                }
            }
        }
    }
    
    /**
     * Handles actions of buttons
     */
    this._actionHandler = function ()
    {
        var $btn = $(this);
        
        for (var i in _buttons)
        {
            if ($btn.attr('id').match(_buttons[i].id))
            {
                self.setButtonEnable(_buttons[i].id, false);
                _buttons[i].action();
                self.setButtonEnable(_buttons[i].id, true);
                break;
            }
        }
        
        return false;
    }
    
    /**
     * Does not handle actions of buttons
     */
    this._actionVoidHandler = function ()
    {
        return false;
    }
    
    // CONSTRUCT START /////////////////////////////////////////////////////////
    
    if (!gameParent)
    {
        throw new SudokuGame_NullPointerException('An empty parent');
    }
    
    // set parent
    _parent = gameParent;
    // inits items
    _buttons = {
        bnew: {
            id          : 'button_new',
            gameRelated : false,
            action      : function ()
            {
                var request = new SudokuGame_Request(_parent.getAppPath());
                var data, id;

                try
                {
                    // create a game
                    data = request.makePost('/game', {typeDifficulty: 'EXPERT'});

                    // get an ID of the created game
                    id = data.location.split('/').pop();
                    // get the created game
                    data = request.makeGet('/game/' + id);

                    // create a game solution of the created game
                    data = request.makePost('/game_solution/' + id, {
                        userId     : SudokuGame_userId, // global property
                        userName   : null,
                        values     : data.initValues
                    });

                    // get an ID of the created game solution
                    id = data.location.split('/').pop();
                    // get the created solution
                    data = request.makeGet('/game_solution/' + id);

                    // start the game
                    _parent.pause();
                    _parent.getTimer().setTimeout(0);
                    _parent.start(data);
                }
                catch (e)
                {
                    alert('Can not create a new game.\nError: ' + e.toString());
                }
            }
        },
        breset: {
            id          : 'button_reset',
            gameRelated : true,
            action      : function ()
            {
                _parent.reset();
            }
        },
        bcheck: {
            id          : 'button_check',
            gameRelated : true,
            action      : function ()
            {
                _parent.pause();
                
                var request = new SudokuGame_Request(_parent.getAppPath());
                var game = _parent.getGameBoard().getFieldsValues();

                try
                {
                    var respObj = request.makePostText('/game_solution/check', game);

                    if (!respObj.state)
                    {
                        throw respObj.message;
                    }

                    if (respObj.check.valid)
                    {
                        return false;
                    }

                    for (var i = 0; i < respObj.check.fields.length; i++)
                    {
                        var index = respObj.check.fields[i];

                        if (!_parent.getGameBoard().getField(index).isFixed())
                        {
                            var id = '#' + _parent.getNamespace() + '_board ' +
                                     'input[name="board_field[' + index + ']"]';

                            _parent.getGameBoard().addErrorToField($(id), false);
                        }
                    }
                }
                catch (e)
                {
                    alert('Can not check the current game.\nError: ' + e);
                }
                finally
                {
                    _parent.start();
                }
            }
        },
        bsave: {
            id          : 'button_save',
            gameRelated : true,
            action      : function ()
            {
                var $dialog = $('#' + _parent.getNamespace() + '_dialog-save');
                var $dialogBody = $dialog.find('._ui-dialog-body');
                var $dialogInfo = $dialog.find('.ui-state-box');
                var $dialogInput = $dialog.find('input');
                var m = 'The current game was successfully saved.';
                
                _parent.pause();
                $dialogBody.show();
                $dialogInfo.hide();
                
                $dialog.dialog({
                    buttons: [{
                        text    : 'Save',
                        click   : function()
                        {
                            // check
                            if (!$dialogInput.val().length) 
                            {
                                $dialogInput.focus();
                                return;
                            }
                            
                            // hide body and buttons
                            $dialogBody.fadeOut('slow');
                            $dialog.dialog({buttons: []});
                            
                            // save
                            var request = new SudokuGame_Request(_parent.getAppPath());
                            var inputData = {
                                name        : $dialogInput.val(),
                                values      : _parent.getGameBoard().getFieldsValues(),
                                lasting     : _parent.getTimer().getTimeout()
                            };
                            
                            try
                            {
                                // create a saved game
                                request.makePost('/saved_game/' + _parent.getGameSolutionId(), inputData);
                            }
                            catch (e)
                            {
                                m = 'The current game was not saved. Error: ' + e
                                
                                $dialogInfo.stateBox('Game not saved', m, 'error', 'alert').fadeIn('slow', function ()
                                {
                                    setTimeout(function ()
                                    {
                                        $dialog.dialog('close');
                                    }, 6000);
                                });
                                
                                return;
                            }
                            
                            // show a notification and hide the dialog
                            $dialogInfo.stateBox('Game saved', m).fadeIn('slow', function ()
                            {
                                setTimeout(function ()
                                {
                                    $dialog.dialog('close');
                                }, 3000);
                            });
                        }
                    },
                    {
                        text    : 'Cancel',
                        click   : function()
                        {
                            $(this).dialog('close');
                        }
                    }]
                }).bind('dialogclose', function(e)
                {
                    $(this).unbind('dialogclose');
                    _parent.start();
                }).dialog('open');
            }
        },
        bload: {
            id          : 'button_load',
            gameRelated : false,
            oTable      : null,
            action      : function ()
            {
                var request = new SudokuGame_Request(_parent.getAppPath());
                var $dialog = $('#' + _parent.getNamespace() + '_dialog-load');
                var $dialogTable = $dialog.find('table');
                var $dialogTableBody = $dialogTable.find('tbody');
                var data, start = false;
                
                // pause the game
                if (_parent.getTimer().isStarted())
                {
                    _parent.pause();
                    start = true;
                }
                
                // get data
                try
                {
                    data = request.makeGet('/saved_game/user/' + SudokuGame_userId);
                }
                catch (e)
                {
                    alert('Can not load a game. Error: ' + e.toString());
                    
                    if (start)
                    {
                        _parent.start();
                    }
                    
                    return;
                }
                
                // clear the table
                if (this.oTable)
                {
                    this.oTable.fnClearTable();
                }
                
                // put data into the table
                for (var i = 0; i < data.length; i++)
                {
                    var d = new Date(data[i].saved);
                    var dstr = d.toLocaleFormat('%Y/%m/%d %H:%M:%S');
                    
                    $dialogTableBody.append(
                        $('<tr>').append(
                            $('<td>').text(data[i].id)
                        ).append(
                            $('<td>').text(data[i].name)
                        ).append(
                            $('<td>').text(dstr)
                        ).append(
                            $('<td>').text(data[i].lasting)
                        )
                    );
                }
                
                // init the data table
                var _oTable = this.oTable = $dialogTable.dataTable({
                    'sDom'              : 't<"F"fp>',
                    'aaSorting'         : [[ 2, 'desc' ]],
                    'sPaginationType'   : 'full_numbers',
                    'bJQueryUI'         : true,
                    'bDestroy'          : true
                });
                
                // init and open the dialog
                $dialog.dialog({
                    buttons: [{
                        text     : 'Load',
                        disabled : true,
                        click    : function()
                        {
                            var sel = _oTable.$('tr.row_selected');
                            var id = parseInt($(sel.find('td')[0]).html(), 10);
                            var request = new SudokuGame_Request(_parent.getAppPath());

                            try
                            {
                                // get the created solution
                                var data = request.makeGet('/saved_game/' + id);

                                // start the game
                                _parent.pause();
                                _parent.getTimer().setTimeout(data.lasting);
                                _parent.start(data);
                            }
                            catch (e)
                            {
                                alert('Can not load a game.\nError: ' + e.toString());
                                $(this).dialog('close');
                                return;
                            }
                            
                            // close dialog without an event
                            $(this).unbind('dialogclose');
                            $(this).dialog('close');
                        }
                    },
                    {
                        text    : 'Cancel',
                        click   : function()
                        {
                            $(this).dialog('close');
                        }
                    }]
                }).bind('dialogclose', function(e)
                {
                    $(this).unbind('dialogclose');
                    
                    if (start)
                    {
                        _parent.start();
                    }
                }).dialog('open');
                
                // line select
                $dialogTableBody.find('tr').click(function()
                {
                    var btn = $('.ui-dialog-buttonpane button:contains("Load")');
                    
                    if ($(this).hasClass('row_selected'))
                    {
                        $(this).removeClass('row_selected');
                        
                        btn.attr('disabled', true)
                           .addClass('ui-state-disabled');
                    }
                    else
                    {
                        _oTable.$('tr.row_selected').removeClass('row_selected');
                        $(this).addClass('row_selected');
                        
                        btn.removeAttr('disabled')
                           .removeClass('ui-state-disabled')
                           .removeClass('ui-button-disabled');
                    }
                });
            }
        }
    };
    
    // hack for actions
    var self = this;
    
    // set states
    this.setButtonsEnable(0, true);
    this.setButtonsEnable(1, false);
    
    // CONTRUCT END    /////////////////////////////////////////////////////////
    
    // returns an instance of the class
    return this;
}


