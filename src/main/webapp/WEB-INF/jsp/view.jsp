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

<script type="text/javascript"><!--
    
    SudokuGame_userId = null;
    <c:if test="${not empty pageContext.request.remoteUser}">
    SudokuGame_userId = '<c:out value="${pageContext.request.remoteUser}"/>'
    </c:if>
    
    // after the document is loaded
    $(document).ready(function ()
    {
        /* Game ***************************************************************/
        
        if (window['<portlet:namespace/>_game'] == undefined)
        {
            window['<portlet:namespace/>_game'] = new SudokuGame_Game('<portlet:namespace/>');
        }
        
        window['<portlet:namespace/>_game'].init();
        
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
        
        /* Statistics *********************************************************/
        
        // switching to statistics in normal state of portlet
        $('#<portlet:namespace/>_footer-statistics').click(function ()
        {
            var $game_board = $('#<portlet:namespace/>_board');
            var $game_stats = $('#<portlet:namespace/>_statistics');
            var $show_statistics_button = $('#<portlet:namespace/>_footer-statistics');
            var $show_game_button = $('#<portlet:namespace/>_footer-show-game');
            var $game_buttons = $('#<portlet:namespace/>_footer-pause, #<portlet:namespace/>_footer-play');

            if ($game_board && $game_stats)
            {
                // pause the game if started
                if (window['<portlet:namespace/>_game'].getGameBoard().isGamePlayed())
                {
                    window['<portlet:namespace/>_game'].pause();
                }
                // hide button and board
                $game_buttons.hide();
                $show_statistics_button.hide();
                $game_board.hide();
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

            if ($game_board && $game_stats)
            {
                // resume the game if started
                if (window['<portlet:namespace/>_game'].getGameBoard().isGamePlayed())
                {
                    window['<portlet:namespace/>_game'].start();
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
        
        /* Toolbar Menu *******************************************************/
        
        $('#<portlet:namespace/>_button_new').click(function ()
        {
            var request = new SudokuGame_Request('<c:out value="${app_path}"/>');
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
                    userId     : SudokuGame_userId,
                    userName   : null,
                    values     : data.initValues
                });
                
                // get an ID of the created game solution
                id = data.location.split('/').pop();
                // get the created solution
                data = request.makeGet('/game_solution/' + id);
                
                // start the game
                window['<portlet:namespace/>_game'].pause();
                window['<portlet:namespace/>_game'].getTimer().setTimeout(0);
                window['<portlet:namespace/>_game'].start(data.values);
            }
            catch (e)
            {
                alert('Can not create a new game.\nError: ' + e.toString());
            }
            
            return false;
        });
       
    });
   
--></script>

<div class="sudoku-game_container">
    
    <noscript>
        <div class="sudoku-game_noscript">
            <p>
                <strong>JavaScript required!</strong>
                <br/>Please, enable JavaScript in your browser.
            </p>
        </div>
    </noscript>

    <div class="sudoku-game_toolbar">
        <a href="#" id="<portlet:namespace/>_button_new" class="sudoku-game_button" title="Create a new game">
            <img alt="New icon" src="<c:out value="${app_path}"/>/images/icons/new_16x16.png" />
            <span>New</span>
        </a>
        <c:if test="${not empty pageContext.request.remoteUser}">
            <a href="#" class="sudoku-game_button" title="Save the current game">
                <img alt="Save icon" src="<c:out value="${app_path}"/>/images/icons/save_16x16.png" />
                <span>Save</span>
            </a>
            <a href="#" class="sudoku-game_button" title="Load a previous played game">
                <img alt="Load icon" src="<c:out value="${app_path}"/>/images/icons/load_16x16.png" />
                <span>Load</span>
            </a>
        </c:if>
        <a href="#" class="sudoku-game_button" title="Reset the current game">
            <img alt="Reset icon" src="<c:out value="${app_path}"/>/images/icons/reset_16x16.png" />
            <span>Reset</span>
        </a>
        <a href="#" class="sudoku-game_button" title="Check your solution of the current game">
            <img alt="Check icon" src="<c:out value="${app_path}"/>/images/icons/check_16x16.png" />
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
                                <input name="board_field[<c:out value="${(rowIndex - 1) * 9 + (colIndex - 1)}"/>]" type="text" class="sudoku-game_board-field" />
                            </td>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </table>
            <div id="<portlet:namespace/>_board-locker" class="sudoku-game_board-locker"></div>
        </div>
        <c:import url="stat.jsp" />
    </div>

    <div class="sudoku-game_footer">
        <span id="<portlet:namespace/>_footer-timer" class="sudoku-game_footer-timer">x:x</span>
        <a href="#" id="<portlet:namespace/>_footer-pause" class="sudoku-game_button sudoku-game_footer-pause" title="Pause the game">Pause</a>
        <a href="#" id="<portlet:namespace/>_footer-play" class="sudoku-game_button sudoku-game_footer-play" title="Continue the game">Continue</a>
        <c:if test="${renderRequest.windowState ne 'maximized'}">
            <a href="#" id="<portlet:namespace/>_footer-statistics" class="sudoku-game_button sudoku-game_footer-statistics" title="Show statistics">Statistics</a>
            <a href="#" id="<portlet:namespace/>_footer-show-game" class="sudoku-game_button sudoku-game_footer-show-game" title="Show game">Continue the game</a>
        </c:if>
        <div class="sudoku-game_cleared"></div>
    </div>

</div>