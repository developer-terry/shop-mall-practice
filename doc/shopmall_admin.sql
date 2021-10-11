-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- 主機： mysql
-- 產生時間： 2021 年 10 月 11 日 02:28
-- 伺服器版本： 8.0.25
-- PHP 版本： 7.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `shopmall_admin`
--
CREATE DATABASE /*!32312 IF NOT EXISTS*/`shopmall_admin` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `shopmall_admin`;

-- --------------------------------------------------------

--
-- 資料表結構 `QRTZ_BLOB_TRIGGERS`
--

CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- --------------------------------------------------------

--
-- 資料表結構 `QRTZ_CALENDARS`
--

CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- --------------------------------------------------------

--
-- 資料表結構 `QRTZ_CRON_TRIGGERS`
--

CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- 傾印資料表的資料 `QRTZ_CRON_TRIGGERS`
--

INSERT INTO `QRTZ_CRON_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `CRON_EXPRESSION`, `TIME_ZONE_ID`) VALUES
('RenrenScheduler', 'TASK_1', 'DEFAULT', '0 0/30 * * * ?', 'Asia/Taipei');

-- --------------------------------------------------------

--
-- 資料表結構 `QRTZ_FIRED_TRIGGERS`
--

CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint NOT NULL,
  `SCHED_TIME` bigint NOT NULL,
  `PRIORITY` int NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- --------------------------------------------------------

--
-- 資料表結構 `QRTZ_JOB_DETAILS`
--

CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- 傾印資料表的資料 `QRTZ_JOB_DETAILS`
--

INSERT INTO `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`, `DESCRIPTION`, `JOB_CLASS_NAME`, `IS_DURABLE`, `IS_NONCONCURRENT`, `IS_UPDATE_DATA`, `REQUESTS_RECOVERY`, `JOB_DATA`) VALUES
('RenrenScheduler', 'TASK_1', 'DEFAULT', NULL, 'io.renren.modules.job.utils.ScheduleJob', '0', '0', '0', '0', 0xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000174000d4a4f425f504152414d5f4b45597372002e696f2e72656e72656e2e6d6f64756c65732e6a6f622e656e746974792e5363686564756c654a6f62456e7469747900000000000000010200074c00086265616e4e616d657400124c6a6176612f6c616e672f537472696e673b4c000a63726561746554696d657400104c6a6176612f7574696c2f446174653b4c000e63726f6e45787072657373696f6e71007e00094c00056a6f6249647400104c6a6176612f6c616e672f4c6f6e673b4c0006706172616d7371007e00094c000672656d61726b71007e00094c00067374617475737400134c6a6176612f6c616e672f496e74656765723b7870740008746573745461736b7372000e6a6176612e7574696c2e44617465686a81014b5974190300007870770800000179f4d252987874000e3020302f3330202a202a202a203f7372000e6a6176612e6c616e672e4c6f6e673b8be490cc8f23df0200014a000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b0200007870000000000000000174000672656e72656e74000ce58f82e695b0e6b58be8af95737200116a6176612e6c616e672e496e746567657212e2a0a4f781873802000149000576616c75657871007e0013000000007800);

-- --------------------------------------------------------

--
-- 資料表結構 `QRTZ_LOCKS`
--

CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- 傾印資料表的資料 `QRTZ_LOCKS`
--

INSERT INTO `QRTZ_LOCKS` (`SCHED_NAME`, `LOCK_NAME`) VALUES
('RenrenScheduler', 'STATE_ACCESS'),
('RenrenScheduler', 'TRIGGER_ACCESS');

-- --------------------------------------------------------

--
-- 資料表結構 `QRTZ_PAUSED_TRIGGER_GRPS`
--

CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- --------------------------------------------------------

--
-- 資料表結構 `QRTZ_SCHEDULER_STATE`
--

CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint NOT NULL,
  `CHECKIN_INTERVAL` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- 傾印資料表的資料 `QRTZ_SCHEDULER_STATE`
--

INSERT INTO `QRTZ_SCHEDULER_STATE` (`SCHED_NAME`, `INSTANCE_NAME`, `LAST_CHECKIN_TIME`, `CHECKIN_INTERVAL`) VALUES
('RenrenScheduler', 'TerrydeMacBook-Pro.local1629633339758', 1633919326770, 15000);

-- --------------------------------------------------------

--
-- 資料表結構 `QRTZ_SIMPLE_TRIGGERS`
--

CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint NOT NULL,
  `REPEAT_INTERVAL` bigint NOT NULL,
  `TIMES_TRIGGERED` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- --------------------------------------------------------

--
-- 資料表結構 `QRTZ_SIMPROP_TRIGGERS`
--

CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int DEFAULT NULL,
  `INT_PROP_2` int DEFAULT NULL,
  `LONG_PROP_1` bigint DEFAULT NULL,
  `LONG_PROP_2` bigint DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- --------------------------------------------------------

--
-- 資料表結構 `QRTZ_TRIGGERS`
--

CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint DEFAULT NULL,
  `PREV_FIRE_TIME` bigint DEFAULT NULL,
  `PRIORITY` int DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint NOT NULL,
  `END_TIME` bigint DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint DEFAULT NULL,
  `JOB_DATA` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- 傾印資料表的資料 `QRTZ_TRIGGERS`
--

INSERT INTO `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `JOB_NAME`, `JOB_GROUP`, `DESCRIPTION`, `NEXT_FIRE_TIME`, `PREV_FIRE_TIME`, `PRIORITY`, `TRIGGER_STATE`, `TRIGGER_TYPE`, `START_TIME`, `END_TIME`, `CALENDAR_NAME`, `MISFIRE_INSTR`, `JOB_DATA`) VALUES
('RenrenScheduler', 'TASK_1', 'DEFAULT', 'TASK_1', 'DEFAULT', NULL, 1633919400000, 1633917600000, 5, 'WAITING', 'CRON', 1623339511000, 0, NULL, 2, 0xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000174000d4a4f425f504152414d5f4b45597372002e696f2e72656e72656e2e6d6f64756c65732e6a6f622e656e746974792e5363686564756c654a6f62456e7469747900000000000000010200074c00086265616e4e616d657400124c6a6176612f6c616e672f537472696e673b4c000a63726561746554696d657400104c6a6176612f7574696c2f446174653b4c000e63726f6e45787072657373696f6e71007e00094c00056a6f6249647400104c6a6176612f6c616e672f4c6f6e673b4c0006706172616d7371007e00094c000672656d61726b71007e00094c00067374617475737400134c6a6176612f6c616e672f496e74656765723b7870740008746573745461736b7372000e6a6176612e7574696c2e44617465686a81014b5974190300007870770800000179f4d252987874000e3020302f3330202a202a202a203f7372000e6a6176612e6c616e672e4c6f6e673b8be490cc8f23df0200014a000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b0200007870000000000000000174000672656e72656e74000ce58f82e695b0e6b58be8af95737200116a6176612e6c616e672e496e746567657212e2a0a4f781873802000149000576616c75657871007e0013000000007800);

-- --------------------------------------------------------

--
-- 資料表結構 `schedule_job`
--

CREATE TABLE `schedule_job` (
  `job_id` bigint NOT NULL COMMENT '任务id',
  `bean_name` varchar(200) DEFAULT NULL COMMENT 'spring bean名称',
  `params` varchar(2000) DEFAULT NULL COMMENT '参数',
  `cron_expression` varchar(100) DEFAULT NULL COMMENT 'cron表达式',
  `status` tinyint DEFAULT NULL COMMENT '任务状态  0：正常  1：暂停',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务';

--
-- 傾印資料表的資料 `schedule_job`
--

INSERT INTO `schedule_job` (`job_id`, `bean_name`, `params`, `cron_expression`, `status`, `remark`, `create_time`) VALUES
(1, 'testTask', 'renren', '0 0/30 * * * ?', 0, '参数测试', '2021-06-10 15:28:15');

-- --------------------------------------------------------

--
-- 資料表結構 `schedule_job_log`
--

CREATE TABLE `schedule_job_log` (
  `log_id` bigint NOT NULL COMMENT '任务日志id',
  `job_id` bigint NOT NULL COMMENT '任务id',
  `bean_name` varchar(200) DEFAULT NULL COMMENT 'spring bean名称',
  `params` varchar(2000) DEFAULT NULL COMMENT '参数',
  `status` tinyint NOT NULL COMMENT '任务状态    0：成功    1：失败',
  `error` varchar(2000) DEFAULT NULL COMMENT '失败信息',
  `times` int NOT NULL COMMENT '耗时(单位：毫秒)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务日志';

--
-- 傾印資料表的資料 `schedule_job_log`
--

INSERT INTO `schedule_job_log` (`log_id`, `job_id`, `bean_name`, `params`, `status`, `error`, `times`, `create_time`) VALUES
(1, 1, 'testTask', 'renren', 0, NULL, 13, '2021-06-11 00:00:00'),
(2, 1, 'testTask', 'renren', 0, NULL, 14, '2021-06-11 00:30:00'),
(3, 1, 'testTask', 'renren', 0, NULL, 10, '2021-06-11 01:00:00'),
(4, 1, 'testTask', 'renren', 0, NULL, 2, '2021-06-26 22:00:00'),
(5, 1, 'testTask', 'renren', 0, NULL, 1, '2021-06-26 22:30:00'),
(6, 1, 'testTask', 'renren', 0, NULL, 4, '2021-06-26 23:30:00'),
(7, 1, 'testTask', 'renren', 0, NULL, 36, '2021-06-27 00:00:00'),
(8, 1, 'testTask', 'renren', 0, NULL, 6, '2021-06-27 02:30:00'),
(9, 1, 'testTask', 'renren', 0, NULL, 2, '2021-06-27 04:00:00'),
(10, 1, 'testTask', 'renren', 0, NULL, 3, '2021-06-27 10:30:00'),
(11, 1, 'testTask', 'renren', 0, NULL, 1, '2021-06-27 11:00:00'),
(12, 1, 'testTask', 'renren', 0, NULL, 3, '2021-06-27 11:30:00'),
(13, 1, 'testTask', 'renren', 0, NULL, 4, '2021-06-27 13:00:00'),
(14, 1, 'testTask', 'renren', 0, NULL, 5, '2021-06-27 13:30:00'),
(15, 1, 'testTask', 'renren', 0, NULL, 15, '2021-06-27 14:00:00'),
(16, 1, 'testTask', 'renren', 0, NULL, 8, '2021-06-27 14:30:00'),
(17, 1, 'testTask', 'renren', 0, NULL, 16, '2021-06-27 15:00:00'),
(18, 1, 'testTask', 'renren', 0, NULL, 7, '2021-06-27 15:30:00'),
(19, 1, 'testTask', 'renren', 0, NULL, 10, '2021-06-27 16:00:00'),
(20, 1, 'testTask', 'renren', 0, NULL, 2, '2021-06-27 16:30:00'),
(21, 1, 'testTask', 'renren', 0, NULL, 8, '2021-06-27 17:00:00'),
(22, 1, 'testTask', 'renren', 0, NULL, 17, '2021-06-27 17:30:00'),
(23, 1, 'testTask', 'renren', 0, NULL, 8, '2021-06-27 18:00:00'),
(24, 1, 'testTask', 'renren', 0, NULL, 6, '2021-06-27 18:30:00'),
(25, 1, 'testTask', 'renren', 0, NULL, 4, '2021-06-27 19:00:00'),
(26, 1, 'testTask', 'renren', 0, NULL, 3, '2021-06-27 19:30:00'),
(27, 1, 'testTask', 'renren', 0, NULL, 11, '2021-06-27 20:00:00'),
(28, 1, 'testTask', 'renren', 0, NULL, 4, '2021-06-27 20:30:00'),
(29, 1, 'testTask', 'renren', 0, NULL, 13, '2021-06-27 21:00:00'),
(30, 1, 'testTask', 'renren', 0, NULL, 9, '2021-06-27 21:30:00'),
(31, 1, 'testTask', 'renren', 0, NULL, 4, '2021-06-27 22:00:00'),
(32, 1, 'testTask', 'renren', 0, NULL, 9, '2021-06-27 22:30:00'),
(33, 1, 'testTask', 'renren', 0, NULL, 8, '2021-06-27 23:00:00'),
(34, 1, 'testTask', 'renren', 0, NULL, 7, '2021-06-27 23:30:00'),
(35, 1, 'testTask', 'renren', 0, NULL, 91, '2021-06-28 00:00:00'),
(36, 1, 'testTask', 'renren', 0, NULL, 6, '2021-06-28 06:05:14'),
(37, 1, 'testTask', 'renren', 0, NULL, 6, '2021-06-28 23:00:00'),
(38, 1, 'testTask', 'renren', 0, NULL, 6, '2021-06-28 23:30:00'),
(39, 1, 'testTask', 'renren', 0, NULL, 46, '2021-06-29 00:00:00'),
(40, 1, 'testTask', 'renren', 0, NULL, 13, '2021-06-29 00:30:00'),
(41, 1, 'testTask', 'renren', 0, NULL, 10, '2021-06-29 01:00:00'),
(42, 1, 'testTask', 'renren', 0, NULL, 33, '2021-06-29 21:30:00'),
(43, 1, 'testTask', 'renren', 0, NULL, 47, '2021-06-29 22:00:00'),
(44, 1, 'testTask', 'renren', 0, NULL, 15, '2021-06-29 22:30:00'),
(45, 1, 'testTask', 'renren', 0, NULL, 8, '2021-06-29 23:00:00'),
(46, 1, 'testTask', 'renren', 0, NULL, 21, '2021-06-29 23:30:00'),
(47, 1, 'testTask', 'renren', 0, NULL, 364, '2021-06-30 00:00:01'),
(48, 1, 'testTask', 'renren', 0, NULL, 5, '2021-06-30 00:30:00'),
(49, 1, 'testTask', 'renren', 0, NULL, 10, '2021-06-30 01:00:00'),
(50, 1, 'testTask', 'renren', 0, NULL, 26, '2021-06-30 01:30:00'),
(51, 1, 'testTask', 'renren', 0, NULL, 14, '2021-06-30 21:00:00'),
(52, 1, 'testTask', 'renren', 0, NULL, 7, '2021-06-30 21:30:00'),
(53, 1, 'testTask', 'renren', 0, NULL, 7, '2021-06-30 22:00:00'),
(54, 1, 'testTask', 'renren', 0, NULL, 2, '2021-06-30 22:30:00'),
(55, 1, 'testTask', 'renren', 0, NULL, 4, '2021-06-30 23:00:00'),
(56, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-01 01:30:00'),
(57, 1, 'testTask', 'renren', 0, NULL, 52, '2021-07-01 02:00:00'),
(58, 1, 'testTask', 'renren', 0, NULL, 2, '2021-07-01 02:30:00'),
(59, 1, 'testTask', 'renren', 0, NULL, 1, '2021-07-01 03:00:00'),
(60, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-01 03:30:00'),
(61, 1, 'testTask', 'renren', 0, NULL, 2, '2021-07-01 04:00:00'),
(62, 1, 'testTask', 'renren', 0, NULL, 2, '2021-07-01 04:30:00'),
(63, 1, 'testTask', 'renren', 0, NULL, 24, '2021-07-01 20:30:00'),
(64, 1, 'testTask', 'renren', 0, NULL, 7, '2021-07-01 21:00:00'),
(65, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-01 21:30:00'),
(66, 1, 'testTask', 'renren', 0, NULL, 24, '2021-07-01 22:00:00'),
(67, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-01 22:30:00'),
(68, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-01 23:00:00'),
(69, 1, 'testTask', 'renren', 0, NULL, 28, '2021-07-01 23:30:00'),
(70, 1, 'testTask', 'renren', 0, NULL, 6, '2021-07-02 05:41:12'),
(71, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-02 21:30:00'),
(72, 1, 'testTask', 'renren', 0, NULL, 21, '2021-07-02 22:00:00'),
(73, 1, 'testTask', 'renren', 0, NULL, 1, '2021-07-03 15:00:00'),
(74, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-03 15:30:00'),
(75, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-03 16:00:00'),
(76, 1, 'testTask', 'renren', 0, NULL, 1, '2021-07-03 16:30:00'),
(77, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-03 17:00:00'),
(78, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-03 17:30:00'),
(79, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-03 18:00:00'),
(80, 1, 'testTask', 'renren', 0, NULL, 7, '2021-07-03 19:00:00'),
(81, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-03 19:30:00'),
(82, 1, 'testTask', 'renren', 0, NULL, 7, '2021-07-03 20:00:00'),
(83, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-03 20:30:00'),
(84, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-03 21:00:00'),
(85, 1, 'testTask', 'renren', 0, NULL, 7, '2021-07-03 21:30:00'),
(86, 1, 'testTask', 'renren', 0, NULL, 7, '2021-07-03 22:00:00'),
(87, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-03 22:30:00'),
(88, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-03 23:00:00'),
(89, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-03 23:30:00'),
(90, 1, 'testTask', 'renren', 0, NULL, 377, '2021-07-04 00:00:01'),
(91, 1, 'testTask', 'renren', 0, NULL, 2, '2021-07-04 00:32:05'),
(92, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-04 01:00:00'),
(93, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-04 01:30:00'),
(94, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-04 09:00:00'),
(95, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-04 09:30:00'),
(96, 1, 'testTask', 'renren', 0, NULL, 6, '2021-07-04 10:00:00'),
(97, 1, 'testTask', 'renren', 0, NULL, 7, '2021-07-04 10:30:00'),
(98, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-04 11:00:00'),
(99, 1, 'testTask', 'renren', 0, NULL, 8, '2021-07-04 15:00:00'),
(100, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-04 15:30:00'),
(101, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-04 16:00:00'),
(102, 1, 'testTask', 'renren', 0, NULL, 46, '2021-07-04 16:30:00'),
(103, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-04 17:00:00'),
(104, 1, 'testTask', 'renren', 0, NULL, 6, '2021-07-04 17:30:00'),
(105, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-04 18:00:00'),
(106, 1, 'testTask', 'renren', 0, NULL, 7, '2021-07-04 19:00:00'),
(107, 1, 'testTask', 'renren', 0, NULL, 19, '2021-07-04 19:30:00'),
(108, 1, 'testTask', 'renren', 0, NULL, 127, '2021-07-04 20:00:00'),
(109, 1, 'testTask', 'renren', 0, NULL, 21, '2021-07-04 20:30:00'),
(110, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-04 21:00:00'),
(111, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-04 21:30:00'),
(112, 1, 'testTask', 'renren', 0, NULL, 12, '2021-07-04 22:00:00'),
(113, 1, 'testTask', 'renren', 0, NULL, 11, '2021-07-04 22:30:00'),
(114, 1, 'testTask', 'renren', 0, NULL, 8, '2021-07-04 23:00:00'),
(115, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-05 09:00:12'),
(116, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-06 09:21:17'),
(117, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-06 18:36:01'),
(118, 1, 'testTask', 'renren', 0, NULL, 2, '2021-07-06 21:00:00'),
(119, 1, 'testTask', 'renren', 0, NULL, 7, '2021-07-06 21:30:00'),
(120, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-06 22:00:00'),
(121, 1, 'testTask', 'renren', 0, NULL, 2, '2021-07-06 22:30:00'),
(122, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-06 23:00:00'),
(123, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-06 23:30:00'),
(124, 1, 'testTask', 'renren', 0, NULL, 150, '2021-07-07 00:00:01'),
(125, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-07 00:30:00'),
(126, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-07 01:00:00'),
(127, 1, 'testTask', 'renren', 0, NULL, 14, '2021-07-07 11:30:00'),
(128, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-07 14:00:00'),
(129, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-07 16:15:54'),
(130, 1, 'testTask', 'renren', 0, NULL, 19, '2021-07-07 17:00:00'),
(131, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-07 22:37:43'),
(132, 1, 'testTask', 'renren', 0, NULL, 42, '2021-07-08 00:30:00'),
(133, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-08 01:00:00'),
(134, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-08 01:30:00'),
(135, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-08 02:00:00'),
(136, 1, 'testTask', 'renren', 0, NULL, 2, '2021-07-08 02:30:11'),
(137, 1, 'testTask', 'renren', 0, NULL, 16, '2021-07-08 21:30:00'),
(138, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-08 22:00:00'),
(139, 1, 'testTask', 'renren', 0, NULL, 26, '2021-07-08 22:30:00'),
(140, 1, 'testTask', 'renren', 0, NULL, 14, '2021-07-08 23:00:00'),
(141, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-08 23:30:00'),
(142, 1, 'testTask', 'renren', 0, NULL, 41, '2021-07-09 00:00:00'),
(143, 1, 'testTask', 'renren', 0, NULL, 14, '2021-07-09 00:30:00'),
(144, 1, 'testTask', 'renren', 0, NULL, 36, '2021-07-09 01:00:00'),
(145, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-09 23:00:00'),
(146, 1, 'testTask', 'renren', 0, NULL, 22, '2021-07-10 08:00:00'),
(147, 1, 'testTask', 'renren', 0, NULL, 7, '2021-07-10 08:30:00'),
(148, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-10 14:30:00'),
(149, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-10 16:30:00'),
(150, 1, 'testTask', 'renren', 0, NULL, 85, '2021-07-10 20:00:00'),
(151, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-10 21:00:00'),
(152, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-10 21:30:00'),
(153, 1, 'testTask', 'renren', 0, NULL, 49, '2021-07-10 22:00:00'),
(154, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-10 22:30:00'),
(155, 1, 'testTask', 'renren', 0, NULL, 6, '2021-07-10 23:00:00'),
(156, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-10 23:30:00'),
(157, 1, 'testTask', 'renren', 0, NULL, 617, '2021-07-11 00:00:01'),
(158, 1, 'testTask', 'renren', 0, NULL, 39, '2021-07-11 10:30:00'),
(159, 1, 'testTask', 'renren', 0, NULL, 11, '2021-07-11 11:00:00'),
(160, 1, 'testTask', 'renren', 0, NULL, 7, '2021-07-11 11:30:00'),
(161, 1, 'testTask', 'renren', 0, NULL, 17, '2021-07-11 12:00:00'),
(162, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-11 12:30:00'),
(163, 1, 'testTask', 'renren', 0, NULL, 57, '2021-07-11 13:00:00'),
(164, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-11 13:30:00'),
(165, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-11 14:00:00'),
(166, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-11 14:30:00'),
(167, 1, 'testTask', 'renren', 0, NULL, 134, '2021-07-11 15:00:00'),
(168, 1, 'testTask', 'renren', 0, NULL, 6, '2021-07-11 16:00:00'),
(169, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-11 16:30:00'),
(170, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-11 17:00:00'),
(171, 1, 'testTask', 'renren', 0, NULL, 22, '2021-07-11 17:30:00'),
(172, 1, 'testTask', 'renren', 0, NULL, 22, '2021-07-11 18:00:00'),
(173, 1, 'testTask', 'renren', 0, NULL, 27, '2021-07-11 18:30:00'),
(174, 1, 'testTask', 'renren', 0, NULL, 15, '2021-07-11 19:00:00'),
(175, 1, 'testTask', 'renren', 0, NULL, 14, '2021-07-11 19:30:00'),
(176, 1, 'testTask', 'renren', 0, NULL, 21, '2021-07-11 20:00:00'),
(177, 1, 'testTask', 'renren', 0, NULL, 2, '2021-07-11 20:30:00'),
(178, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-11 21:30:03'),
(179, 1, 'testTask', 'renren', 0, NULL, 16, '2021-07-11 22:30:00'),
(180, 1, 'testTask', 'renren', 0, NULL, 20, '2021-07-11 23:00:00'),
(181, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-11 23:30:00'),
(182, 1, 'testTask', 'renren', 0, NULL, 80, '2021-07-12 00:00:00'),
(183, 1, 'testTask', 'renren', 0, NULL, 218, '2021-07-12 00:30:00'),
(184, 1, 'testTask', 'renren', 0, NULL, 23, '2021-07-12 01:00:00'),
(185, 1, 'testTask', 'renren', 0, NULL, 2, '2021-07-12 20:56:00'),
(186, 1, 'testTask', 'renren', 0, NULL, 21, '2021-07-12 22:00:00'),
(187, 1, 'testTask', 'renren', 0, NULL, 12, '2021-07-12 22:30:00'),
(188, 1, 'testTask', 'renren', 0, NULL, 16, '2021-07-12 23:00:00'),
(189, 1, 'testTask', 'renren', 0, NULL, 18, '2021-07-12 23:30:00'),
(190, 1, 'testTask', 'renren', 0, NULL, 28, '2021-07-13 00:30:00'),
(191, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-13 23:00:04'),
(192, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-13 23:30:00'),
(193, 1, 'testTask', 'renren', 0, NULL, 448, '2021-07-14 00:00:01'),
(194, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-14 00:30:00'),
(195, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-14 01:00:00'),
(196, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-14 01:30:00'),
(197, 1, 'testTask', 'renren', 0, NULL, 8, '2021-07-14 22:30:00'),
(198, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-14 23:00:00'),
(199, 1, 'testTask', 'renren', 0, NULL, 6, '2021-07-14 23:30:00'),
(200, 1, 'testTask', 'renren', 0, NULL, 78, '2021-07-15 00:00:00'),
(201, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-15 00:30:00'),
(202, 1, 'testTask', 'renren', 0, NULL, 27, '2021-07-15 01:00:00'),
(203, 1, 'testTask', 'renren', 0, NULL, 12, '2021-07-15 15:16:59'),
(204, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-15 22:30:00'),
(205, 1, 'testTask', 'renren', 0, NULL, 6, '2021-07-15 23:00:00'),
(206, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-15 23:30:00'),
(207, 1, 'testTask', 'renren', 0, NULL, 211, '2021-07-16 00:00:01'),
(208, 1, 'testTask', 'renren', 0, NULL, 35, '2021-07-16 00:30:00'),
(209, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-16 01:00:00'),
(210, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-16 01:30:00'),
(211, 1, 'testTask', 'renren', 0, NULL, 18, '2021-07-16 21:00:00'),
(212, 1, 'testTask', 'renren', 0, NULL, 20, '2021-07-16 21:30:00'),
(213, 1, 'testTask', 'renren', 0, NULL, 8, '2021-07-16 22:00:00'),
(214, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-16 22:30:00'),
(215, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-16 23:00:00'),
(216, 1, 'testTask', 'renren', 0, NULL, 6, '2021-07-16 23:30:00'),
(217, 1, 'testTask', 'renren', 0, NULL, 45, '2021-07-17 00:00:00'),
(218, 1, 'testTask', 'renren', 0, NULL, 49, '2021-07-17 00:30:00'),
(219, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-17 01:30:00'),
(220, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-17 03:01:02'),
(221, 1, 'testTask', 'renren', 0, NULL, 14, '2021-07-17 11:00:00'),
(222, 1, 'testTask', 'renren', 0, NULL, 15, '2021-07-17 11:30:00'),
(223, 1, 'testTask', 'renren', 0, NULL, 30, '2021-07-17 12:00:00'),
(224, 1, 'testTask', 'renren', 0, NULL, 80, '2021-07-17 12:30:00'),
(225, 1, 'testTask', 'renren', 0, NULL, 11, '2021-07-17 13:00:00'),
(226, 1, 'testTask', 'renren', 0, NULL, 70, '2021-07-17 15:30:00'),
(227, 1, 'testTask', 'renren', 0, NULL, 14, '2021-07-17 16:00:00'),
(228, 1, 'testTask', 'renren', 0, NULL, 12, '2021-07-17 16:30:00'),
(229, 1, 'testTask', 'renren', 0, NULL, 27, '2021-07-17 17:00:00'),
(230, 1, 'testTask', 'renren', 0, NULL, 8, '2021-07-17 17:30:00'),
(231, 1, 'testTask', 'renren', 0, NULL, 19, '2021-07-17 18:00:00'),
(232, 1, 'testTask', 'renren', 0, NULL, 14, '2021-07-17 18:30:00'),
(233, 1, 'testTask', 'renren', 0, NULL, 39, '2021-07-17 19:00:00'),
(234, 1, 'testTask', 'renren', 0, NULL, 79, '2021-07-17 19:30:00'),
(235, 1, 'testTask', 'renren', 0, NULL, 8, '2021-07-17 20:00:00'),
(236, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-17 20:30:00'),
(237, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-18 00:30:00'),
(238, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-18 01:00:00'),
(239, 1, 'testTask', 'renren', 0, NULL, 22, '2021-07-18 10:30:00'),
(240, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-18 11:00:00'),
(241, 1, 'testTask', 'renren', 0, NULL, 14, '2021-07-18 12:00:00'),
(242, 1, 'testTask', 'renren', 0, NULL, 16, '2021-07-18 12:30:00'),
(243, 1, 'testTask', 'renren', 0, NULL, 24, '2021-07-18 15:00:00'),
(244, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-18 15:30:00'),
(245, 1, 'testTask', 'renren', 0, NULL, 360, '2021-07-18 16:00:00'),
(246, 1, 'testTask', 'renren', 0, NULL, 11, '2021-07-18 16:30:00'),
(247, 1, 'testTask', 'renren', 0, NULL, 23, '2021-07-18 17:00:00'),
(248, 1, 'testTask', 'renren', 0, NULL, 24, '2021-07-18 17:30:00'),
(249, 1, 'testTask', 'renren', 0, NULL, 8, '2021-07-18 18:00:00'),
(250, 1, 'testTask', 'renren', 0, NULL, 29, '2021-07-18 18:30:00'),
(251, 1, 'testTask', 'renren', 0, NULL, 19, '2021-07-18 19:00:00'),
(252, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-18 19:30:00'),
(253, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-18 20:00:00'),
(254, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-18 23:00:00'),
(255, 1, 'testTask', 'renren', 0, NULL, 15, '2021-07-18 23:30:00'),
(256, 1, 'testTask', 'renren', 0, NULL, 451, '2021-07-19 00:00:02'),
(257, 1, 'testTask', 'renren', 0, NULL, 33, '2021-07-19 00:30:00'),
(258, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-20 22:30:00'),
(259, 1, 'testTask', 'renren', 0, NULL, 18, '2021-07-20 23:00:00'),
(260, 1, 'testTask', 'renren', 0, NULL, 18, '2021-07-20 23:30:00'),
(261, 1, 'testTask', 'renren', 0, NULL, 51, '2021-07-21 00:00:00'),
(262, 1, 'testTask', 'renren', 0, NULL, 35, '2021-07-21 00:30:00'),
(263, 1, 'testTask', 'renren', 0, NULL, 16, '2021-07-21 01:00:00'),
(264, 1, 'testTask', 'renren', 0, NULL, 15, '2021-07-21 23:30:00'),
(265, 1, 'testTask', 'renren', 0, NULL, 258, '2021-07-22 00:00:02'),
(266, 1, 'testTask', 'renren', 0, NULL, 54, '2021-07-22 00:30:00'),
(267, 1, 'testTask', 'renren', 0, NULL, 17, '2021-07-22 01:00:00'),
(268, 1, 'testTask', 'renren', 0, NULL, 2, '2021-07-22 09:58:55'),
(269, 1, 'testTask', 'renren', 0, NULL, 7, '2021-07-22 23:30:00'),
(270, 1, 'testTask', 'renren', 0, NULL, 45, '2021-07-23 00:00:00'),
(271, 1, 'testTask', 'renren', 0, NULL, 12, '2021-07-23 00:30:00'),
(272, 1, 'testTask', 'renren', 0, NULL, 7, '2021-07-23 01:00:00'),
(273, 1, 'testTask', 'renren', 0, NULL, 6, '2021-07-23 01:30:00'),
(274, 1, 'testTask', 'renren', 0, NULL, 5, '2021-07-23 02:00:00'),
(275, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-23 02:30:00'),
(276, 1, 'testTask', 'renren', 0, NULL, 6, '2021-07-24 09:54:23'),
(277, 1, 'testTask', 'renren', 0, NULL, 7, '2021-07-24 13:30:00'),
(278, 1, 'testTask', 'renren', 0, NULL, 12, '2021-07-24 14:00:00'),
(279, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-24 14:30:00'),
(280, 1, 'testTask', 'renren', 0, NULL, 0, '2021-07-24 15:00:00'),
(281, 1, 'testTask', 'renren', 0, NULL, 8, '2021-07-24 15:30:00'),
(282, 1, 'testTask', 'renren', 0, NULL, 1, '2021-07-24 16:00:00'),
(283, 1, 'testTask', 'renren', 0, NULL, 2, '2021-07-24 16:30:00'),
(284, 1, 'testTask', 'renren', 0, NULL, 1, '2021-07-24 17:00:00'),
(285, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-24 17:30:00'),
(286, 1, 'testTask', 'renren', 0, NULL, 15, '2021-07-24 18:00:00'),
(287, 1, 'testTask', 'renren', 0, NULL, 19, '2021-07-24 18:30:00'),
(288, 1, 'testTask', 'renren', 0, NULL, 22, '2021-07-24 19:00:00'),
(289, 1, 'testTask', 'renren', 0, NULL, 8, '2021-07-24 19:30:00'),
(290, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-24 20:00:00'),
(291, 1, 'testTask', 'renren', 0, NULL, 14, '2021-07-24 20:30:00'),
(292, 1, 'testTask', 'renren', 0, NULL, 8, '2021-07-24 21:00:00'),
(293, 1, 'testTask', 'renren', 0, NULL, 128, '2021-07-24 21:30:00'),
(294, 1, 'testTask', 'renren', 0, NULL, 33, '2021-07-24 22:00:00'),
(295, 1, 'testTask', 'renren', 0, NULL, 34, '2021-07-25 15:30:00'),
(296, 1, 'testTask', 'renren', 0, NULL, 14, '2021-07-25 16:00:00'),
(297, 1, 'testTask', 'renren', 0, NULL, 8, '2021-07-25 16:30:00'),
(298, 1, 'testTask', 'renren', 0, NULL, 33, '2021-07-25 17:00:00'),
(299, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-25 17:30:00'),
(300, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-25 18:00:00'),
(301, 1, 'testTask', 'renren', 0, NULL, 12, '2021-07-25 18:30:00'),
(302, 1, 'testTask', 'renren', 0, NULL, 8, '2021-07-25 19:00:00'),
(303, 1, 'testTask', 'renren', 0, NULL, 52, '2021-07-25 19:30:00'),
(304, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-25 20:00:00'),
(305, 1, 'testTask', 'renren', 0, NULL, 6, '2021-07-26 06:52:33'),
(306, 1, 'testTask', 'renren', 0, NULL, 1, '2021-07-26 11:25:32'),
(307, 1, 'testTask', 'renren', 0, NULL, 2, '2021-07-26 16:47:05'),
(308, 1, 'testTask', 'renren', 0, NULL, 7, '2021-07-26 20:00:00'),
(309, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-26 23:30:00'),
(310, 1, 'testTask', 'renren', 0, NULL, 400, '2021-07-27 00:00:01'),
(311, 1, 'testTask', 'renren', 0, NULL, 12, '2021-07-27 00:30:00'),
(312, 1, 'testTask', 'renren', 0, NULL, 16, '2021-07-27 22:00:00'),
(313, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-27 22:30:00'),
(314, 1, 'testTask', 'renren', 0, NULL, 12, '2021-07-27 23:00:00'),
(315, 1, 'testTask', 'renren', 0, NULL, 34, '2021-07-27 23:30:00'),
(316, 1, 'testTask', 'renren', 0, NULL, 125, '2021-07-28 00:00:01'),
(317, 1, 'testTask', 'renren', 0, NULL, 90, '2021-07-28 00:30:00'),
(318, 1, 'testTask', 'renren', 0, NULL, 18, '2021-07-28 23:00:00'),
(319, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-28 23:30:00'),
(320, 1, 'testTask', 'renren', 0, NULL, 145, '2021-07-29 00:00:01'),
(321, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-29 05:06:55'),
(322, 1, 'testTask', 'renren', 0, NULL, 8, '2021-07-30 21:30:00'),
(323, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-30 22:00:00'),
(324, 1, 'testTask', 'renren', 0, NULL, 10, '2021-07-30 22:30:00'),
(325, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-30 23:00:00'),
(326, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-30 23:30:00'),
(327, 1, 'testTask', 'renren', 0, NULL, 456, '2021-07-31 00:00:01'),
(328, 1, 'testTask', 'renren', 0, NULL, 11, '2021-07-31 00:30:00'),
(329, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-31 01:00:00'),
(330, 1, 'testTask', 'renren', 0, NULL, 1, '2021-07-31 01:30:00'),
(331, 1, 'testTask', 'renren', 0, NULL, 2, '2021-07-31 02:00:00'),
(332, 1, 'testTask', 'renren', 0, NULL, 13, '2021-07-31 02:30:00'),
(333, 1, 'testTask', 'renren', 0, NULL, 1, '2021-07-31 03:00:00'),
(334, 1, 'testTask', 'renren', 0, NULL, 3, '2021-07-31 03:30:00'),
(335, 1, 'testTask', 'renren', 0, NULL, 4, '2021-07-31 04:00:00'),
(336, 1, 'testTask', 'renren', 0, NULL, 9, '2021-07-31 12:00:00'),
(337, 1, 'testTask', 'renren', 0, NULL, 67, '2021-08-17 19:30:00'),
(338, 1, 'testTask', 'renren', 0, NULL, 26, '2021-08-17 20:02:17'),
(339, 1, 'testTask', 'renren', 0, NULL, 1, '2021-08-17 21:03:38'),
(340, 1, 'testTask', 'renren', 0, NULL, 1, '2021-08-17 22:05:40'),
(341, 1, 'testTask', 'renren', 0, NULL, 9, '2021-08-17 23:30:00'),
(342, 1, 'testTask', 'renren', 0, NULL, 183, '2021-08-18 00:00:00'),
(343, 1, 'testTask', 'renren', 0, NULL, 22, '2021-08-18 00:30:00'),
(344, 1, 'testTask', 'renren', 0, NULL, 28, '2021-08-18 16:34:05'),
(345, 1, 'testTask', 'renren', 0, NULL, 1, '2021-08-18 18:04:07'),
(346, 1, 'testTask', 'renren', 0, NULL, 9, '2021-08-18 22:00:00'),
(347, 1, 'testTask', 'renren', 0, NULL, 9, '2021-08-18 22:30:00'),
(348, 1, 'testTask', 'renren', 0, NULL, 12, '2021-08-18 23:00:00'),
(349, 1, 'testTask', 'renren', 0, NULL, 75, '2021-08-18 23:30:00'),
(350, 1, 'testTask', 'renren', 0, NULL, 518, '2021-08-19 00:00:02'),
(351, 1, 'testTask', 'renren', 0, NULL, 19, '2021-08-19 00:30:00'),
(352, 1, 'testTask', 'renren', 0, NULL, 24, '2021-08-19 19:00:00'),
(353, 1, 'testTask', 'renren', 0, NULL, 19, '2021-08-19 19:30:00'),
(354, 1, 'testTask', 'renren', 0, NULL, 5, '2021-08-19 20:37:28'),
(355, 1, 'testTask', 'renren', 0, NULL, 10, '2021-08-20 00:30:00'),
(356, 1, 'testTask', 'renren', 0, NULL, 12, '2021-08-20 01:00:00'),
(357, 1, 'testTask', 'renren', 0, NULL, 38, '2021-08-20 01:30:00'),
(358, 1, 'testTask', 'renren', 0, NULL, 21, '2021-08-20 02:00:00'),
(359, 1, 'testTask', 'renren', 0, NULL, 15, '2021-08-20 02:30:00'),
(360, 1, 'testTask', 'renren', 0, NULL, 6, '2021-08-20 03:00:00'),
(361, 1, 'testTask', 'renren', 0, NULL, 21, '2021-08-20 03:30:00'),
(362, 1, 'testTask', 'renren', 0, NULL, 15, '2021-08-21 10:32:26'),
(363, 1, 'testTask', 'renren', 0, NULL, 18, '2021-08-21 11:00:00'),
(364, 1, 'testTask', 'renren', 0, NULL, 35, '2021-08-21 11:30:00'),
(365, 1, 'testTask', 'renren', 0, NULL, 18, '2021-08-21 12:00:00'),
(366, 1, 'testTask', 'renren', 0, NULL, 14, '2021-08-21 12:30:00'),
(367, 1, 'testTask', 'renren', 0, NULL, 10, '2021-08-21 13:00:00'),
(368, 1, 'testTask', 'renren', 0, NULL, 38, '2021-08-21 21:30:11'),
(369, 1, 'testTask', 'renren', 0, NULL, 58, '2021-08-21 22:00:00'),
(370, 1, 'testTask', 'renren', 0, NULL, 19, '2021-08-21 22:30:00'),
(371, 1, 'testTask', 'renren', 0, NULL, 43, '2021-08-21 23:00:00'),
(372, 1, 'testTask', 'renren', 0, NULL, 17, '2021-08-21 23:30:00'),
(373, 1, 'testTask', 'renren', 0, NULL, 447, '2021-08-22 00:00:02'),
(374, 1, 'testTask', 'renren', 0, NULL, 17, '2021-08-22 00:30:00'),
(375, 1, 'testTask', 'renren', 0, NULL, 66, '2021-08-22 01:00:00'),
(376, 1, 'testTask', 'renren', 0, NULL, 33, '2021-08-22 01:30:00'),
(377, 1, 'testTask', 'renren', 0, NULL, 12, '2021-08-22 02:00:00'),
(378, 1, 'testTask', 'renren', 0, NULL, 9, '2021-08-22 11:00:00'),
(379, 1, 'testTask', 'renren', 0, NULL, 15, '2021-08-22 11:30:00'),
(380, 1, 'testTask', 'renren', 0, NULL, 279, '2021-08-22 12:00:01'),
(381, 1, 'testTask', 'renren', 0, NULL, 43, '2021-08-22 12:30:00'),
(382, 1, 'testTask', 'renren', 0, NULL, 14, '2021-08-22 13:00:00'),
(383, 1, 'testTask', 'renren', 0, NULL, 13, '2021-08-22 13:30:00'),
(384, 1, 'testTask', 'renren', 0, NULL, 9, '2021-08-22 14:00:00'),
(385, 1, 'testTask', 'renren', 0, NULL, 35, '2021-08-22 14:30:00'),
(386, 1, 'testTask', 'renren', 0, NULL, 14, '2021-08-22 15:00:00'),
(387, 1, 'testTask', 'renren', 0, NULL, 10, '2021-08-22 16:00:00'),
(388, 1, 'testTask', 'renren', 0, NULL, 16, '2021-08-22 16:30:02'),
(389, 1, 'testTask', 'renren', 0, NULL, 19, '2021-08-22 17:00:00'),
(390, 1, 'testTask', 'renren', 0, NULL, 18, '2021-08-22 17:30:00'),
(391, 1, 'testTask', 'renren', 0, NULL, 10, '2021-08-22 18:00:00'),
(392, 1, 'testTask', 'renren', 0, NULL, 16, '2021-08-22 19:00:01'),
(393, 1, 'testTask', 'renren', 0, NULL, 8, '2021-08-22 19:30:00'),
(394, 1, 'testTask', 'renren', 0, NULL, 8, '2021-08-22 20:00:00'),
(395, 1, 'testTask', 'renren', 0, NULL, 27, '2021-08-22 20:30:00'),
(396, 1, 'testTask', 'renren', 0, NULL, 7, '2021-08-22 21:00:16'),
(397, 1, 'testTask', 'renren', 0, NULL, 22, '2021-08-22 21:30:00'),
(398, 1, 'testTask', 'renren', 0, NULL, 11, '2021-08-23 00:46:55'),
(399, 1, 'testTask', 'renren', 0, NULL, 5, '2021-08-23 21:37:03'),
(400, 1, 'testTask', 'renren', 0, NULL, 8, '2021-08-23 23:00:00'),
(401, 1, 'testTask', 'renren', 0, NULL, 29, '2021-08-23 23:30:00'),
(402, 1, 'testTask', 'renren', 0, NULL, 28, '2021-08-24 00:00:00'),
(403, 1, 'testTask', 'renren', 0, NULL, 21, '2021-08-24 00:30:00'),
(404, 1, 'testTask', 'renren', 0, NULL, 13, '2021-08-24 01:00:00'),
(405, 1, 'testTask', 'renren', 0, NULL, 10, '2021-08-24 01:30:00'),
(406, 1, 'testTask', 'renren', 0, NULL, 4, '2021-08-24 07:30:04'),
(407, 1, 'testTask', 'renren', 0, NULL, 17, '2021-08-25 08:00:00'),
(408, 1, 'testTask', 'renren', 0, NULL, 15, '2021-09-20 23:30:00'),
(409, 1, 'testTask', 'renren', 0, NULL, 157, '2021-09-21 00:00:01'),
(410, 1, 'testTask', 'renren', 0, NULL, 14, '2021-09-21 00:30:00'),
(411, 1, 'testTask', 'renren', 0, NULL, 25, '2021-09-21 01:00:00'),
(412, 1, 'testTask', 'renren', 0, NULL, 23, '2021-09-21 09:00:00'),
(413, 1, 'testTask', 'renren', 0, NULL, 40, '2021-09-21 09:30:00'),
(414, 1, 'testTask', 'renren', 0, NULL, 4, '2021-09-21 10:00:00'),
(415, 1, 'testTask', 'renren', 0, NULL, 11, '2021-09-21 10:30:00'),
(416, 1, 'testTask', 'renren', 0, NULL, 8, '2021-09-21 11:00:00'),
(417, 1, 'testTask', 'renren', 0, NULL, 32, '2021-09-21 11:30:00'),
(418, 1, 'testTask', 'renren', 0, NULL, 12, '2021-09-21 12:00:00'),
(419, 1, 'testTask', 'renren', 0, NULL, 13, '2021-09-21 12:30:00'),
(420, 1, 'testTask', 'renren', 0, NULL, 52, '2021-09-21 13:00:00'),
(421, 1, 'testTask', 'renren', 0, NULL, 4, '2021-09-21 13:30:00'),
(422, 1, 'testTask', 'renren', 0, NULL, 10, '2021-09-21 14:00:00'),
(423, 1, 'testTask', 'renren', 0, NULL, 2, '2021-09-21 14:30:00'),
(424, 1, 'testTask', 'renren', 0, NULL, 5, '2021-09-21 15:00:00'),
(425, 1, 'testTask', 'renren', 0, NULL, 2, '2021-09-21 15:30:00'),
(426, 1, 'testTask', 'renren', 0, NULL, 7, '2021-09-21 16:00:00'),
(427, 1, 'testTask', 'renren', 0, NULL, 18, '2021-09-21 16:30:00'),
(428, 1, 'testTask', 'renren', 0, NULL, 2, '2021-09-21 17:00:00'),
(429, 1, 'testTask', 'renren', 0, NULL, 14, '2021-09-21 17:30:00'),
(430, 1, 'testTask', 'renren', 0, NULL, 15, '2021-09-21 18:00:00'),
(431, 1, 'testTask', 'renren', 0, NULL, 4, '2021-09-21 18:30:00'),
(432, 1, 'testTask', 'renren', 0, NULL, 11, '2021-09-21 19:00:00'),
(433, 1, 'testTask', 'renren', 0, NULL, 3, '2021-09-21 19:30:00'),
(434, 1, 'testTask', 'renren', 0, NULL, 30, '2021-09-22 00:00:00'),
(435, 1, 'testTask', 'renren', 0, NULL, 16, '2021-09-22 00:30:00'),
(436, 1, 'testTask', 'renren', 0, NULL, 2, '2021-09-22 01:00:00'),
(437, 1, 'testTask', 'renren', 0, NULL, 8, '2021-09-22 01:30:00'),
(438, 1, 'testTask', 'renren', 0, NULL, 13, '2021-09-22 02:00:00'),
(439, 1, 'testTask', 'renren', 0, NULL, 1, '2021-09-22 02:30:00'),
(440, 1, 'testTask', 'renren', 0, NULL, 9, '2021-09-22 03:00:00'),
(441, 1, 'testTask', 'renren', 0, NULL, 3, '2021-09-22 03:30:00'),
(442, 1, 'testTask', 'renren', 0, NULL, 2, '2021-09-22 04:00:00'),
(443, 1, 'testTask', 'renren', 0, NULL, 7, '2021-09-22 04:30:00'),
(444, 1, 'testTask', 'renren', 0, NULL, 1, '2021-09-22 05:00:00'),
(445, 1, 'testTask', 'renren', 0, NULL, 19, '2021-09-22 10:00:00'),
(446, 1, 'testTask', 'renren', 0, NULL, 7, '2021-09-22 10:30:00'),
(447, 1, 'testTask', 'renren', 0, NULL, 27, '2021-09-22 11:00:00'),
(448, 1, 'testTask', 'renren', 0, NULL, 4, '2021-09-22 11:30:00'),
(449, 1, 'testTask', 'renren', 0, NULL, 16, '2021-09-22 12:30:00'),
(450, 1, 'testTask', 'renren', 0, NULL, 3, '2021-09-22 13:00:00'),
(451, 1, 'testTask', 'renren', 0, NULL, 1, '2021-09-22 13:30:00'),
(452, 1, 'testTask', 'renren', 0, NULL, 12, '2021-09-22 14:30:00'),
(453, 1, 'testTask', 'renren', 0, NULL, 110, '2021-09-22 15:00:00'),
(454, 1, 'testTask', 'renren', 0, NULL, 46, '2021-09-22 15:30:00'),
(455, 1, 'testTask', 'renren', 0, NULL, 31, '2021-09-22 16:00:00'),
(456, 1, 'testTask', 'renren', 0, NULL, 18, '2021-09-22 16:30:00'),
(457, 1, 'testTask', 'renren', 0, NULL, 8, '2021-09-22 17:00:00'),
(458, 1, 'testTask', 'renren', 0, NULL, 137, '2021-09-22 17:30:00'),
(459, 1, 'testTask', 'renren', 0, NULL, 12, '2021-09-22 19:00:00'),
(460, 1, 'testTask', 'renren', 0, NULL, 3, '2021-09-22 19:30:00'),
(461, 1, 'testTask', 'renren', 0, NULL, 98, '2021-09-22 20:00:00'),
(462, 1, 'testTask', 'renren', 0, NULL, 6, '2021-09-22 20:30:00'),
(463, 1, 'testTask', 'renren', 0, NULL, 8, '2021-09-22 21:30:00'),
(464, 1, 'testTask', 'renren', 0, NULL, 32, '2021-09-22 22:00:00'),
(465, 1, 'testTask', 'renren', 0, NULL, 1, '2021-09-22 22:30:00'),
(466, 1, 'testTask', 'renren', 0, NULL, 3, '2021-09-22 23:00:00'),
(467, 1, 'testTask', 'renren', 0, NULL, 351, '2021-09-23 01:00:01'),
(468, 1, 'testTask', 'renren', 0, NULL, 11, '2021-09-23 09:00:00'),
(469, 1, 'testTask', 'renren', 0, NULL, 59, '2021-09-23 09:30:00'),
(470, 1, 'testTask', 'renren', 0, NULL, 8, '2021-09-23 10:00:00'),
(471, 1, 'testTask', 'renren', 0, NULL, 8, '2021-09-23 10:30:00'),
(472, 1, 'testTask', 'renren', 0, NULL, 6, '2021-09-23 11:00:00'),
(473, 1, 'testTask', 'renren', 0, NULL, 13, '2021-09-23 11:30:00'),
(474, 1, 'testTask', 'renren', 0, NULL, 9, '2021-09-23 12:30:00'),
(475, 1, 'testTask', 'renren', 0, NULL, 2, '2021-09-23 13:00:00'),
(476, 1, 'testTask', 'renren', 0, NULL, 103, '2021-09-23 13:30:00'),
(477, 1, 'testTask', 'renren', 0, NULL, 18, '2021-09-23 14:00:00'),
(478, 1, 'testTask', 'renren', 0, NULL, 11, '2021-09-23 14:30:00'),
(479, 1, 'testTask', 'renren', 0, NULL, 36, '2021-09-23 15:00:00'),
(480, 1, 'testTask', 'renren', 0, NULL, 14, '2021-09-23 15:30:00'),
(481, 1, 'testTask', 'renren', 0, NULL, 15, '2021-09-23 16:00:00'),
(482, 1, 'testTask', 'renren', 0, NULL, 30, '2021-09-23 16:30:00'),
(483, 1, 'testTask', 'renren', 0, NULL, 25, '2021-09-23 17:00:00'),
(484, 1, 'testTask', 'renren', 0, NULL, 19, '2021-09-23 17:30:00'),
(485, 1, 'testTask', 'renren', 0, NULL, 21, '2021-09-23 18:00:00'),
(486, 1, 'testTask', 'renren', 0, NULL, 4, '2021-09-23 18:30:00'),
(487, 1, 'testTask', 'renren', 0, NULL, 14, '2021-09-23 19:00:00'),
(488, 1, 'testTask', 'renren', 0, NULL, 14, '2021-09-23 22:30:00'),
(489, 1, 'testTask', 'renren', 0, NULL, 14, '2021-09-23 23:00:00'),
(490, 1, 'testTask', 'renren', 0, NULL, 28, '2021-09-23 23:30:00'),
(491, 1, 'testTask', 'renren', 0, NULL, 19, '2021-09-24 00:00:00'),
(492, 1, 'testTask', 'renren', 0, NULL, 11, '2021-09-24 00:30:00'),
(493, 1, 'testTask', 'renren', 0, NULL, 42, '2021-09-24 01:00:00'),
(494, 1, 'testTask', 'renren', 0, NULL, 19, '2021-09-24 09:30:00'),
(495, 1, 'testTask', 'renren', 0, NULL, 11, '2021-09-24 10:00:00'),
(496, 1, 'testTask', 'renren', 0, NULL, 17, '2021-09-24 10:30:00'),
(497, 1, 'testTask', 'renren', 0, NULL, 23, '2021-09-24 11:00:00'),
(498, 1, 'testTask', 'renren', 0, NULL, 10, '2021-09-24 11:30:00'),
(499, 1, 'testTask', 'renren', 0, NULL, 14, '2021-09-25 03:27:56'),
(500, 1, 'testTask', 'renren', 0, NULL, 5, '2021-09-26 01:30:07'),
(501, 1, 'testTask', 'renren', 0, NULL, 41, '2021-09-26 21:30:00'),
(502, 1, 'testTask', 'renren', 0, NULL, 10, '2021-09-26 22:00:00'),
(503, 1, 'testTask', 'renren', 0, NULL, 36, '2021-09-26 22:30:00'),
(504, 1, 'testTask', 'renren', 0, NULL, 7, '2021-09-26 23:00:00'),
(505, 1, 'testTask', 'renren', 0, NULL, 12, '2021-09-27 08:00:00'),
(506, 1, 'testTask', 'renren', 0, NULL, 1, '2021-09-28 20:08:10'),
(507, 1, 'testTask', 'renren', 0, NULL, 1, '2021-09-28 22:00:25'),
(508, 1, 'testTask', 'renren', 0, NULL, 8, '2021-09-29 23:00:00'),
(509, 1, 'testTask', 'renren', 0, NULL, 11, '2021-09-29 23:30:00'),
(510, 1, 'testTask', 'renren', 0, NULL, 40, '2021-09-30 00:00:00'),
(511, 1, 'testTask', 'renren', 0, NULL, 9, '2021-09-30 00:30:00'),
(512, 1, 'testTask', 'renren', 0, NULL, 1, '2021-10-05 14:05:39'),
(513, 1, 'testTask', 'renren', 0, NULL, 14, '2021-10-07 00:30:00'),
(514, 1, 'testTask', 'renren', 0, NULL, 69, '2021-10-09 10:00:00'),
(515, 1, 'testTask', 'renren', 0, NULL, 5, '2021-10-09 10:30:00'),
(516, 1, 'testTask', 'renren', 0, NULL, 45, '2021-10-09 11:00:00'),
(517, 1, 'testTask', 'renren', 0, NULL, 1, '2021-10-09 11:30:00'),
(518, 1, 'testTask', 'renren', 0, NULL, 5, '2021-10-09 14:00:00'),
(519, 1, 'testTask', 'renren', 0, NULL, 11, '2021-10-09 14:30:00'),
(520, 1, 'testTask', 'renren', 0, NULL, 6, '2021-10-09 15:00:00'),
(521, 1, 'testTask', 'renren', 0, NULL, 5, '2021-10-09 15:30:00'),
(522, 1, 'testTask', 'renren', 0, NULL, 19, '2021-10-09 16:00:00'),
(523, 1, 'testTask', 'renren', 0, NULL, 7, '2021-10-09 16:30:00'),
(524, 1, 'testTask', 'renren', 0, NULL, 15, '2021-10-09 17:00:00'),
(525, 1, 'testTask', 'renren', 0, NULL, 8, '2021-10-09 17:30:00'),
(526, 1, 'testTask', 'renren', 0, NULL, 5, '2021-10-09 18:00:00'),
(527, 1, 'testTask', 'renren', 0, NULL, 25, '2021-10-09 18:30:00'),
(528, 1, 'testTask', 'renren', 0, NULL, 13, '2021-10-09 19:00:00'),
(529, 1, 'testTask', 'renren', 0, NULL, 5, '2021-10-09 19:30:00'),
(530, 1, 'testTask', 'renren', 0, NULL, 12, '2021-10-10 00:30:00'),
(531, 1, 'testTask', 'renren', 0, NULL, 56, '2021-10-10 01:00:00'),
(532, 1, 'testTask', 'renren', 0, NULL, 34, '2021-10-10 01:30:00'),
(533, 1, 'testTask', 'renren', 0, NULL, 8, '2021-10-10 02:00:00'),
(534, 1, 'testTask', 'renren', 0, NULL, 4, '2021-10-10 02:30:00'),
(535, 1, 'testTask', 'renren', 0, NULL, 45, '2021-10-10 03:00:00'),
(536, 1, 'testTask', 'renren', 0, NULL, 5, '2021-10-10 03:30:00'),
(537, 1, 'testTask', 'renren', 0, NULL, 12, '2021-10-10 04:00:00'),
(538, 1, 'testTask', 'renren', 0, NULL, 7, '2021-10-10 04:30:00'),
(539, 1, 'testTask', 'renren', 0, NULL, 23, '2021-10-10 09:00:00'),
(540, 1, 'testTask', 'renren', 0, NULL, 11, '2021-10-10 09:30:00'),
(541, 1, 'testTask', 'renren', 0, NULL, 6, '2021-10-10 10:16:08'),
(542, 1, 'testTask', 'renren', 0, NULL, 20, '2021-10-10 15:30:00'),
(543, 1, 'testTask', 'renren', 0, NULL, 5, '2021-10-10 20:30:00'),
(544, 1, 'testTask', 'renren', 0, NULL, 3, '2021-10-10 21:00:00'),
(545, 1, 'testTask', 'renren', 0, NULL, 8, '2021-10-10 21:30:00'),
(546, 1, 'testTask', 'renren', 0, NULL, 27, '2021-10-10 22:00:00'),
(547, 1, 'testTask', 'renren', 0, NULL, 22, '2021-10-10 22:30:00'),
(548, 1, 'testTask', 'renren', 0, NULL, 15, '2021-10-10 23:00:00'),
(549, 1, 'testTask', 'renren', 0, NULL, 32, '2021-10-10 23:30:00'),
(550, 1, 'testTask', 'renren', 0, NULL, 357, '2021-10-11 00:00:02'),
(551, 1, 'testTask', 'renren', 0, NULL, 50, '2021-10-11 00:30:00'),
(552, 1, 'testTask', 'renren', 0, NULL, 36, '2021-10-11 01:00:00'),
(553, 1, 'testTask', 'renren', 0, NULL, 25, '2021-10-11 01:30:00'),
(554, 1, 'testTask', 'renren', 0, NULL, 9, '2021-10-11 02:00:00'),
(555, 1, 'testTask', 'renren', 0, NULL, 5, '2021-10-11 08:30:00'),
(556, 1, 'testTask', 'renren', 0, NULL, 19, '2021-10-11 09:00:00'),
(557, 1, 'testTask', 'renren', 0, NULL, 16, '2021-10-11 09:30:00'),
(558, 1, 'testTask', 'renren', 0, NULL, 20, '2021-10-11 10:00:00');

-- --------------------------------------------------------

--
-- 資料表結構 `sys_captcha`
--

CREATE TABLE `sys_captcha` (
  `uuid` char(36) NOT NULL COMMENT 'uuid',
  `code` varchar(6) NOT NULL COMMENT '验证码',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统验证码';

--
-- 傾印資料表的資料 `sys_captcha`
--

INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES
('2a052cfe-ae57-41d6-8e70-1e68f3ba9976', '8y6x3', '2021-07-06 22:16:39'),
('2d77763e-e7d2-4c49-8ef9-4cbf6133bcda', 'xbgwx', '2021-06-27 11:37:51'),
('651bf467-3f19-4c83-8cc9-96028b75cf53', '5ex2e', '2021-06-27 11:24:40'),
('70870252-1139-47b2-89fc-86fa1cde7b1f', 'c3enm', '2021-07-06 22:16:43'),
('824783f4-c737-4b04-8c54-34a8682cc45d', 'dx6m8', '2021-07-06 22:16:42'),
('8b4d04f3-85c3-41d6-88c6-16ec317bdec9', 'y7wnw', '2021-07-11 21:08:27'),
('93d7fcb6-28dc-4a60-8e0a-d61a237fe38d', '5xn6n', '2021-06-27 11:04:08'),
('9a48f27e-4be6-467f-8c01-ff52ae70a1c9', '7mnay', '2021-06-10 23:49:29'),
('a0d92179-0230-488e-8ee0-8751d111c3e6', 'ncpga', '2021-06-27 11:32:30'),
('a3209c15-98dc-4d6a-8f03-aa549d181e1c', '6fmx2', '2021-06-27 11:04:10'),
('c2c2847a-b33f-4991-82a5-a50482414f14', 'gy4bf', '2021-06-27 11:04:14'),
('c4400327-285a-47fb-8e06-143505bef536', '53825', '2021-07-04 21:36:20'),
('c6d78e05-be82-42c6-8d2a-96285a565495', 'yn4mx', '2021-06-27 11:28:19'),
('dcc959a0-6fe6-44bb-8b27-da5a0ef36ba4', '4g2ec', '2021-06-27 11:02:38'),
('f20a0ea1-7e08-411a-818e-8504cfeef53e', 'exax6', '2021-07-06 22:31:49');

-- --------------------------------------------------------

--
-- 資料表結構 `sys_config`
--

CREATE TABLE `sys_config` (
  `id` bigint NOT NULL,
  `param_key` varchar(50) DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) DEFAULT NULL COMMENT 'value',
  `status` tinyint DEFAULT '1' COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置信息表';

--
-- 傾印資料表的資料 `sys_config`
--

INSERT INTO `sys_config` (`id`, `param_key`, `param_value`, `status`, `remark`) VALUES
(1, 'CLOUD_STORAGE_CONFIG_KEY', '{\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"aliyunDomain\":\"\",\"aliyunEndPoint\":\"\",\"aliyunPrefix\":\"\",\"qcloudBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qiniuAccessKey\":\"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ\",\"qiniuBucketName\":\"ios-app\",\"qiniuDomain\":\"http://7xqbwh.dl1.z0.glb.clouddn.com\",\"qiniuPrefix\":\"upload\",\"qiniuSecretKey\":\"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV\",\"type\":1}', 0, '云存储配置信息');

-- --------------------------------------------------------

--
-- 資料表結構 `sys_log`
--

CREATE TABLE `sys_log` (
  `id` bigint NOT NULL,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `time` bigint NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统日志';

--
-- 傾印資料表的資料 `sys_log`
--

INSERT INTO `sys_log` (`id`, `username`, `operation`, `method`, `params`, `time`, `ip`, `create_date`) VALUES
(1, 'admin', '保存菜单', 'io.renren.modules.sys.controller.SysMenuController.save()', '[{\"menuId\":31,\"parentId\":0,\"name\":\"商品系統\",\"url\":\"\",\"perms\":\"\",\"type\":0,\"icon\":\"\",\"orderNum\":0}]', 24, '0:0:0:0:0:0:0:1', '2021-06-26 21:55:16'),
(2, 'admin', '修改菜单', 'io.renren.modules.sys.controller.SysMenuController.update()', '[{\"menuId\":31,\"parentId\":0,\"name\":\"商品系統\",\"url\":\"\",\"perms\":\"\",\"type\":0,\"icon\":\"editor\",\"orderNum\":0}]', 21, '0:0:0:0:0:0:0:1', '2021-06-26 21:55:51'),
(3, 'admin', '保存菜单', 'io.renren.modules.sys.controller.SysMenuController.save()', '[{\"menuId\":32,\"parentId\":31,\"name\":\"分類維護\",\"url\":\"product/category\",\"perms\":\"\",\"type\":1,\"icon\":\"menu\",\"orderNum\":0}]', 18, '0:0:0:0:0:0:0:1', '2021-06-26 21:56:44'),
(4, 'admin', '保存菜单', 'io.renren.modules.sys.controller.SysMenuController.save()', '[{\"menuId\":33,\"parentId\":31,\"name\":\"品牌管理\",\"url\":\"product/brand\",\"perms\":\"\",\"type\":1,\"icon\":\"bianji\",\"orderNum\":0}]', 32, '0:0:0:0:0:0:0:1', '2021-06-27 23:41:17'),
(5, 'admin', '保存用户', 'io.renren.modules.sys.controller.SysUserController.save()', '[{\"userId\":2,\"username\":\"terry\",\"password\":\"ca2a1a3fe7e7d05922ad949bda974b04c96c5a392af161ad032a1160c2b3fe4b\",\"salt\":\"VEvvFZloNrMsUHoRLy4X\",\"email\":\"terry@fanswoo.com\",\"mobile\":\"0986949850\",\"status\":1,\"roleIdList\":[],\"createUserId\":1,\"createTime\":\"Jul 4, 2021, 9:42:18 PM\"}]', 274, '0:0:0:0:0:0:0:1', '2021-07-04 21:42:19'),
(6, 'admin', '保存菜单', 'io.renren.modules.sys.controller.SysMenuController.save()', '[{\"menuId\":76,\"parentId\":41,\"name\":\"規格維護\",\"url\":\"product-attrupdate\",\"perms\":\"\",\"type\":1,\"icon\":\"menu\",\"orderNum\":0}]', 41, '0:0:0:0:0:0:0:1', '2021-07-06 22:19:26'),
(7, 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '[76]', 82, '0:0:0:0:0:0:0:1', '2021-07-06 22:29:12'),
(8, 'admin', '保存菜单', 'io.renren.modules.sys.controller.SysMenuController.save()', '[{\"menuId\":77,\"parentId\":41,\"name\":\"規格維護\",\"url\":\"product/attrupdate\",\"perms\":\"\",\"type\":1,\"icon\":\"menu\",\"orderNum\":0}]', 17, '0:0:0:0:0:0:0:1', '2021-07-06 22:30:19');

-- --------------------------------------------------------

--
-- 資料表結構 `sys_menu`
--

CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL,
  `parent_id` bigint DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int DEFAULT NULL COMMENT '排序'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单管理';

--
-- 傾印資料表的資料 `sys_menu`
--

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES
(1, 0, '系统管理', NULL, NULL, 0, 'system', 0),
(2, 1, '管理员列表', 'sys/user', NULL, 1, 'admin', 1),
(3, 1, '角色管理', 'sys/role', NULL, 1, 'role', 2),
(4, 1, '菜单管理', 'sys/menu', NULL, 1, 'menu', 3),
(5, 1, 'SQL监控', 'http://localhost:8080/renren-fast/druid/sql.html', NULL, 1, 'sql', 4),
(6, 1, '定时任务', 'job/schedule', NULL, 1, 'job', 5),
(7, 6, '查看', NULL, 'sys:schedule:list,sys:schedule:info', 2, NULL, 0),
(8, 6, '新增', NULL, 'sys:schedule:save', 2, NULL, 0),
(9, 6, '修改', NULL, 'sys:schedule:update', 2, NULL, 0),
(10, 6, '删除', NULL, 'sys:schedule:delete', 2, NULL, 0),
(11, 6, '暂停', NULL, 'sys:schedule:pause', 2, NULL, 0),
(12, 6, '恢复', NULL, 'sys:schedule:resume', 2, NULL, 0),
(13, 6, '立即执行', NULL, 'sys:schedule:run', 2, NULL, 0),
(14, 6, '日志列表', NULL, 'sys:schedule:log', 2, NULL, 0),
(15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0),
(16, 2, '新增', NULL, 'sys:user:save,sys:role:select', 2, NULL, 0),
(17, 2, '修改', NULL, 'sys:user:update,sys:role:select', 2, NULL, 0),
(18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 0),
(19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0),
(20, 3, '新增', NULL, 'sys:role:save,sys:menu:list', 2, NULL, 0),
(21, 3, '修改', NULL, 'sys:role:update,sys:menu:list', 2, NULL, 0),
(22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0),
(23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0),
(24, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0),
(25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0),
(26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0),
(27, 1, '参数管理', 'sys/config', 'sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', 1, 'config', 6),
(29, 1, '系统日志', 'sys/log', 'sys:log:list', 1, 'log', 7),
(30, 1, '文件上传', 'oss/oss', 'sys:oss:all', 1, 'oss', 6),
(31, 0, '商品系统', '', '', 0, 'editor', 0),
(32, 31, '分类维护', 'product/category', '', 1, 'menu', 0),
(34, 31, '品牌管理', 'product/brand', '', 1, 'editor', 0),
(37, 31, '平台属性', '', '', 0, 'system', 0),
(38, 37, '属性分组', 'product/attrgroup', '', 1, 'tubiao', 0),
(39, 37, '规格参数', 'product/baseattr', '', 1, 'log', 0),
(40, 37, '销售属性', 'product/saleattr', '', 1, 'zonghe', 0),
(41, 31, '商品维护', 'product/spu', '', 0, 'zonghe', 0),
(42, 0, '优惠营销', '', '', 0, 'mudedi', 0),
(43, 0, '库存系统', '', '', 0, 'shouye', 0),
(44, 0, '订单系统', '', '', 0, 'config', 0),
(45, 0, '用户系统', '', '', 0, 'admin', 0),
(46, 0, '内容管理', '', '', 0, 'sousuo', 0),
(47, 42, '优惠券管理', 'coupon/coupon', '', 1, 'zhedie', 0),
(48, 42, '发放记录', 'coupon/history', '', 1, 'sql', 0),
(49, 42, '专题活动', 'coupon/subject', '', 1, 'tixing', 0),
(50, 42, '秒杀活动', 'coupon/seckill', '', 1, 'daohang', 0),
(51, 42, '积分维护', 'coupon/bounds', '', 1, 'geren', 0),
(52, 42, '满减折扣', 'coupon/full', '', 1, 'shoucang', 0),
(53, 43, '仓库维护', 'ware/wareinfo', '', 1, 'shouye', 0),
(54, 43, '库存工作单', 'ware/task', '', 1, 'log', 0),
(55, 43, '商品库存', 'ware/sku', '', 1, 'jiesuo', 0),
(56, 44, '订单查询', 'order/order', '', 1, 'zhedie', 0),
(57, 44, '退货单处理', 'order/return', '', 1, 'shanchu', 0),
(58, 44, '等级规则', 'order/settings', '', 1, 'system', 0),
(59, 44, '支付流水查询', 'order/payment', '', 1, 'job', 0),
(60, 44, '退款流水查询', 'order/refund', '', 1, 'mudedi', 0),
(61, 45, '会员列表', 'member/member', '', 1, 'geren', 0),
(62, 45, '会员等级', 'member/level', '', 1, 'tubiao', 0),
(63, 45, '积分变化', 'member/growth', '', 1, 'bianji', 0),
(64, 45, '统计信息', 'member/statistics', '', 1, 'sql', 0),
(65, 46, '首页推荐', 'content/index', '', 1, 'shouye', 0),
(66, 46, '分类热门', 'content/category', '', 1, 'zhedie', 0),
(67, 46, '评论管理', 'content/comments', '', 1, 'pinglun', 0),
(68, 41, 'spu管理', 'product/spu', '', 1, 'config', 0),
(69, 41, '发布商品', 'product/spuadd', '', 1, 'bianji', 0),
(70, 43, '采购单维护', '', '', 0, 'tubiao', 0),
(71, 70, '采购需求', 'ware/purchaseitem', '', 1, 'editor', 0),
(72, 70, '采购单', 'ware/purchase', '', 1, 'menu', 0),
(73, 41, '商品管理', 'product/manager', '', 1, 'zonghe', 0),
(74, 42, '会员价格', 'coupon/memberprice', '', 1, 'admin', 0),
(75, 42, '每日秒杀', 'coupon/seckillsession', '', 1, 'job', 0),
(77, 41, '規格維護', 'product/attrupdate', '', 1, 'menu', 0);

-- --------------------------------------------------------

--
-- 資料表結構 `sys_oss`
--

CREATE TABLE `sys_oss` (
  `id` bigint NOT NULL,
  `url` varchar(200) DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件上传';

-- --------------------------------------------------------

--
-- 資料表結構 `sys_role`
--

CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色';

-- --------------------------------------------------------

--
-- 資料表結構 `sys_role_menu`
--

CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL,
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint DEFAULT NULL COMMENT '菜单ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色与菜单对应关系';

-- --------------------------------------------------------

--
-- 資料表結構 `sys_user`
--

CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '盐',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户';

--
-- 傾印資料表的資料 `sys_user`
--

INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `create_time`) VALUES
(1, 'admin', '9ec9750e709431dad22365cabc5c625482e574c74adaebba7dd02f1129e4ce1d', 'YzcmCZNvbXocrsz9dm8e', 'root@renren.io', '13612345678', 1, 1, '2016-11-11 11:11:11'),
(2, 'terry', 'ca2a1a3fe7e7d05922ad949bda974b04c96c5a392af161ad032a1160c2b3fe4b', 'VEvvFZloNrMsUHoRLy4X', 'terry@fanswoo.com', '0986949850', 1, 1, '2021-07-04 21:42:19');

-- --------------------------------------------------------

--
-- 資料表結構 `sys_user_role`
--

CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与角色对应关系';

-- --------------------------------------------------------

--
-- 資料表結構 `sys_user_token`
--

CREATE TABLE `sys_user_token` (
  `user_id` bigint NOT NULL,
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户Token';

--
-- 傾印資料表的資料 `sys_user_token`
--

INSERT INTO `sys_user_token` (`user_id`, `token`, `expire_time`, `update_time`) VALUES
(1, '785bc1e3e6ed7d47d2d7df6215a7bd54', '2021-08-18 07:02:47', '2021-08-17 19:02:47');

-- --------------------------------------------------------

--
-- 資料表結構 `tb_user`
--

CREATE TABLE `tb_user` (
  `user_id` bigint NOT NULL,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `mobile` varchar(20) NOT NULL COMMENT '手机号',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户';

--
-- 傾印資料表的資料 `tb_user`
--

INSERT INTO `tb_user` (`user_id`, `username`, `mobile`, `password`, `create_time`) VALUES
(1, 'mark', '13612345678', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '2017-03-23 22:37:41');

-- --------------------------------------------------------

--
-- 資料表結構 `undo_log`
--

CREATE TABLE `undo_log` (
  `id` bigint NOT NULL,
  `branch_id` bigint NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `QRTZ_BLOB_TRIGGERS`
--
ALTER TABLE `QRTZ_BLOB_TRIGGERS`
  ADD PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  ADD KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`);

--
-- 資料表索引 `QRTZ_CALENDARS`
--
ALTER TABLE `QRTZ_CALENDARS`
  ADD PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`);

--
-- 資料表索引 `QRTZ_CRON_TRIGGERS`
--
ALTER TABLE `QRTZ_CRON_TRIGGERS`
  ADD PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`);

--
-- 資料表索引 `QRTZ_FIRED_TRIGGERS`
--
ALTER TABLE `QRTZ_FIRED_TRIGGERS`
  ADD PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  ADD KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  ADD KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  ADD KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  ADD KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  ADD KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  ADD KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`);

--
-- 資料表索引 `QRTZ_JOB_DETAILS`
--
ALTER TABLE `QRTZ_JOB_DETAILS`
  ADD PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  ADD KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  ADD KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`);

--
-- 資料表索引 `QRTZ_LOCKS`
--
ALTER TABLE `QRTZ_LOCKS`
  ADD PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`);

--
-- 資料表索引 `QRTZ_PAUSED_TRIGGER_GRPS`
--
ALTER TABLE `QRTZ_PAUSED_TRIGGER_GRPS`
  ADD PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`);

--
-- 資料表索引 `QRTZ_SCHEDULER_STATE`
--
ALTER TABLE `QRTZ_SCHEDULER_STATE`
  ADD PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`);

--
-- 資料表索引 `QRTZ_SIMPLE_TRIGGERS`
--
ALTER TABLE `QRTZ_SIMPLE_TRIGGERS`
  ADD PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`);

--
-- 資料表索引 `QRTZ_SIMPROP_TRIGGERS`
--
ALTER TABLE `QRTZ_SIMPROP_TRIGGERS`
  ADD PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`);

--
-- 資料表索引 `QRTZ_TRIGGERS`
--
ALTER TABLE `QRTZ_TRIGGERS`
  ADD PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  ADD KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  ADD KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  ADD KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  ADD KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  ADD KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  ADD KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  ADD KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  ADD KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  ADD KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  ADD KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  ADD KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  ADD KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`);

--
-- 資料表索引 `schedule_job`
--
ALTER TABLE `schedule_job`
  ADD PRIMARY KEY (`job_id`);

--
-- 資料表索引 `schedule_job_log`
--
ALTER TABLE `schedule_job_log`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `job_id` (`job_id`);

--
-- 資料表索引 `sys_captcha`
--
ALTER TABLE `sys_captcha`
  ADD PRIMARY KEY (`uuid`);

--
-- 資料表索引 `sys_config`
--
ALTER TABLE `sys_config`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `param_key` (`param_key`);

--
-- 資料表索引 `sys_log`
--
ALTER TABLE `sys_log`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `sys_menu`
--
ALTER TABLE `sys_menu`
  ADD PRIMARY KEY (`menu_id`);

--
-- 資料表索引 `sys_oss`
--
ALTER TABLE `sys_oss`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `sys_role`
--
ALTER TABLE `sys_role`
  ADD PRIMARY KEY (`role_id`);

--
-- 資料表索引 `sys_role_menu`
--
ALTER TABLE `sys_role_menu`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `sys_user`
--
ALTER TABLE `sys_user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- 資料表索引 `sys_user_role`
--
ALTER TABLE `sys_user_role`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `sys_user_token`
--
ALTER TABLE `sys_user_token`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `token` (`token`);

--
-- 資料表索引 `tb_user`
--
ALTER TABLE `tb_user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- 資料表索引 `undo_log`
--
ALTER TABLE `undo_log`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `schedule_job`
--
ALTER TABLE `schedule_job`
  MODIFY `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务id', AUTO_INCREMENT=2;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `schedule_job_log`
--
ALTER TABLE `schedule_job_log`
  MODIFY `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志id', AUTO_INCREMENT=559;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sys_config`
--
ALTER TABLE `sys_config`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sys_log`
--
ALTER TABLE `sys_log`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sys_menu`
--
ALTER TABLE `sys_menu`
  MODIFY `menu_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=78;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sys_oss`
--
ALTER TABLE `sys_oss`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sys_role`
--
ALTER TABLE `sys_role`
  MODIFY `role_id` bigint NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sys_role_menu`
--
ALTER TABLE `sys_role_menu`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sys_user`
--
ALTER TABLE `sys_user`
  MODIFY `user_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sys_user_role`
--
ALTER TABLE `sys_user_role`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `tb_user`
--
ALTER TABLE `tb_user`
  MODIFY `user_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `undo_log`
--
ALTER TABLE `undo_log`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `QRTZ_BLOB_TRIGGERS`
--
ALTER TABLE `QRTZ_BLOB_TRIGGERS`
  ADD CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`);

--
-- 資料表的限制式 `QRTZ_CRON_TRIGGERS`
--
ALTER TABLE `QRTZ_CRON_TRIGGERS`
  ADD CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`);

--
-- 資料表的限制式 `QRTZ_SIMPLE_TRIGGERS`
--
ALTER TABLE `QRTZ_SIMPLE_TRIGGERS`
  ADD CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`);

--
-- 資料表的限制式 `QRTZ_SIMPROP_TRIGGERS`
--
ALTER TABLE `QRTZ_SIMPROP_TRIGGERS`
  ADD CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`);

--
-- 資料表的限制式 `QRTZ_TRIGGERS`
--
ALTER TABLE `QRTZ_TRIGGERS`
  ADD CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
