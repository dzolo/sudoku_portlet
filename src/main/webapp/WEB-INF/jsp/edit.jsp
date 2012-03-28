<%-- 
    Project       : Bachelor Thesis - Sudoku game implementation as portlet
    Document      : edit.jsp
    Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
    Organization: : FIT VUT <http://www.fit.vutbr.cz>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<portlet:defineObjects />
<c:set var="app_path" value="${pageContext.request.contextPath}" />

<script type="text/javascript"><!--

    // pause a game if running
    if (window['<portlet:namespace/>_game'] != undefined)
    {
        window['<portlet:namespace/>_game'].getTimer().pause();
    }

    $(document).ready(function ()
    {
        /** Tabs **************************************************************/
        
        $('#<portlet:namespace/>_edit-tab1 .sudoku-game_edit-tab-button').click(function ()
        {
            var $this = $(this);
            var id = $this.attr('rel');
            var $tab = $('#' + id);
            var activeClass = 'sudoku-game_edit-tab-button-active';
            
            if ($tab.length && !$this.hasClass(activeClass))
            {
                $this.parent().find('.' + activeClass).removeClass(activeClass);
                $this.addClass(activeClass);
                $this.parent().find('.sudoku-game_edit-tab').hide();
                $tab.show();
            }
            
            return false;
        });
        
        /** Skin **************************************************************/
        
        // skins data
        window['<portlet:namespace/>_skins'] = {
            <c:forEach var="item" items="${skinsMap}">
                '${item.key}': [
                    '${item.value.fontColor}',
                    '${item.value.borderColor}',
                    '${item.value.background}',
                    '${item.value.fixedBackground}',
                    '${item.value.font}'
                ],
            </c:forEach>
        };
        // color pickers
        $('#<portlet:namespace/>_edit-tab1 .sudoku-game_edit-colorpicker').each(function (i, v)
        {
            $(v).ColorPicker({
                onBeforeShow: function ()
                {
                    var rgbString = $(this).find('div').css('backgroundColor');
                    var r = rgbString.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
                    $(this).ColorPickerSetColor({ r : r[1], g : r[2], b : r[3] });
                },
                onChange: function (hsb, hex, rgb)
                {
                    $(v).find('div').css('backgroundColor', '#' + hex);
                    $(v).find('input').val(hex);
                },
                onSubmit: function(hsb, hex, rgb, el)
                {
                    $(el).ColorPickerHide();
                }
            });
        });
        // selectbox change => reload colors
        $('#<portlet:namespace/>_edit-skin-current').change(function ()
        {
            var name = $(this).attr('value');
            var ps = $('#<portlet:namespace/>_edit-tab1 .sudoku-game_edit-colorpicker');
            var fontChooser = $('#<portlet:namespace/>_edit-skin-field-font');
            var skin;
            
            if (name != null && name.length > 0)
            {
                skin = window['<portlet:namespace/>_skins'][name];
                                
                ps.each(function (i, v)
                {
                    if ($(v).find('span').length == 0)
                    {
                        $(v).append(
                            $('<span>').css({
                                display   : 'block',
                                position  : 'absolute',
                                top       : '0px',
                                left      : '0px',
                                width     : '100%',
                                height    : '100%'
                            }).click(function ()
                            {
                                return false;
                            })
                        ).addClass('sudoku-game_edit-colorpicker-disabled');
                    }
                });
            
                fontChooser.css({
                    'fontFamily' : skin[skin.length - 1],
                    'padding'    : '8px 4px 4px 6px',
                    'width'      : '152px',
                    'height'     : '25px',
                    'overflow'   : 'hidden'
                }).text(skin[skin.length - 1]).append(
                    $('<input>').attr('type', 'hidden')
                        .attr('name', 'field-font')
                        .val(skin[skin.length - 1])
                );
            }
            else
            {
                skin = [
                    '${currentSkin['fontColor']}',
                    '${currentSkin['borderColor']}',
                    '${currentSkin['background']}',
                    '${currentSkin['fixedBackground']}',
                    '${currentSkin['font']}'
                ];
                
                ps.removeClass('sudoku-game_edit-colorpicker-disabled');
                ps.find('span').remove();
            
                fontChooser.html(
                    $('<input>').attr('type', 'text').attr('name', 'field-font').css({
                        'width'      : '150px',
                        'padding'    : '0 0 0 0',
                        'fontFamily' : skin[skin.length - 1],
                        'fontSize'   : '10px'
                    }).val(skin[skin.length - 1]).fontSelector()
                );
            }
            
            var counter = 0;
            
            ps.each(function (i, v)
            {
                $(v).find('div').css('backgroundColor', '#' + skin[counter]);
                $(v).find('input').val(skin[counter++]);
            });
            
        }).trigger('change');
    });

--></script>

<div class="sudoku-game_edit">
    
    <a href="" class="sudoku-game_edit-tab-button sudoku-game_edit-tab-button-active" rel="<portlet:namespace/>_edit-tab1"><b>Skin</b></a>
    
    <c:if test="${isAdmin}">
        <a href="" class="sudoku-game_edit-tab-button" rel="<portlet:namespace/>_edit-tab2"><b>Remote publishers</b></a>
    </c:if>
    
    <div class="sudoku-game_edit-tab" id="<portlet:namespace/>_edit-tab1">
        
        <form action="<portlet:actionURL name="changeSkin"/>" method="post">
        
            <table id="<portlet:namespace/>_edit-table">
                <tr>
                    <td>The current skin:</td>
                    <td>
                        <select id="<portlet:namespace/>_edit-skin-current" style="width: 150px">
                                <option value=""> - Custom - </option>
                            <c:forEach items="${skinNames}" var="name">
                                <option value="${name}"<c:if test="${name eq currentSkinName}"> selected="selected"</c:if>>${name}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><b>Game board prefrences</b></td>
                </tr>
                <tr>
                    <td>Font color:</td>
                    <td>
                        <div id="<portlet:namespace/>_edit-skin-board-font-color" class="sudoku-game_edit-colorpicker">
                            <div style="background-color: white"></div>
                            <input type="hidden" name="board-font-color" />
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>Border color:</td>
                    <td>
                        <div id="<portlet:namespace/>_edit-skin-board-border-color" class="sudoku-game_edit-colorpicker">
                            <div style="background-color: white"></div>
                            <input type="hidden" name="board-border-color" />
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><b>Fields prefrences</b></td>
                </tr>
                <tr>
                    <td>Font of field:</td>
                    <td>
                        <div id="<portlet:namespace/>_edit-skin-field-font"></div>
                    </td>
                </tr>
                <tr>
                    <td>Backgroud color:</td>
                    <td>
                        <div id="<portlet:namespace/>_edit-skin-field-bg-color" class="sudoku-game_edit-colorpicker">
                            <div style="background-color: white"></div>
                            <input type="hidden" name="field-bg-color" />
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>Background color of fixed field:</td>
                    <td>
                        <div id="<portlet:namespace/>_edit-skin-field-bg-fixed-color" class="sudoku-game_edit-colorpicker">
                            <div style="background-color: white"></div>
                            <input type="hidden" name="field-bg-fixed-color" />
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <button style="margin-top: 10px" type="submit">Save</button>
                    </td>
                </tr>
            </table>
                        
        </form>
        
    </div>
                            
    <c:if test="${isAdmin}">
        <div class="sudoku-game_edit-tab" id="<portlet:namespace/>_edit-tab2" style="display: none">
            <h1>Not implemeted yet.</h1>
        </div>
    </c:if>
    
</div>