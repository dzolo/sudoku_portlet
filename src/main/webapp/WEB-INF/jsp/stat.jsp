<%-- 
    Project       : Bachelor Thesis - Sudoku game implementation as portlet
    Document      : stat.jsp
    Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
    Organization: : FIT VUT <http://www.fit.vutbr.cz>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<portlet:defineObjects />
<c:set var="app_path" value="${pageContext.request.contextPath}" />

<script type="text/javascript"><!--
   
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
        var gameId = game.getGameId()
        var table = $game_stats_body.find('table');
        var ul = $game_stats_body.find('ul');
        var request = new SudokuGame_Request(game.getAppPath());

        // empty stats
        if (!gameId)
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
                    var solvers = request.makeGet('/game/stats/best_solvers/' + gameId);

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
                                case 0:
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

                            table.append($('<tr>').append(
                                pos.text((i + 1) + '.')
                            ).append(
                                $('<td>').html(solvers[i].userId) // @todo name
                            ).append(
                                $('<td>').html(Math.floor(solvers[i].lasting / 60)
                                                          + ':' + (solvers[i].lasting % 60))
                            ).append(
                                $('<td>').html($('<span>').raty({
                                    readOnly : true,
                                    start    : Math.floor(solvers[i].rating),
                                    starOn   : '${app_path}images/icons/star-on_small.png',
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
                    var stats = request.makeGet('/game/stats/' + gameId);

                    if (!stats.state)
                    {
                        throw stats.message;
                    }
                    
                    var lasting = parseInt(stats.stats.a_lasting, 10);

                    ul.html(
                        $('<li>').html('The game was played by ')
                            .append($('<b>').text(stats.stats.c_played))
                            .append(' players and solved by ')
                            .append($('<b>').text(stats.stats.c_solved))
                            .append(' of them.')
                    ).append(
                        $('<li>').html('The average rating of game is ').append($('<span>').raty({
                            readOnly : true,
                            start    : Math.floor(stats.stats.a_rating),
                            starOn   : '${app_path}images/icons/star-on_small.png',
                            starOff  : '${app_path}images/icons/star-off_small.png'
                        }))
                    ).append(
                        $('<li>').html('The average solution time is ')
                            .append($('<b>').text(Math.floor(lasting / 60) + ':' + (lasting % 60)))
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
                            $('<td>').html(Math.floor(data.solvers[i].s_lasting / 60)
                                                      + ':' + (data.solvers[i].s_lasting % 60))
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
                        .append($('<b>').text(Math.floor(lasting2 / 60) + ':' + (lasting2 % 60)))
                        .append(' seconds.')
                ).append(
                    $('<li>').html('The average solution time is ')
                        .append($('<b>').text(Math.floor(lasting / 60) + ':' + (lasting % 60)))
                        .append(' seconds.')
                ).append(
                    $('<li>').html('The average rating is ').append($('<span>').raty({
                        readOnly : true,
                        start    : Math.floor(stats.stats.a_rating),
                        starOn   : '${app_path}images/icons/star-on_small.png',
                        starOff  : '${app_path}images/icons/star-off_small.png'
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
        
    // after document is loaded
    $(document).ready(function ()
    {
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
    });
   
--></script>

<div id="<portlet:namespace/>_statistics" class="sudoku-game_statistics">
    <div class="sudoku-game_statistics-item">
        <div class="sudoku-game_statistics-item-header">
            <img alt="Stat icon" src="${app_path}/images/icons/stats_12x12.png" />
            <span>Statistics of this game</span>
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
