/*
 Navicat Premium Data Transfer

 Source Server         : 公司mysql
 Source Server Type    : MySQL
 Source Server Version : 50645
 Source Host           : 10.10.10.208:3306
 Source Schema         : sj_lstj

 Target Server Type    : MySQL
 Target Server Version : 50645
 File Encoding         : 65001

 Date: 24/02/2020 15:07:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bank_statement
-- ----------------------------
DROP TABLE IF EXISTS `bank_statement`;
CREATE TABLE `bank_statement`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `case_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '案件id',
  `transaction_date` date NULL DEFAULT NULL COMMENT '交易日期',
  `transaction_time` time(0) NULL DEFAULT NULL COMMENT '交易时间',
  `query_card_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '查询卡号',
  `full_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `account_balance` double(10, 2) NOT NULL COMMENT '账号余额',
  `transaction_amount` double(10, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `loan_identification` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借贷标识',
  `opponent_account_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对手账号',
  `account_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '户名',
  `delete_identifier` int(2) NULL DEFAULT NULL COMMENT '删除标识',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `time_stamp` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间戳',
  `Reserve1` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Reserve1',
  `Reserve2` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Reserve2',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '银行流水' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Data Transfer

 Source Server         : 公司mysql
 Source Server Type    : MySQL
 Source Server Version : 50645
 Source Host           : 10.10.10.208:3306
 Source Schema         : sj_lstj

 Target Server Type    : MySQL
 Target Server Version : 50645
 File Encoding         : 65001

 Date: 24/02/2020 14:48:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for case_table
-- ----------------------------
DROP TABLE IF EXISTS `case_table`;
CREATE TABLE `case_table`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `case_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '案件名称',
  `case_type_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '案件类型',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `introduction_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人id',
  `path` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '地址',
  `Remarks` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  `time_stamp` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间戳',
  `update_count` int(5) NULL DEFAULT NULL COMMENT '更改次数',
  `delete_identifier` int(1) NULL DEFAULT NULL COMMENT '删除标识',
  `Reserve1` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Reserve1',
  `Reserve2` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Reserve2',
  `Reserve3` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Reserve3',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : 公司mysql
 Source Server Type    : MySQL
 Source Server Version : 50645
 Source Host           : 10.10.10.208:3306
 Source Schema         : sj_lstj

 Target Server Type    : MySQL
 Target Server Version : 50645
 File Encoding         : 65001

 Date: 24/02/2020 14:49:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for case_type
-- ----------------------------
DROP TABLE IF EXISTS `case_type`;
CREATE TABLE `case_type`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `type_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型名称',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `delete_identifier` int(1) NULL DEFAULT NULL COMMENT '删除标识(0没删，1删除)',
  `time_stamp` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '案件类型' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : 公司mysql
 Source Server Type    : MySQL
 Source Server Version : 50645
 Source Host           : 10.10.10.208:3306
 Source Schema         : sj_lstj

 Target Server Type    : MySQL
 Target Server Version : 50645
 File Encoding         : 65001

 Date: 24/02/2020 15:03:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for end_card_number
-- ----------------------------
DROP TABLE IF EXISTS `end_card_number`;
CREATE TABLE `end_card_number`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'id',
  `card_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '卡号/户名',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名/户名',
  `case_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '案件id',
  `bddbz` int(1) NOT NULL COMMENT '本对端标志(0本端,1对端)',
  `Reserve1` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Reserve1'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '本端卡号信息' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Data Transfer

 Source Server         : 公司mysql
 Source Server Type    : MySQL
 Source Server Version : 50645
 Source Host           : 10.10.10.208:3306
 Source Schema         : sj_lstj

 Target Server Type    : MySQL
 Target Server Version : 50645
 File Encoding         : 65001

 Date: 24/02/2020 17:50:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for maximum_balance
-- ----------------------------
DROP TABLE IF EXISTS `maximum_balance`;
CREATE TABLE `maximum_balance`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'id',
  `date` date NULL DEFAULT NULL COMMENT '日期',
  `card_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `max_money` double(10, 2) NULL DEFAULT NULL COMMENT '最大金额',
  `max_balance` double(10, 2) NULL DEFAULT NULL COMMENT '最大余额',
  `case_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '案件id',
  `Reserve1` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Reserve1',
  `Reserve2` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Reserve2'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '最大余额表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

