--
-- 创建数据库 `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(15) DEFAULT NULL,
  `nickname` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
