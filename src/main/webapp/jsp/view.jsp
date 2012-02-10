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
   
    // after document is loaded
    $(document).ready(function ()
    {
        /* Game board */
        
        // activate input check on fields
        $('#sudoku-game_board input').keypress(sudoku_game__digit_only)
        // movement on fields with arrows
        $('#sudoku-game_board input').keyup(sudoku_game__arrow_movement);
        // change size of board
        sudoku_game__board_resizer();
        // focus on first field
        $('#sudoku-game_board input[name="board_field[0]"]').focus();
        
        /* Timer */
        
        // only if not already started
        if (window['sudoku_game__timeout_id'] == undefined)
        {
            // init timeout id var
            sudoku_game__timeout_id = null;
            // start timer
            if (window['sudoku_game__timeout'])
                sudoku_game__timer(sudoku_game__timeout);
            else
                sudoku_game__timer();
        }
        
        // pause timer event
        $('#sudoku-game_footer-pause').click(function ()
        {
            sudoku_game__pause_game();
            return false;
        });
        
        /* Statistics */
        
        // switching to statistics in normal state of portlet
        $('#sudoku-game_footer-statistics').click(function ()
        {
            sudoku_game__show_stats();
            return false;
        });
        
        // switching to game from statistics in normal state of portlet
        $('#sudoku-game_footer-show-game').click(function ()
        {
            sudoku_game__hide_stats();
            return false;
        });
        
    });
   
--></script>

<div class="sudoku-game_container">
    
    <noscript>
        <div>
            <p>
                <strong>JavaScript required!</strong>
                <br/>Please, enable JavaScript in your browser.
            </p>
        </div>
    </noscript>

    <div class="sudoku-game_toolbar">
        <a href="#" class="sudoku-game_button" title="Create a new game">
            <img alt="New icon" src="<c:out value="${app_path}"/>/resources/icons/new_16x16.png" />
            <span>New</span>
        </a>
        <c:if test="${not empty pageContext.request.remoteUser}">
            <a href="#" class="sudoku-game_button" title="Save the current game">
                <img alt="Save icon" src="<c:out value="${app_path}"/>/resources/icons/save_16x16.png" />
                <span>Save</span>
            </a>
            <a href="#" class="sudoku-game_button" title="Load a previous played game">
                <img alt="Load icon" src="<c:out value="${app_path}"/>/resources/icons/load_16x16.png" />
                <span>Load</span>
            </a>
        </c:if>
        <a href="#" class="sudoku-game_button" title="Reset the current game">
            <img alt="Reset icon" src="<c:out value="${app_path}"/>/resources/icons/reset_16x16.png" />
            <span>Reset</span>
        </a>
        <a href="#" class="sudoku-game_button" title="Check your solution of the current game">
            <img alt="Check icon" src="<c:out value="${app_path}"/>/resources/icons/check_16x16.png" />
            <span>Check</span>
        </a>
        <div class="sudoku-game_cleared"></div>
    </div>

    <div class="sudoku-game_body<c:if test="${renderRequest.windowState eq 'maximized'}"> sudoku-game_state-maximized</c:if>">
        <div class="sudoku-game_board" id="sudoku-game_board">
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
            <div id="sudoku-game_board-locker" class="sudoku-game_board-locker"></div>
        </div>
        <c:import url="stat.jsp" />
    </div>

    <div class="sudoku-game_footer">
        <span id="sudoku-game_footer-timer" class="sudoku-game_footer-timer">x:x</span>
        <a href="#" id="sudoku-game_footer-pause" class="sudoku-game_button sudoku-game_footer-pause" title="Pause the game">Pause</a>
        <c:if test="${renderRequest.windowState ne 'maximized'}">
            <a href="#" id="sudoku-game_footer-statistics" class="sudoku-game_button sudoku-game_footer-statistics" title="Show statistics">Statistics</a>
            <a href="#" id="sudoku-game_footer-show-game" class="sudoku-game_button sudoku-game_footer-show-game" title="Show game">Continue the game</a>
        </c:if>
        <div class="sudoku-game_cleared"></div>
    </div>

</div>