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

    $(document).ready(function ()
    {
        // skins data
        window['<portlet:namespace/>_skins'] = {
            <c:forEach var="item" items="${skinsMap}">
                '${item.key}': [
                    '${item.value.fontColor}',
                    '${item.value.borderColor}',
                    '${item.value.background}',
                    '${item.value.fixedBackground}'
                ],
            </c:forEach>
        };
        // color pickers
        $('.sudoku-game_edit-colorpicker').each(function (i, v)
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
            var ps = $('.sudoku-game_edit-colorpicker');
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
            }
            else
            {
                skin = [
                    '${currentSkin['fontColor']}',
                    '${currentSkin['borderColor']}',
                    '${currentSkin['background']}',
                    '${currentSkin['fixedBackground']}'
                ];
                
                ps.removeClass('sudoku-game_edit-colorpicker-disabled');
                ps.find('span').remove();
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
    
    <a href="" class="sudoku-game_edit-tab-button-active"><b>Skin</b></a>
    
    <div class="sudoku-game_edit-tab">
        
        
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
                    <td>Backgroud color:</td>
                    <td>
                        <div id="<portlet:namespace/>_edit-skin-field-bg-color" class="sudoku-game_edit-colorpicker">
                            <div style="background-color: white"></div>
                            <input type="hidden" name="field-bg-color" />
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>Background color fo fixed field:</td>
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
    
</div>