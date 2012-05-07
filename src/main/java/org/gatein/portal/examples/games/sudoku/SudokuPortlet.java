/* 
 * Project       : Bachelor Thesis - SudokuPortlet game implementation as portlet
 * Document      : SudokuPortlet.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.portlet.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.gatein.portal.examples.games.sudoku.util.CustomErrorHandler;
import org.w3c.dom.*;
import org.xml.sax.SAXParseException;

/**
 * The Sudoku Portlet Class implements view, edit and help modes of portlet.
 * The content of the portlet in the view mode contains an JavaScript application
 * for playing of games and statistics.
 * The content of the portlet in the edit mode allows to change a skin of the
 * game. Services may be also managed from this mode if an admin if logged in.
 * The content of the portlet in the help mode contains a link to an end-user
 * documentation.
 * 
 * @author Ondřej Fibich
 * @version 1.0
 */
public class SudokuPortlet extends GenericPortlet
{
    /**
     * Path to a document which contains a skin definition
     */
    private static final String skinConfPath = "/WEB-INF/sudoku-portlet-skins.xml";
    
    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(SudokuPortlet.class.getName());
    
    /**
     * A map of predefined skins loaded during initialization
     */
    private Map<String, Skin> skins;
    
    /**
     * Name of a default skin name which correspondes to one in the skins Map
     */
    private String defaultSkinName;
    
    /**
     * Enables/disables remote publishers
     */
    private boolean remotePublishersEnabled = true;
    
    /**
     * @see GenericPortlet#init(javax.portlet.PortletConfig) 
     * @throws PortletException 
     */
    @Override
    public void init(PortletConfig config) throws PortletException
    {
        super.init(config);

        // enable/disable remote publisher
        final String rpe = config.getInitParameter("remotePublishersEnabled");
        remotePublishersEnabled = Boolean.valueOf(rpe);
        
        // init skins
        skins = new LinkedHashMap<String, Skin>();
        defaultSkinName = null;
        
        try
        {
            loadSkins();
        }
        catch (SAXParseException npex)
        {
            final String m = "Wrong sytax of portlet skins document, "
                           + "check DTD for more details";
            
            throw new PortletException(m, npex);
        }
        catch (Exception ex)
        {
            throw new PortletException("Cannot load portlet skins", ex);
        }
    }
    
    
    /**
     * @see GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse) 
     */
    @Override
    public void doView(RenderRequest request, RenderResponse response)
            throws PortletException, IOException
    {
        final String prevDolId = request.getPreferences().getValue("game-lastPlayedSolutionId", null);
        
        request.setAttribute("lastPlayedSolutionId", prevDolId);
        request.setAttribute("remotePublisherEnabled", remotePublishersEnabled);
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
        String skinName;
        Skin skin = new Skin();
        
        skin.load(request.getRemoteUser(), request.getPreferences(), skins.get(defaultSkinName));
        skinName = getNameOf(skin);
        
        
        request.setAttribute("skinsMap", skins);
        request.setAttribute("skinNames", skins.keySet());
        request.setAttribute("defaultSkinName", defaultSkinName);
        request.setAttribute("currentSkin", skin);
        request.setAttribute("currentSkinName", skinName);
        request.setAttribute("remotePublisherEnabled", remotePublishersEnabled);
        request.setAttribute("isAdmin", request.isUserInRole("administrator"));
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
     * Changes skin of a game for the current logged user.
     * 
     * @param request
     * @param response 
     */
    @ProcessAction(name="changeSkin")
    public void processChangeSkin(ActionRequest request, ActionResponse response)
            throws PortletException, PortletSecurityException, IOException
    {
        Skin skin = new Skin(
                request.getParameter("board-font-color"),
                request.getParameter("board-border-color"),
                request.getParameter("field-bg-color"),
                request.getParameter("field-bg-fixed-color"),
                request.getParameter("field-font")
        );
        
        try
        {
            skin.store(request.getRemoteUser(), request.getPreferences());
        }
        catch (ReadOnlyException ex)
        {
            logger.log(Level.SEVERE, "read only preferences", ex);
        }
    }
    
    /**
     * @see GenericPortlet#doHeaders(javax.portlet.RenderRequest, javax.portlet.RenderResponse) 
     */
    @Override
    public void doHeaders(RenderRequest request, RenderResponse response)
    {
        final String path = request.getContextPath();
        final String uid = request.getRemoteUser();
        
        Element link = response.createElement("link");
        link.setAttribute("type", "text/css");
        link.setAttribute("rel", "stylesheet");
        link.setAttribute("href", path + "/css/all-packed.css");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, link);
        
        link = response.createElement("style");
        link.setAttribute("type", "text/css");
        link.setAttribute("id", "sudoku-game_skin");
        link.setTextContent(buildSkinStyleTagContent(uid, request.getPreferences()));
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, link);

        Element script = response.createElement("script");
        script.setAttribute("type", "text/javascript");
        script.setAttribute("src", path + "/js/all-packed.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, script);
    }
    
    /**
     * Loads predefined skins
     * 
     * @throws SAXParseException    On invalid document syntax
     * @throws Exception            On other errors
     */
    private void loadSkins() throws SAXParseException, Exception
    {
        final String skinDocPath = getPortletContext().getRealPath(skinConfPath);
        
        // load a factory
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        fac.setValidating(true);

        // load a builder
        DocumentBuilder builder = fac.newDocumentBuilder();
        builder.setErrorHandler(new CustomErrorHandler(logger));

        // load a document
        Document doc = builder.parse(skinDocPath);
        NodeList skinNodes = doc.getElementsByTagName("skin");

        // load each skin tag
        for (int i = 0; i < skinNodes.getLength(); i++)
        {
            Skin skin = new Skin();
            Node skinNode = skinNodes.item(i);
            NamedNodeMap attrsSkin = skinNode.getAttributes();
            String name = attrsSkin.getNamedItem("name").getTextContent();
            NodeList childrensNodes = skinNode.getChildNodes();

            for (int u = 0; u < childrensNodes.getLength(); u++)
            {
                Node childNode = childrensNodes.item(u);
                NamedNodeMap attrsChild = childNode.getAttributes();

                if (childNode.getNodeName().equals("game-board"))
                {
                    Node fontColorNode = attrsChild.getNamedItem("font-color");
                    Node borderColorNode = attrsChild.getNamedItem("border-color");

                    skin.fontColor.setValue(fontColorNode.getTextContent());
                    skin.borderColor.setValue(borderColorNode.getTextContent());

                    childrensNodes = childNode.getChildNodes();

                    for (int w = 0; w < childrensNodes.getLength(); w++)
                    {
                        childNode = childrensNodes.item(w);
                        attrsChild = childNode.getAttributes();

                        if (childNode.getNodeName().equals("field"))
                        {
                            Node bgNode = attrsChild.getNamedItem("background");
                            Node fixBgNode = attrsChild.getNamedItem("fixed-background");
                            Node fontNode = attrsChild.getNamedItem("font");

                            skin.background.setValue(bgNode.getTextContent());
                            skin.fixedBackground.setValue(fixBgNode.getTextContent());
                            skin.font.setValue(fontNode.getTextContent());

                            break;
                        }
                    }

                    break; 
                }
            }

            // undefined or empty name of a skin
            if (name == null || name.isEmpty())
            {
                name = "Unknown";
                logger.log(Level.WARNING, "Empty skin name");
            }

            int counter = 1;
            String newName = name;

            while (skins.containsKey(newName))
            {
                newName = name + " " + (++counter);
            }

            name = newName;

            skins.put(name, skin);

            Node defaultAttr = attrsSkin.getNamedItem("default");

            if (defaultAttr != null && defaultAttr.getTextContent().equals("true"))
            {
                defaultSkinName = name;
            }
        }

        if (defaultSkinName == null)
        {
            logger.log(Level.WARNING, "Undefined default skin");

            // pick a random skin if there is any 
            if (skins.size() > 0)
            {
                defaultSkinName = skins.keySet().iterator().next();
            }
            else
            {
                throw new Exception("no skin defined");
            }
        }
    }
    
    /**
     * Gets name of given skin or null if the skin is a custom skin.
     * 
     * @param skin              Skin
     * @return                  Skin name
     */
    private String getNameOf(Skin skin)
    {
        if (skins.containsValue(skin))
        {
            for (String name : skins.keySet())
            {
                if (skins.get(name).equals(skin))
                {
                    return name;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Builds a skin of the portlet by prefrences or default skin
     * 
     * @param p                 Preferences of a user
     * @return                  Builded content of a style element
     */
    private String buildSkinStyleTagContent(String userId, PortletPreferences p)
    {
        Skin skin = new Skin();
        skin.load(userId, p, skins.get(defaultSkinName));
        return skin.toString();
    }
    
    /**
     * This class represents a skin of the game board.
     */
    public class Skin
    {
        // predefined teplates for skin properties
        private static final String FONT_COLOR_CSS = "td.sudoku-game_board-field-package input, table.sudoku-game_input-hint td span { color: #%s ! important }";
        private static final String BORDER_COLOR_CSS = "td.sudoku-game_board-field-package, .sudoku-game_board, table.sudoku-game_input-hint td span, .sudoku-game_input-hint-close { border-color: #%s ! important }";
        private static final String BACKGROUND_COLOR_CSS = "td.sudoku-game_board-field-package, td.sudoku-game_board-field-package input { background-color: #%s ! important }";
        private static final String FIXED_BACKGROUND_COLOR_CSS = "td.sudoku-game_readonly-input, td.sudoku-game_readonly-input input { background-color: #%s ! important } ";
        private static final String FONT_CSS = "td.sudoku-game_board-field-package input { font-family: %s }";
        
        // skin properties
        private SkinItem fontColor;
        private SkinItem borderColor;
        private SkinItem background;
        private SkinItem fixedBackground;
        private SkinItem font;

        /**
         * Creates an empty skin
         */
        public Skin()
        {
            this(null, null, null, null, null);
        }

        /**
         * Creates an skin with given properites
         * 
         * @param fontColor
         * @param borderColor
         * @param background
         * @param fixedBackground
         * @param font 
         */
        public Skin(String fontColor, String borderColor, String background,
                    String fixedBackground, String font)
        {
    
            this.fontColor = new SkinItem(fontColor, FONT_COLOR_CSS);
            this.borderColor = new SkinItem(borderColor, BORDER_COLOR_CSS);
            this.background = new SkinItem(background, BACKGROUND_COLOR_CSS);
            this.fixedBackground = new SkinItem(fixedBackground, FIXED_BACKGROUND_COLOR_CSS);
            this.font = new SkinItem(font, FONT_CSS);
        }
        
        /**
         * Loads properties from portlet preferences of the given user.
         * If the preferences of the user are not avaivable, default skin is used.
         * 
         * @param userId        Identificator of user
         * @param p             Portlet preferences
         * @param defaultSkin   Default skin
         */
        public void load(String userId, PortletPreferences p, Skin defaultSkin)
        {
            fontColor.setValue(p.getValue(userId + ":fontColor", defaultSkin.fontColor.getValue()));
            borderColor.setValue(p.getValue(userId + ":borderColor", defaultSkin.borderColor.getValue()));
            background.setValue(p.getValue(userId + ":background", defaultSkin.background.getValue()));
            fixedBackground.setValue(p.getValue(userId + ":fixedBackground", defaultSkin.fixedBackground.getValue()));
            font.setValue(p.getValue(userId + ":font", defaultSkin.font.getValue()));
        }
        
        /**
         * Strores properties to portlet preferences of the given user.
         * 
         * @param userId        Identificator of user
         * @param p             Portlet preferences
         * @throws ReadOnlyException
         * @throws IOException
         * @throws ValidatorException 
         */
        public void store(String userId, PortletPreferences p)
                throws ReadOnlyException, IOException, ValidatorException
        {
            if (userId != null && !userId.isEmpty())
            {
                p.setValue(userId + ":fontColor", fontColor.getValue());
                p.setValue(userId + ":borderColor", borderColor.getValue());
                p.setValue(userId + ":background", background.getValue());
                p.setValue(userId + ":fixedBackground", fixedBackground.getValue());
                p.setValue(userId + ":font", font.getValue());
                p.store();
            }
        }

        @Override
        public String toString()
        {
            StringBuilder b = new StringBuilder();
            b.append(fontColor.toString());
            b.append(" ");
            b.append(borderColor.toString());
            b.append(" ");
            b.append(background.toString());
            b.append(" ");
            b.append(fixedBackground.toString());
            b.append(" ");
            b.append(font.toString());
            return b.toString();
        }

        /**
         * Gets background of the skin
         * 
         * @return          Background color in hexa form without leadin sharp 
         */
        public SkinItem getBackground()
        {
            return background;
        }


        /**
         * Gets background of the skin
         * 
         * @return          Color in hexa form without leading sharp 
         */
        public SkinItem getBorderColor()
        {
            return borderColor;
        }

        /**
         * Gets fixed background of the skin
         * 
         * @return          Color in hexa form without leading sharp 
         */
        public SkinItem getFixedBackground()
        {
            return fixedBackground;
        }

        /**
         * Gets font of the skin
         * 
         * @return          A list of fonts 
         */
        public SkinItem getFont()
        {
            return font;
        }

        /**
         * Gets font color of the skin
         * 
         * @return          Color in hexa form without leading sharp 
         */
        public SkinItem getFontColor()
        {
            return fontColor;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            
            if (getClass() != obj.getClass())
            {
                return false;
            }
            
            final Skin other = (Skin) obj;
            
            if (this.fontColor != other.fontColor &&
                (this.fontColor == null || !this.fontColor.equals(other.fontColor)))
            {
                return false;
            }
            
            if (this.borderColor != other.borderColor &&
                (this.borderColor == null || !this.borderColor.equals(other.borderColor)))
            {
                return false;
            }
            
            if (this.background != other.background &&
                (this.background == null || !this.background.equals(other.background)))
            {
                return false;
            }
            
            if (this.fixedBackground != other.fixedBackground &&
                (this.fixedBackground == null || !this.fixedBackground.equals(other.fixedBackground)))
            {
                return false;
            }
            
            if (this.font != other.font &&
                (this.font == null || !this.font.equals(other.font)))
            {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode()
        {
            int hash = 5;
            hash = 31 * hash + (this.fontColor != null ? this.fontColor.hashCode() : 0);
            hash = 31 * hash + (this.borderColor != null ? this.borderColor.hashCode() : 0);
            hash = 31 * hash + (this.background != null ? this.background.hashCode() : 0);
            hash = 31 * hash + (this.fixedBackground != null ? this.fixedBackground.hashCode() : 0);
            hash = 31 * hash + (this.font != null ? this.font.hashCode() : 0);
            return hash;
        }
        
    }
    
    /**
     * This class representes an item of skin.
     * 
     * @see Skin
     */
    public class SkinItem
    {
        private String value;
        private String template;

        /**
         * Creates a skin with a teplate
         * 
         * @param template  String teplate for value
         */
        public SkinItem(String template)
        {
            this(null, template);
        }

        /**
         * Creates a skin with a teplate and its value
         * 
         * @param value     String value
         * @param template  String teplate for value
         */
        public SkinItem(String value, String template)
        {
            this.template = template;
            this.value = value;
        }
        
        /**
         * Gets value of the skin item
         * 
         * @return          String value 
         */
        public String getValue()
        {
            return value;
        }

        /**
         * Sets value of the skin item
         * 
         * @param value     New string value
         */
        public void setValue(String value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            if (template == null || template.isEmpty())
            {
                return "";
            }
            
            return String.format(template, new Object[] {value});
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            
            if (getClass() != obj.getClass())
            {
                return false;
            }
            
            final SkinItem other = (SkinItem) obj;
            
            if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value))
            {
                return false;
            }
            
            if ((this.template == null) ? (other.template != null) : !this.template.equals(other.template))
            {
                return false;
            }
            
            return true;
        }

        @Override
        public int hashCode()
        {
            int hash = 5;
            hash = 13 * hash + (this.value != null ? this.value.hashCode() : 0);
            hash = 13 * hash + (this.template != null ? this.template.hashCode() : 0);
            return hash;
        }
        
    }
    
}
