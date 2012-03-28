<%-- 
    Project       : Bachelor Thesis - Sudoku game implementation as portlet
    Document      : help.jsp
    Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
    Organization: : FIT VUT <http://www.fit.vutbr.cz>
--%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<script type="text/javascript"><!--

    // pause a game if running
    if (window['<portlet:namespace/>_game'] != undefined)
    {
        window['<portlet:namespace/>_game'].getTimer().pause();
    }
    
--></script>

<b>
    Not implemented yet
</b>