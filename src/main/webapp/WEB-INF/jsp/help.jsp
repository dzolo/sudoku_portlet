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

<h3 style="margin-top: 5px; margin-left: 5px;">Documentation</h3>

<p style="margin-left: 5px;">A user manual about the Sudoku game portlet: <a href="${pageContext.request.contextPath}/user-manual.pdf" style="color: blue; text-decoration: underline">user-manual.pdf</a>.</p>
<p style="margin-left: 5px;">The manual is a PDF file which may be openned by <a href="http://get.adobe.com/reader/" target="_blank" style="color: blue; text-decoration: underline">Adobe Reader</a>.</p>