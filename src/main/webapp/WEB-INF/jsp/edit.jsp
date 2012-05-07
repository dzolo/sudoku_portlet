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
        
        $('#<portlet:namespace/>_edit_mode .sudoku-game_edit-tab-button').click(function ()
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
            //<c:forEach var="item" items="${skinsMap}" varStatus="status">
                '${item.key}': [
                    '${item.value['fontColor']['value']}',
                    '${item.value['borderColor']['value']}',
                    '${item.value['background']['value']}',
                    '${item.value['fixedBackground']['value']}',
                    '${item.value['font']['value']}'
                ]${not status.last ? ',' : ''}
            //</c:forEach>
        };
        // color pickers
        $('#<portlet:namespace/>_edit-tab1 .sudoku-game_edit-colorpicker').each(function (i, v)
        {
            $(v).ColorPicker({
                onBeforeShow: function ()
                {
                    var rgbString = $(this).find('div').css('backgroundColor');
                    
                    if (rgbString.search('rgb') == -1)
                    {
                        $(this).ColorPickerSetColor(rgbString);
                    }
                    else
                    {
                        var r = rgbString.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
                        $(this).ColorPickerSetColor({ r : r[1], g : r[2], b : r[3] });
                    }
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
                    '${currentSkin['fontColor']['value']}',
                    '${currentSkin['borderColor']['value']}',
                    '${currentSkin['background']['value']}',
                    '${currentSkin['fixedBackground']['value']}',
                    '${currentSkin['font']['value']}'
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
        
        //<c:if test="${isAdmin and remotePublisherEnabled}">
        /** Services **********************************************************/
        
        /**
         * Loads services to a data table
         */
        function SudokuGame_loadServices()
        {
            var $table = $('#<portlet:namespace/>_edit-tab2 table.dataTables_display');
            var $tableBody = $table.find('tbody');
            var request = new SudokuGame_Request('${app_path}');
            var data;
            
            window['<portlet:namespace/>_publishers'] = null;
            
            try
            {
                window['<portlet:namespace/>_publishers'] = data = request.makeGet('/service');
            }
            catch (e)
            {
                $tableBody.html($('<tr>').append(
                    $('<td>').stateBox('Error during loading', e, 'error', 'alert')
                ));
                return;
            }
            
            // clear the table
            if (this.oTable)
            {
                this.oTable.fnClearTable();
            }
            
            // put data into the table
            for (var i = 0; i < data.length; i++)
            {
                var url = data[i].url;
                var state = data[i].enabled ? 'enabled' : 'disabled';
                
                if (url.length > 20)
                {
                    url = url.substr(0, 17) + '..';
                }
                
                $tableBody.append(
                    $('<tr>').append(
                        $('<td>').css('cursor', 'default').text(data[i].name)
                    ).append(
                        $('<td>').css('cursor', 'default').html(
                            $('<img>').attr('src', '${app_path}/images/icons/' + state + '_12x12.png')
                        )
                    ).append(
                        $('<td>').css('cursor', 'default').html(
                            $('<a>').attr('href', data[i].url).attr('target', '_blank').text(url)
                        )
                    ).append(
                        $('<td>').css('cursor', 'default').text(SudokuGame_lasting(new Date(data[i].checkTime)))
                    ).append(
                        $('<td>').css('cursor', 'default').html(
                            $('<a>').click(SudokuGame_prepareEditPublisher)
                                .attr('rel', i)
                                .attr('href', '#')
                                .html($('<img>').attr('src', '${app_path}/images/icons/edit_12x12.png'))
                        )
                    )
                );
            }
            
            // init datatable
            this.oTable = $table.dataTable({
                'aoColumns'         : [
                    null, { 'bSortable' : false }, null,
                    { 'bSortable' : false }, { 'bSortable' : false }
                ],
                'sDom'              : 't<"F"p>',
                'sPaginationType'   : 'full_numbers',
                'bJQueryUI'         : true,
                'bDestroy'          : true
            });
        }
        
        // add new
        $('#<portlet:namespace/>_edit-tab2-link-add-service').click(function ()
        {
            var $dialog = $('#<portlet:namespace/>_dialog-publisher');
            
            $('#<portlet:namespace/>_dialog-publisher-name').val('').css('border', '1px solid #ccc');
            $('#<portlet:namespace/>_dialog-publisher-url').val('').css('border', '1px solid #ccc');
            $('#<portlet:namespace/>_dialog-publisher-checktime').val('').css('border', '1px solid #ccc');
            $('#<portlet:namespace/>_dialog-publisher-enabled').attr('checked', true);
                    
            $dialog.dialog('option', 'title', 'Add a new publisher')
                .attr('publisherIndex', '')
                .dialog('open');
            
            return false;
        });
        
        /**
         * Prepares the dialog for an editation and opens it.
         */
        function SudokuGame_prepareEditPublisher()
        {
            var publisherIndex = $(this).attr('rel');
            var $dialog = $('#<portlet:namespace/>_dialog-publisher');
            var publisher = window['<portlet:namespace/>_publishers'][publisherIndex];
            
            if (publisher)
            {
                $('#<portlet:namespace/>_dialog-publisher-name').val(publisher.name)
                    .css('border', '1px solid #ccc');
                $('#<portlet:namespace/>_dialog-publisher-url').val(publisher.url)
                    .css('border', '1px solid #ccc');
                $('#<portlet:namespace/>_dialog-publisher-checktime').val(publisher.checkTime)
                    .css('border', '1px solid #ccc');

                if (publisher.enabled)
                {
                    $('#<portlet:namespace/>_dialog-publisher-enabled').attr('checked', true);
                }
                else
                {
                    $('#<portlet:namespace/>_dialog-publisher-enabled').removeAttr('checked');
                }

                $dialog.dialog('option', 'title', 'Edit the ' + publisher.name + ' publisher')
                    .attr('publisherIndex', publisherIndex)
                    .dialog('open');
            }
            
            return false;
        }
        
        // load services at start
        SudokuGame_loadServices();
        
        // add dialog
        $('#<portlet:namespace/>_dialog-publisher').dialog({
            width           : 300,
            height          : 'auto',
            modal           : true,
            autoOpen        : false,
            closeOnEscape   : true,
            buttons: [{
                text    : 'Save',
                click   : function()
                {
                    var $dialog = $('#<portlet:namespace/>_dialog-publisher');
                    var $nameI = $('#<portlet:namespace/>_dialog-publisher-name');
                    var $urlI = $('#<portlet:namespace/>_dialog-publisher-url');
                    var $checkTimeI = $('#<portlet:namespace/>_dialog-publisher-checktime');
                    var isEnabled = $('#<portlet:namespace/>_dialog-publisher-checked').is(':checked');
                    var valid = true;
                    
                    // check
                    if (!$nameI.val().length) 
                    {
                        $nameI.css('border', '1px solid red');
                        $nameI.focus();
                        valid = false;
                    }
                    else
                    {
                        $nameI.css('border', '1px solid #ccc');
                    }
                    
                    if (!$urlI.val().match(/[-a-zA-Z0-9@:%_\+.~#?&//=]{2,256}\.[a-z]{2,4}\b(\/[-a-zA-Z0-9@:%_\+.~#?&//=]*)?/gi)) 
                    {
                        $urlI.css('border', '1px solid red');
                        if (valid)
                            $urlI.focus();
                        valid = false;
                    }
                    else
                    {
                        $urlI.css('border', '1px solid #ccc');
                    }
                    
                    if (!$checkTimeI.val().match(/^[1-9][0-9]*$/)) 
                    {
                        $checkTimeI.css('border', '1px solid red');
                        if (valid)
                            $checkTimeI.focus();
                        valid = false;
                    }
                    else
                    {
                        $checkTimeI.css('border', '1px solid #ccc');
                    }
                    
                    if (!valid) return;

                    // save
                    var request = new SudokuGame_Request('${app_path}');
                    var publisherIndex = $dialog.attr('publisherIndex');

                    try
                    {
                        // create
                        if (!publisherIndex)
                        {
                            var d = {
                                name        : $nameI.val(),
                                url         : $urlI.val(),
                                checkTime   : $checkTimeI.val(),
                                enabled     : isEnabled
                            };
                            
                            request.makePost('/service', d);
                        }
                        // edit
                        else
                        {
                            d = window['<portlet:namespace/>_publishers'][publisherIndex];
                            
                            d.name = $nameI.val();
                            d.url = $urlI.val();
                            d.checkTime = $checkTimeI.val();
                            d.enabled = isEnabled;
                            
                            request.makePut('/service', d);
                        }
                    }
                    catch (e)
                    {
                        alert('The publisher was not saved. Error: ' + e.toString());
                        return;
                    }

                    $dialog.dialog('close');
                    SudokuGame_loadServices();
                }
            },
            {
                text    : 'Cancel',
                click   : function()
                {
                    $(this).dialog('close');
                }
            }]
        });
        
        //</c:if>
    });

--></script>

<div id="<portlet:namespace/>_dialog-publisher" style="display: none">

    <table>
        <tr>
            <td><label>Name:</label></td>
            <td><input type="text" id="<portlet:namespace/>_dialog-publisher-name" style="width: 150px; margin: 2px; border: 1px solid #ccc" /></td>
        </tr>
        <tr>
            <td><label>URL:</label></td>
            <td><input type="text" id="<portlet:namespace/>_dialog-publisher-url" style="width: 150px; margin: 2px; border: 1px solid #ccc" /></td>
        </tr>
        <tr>
            <td><label>Check time (s):</label></td>
            <td><input type="text" id="<portlet:namespace/>_dialog-publisher-checktime" style="width: 40px; margin: 2px; border: 1px solid #ccc" /></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="checkbox" checked="checked" id="<portlet:namespace/>_dialog-publisher-checked" style="margin: 2px" /> <label>Enable obtaining of games?</label></td>
        </tr>
    </table>

</div>

<div class="sudoku-game_edit" id="<portlet:namespace/>_edit_mode">
    
    <a href="#" class="sudoku-game_edit-tab-button sudoku-game_edit-tab-button-active" rel="<portlet:namespace/>_edit-tab1"><b>Skin</b></a>
    
    <c:if test="${isAdmin and remotePublisherEnabled}">
        <a href="#" class="sudoku-game_edit-tab-button" rel="<portlet:namespace/>_edit-tab2"><b>Remote publishers</b></a>
    </c:if>
    
    <div class="sudoku-game_edit-tab" id="<portlet:namespace/>_edit-tab1">
        
        <form action="<portlet:actionURL name="changeSkin" portletMode="view"/>" method="post">
        
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
                            
    <c:if test="${isAdmin and remotePublisherEnabled}">
        <div class="sudoku-game_edit-tab" id="<portlet:namespace/>_edit-tab2" style="display: none">
            
            <a href="#" id="<portlet:namespace/>_edit-tab2-link-add-service" class="sudoku-game_button" style="margin: 5px 0; padding: 2px; display: block; float: left;">
                <img alt="New icon" src="${app_path}/images/icons/new_16x16.png" /> Add a new publisher
            </a>
            
            <table cellpadding="0" cellspacing="0" border="0" class="dataTables_display">
                <thead>
                    <tr>
                        <th>Name</th><th>State</th><th>URL</th><th>Expiration</th><th></th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
            
        </div>
    </c:if>
    
</div>