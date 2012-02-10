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
   
    // after document is loaded
    $(document).ready(function ()
    {
        $('#<c:out value="${wid}"/>_statistics-game-avg-rating').raty({
            readOnly : true,
            start    : 3,
            starOn   : '<c:out value="${app_path}"/>/resources/icons/star-on_small.png',
            starOff  : '<c:out value="${app_path}"/>/resources/icons/star-off_small.png'
        });
        $('#<c:out value="${wid}"/>_statistics-global-avg-rating').raty({
            readOnly : true,
            start    : 4,
            starOn   : '<c:out value="${app_path}"/>/resources/icons/star-on_small.png',
            starOff  : '<c:out value="${app_path}"/>/resources/icons/star-off_small.png'
        });
    });
   
--></script>

<div id="<c:out value="${wid}"/>_statistics" class="sudoku-game_statistics">
    <div class="sudoku-game_statistics-item">
        <div class="sudoku-game_statistics-item-header">
            <img alt="Stat icon" src="<c:out value="${app_path}"/>/resources/icons/stats_12x12.png" />
            <span>Statistics of this game</span>
        </div>
        <div class="sudoku-game_statistics-item-body">
            <strong>Best solvers</strong>
            <table>
                <tr>
                    <th style="width: 15px;">#</th>
                    <th>Name</th>
                    <th>Time</th>
                </tr>
                <tr>
                    <td style="font-weight: bold; color: gold">1.</td>
                    <td>Jon Snow</td>
                    <td>9:05</td>
                </tr>
                <tr>
                    <td style="font-weight: bold; color: silver">2.</td>
                    <td>Jaime Lannister</td>
                    <td>10:11</td>
                </tr>
                <tr>
                    <td style="font-weight: bold; color: #a18631">3.</td>
                    <td>Brandon Tully</td>
                    <td>16:08</td>
                </tr>
                <tr>
                    <td>4.</td>
                    <td>Jon Arryn</td>
                    <td>21:48</td>
                </tr>
                <tr>
                    <td>5.</td>
                    <td>Samwell Tarly</td>
                    <td>22:14</td>
                </tr>
            </table>
            <strong>Statistics of the game</strong>
            <ul>
                <li>The average rating is <span id="<c:out value="${wid}"/>_statistics-game-avg-rating"></span></li>
                <li><b>9</b> solvers are playing right now</li>
                <li>Played by <b>3</b> solvers</li>
                <li>The average solution time is <b>28:10</b></li>
            </ul>
        </div>
    </div>
    <div class="sudoku-game_statistics-item" style="border-top: 1px solid #a6a6a6;">
        <div class="sudoku-game_statistics-item-header">
            <img alt="Global stat icon" src="<c:out value="${app_path}"/>/resources/icons/global_stats_12x12.png" />
            <span>Total statistics</span>
        </div>
        <div class="sudoku-game_statistics-item-body">
            <strong>The best solvers on the portal</strong>
            <table>
                <tr>
                    <th style="width: 15px;">#</th>
                    <th>Name</th>
                    <th>Games count</th>
                    <th>Time</th>
                </tr>
                <tr>
                    <td style="font-weight: bold; color: gold">1.</td>
                    <td>Jaime Lannister</td>
                    <td>25</td>
                    <td>155:04</td>
                </tr>
                <tr>
                    <td style="font-weight: bold; color: silver">2.</td>
                    <td>Rob Stark</td>
                    <td>12</td>
                    <td>120:15</td>
                </tr>
                <tr>
                    <td style="font-weight: bold; color: #a18631">3.</td>
                    <td>Robert Baratheon</td>
                    <td>10</td>
                    <td>105:41</td>
                </tr>
                <tr>
                    <td>4.</td>
                    <td>Brandon Tully</td>
                    <td>8</td>
                    <td>90:41</td>
                </tr>
                <tr>
                    <td>5.</td>
                    <td>Samwell Tarly</td>
                    <td>5</td>
                    <td>85:41</td>
                </tr>
            </table>
            <strong>Statistics of all games</strong>
            <ul>
                <li>The average rating is <span id="<c:out value="${wid}"/>_statistics-global-avg-rating"></span></li>
                <li><b>16</b> solvers are playing <b>4</b> games right now</li>
                <li><b>423</b> games was already played by <b>78</b> players</li>
                <li>The average solution time is <b>24:07</b></li>
            </ul>
        </div>
    </div>
</div>
