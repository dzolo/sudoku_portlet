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
    
    // after the document is loaded
    $(document).ready(function ()
    {
        /* Game board *********************************************************/
        
        window['<portlet:namespace/>_board'] = new SudokuGame_GameBoard('#<portlet:namespace/>_board');
        window['<portlet:namespace/>_board'].init();
        
        
        /* Timer **************************************************************/
        
        // a start event function
        window['<portlet:namespace/>_start'] = function ()
        {
            window['<portlet:namespace/>_board'].setEnabled(true);
            $('#<portlet:namespace/>_footer-pause').show();
            $('#<portlet:namespace/>_footer-play').hide();
        }
        
        // a pause event function
        window['<portlet:namespace/>_pause'] = function ()
        {
            window['<portlet:namespace/>_board'].setEnabled(false);
            $('#<portlet:namespace/>_footer-pause').hide();
            $('#<portlet:namespace/>_footer-play').show();
        }
        
        // init the timer or replace the root element
        if (window['<portlet:namespace/>__timer'] == undefined)
        {            
            window['<portlet:namespace/>__timer'] = new SudokuGame_Timer(
                    '#<portlet:namespace/>_footer-timer',
                    '<portlet:namespace/>_start', 
                    '<portlet:namespace/>_pause'
            );
        }
        else
        {
            window['<portlet:namespace/>__timer'].setRootElement('#<portlet:namespace/>_footer-timer');
        }
        
        // render if paused renders the timer otherwise if not started starts the timer
        if (window['<portlet:namespace/>__timer'].isPaused())
        {
            window['<portlet:namespace/>__timer'].pause();
        }
        else if (!window['<portlet:namespace/>__timer'].isStarted())
        {
            window['<portlet:namespace/>__timer'].start();
        }
        
        // pause timer event
        $('#<portlet:namespace/>_footer-pause').click(function ()
        {
            window['<portlet:namespace/>__timer'].pause();
            return false;
        });
        
        // resume timer event
        $('#<portlet:namespace/>_footer-play').click(function ()
        {
            window['<portlet:namespace/>__timer'].start();
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
                // pause the game
                window['<portlet:namespace/>__timer'].pause();
                $game_buttons.hide();
                // hide button and board
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
                // resume the game
                window['<portlet:namespace/>__timer'].start();
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
        <a href="#" class="sudoku-game_button" title="Create a new game">
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
                <c:forEach items="${game}" var="row" varStatus="row_status">
                    <c:set var="border_bottom" value="${((row_status.count mod 3) eq 0) and (not row_status.first) and (not row_status.last)}" />
                    <tr>
                        <c:forEach items="${row}" var="field" varStatus="field_status">
                            <c:set var="border_right" value="${((field_status.count mod 3) eq 0) and (not field_status.first) and (not field_status.last)}" />
                            <c:set var="index" value="${(row_status.count - 1) * 9 + (field_status.count - 1)}" />
                            <td style="<c:if test="${border_bottom}">border-bottom: 2px solid #a6a6a6;</c:if><c:if test="${border_right}">border-right: 2px solid #a6a6a6;</c:if>">
                                <input name="board_field[<c:out value="${index}"/>]" type="text"<c:if test="${not empty field}"> value="${field}" readonly="readonly"</c:if> class="sudoku-game_board-field" />
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