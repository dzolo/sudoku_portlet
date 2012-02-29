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
    /**
     * @see GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse) 
     */
    @Override
    public void doView(RenderRequest request, RenderResponse response)
            throws PortletException, IOException
    {
        response.setContentType("text/html");
        PortletRequestDispatcher dispatcher =
                getPortletContext().getRequestDispatcher("/WEB-INF/jsp/view.jsp");
        dispatcher.include(request, response);
    }

    /**
     * @see GenericPortlet#doEdit(javax.portlet.RenderRequest, javax.portlet.RenderResponse) 
     */
    @Override
    public void doEdit(RenderRequest request, RenderResponse response)
            throws PortletException, IOException
    {
        response.setContentType("text/html");
        PortletRequestDispatcher dispatcher =
                getPortletContext().getRequestDispatcher("/WEB-INF/jsp/edit.jsp");
        dispatcher.include(request, response);
    }

    /**
     * @see GenericPortlet#doHelp(javax.portlet.RenderRequest, javax.portlet.RenderResponse) 
     */
    @Override
    public void doHelp(RenderRequest request, RenderResponse response)
            throws PortletException, IOException
    {
        response.setContentType("text/html");
        PortletRequestDispatcher dispatcher =
                getPortletContext().getRequestDispatcher("/WEB-INF/jsp/help.jsp");
        dispatcher.include(request, response);
    }

    /**
     * @see GenericPortlet#doHeaders(javax.portlet.RenderRequest, javax.portlet.RenderResponse) 
     */
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
        script.setAttribute("src", path + "/js/lib/jquery-1.7.1.min.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, script);

        script = response.createElement("script");
        script.setAttribute("type", "text/javascript");
        script.setAttribute("src", path + "/js/lib/jquery.raty.min.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, script);

        script = response.createElement("script");
        script.setAttribute("type", "text/javascript");
        script.setAttribute("src", path + "/js/exceptions.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, script);

        script = response.createElement("script");
        script.setAttribute("type", "text/javascript");
        script.setAttribute("src", path + "/js/timer.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, script);

        script = response.createElement("script");
        script.setAttribute("type", "text/javascript");
        script.setAttribute("src", path + "/js/game_board_field.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, script);

        script = response.createElement("script");
        script.setAttribute("type", "text/javascript");
        script.setAttribute("src", path + "/js/game_board.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, script);

        script = response.createElement("script");
        script.setAttribute("type", "text/javascript");
        script.setAttribute("src", path + "/js/game.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, script);

        script = response.createElement("script");
        script.setAttribute("type", "text/javascript");
        script.setAttribute("src", path + "/js/request.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, script);
    }
}
