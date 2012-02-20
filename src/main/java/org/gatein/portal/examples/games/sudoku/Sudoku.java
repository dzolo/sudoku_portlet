/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : Sudoku.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku;

import java.io.IOException;
import javax.portlet.*;
import org.w3c.dom.Element;

/**
 * Sudoku Portlet Class
 * 
 * @author Ondřej Fibich
 * @version 1.0
 */
public class Sudoku extends GenericPortlet
{
    /** Example array */
    private final static Integer[][] game = {
        {null, null, null, 4, null, null, null, null, null},
        {1, null, null, null, null, null, null, null, 8},
        {null, 2, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, 4, null, null},
        {null, null, null, null, null, null, null, null, 6},
        {5, null, null, 6, null, null, null, null, null},
        {null, null, null, null, null, null, null, 9, null},
        {null, null, null, null, null, 9, null, null, null},
        {null, null, null, 8, null, null, null, null, null}
    };

    @Override
    public void doView(RenderRequest request, RenderResponse response)
            throws PortletException, IOException
    {
        response.setContentType("text/html");
        request.setAttribute("game", game);
        PortletRequestDispatcher dispatcher =
                getPortletContext().getRequestDispatcher("/jsp/view.jsp");
        dispatcher.include(request, response);
    }

    @Override
    public void doEdit(RenderRequest request, RenderResponse response)
            throws PortletException, IOException
    {
        response.setContentType("text/html");
        PortletRequestDispatcher dispatcher =
                getPortletContext().getRequestDispatcher("/jsp/edit.jsp");
        dispatcher.include(request, response);
    }

    @Override
    public void doHelp(RenderRequest request, RenderResponse response)
            throws PortletException, IOException
    {
        response.setContentType("text/html");
        PortletRequestDispatcher dispatcher =
                getPortletContext().getRequestDispatcher("/jsp/help.jsp");
        dispatcher.include(request, response);
    }

    @Override
    public void doHeaders(RenderRequest request, RenderResponse response)
    {
        final String path = request.getContextPath();

        Element link = response.createElement("link");
        link.setAttribute("type", "text/css");
        link.setAttribute("rel", "stylesheet");
        link.setAttribute("href", path + "/css/style.css");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, link);

        Element script = response.createElement("script");
        script.setAttribute("type", "text/javascript");
        script.setAttribute("src", path + "/js/jquery-1.7.1.min.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, script);

        script = response.createElement("script");
        script.setAttribute("type", "text/javascript");
        script.setAttribute("src", path + "/js/jquery.raty.min.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, script);

        script = response.createElement("script");
        script.setAttribute("type", "text/javascript");
        script.setAttribute("src", path + "/js/site.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, script);
    }
}
