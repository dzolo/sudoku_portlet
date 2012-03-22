--------------------------------------------------------------------------------
-- Project       : Bachelor Thesis - Sudoku game implementation as portlet
-- Document      : database.sql
-- Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
-- Organization  : FIT VUT <http://www.fit.vutbr.cz>
--------------------------------------------------------------------------------
--
-- The init script of database for the MySQL database
--
--------------------------------------------------------------------------------

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

DROP TABLE IF EXISTS `games`;
CREATE TABLE `games` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `init_date` datetime NOT NULL,
  `init_values` text COLLATE utf8_czech_ci NOT NULL,
  `type` enum('GENERATED','SERVICE') COLLATE utf8_czech_ci NOT NULL DEFAULT 'GENERATED',
  `type_dificulty` enum('EASY','MODERATE','HARD','EXPERT') COLLATE utf8_czech_ci DEFAULT 'EASY',
  `type_service_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_games_services1` (`type_service_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_czech_ci AUTO_INCREMENT=0 ;

DROP TABLE IF EXISTS `game_solutions`;
CREATE TABLE `game_solutions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `game_id` int(11) NOT NULL,
  `user_id` varchar(255) COLLATE utf8_czech_ci DEFAULT NULL,
  `user_name` varchar(255) COLLATE utf8_czech_ci DEFAULT NULL,
  `values_` text COLLATE utf8_czech_ci NOT NULL,
  `time_start` datetime NOT NULL,
  `lasting` int(11) NOT NULL DEFAULT '0' COMMENT 'Lasting of solution in seconds',
  `finished` tinyint(1) NOT NULL DEFAULT '0',
  `rating` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_GAME_SOLUTION_game` (`game_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_czech_ci AUTO_INCREMENT=0 ;

DROP TABLE IF EXISTS `saved_games`;
CREATE TABLE `saved_games` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_czech_ci NOT NULL,
  `game_solution_id` int(11) NOT NULL,
  `saved` datetime NOT NULL,
  `lasting` int(11) NOT NULL,
  `values_` text COLLATE utf8_czech_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_saved_games_game_solutions1` (`game_solution_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_czech_ci AUTO_INCREMENT=0 ;

DROP TABLE IF EXISTS `services`;
CREATE TABLE `services` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_czech_ci NOT NULL,
  `url` varchar(255) COLLATE utf8_czech_ci NOT NULL,
  `check_time` int(11) NOT NULL COMMENT 'Period to another check of this service in seconds.',
  `enabled` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_czech_ci AUTO_INCREMENT=0 ;


ALTER TABLE `games`
  ADD CONSTRAINT `fk_games_services1` FOREIGN KEY (`type_service_id`) REFERENCES `services` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION;

ALTER TABLE `game_solutions`
  ADD CONSTRAINT `fk_GAME_SOLUTION_game` FOREIGN KEY (`game_id`) REFERENCES `games` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE `saved_games`
  ADD CONSTRAINT `fk_saved_games_game_solutions1` FOREIGN KEY (`game_solution_id`) REFERENCES `game_solutions` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;
