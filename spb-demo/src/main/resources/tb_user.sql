/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50624
Source Host           : 127.0.0.1:3306
Source Database       : baba

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2017-08-13 11:09:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_user`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) DEFAULT NULL COMMENT '注册手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '注册邮箱',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `phone` (`phone`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('7', 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '13488888888', 'aa@a', '2015-04-06 17:03:55', '2015-04-06 17:03:55');
INSERT INTO `tb_user` VALUES ('9', 'zhangsan1', 'e10adc3949ba59abbe56e057f20f883e', '13333333333', null, '2015-04-07 10:32:08', '2015-04-07 10:32:08');
INSERT INTO `tb_user` VALUES ('10', 'zhangsan2', '195d91be1e3ba6f1c857d46f24c5a454', '13333333334', null, '2015-04-07 10:33:37', '2015-04-07 10:33:37');
INSERT INTO `tb_user` VALUES ('11', 'zhangsan3', '195d91be1e3ba6f1c857d46f24c5a454', '13333333335', null, '2015-04-07 10:35:57', '2015-04-07 10:35:57');
INSERT INTO `tb_user` VALUES ('12', 'zhangsan5', '195d91be1e3ba6f1c857d46f24c5a454', '13333333336', null, '2015-04-07 10:46:19', '2015-04-07 10:46:19');
INSERT INTO `tb_user` VALUES ('14', 'lisi', '202cb962ac59075b964b07152d234b70', '12344444444', null, '2015-06-19 10:02:11', '2015-06-19 10:02:11');
INSERT INTO `tb_user` VALUES ('16', 'lisi1', '202cb962ac59075b964b07152d234b70', '12344444442', null, '2015-06-19 10:24:27', '2015-06-19 10:24:27');
INSERT INTO `tb_user` VALUES ('17', 'jd_gogogo', '745404feaba9fb037e01b4a91c6ddbeb', '18800888888', null, '2015-06-19 10:25:46', '2015-06-19 10:25:46');
INSERT INTO `tb_user` VALUES ('18', 'tidy', '123', '13600112243', null, '2015-07-30 17:26:25', '2015-07-30 17:26:25');
INSERT INTO `tb_user` VALUES ('22', 'tidy1', '202cb962ac59075b964b07152d234b70', '13600112244', null, '2015-07-30 17:48:33', '2015-07-30 17:48:33');
INSERT INTO `tb_user` VALUES ('23', 'niuniu', '202cb962ac59075b964b07152d234b70', '15866777744', '', '2015-08-01 11:48:42', '2015-08-01 11:48:42');
INSERT INTO `tb_user` VALUES ('32', 'niuniu2', '202cb962ac59075b964b07152d234b70', '14322334455', null, '2015-08-01 12:04:50', '2015-08-01 12:04:50');
INSERT INTO `tb_user` VALUES ('33', 'niuniu3', '202cb962ac59075b964b07152d234b70', '14322334456', null, '2015-08-01 12:08:26', '2015-08-01 12:08:26');
INSERT INTO `tb_user` VALUES ('34', 'niuniu4', '202cb962ac59075b964b07152d234b70', '15877680983', null, '2015-08-01 12:13:41', '2015-08-01 12:13:41');
INSERT INTO `tb_user` VALUES ('35', 'test01', '202cb962ac59075b964b07152d234b70', '15600876321', null, '2015-08-01 12:21:53', '2015-08-01 12:21:53');
INSERT INTO `tb_user` VALUES ('36', 'test02', '202cb962ac59075b964b07152d234b70', '1370348890', null, '2015-08-01 12:28:39', '2015-08-01 12:28:39');
