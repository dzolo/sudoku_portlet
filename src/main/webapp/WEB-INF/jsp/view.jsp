<%-- 
    Project       : Bachelor Thesis - Sudoku game implementation as portlet
    Document      : view.jsp
    Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
    Organization: : FIT VUT <http://www.fit.vutbr.cz>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<portlet:defineObjects />
<c:set var="app_path" value="${pageContext.request.contextPath}" />
<portlet:resourceURL var="rURLchangeLastPlayedGame" id="changeLastPlayedGame" />

<script type="text/javascript"><!--
    
    // global value contains an indentificator of remote user or null if
    // the current user is logged in
    SudokuGame_userId = null;
    <c:if test="${not empty pageContext.request.remoteUser}">
    SudokuGame_userId = '${pageContext.request.remoteUser}';
    </c:if>
    
    // after the document is loaded
    $(document).ready(function ()
    {
        /* Game ***************************************************************/
        
        if (window['<portlet:namespace/>_game'] == undefined)
        {
            window['<portlet:namespace/>_game'] = new SudokuGame_Game(
                    '<portlet:namespace/>', '${app_path}',
                    '${rURLchangeLastPlayedGame}'
            );
                
            var sID = (SudokuGame_userId) ? '${lastPlayedSolutionId}' : null;
            
            window['<portlet:namespace/>_game'].init(sID);
        }
        else
        {
            window['<portlet:namespace/>_game'].init();
        }
        
        // pause timer event
        $('#<portlet:namespace/>_footer-pause').click(function ()
        {
            window['<portlet:namespace/>_game'].pause();
            return false;
        });
        
        // resume timer event
        $('#<portlet:namespace/>_footer-play').click(function ()
        {
            window['<portlet:namespace/>_game'].start();
            return false;
        });
        
        /* Dialogs ************************************************************/
        
        $('#<portlet:namespace/>_dialog-save').dialog({
            width           : 200,
            height          : 'auto',
            modal           : true,
            autoOpen        : false,
            closeOnEscape   : true
        });
        
        $('#<portlet:namespace/>_dialog-end').dialog({
            width           : 200,
            height          : 'auto',
            modal           : true,
            autoOpen        : false,
            closeOnEscape   : true
        });
        
        $('#<portlet:namespace/>_dialog-load').dialog({
            width           : 600,
            height          : 'auto',
            modal           : true,
            autoOpen        : false,
            closeOnEscape   : true
        });
        
        $('#<portlet:namespace/>_dialog-new').dialog({
            width           : 600,
            height          : 'auto',
            modal           : true,
            autoOpen        : false,
            closeOnEscape   : true
        });
        
        /** Statistics ********************************************************/
        
        // load content
        SudokuGame_loadStatistics('<portlet:namespace/>');
        
        // switching to statistics in normal state of portlet
        $('#<portlet:namespace/>_footer-statistics').click(function ()
        {
            var $game_board = $('#<portlet:namespace/>_board');
            var $game_stats = $('#<portlet:namespace/>_statistics');
            var $show_statistics_button = $('#<portlet:namespace/>_footer-statistics');
            var $show_game_button = $('#<portlet:namespace/>_footer-show-game');
            var $game_buttons = $('#<portlet:namespace/>_footer-pause, #<portlet:namespace/>_footer-play');
            var game = window['<portlet:namespace/>_game'];

            if ($game_board && $game_stats)
            {
                // pause the game if started
                if (game.getGameBoard().isGamePlayed())
                {
                    game.pause();
                }
                // hide button and board
                $game_buttons.hide();
                $show_statistics_button.hide();
                $game_board.hide();
                
                // load content
                SudokuGame_loadStatistics('<portlet:namespace/>');
                
                // show stats
                $game_stats.fadeIn();
                // show reverse action button
                $show_game_button.show();
            }

            return false;
        });
        
        // switching to game from statistics in normal state of portlet
        $('#<portlet:namespace/>_footer-show-game').click(function ()
        {
            var $game_board = $('#<portlet:namespace/>_board');
            var $game_stats = $('#<portlet:namespace/>_statistics');
            var $show_statistics_button = $('#<portlet:namespace/>_footer-statistics');
            var $show_game_button = $('#<portlet:namespace/>_footer-show-game');
            var game = window['<portlet:namespace/>_game'];

            if ($game_board && $game_stats)
            {
                // resume the game if started
                if (game.getGameBoard().isGamePlayed() && !game.getTimer().isStarted())
                {
                    game.start();
                }
                // hide button and board
                $show_game_button.hide();
                $game_stats.hide();
                // show stats
                $game_board.fadeIn();
                // show reverse action button
                $show_statistics_button.show();
            }

            return false;
        });
    
        // reloads statistics on demand
        $('#<portlet:namespace/>_statistics-reloadbutton').click(function ()
        {
            SudokuGame_loadStatistics('<portlet:namespace/>');
            return false;
        });
       
    });
    
    /**
     * Loads a content of statistics
     * 
     * @param namespace
     */
    function SudokuGame_loadStatistics(namespace)
    {
        var $game_stats = $('#' + namespace + '_statistics');
        var $game_stats_body = $game_stats.find('.sudoku-game_statistics-item-body:eq(0)');
        var $global_stats_body = $game_stats.find('.sudoku-game_statistics-item-body:eq(1)');
        var game = window[namespace + '_game'];
        var gameObj = game.getGame()
        var table = $game_stats_body.find('table');
        var ul = $game_stats_body.find('ul');
        var request = new SudokuGame_Request(game.getAppPath());

        // empty stats
        if (!gameObj || !gameObj.id)
        {
            table.html('<tr><td>Statistics of the current game are not available.</td></tr>');
            ul.html('<li>Statistics of the current game are not available.</li>');
        }
        else
        {
            table.animate({opacity: 0.5}, 200);
            // best solvers
            setTimeout(function () 
            {
                try
                {
                    var solvers = request.makeGet('/game/stats/best_solvers/' + gameObj.id);

                    if (solvers.length == 0)
                    {
                        table.html('<tr><td>No players solved this game yet.</td></tr>');
                    }
                    else
                    {
                        table.html($('<tr>').append(
                            $('<th>').css('width', '15px').text('#')
                        ).append(
                            $('<th>').text('Name of player')
                        ).append(
                            $('<th>').text('Solution time (s)')
                        ).append(
                            $('<th>').text('Rating')
                        ));

                        for (var i = 0; i < solvers.length; i++)
                        {
                            var pos = $('<td>');

                            switch (i)
                            {
                                case 0:
                                    pos.css({
                                        'fontWeight' : 'bold',
                                        'color'      : 'gold'
                                    });
                                    break;
                                case 1:
                                    pos.css({
                                        'fontWeight' : 'bold',
                                        'color'      : 'silver'
                                    });
                                    break;
                                case 2:
                                    pos.css({
                                        'fontWeight' : 'bold',
                                        'color'      : '#a18631'
                                    });
                                    break;
                            }
                            
                            // @todo name
                            var userName = solvers[i].userId;
                            
                            if (!userName)
                            {
                                 userName = 'Anonymous player';
                            }

                            table.append($('<tr>').append(
                                pos.text((i + 1) + '.')
                            ).append(
                                $('<td>').html(userName)
                            ).append(
                                $('<td>').html(SudokuGame_lasting(solvers[i].lasting))
                            ).append(
                                $('<td>').html($('<span>').raty({
                                    readOnly : true,
                                    start    : Math.floor(solvers[i].rating),
                                    starOn   : '${app_path}/images/icons/star-on_small.png',
                                    starOff  : '${app_path}/images/icons/star-off_small.png'
                                }))
                            ));
                        }
                    }
                }
                catch (e)
                {
                    table.html($('<tr>').append(
                        $('<td>').stateBox('Error during loading', e, 'error', 'alert')
                    ));
                }
                finally
                {
                    table.animate({opacity: 1}, 200);
                }
            }, 0);

            // stats

            ul.animate({opacity: 0.5}, 200);
        
            setTimeout(function () 
            {
                try
                {
                    var stats = request.makeGet('/game/stats/' + gameObj.id);

                    if (!stats.state)
                    {
                        throw stats.message;
                    }
                    
                    var lasting = parseInt(stats.stats.a_lasting, 10);

                    if (gameObj.type == 'GENERATED')
                    {
                        ul.html(
                            $('<li>').html('The generated game with ')
                                .append($('<b>').text(gameObj.typeDifficulty.toLowerCase()))
                                .append(' difficulty.')
                        );
                    }
                    else
                    {
                        ul.html(
                            $('<li>').html('The game was obtained from ')
                                .append($('<b>').text(gameObj.typeService.name))
                        );
                    }
                    
                    ul.append(
                        $('<li>').html('The game was played by ')
                            .append($('<b>').text(stats.stats.c_played))
                            .append(' players and solved by ')
                            .append($('<b>').text(stats.stats.c_solved))
                            .append(' of them.')
                    ).append(
                        $('<li>').html('The average rating of game is ').append($('<span>').raty({
                            readOnly : true,
                            start    : Math.floor(stats.stats.a_rating),
                            starOn   : '${app_path}/images/icons/star-on_small.png',
                            starOff  : '${app_path}/images/icons/star-off_small.png'
                        }))
                    ).append(
                        $('<li>').html('The average solution time is ')
                            .append($('<b>').text(SudokuGame_lasting(lasting)))
                            .append(' seconds.')
                    );
                }
                catch (e)
                {
                    ul.html($('<li>').append(
                        $('<div>').stateBox('Error during loading', e, 'error', 'alert')
                    ));
                }
                finally
                {
                    ul.animate({opacity: 1}, 200);
                }
            }, 0);
        }

        /* Global statistics */
        var table2 = $global_stats_body.find('table');
        var ul2 = $global_stats_body.find('ul');

        // best solvers

        table2.animate({opacity: 0.5}, 200);

        setTimeout(function () 
        {
            try
            {
                var data = request.makeGet('/game/stats/best_solvers');

                if (!data.state)
                {
                    throw data.message;
                }

                if (data.solvers.length == 0)
                {
                    table2.html('<tr><td>No players solved any game yet.</td></tr>');
                }
                else
                {
                    table2.html($('<tr>').append(
                        $('<th>').css('width', '15px').text('#')
                    ).append(
                        $('<th>').text('Name of player')
                    ).append(
                        $('<th>').text('Solved games')
                    ).append(
                        $('<th>').text('Spended time (s)')
                    ));

                    for (var i = 0; i < data.solvers.length; i++)
                    {
                        var pos = $('<td>');

                        switch (i)
                        {
                            case 0:
                                pos.css({
                                    'fontWeight' : 'bold',
                                    'color'      : 'gold'
                                });
                                break;
                            case 1:
                                pos.css({
                                    'fontWeight' : 'bold',
                                    'color'      : 'silver'
                                });
                                break;
                            case 2:
                                pos.css({
                                    'fontWeight' : 'bold',
                                    'color'      : '#a18631'
                                });
                                break;
                        }

                        table2.append($('<tr>').append(
                            pos.text((i + 1) + '.')
                        ).append(
                            $('<td>').html(data.solvers[i].userId) // @todo name
                        ).append(
                            $('<td>').html(data.solvers[i].c_solved)
                        ).append(
                            $('<td>').html(SudokuGame_lasting(data.solvers[i].s_lasting))
                        ));
                    }
                }
            }
            catch (e)
            {
                table2.html($('<tr>').append(
                    $('<td>').stateBox('Error during loading', e, 'error', 'alert')
                ));
            }
            finally
            {
                table2.animate({opacity: 1}, 200);
            }
        }, 0);

        ul2.animate({opacity: 0.5}, 200);

        // stats
        setTimeout(function () 
        {
            try
            {
                var stats = request.makeGet('/game/stats');

                if (!stats.state)
                {
                    throw stats.message;
                }
                
                var lasting = Math.floor(stats.stats.a_lasting, 10);
                var lasting2 = stats.stats.s_lasting

                ul2.html(
                    $('<li>').html('There are currently ')
                        .append($('<b>').text(stats.stats.c_games))
                        .append(' games which were solved for ')
                        .append($('<b>').text(stats.stats.c_finished))
                        .append(' times by ')
                        .append($('<b>').text(stats.stats.c_players))
                        .append(' players in a time of ')
                        .append($('<b>').text(SudokuGame_lasting(lasting2)))
                        .append(' seconds.')
                ).append(
                    $('<li>').html('The average solution time is ')
                        .append($('<b>').text(SudokuGame_lasting(lasting)))
                        .append(' seconds.')
                ).append(
                    $('<li>').html('The average rating is ').append($('<span>').raty({
                        readOnly : true,
                        start    : Math.floor(stats.stats.a_rating),
                        starOn   : '${app_path}/images/icons/star-on_small.png',
                        starOff  : '${app_path}/images/icons/star-off_small.png'
                    }))
                );
            }
            catch (e)
            {
                ul2.html($('<li>').append(
                    $('<div>').stateBox('Error during loading', e, 'error', 'alert')
                ));
            }
            finally
            {
                ul2.animate({opacity: 1}, 200);
            }
        }, 0);
    }
   
--></script>

<div class="sudoku-game_container">
    
    <div id="<portlet:namespace/>_dialog-save" style="display: none" title="Save the current game">
        
        <div class="ui-state-box"></div>
        
        <div class="_ui-dialog-body">
            <label>Please enter a name:</label><br />
            <input type="text" id="<portlet:namespace/>_dialog-save-name" style="width: 95%; margin-top: 1em" />
        </div>
        
    </div>
    
    <div id="<portlet:namespace/>_dialog-load" style="display: none; padding: 0" title="Load a saved game">
        <table cellpadding="0" cellspacing="0" border="0" class="dataTables_display">
            <thead>
                <tr>
                    <th width="5%">ID</th>
                    <th width="50%">Name</th>
                    <th width="40%">Time</th>
                    <th width="15%">Lasting</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
    
    <div id="<portlet:namespace/>_dialog-new" style="display: none; padding: 0" title="Create a new game">
        <div>
            <div id="<portlet:namespace/>_dialog-new-first-step-loader" style="display: none">
                <div class="sudoku-game_dialog-new-first-step-loader">
                    <img alt="loader" src="${app_path}/images/icons/loader.gif" />
                </div>
            </div>
            <table>
                <c:if test="${not empty pageContext.request.remoteUser}">
                    <tr>
                        <td>
                            <input type="radio" name="<portlet:namespace/>_dialog-new-choose" value="load_own" checked="checked" />
                        </td>
                        <td>
                            <label>Load from your previously played games which were unfinished.</label>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td>
                        <input type="radio" name="<portlet:namespace/>_dialog-new-choose" value="load"<c:if test="${empty pageContext.request.remoteUser}">checked="checked"</c:if> />
                    </td>
                    <td>
                        <label>Load from games which were played by other players.</label>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="radio" name="<portlet:namespace/>_dialog-new-choose" value="load_service" />
                    </td>
                    <td>
                        <label>Load a game from remote publishers.</label>
                    </td>
                </tr>
                <c:if test="${remotePublisherEnabled}">
                <tr>
                    <td>
                        <input type="radio" name="<portlet:namespace/>_dialog-new-choose" value="generate" />
                    </td>
                    <td>
                        <label>Generate a new random game.</label>
                    </td>
                </tr>
                </c:if>
            </table>
            
        </div>
        <div id="<portlet:namespace/>_dialog-new-second-step"></div>
    </div>
    
    <div id="<portlet:namespace/>_dialog-end" title="The game was solved!"></div>
    

    <div class="sudoku-game_toolbar">
        <a href="#" id="<portlet:namespace/>_button_new" class="sudoku-game_button" title="Create a new game">
            <img alt="New icon" src="${app_path}/images/icons/new_16x16.png" />
            <span>New</span>
        </a>
        <c:if test="${not empty pageContext.request.remoteUser}">
            <a href="#" id="<portlet:namespace/>_button_save" class="sudoku-game_button" title="Save the current game">
                <img alt="Save icon" src="${app_path}/images/icons/save_16x16.png" />
                <span>Save</span>
            </a>
            <a href="#" id="<portlet:namespace/>_button_load" class="sudoku-game_button" title="Load a previous played game">
                <img alt="Load icon" src="${app_path}/images/icons/load_16x16.png" />
                <span>Load</span>
            </a>
        </c:if>
        <a href="#" id="<portlet:namespace/>_button_reset" class="sudoku-game_button" title="Reset the current game">
            <img alt="Reset icon" src="${app_path}/images/icons/reset_16x16.png" />
            <span>Reset</span>
        </a>
        <a href="#" id="<portlet:namespace/>_button_check" class="sudoku-game_button" title="Check your solution of the current game">
            <img alt="Check icon" src="${app_path}/images/icons/check_16x16.png" />
            <span>Check</span>
        </a>
        <div class="sudoku-game_cleared"></div>
    </div>

    <div class="sudoku-game_body<c:if test="${renderRequest.windowState eq 'maximized'}"> sudoku-game_state-maximized</c:if>">
        <div class="sudoku-game_board" id="<portlet:namespace/>_board">
            <table>
                <c:forEach var="rowIndex" begin="1" end="9" step="1">
                    <c:set var="borderBottom" value="${((rowIndex mod 3) eq 0) and (rowIndex ne 1) and (rowIndex ne 9)}" />
                    <tr>
                        <c:forEach var="colIndex" begin="1" end="9" step="1">
                            <c:set var="borderRight" value="${((colIndex mod 3) eq 0) and (colIndex ne 1) and (colIndex ne 9)}" />
                            <td style="<c:if test="${borderBottom}">border-bottom: 2px solid #a6a6a6;</c:if><c:if test="${borderRight}">border-right: 2px solid #a6a6a6;</c:if>">
                                <table class="sudoku-game_input-hint">
                                    <tr><td><span>8</span></td><td><span>9</span></td><td><div class="sudoku-game_input-hint-close"></div></td></tr>
                                    <tr><td><span>7</span></td><td rowspan="2"><div class="sudoku-game_input-hint-center"></div></td><td><span>1</span></td></tr>
                                    <tr><td><span>6</span></td><td><span>2</span></td></tr>
                                    <tr><td><span>5</span></td><td><span>4</span></td><td><span>3</span></td></tr>
                                </table>
                                <input name="board_field[${(rowIndex - 1) * 9 + (colIndex - 1)}]" type="text" class="sudoku-game_board-field" />
                            </td>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </table>
        </div>
    
        <div id="<portlet:namespace/>_statistics" class="sudoku-game_statistics">
            
            <div class="sudoku-game_statistics-item">
                <div class="sudoku-game_statistics-item-header">
                    <img alt="Stat icon" src="${app_path}/images/icons/stats_12x12.png" />
                    <span>Statistics of this game</span>
                    <a href="#" id="<portlet:namespace/>_statistics-reloadbutton" title="Reload statistics" style="float: right">
                        <img alt="Reload icon" src="${app_path}/images/icons/reload_12x12.png" />
                    </a>
                </div>
                <div class="sudoku-game_statistics-item-body">
                    <strong>Best solvers</strong>
                    <table></table>
                    <strong>Statistics of the game</strong>
                    <ul></ul>
                </div>
            </div>
                    
            <div class="sudoku-game_statistics-item" style="border-top: 1px solid #a6a6a6;">
                <div class="sudoku-game_statistics-item-header">
                    <img alt="Global stat icon" src="${app_path}/images/icons/global_stats_12x12.png" />
                    <span>Total statistics</span>
                </div>
                <div class="sudoku-game_statistics-item-body">
                    <strong>The best solvers on the portal</strong>
                    <table></table>
                    <strong>Statistics of all games</strong>
                    <ul></ul>
                </div>
            </div>
                    
        </div>
    </div>

    <div class="sudoku-game_footer">
        <span id="<portlet:namespace/>_footer-timer" class="sudoku-game_footer-timer">x:x</span>
        <a href="#" id="<portlet:namespace/>_footer-pause" class="sudoku-game_button sudoku-game_footer-pause" title="Pause the game">Pause</a>
        <a href="#" id="<portlet:namespace/>_footer-play" class="sudoku-game_button sudoku-game_footer-play" title="Continue the game">Continue</a>
        <c:if test="${renderRequest.windowState ne 'maximized'}">
            <a href="#" id="<portlet:namespace/>_footer-statistics" class="sudoku-game_button sudoku-game_footer-statistics" title="Show statistics">Statistics</a>
            <a href="#" id="<portlet:namespace/>_footer-show-game" class="sudoku-game_button sudoku-game_footer-show-game" title="Show game">Back to the game</a>
        </c:if>
        <div class="sudoku-game_cleared"></div>
    </div>

</div>