/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : site.js
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

/**
 * Resizes fields on the game board to calculated width and height.
 */
function sudoku_game__board_resizer()
{
    var $board_table = $('#sudoku-game_board table');
    var $board_locker = $('#sudoku-game_board-locker');
    
    if ($board_table)
    {
        var $readonly_inputs = $board_table.find('td input[readonly="readonly"]');
        var width = $board_table.width();
        // width / count of rows - borders sizes in pixels
        var item_width = Math.floor(width / 9 - 10);

        $board_table.css('height', width);
        $board_locker.css({
            'width'  : width,
            'height' : width
        });

        $board_table.find('td input').css({
            'fontSize' : Math.round(item_width * 2 / 3)
        });
        
        // set backgroud to solid fields
        $readonly_inputs.css({
            'backgroundColor' : '#f2f2f2'
        }).parent().css({
            'backgroundColor' : '#f2f2f2'
        });
        
        // fix for IE7
        if ($.browser.msie)
        {
            $board_table.find('td').css({
                'width' : item_width
            });
        }
    }
}

/**
 * On key press event hanler. Enables just one digit in field.
 * 
 * @param e         Key press event
 * @return          Bool value - true for accepting
 */
function sudoku_game__digit_only(e)
{
    var key = e.keyCode ? e.keyCode : e.which;

    // control keys
    if ((key == null) || (key == 0) || (key == 8) || 
        (key == 9) || (key == 13) || (key == 27))
    {
        return true;
    }
    // digit
    else if ((("0123456789").indexOf(String.fromCharCode(key)) > -1))
    {
        // just one digit
        return ($(this).val().length == 0);
    }
    
    return false;
}

/**
 * On key up event hanler. Enables arrow a movement over gameboard fields.
 * 
 * @param e         Key press up
 */
function sudoku_game__arrow_movement(e)
{
    var key = e.keyCode ? e.keyCode : e.which;
    var $this = $(this);
    var index = parseInt($this.attr('name').substr('board_field['.length), 10);
    var arrow = false;
    
    switch (key)
    {
    case 37: // left
        arrow = true;
        if (index % 9)
            index--;
        else
            index += 8;
        break;
    case 38: // up
        arrow = true;
        index -= 9;
        break;
    case 39: // right
        arrow = true;
        if ((index + 1) % 9)
            index++;
        else
            index -= 8;
        break;
    case 40: // down
        arrow = true;
        index += 9;
        break;
    }
    
    if (arrow)
    {
        if (index < 0)
        {
            index += 81;    
        }
        else if (index > 80)
        {
            index -= 81;
        }
        
        $('#sudoku-game_board input[name="board_field[' + index + ']"]').focus();
    }    
}

/**
 * Timer function.
 * @param time      Time in seconds
 */
function sudoku_game__timer(time)
{
    var $timer = $('#sudoku-game_footer-timer');
    
    if ($timer)
    {
        if (!time || time < 0)
            time = 0;
        
        var min = Math.floor(time / 60);
        var sec = time % 60;
        
        if (sec <= 9)
            sec = '0' + sec;
        
        $timer.text(min + ':' + sec);

        sudoku_game__timeout = time;
        sudoku_game__timeout_id = setTimeout('sudoku_game__timer(' + (time + 1) + ')', 1000);
    }
}

/**
 * Pauses the game
 */
function sudoku_game__pause_game()
{
    if (sudoku_game__timeout_id)
    {
        // stop counting
        clearTimeout(sudoku_game__timeout_id);
        sudoku_game__timeout_id = undefined;
        // disable buttons
        sudoku_game__game_buttons_set_enable(false);
        
        // disable play
        $('#sudoku-game_board-locker').show();
        $('#sudoku-game_board').css('opacity', 0.5);
        $('#sudoku-game_board input').attr('disabled', true);
        $('#sudoku-game_board input').blur();

        // change button
        $('#sudoku-game_footer-pause').unbind('click').text('Continue').attr({
            'title' : 'Continue the game',
            'id'    : 'sudoku-game_footer-play',
            'class' : 'sudoku-game_footer-play sudoku-game_button'
        }).click(function ()
        {
            sudoku_game__continue_game();
            return false;
        });
    }
}

/**
 * Continues the paused game 
 */
function sudoku_game__continue_game()
{
    if (sudoku_game__timeout)
    {
        // enable buttons
        sudoku_game__game_buttons_set_enable(false);
        // re-start counting
        sudoku_game__timer(sudoku_game__timeout);
        
        // enable play
        $('#sudoku-game_board-locker').hide();
        $('#sudoku-game_board').css('opacity', 1.0);
        $('#sudoku-game_board input').removeAttr('disabled');
        $('#sudoku-game_board input').unbind('focus');
        $('#sudoku-game_board input[name="board_field[0]"]').focus();

        // change button
        $('#sudoku-game_footer-play').unbind('click').text('Pause').attr({
            'title' : 'Pause the game',
            'id'    : 'sudoku-game_footer-pause',
            'class' : 'sudoku-game_footer-pause sudoku-game_button'
        }).click(function ()
        {
            sudoku_game__pause_game();
            return false;
        });
    }
}

/**
 * Event handler for button which show statistics in normal portal state.
 */
function sudoku_game__show_stats()
{
    var $game_board = $('#sudoku-game_board');
    var $game_stats = $('#sudoku-game_statistics');
    var $show_statistics_button = $('#sudoku-game_footer-statistics');
    var $show_game_button = $('#sudoku-game_footer-show-game');
    var $game_buttons = $('#sudoku-game_footer-pause, #sudoku-game_footer-play');

    if ($game_board && $game_stats)
    {
        // pause game
        sudoku_game__pause_game();
        $game_buttons.hide();
        // hide button and board
        $show_statistics_button.hide();
        $game_board.hide();
        // show stats
        $game_stats.fadeIn();
        // show reverse action button
        $show_game_button.show();
    }
}

/**
 * Event handler for button which show game from statistics in normal portal state.
 */
function sudoku_game__hide_stats()
{
    var $game_board = $('#sudoku-game_board');
    var $game_stats = $('#sudoku-game_statistics');
    var $show_statistics_button = $('#sudoku-game_footer-statistics');
    var $show_game_button = $('#sudoku-game_footer-show-game');
    var $game_buttons = $('#sudoku-game_footer-pause, #sudoku-game_footer-play');

    if ($game_board && $game_stats)
    {
        // pause game
        sudoku_game__continue_game();
        $game_buttons.show();
        // hide button and board
        $show_game_button.hide();
        $game_stats.hide();
        // show stats
        $game_board.fadeIn();
        // show reverse action button
        $show_statistics_button.show();
    }
}

/**
 * Sets enable/disable buttons related to current game
 * @param enable    Enable flag
 */
function sudoku_game__game_buttons_set_enable(enable)
{
    
}