INSERT INTO `role` (`id`, `createdDateTime`, `deleted`, `deletedDateTime`, `lastUpdatedDateTime`, `version`, `role_description`, `role_name`)
VALUES
	(1,'2015-10-27 22:47:38',00000000,NULL,'2015-10-28 19:43:55',3,'Admin role, has all permissions','Admin'),
	(2,'2015-10-27 23:47:30',00000000,NULL,'2015-10-28 19:42:17',2,'The role for user','User');

	INSERT INTO `role_permissions` (`role_id`, `permission_id`)
VALUES
	(1,1),
	(1,2),
	(1,3),
	(1,4),
	(1,5),
	(1,6),
	(1,7),
	(1,8),
	(1,9),
	(1,10),
	(1,11),
	(1,12),
	(1,13),
	(1,14),
	(1,15),
	(1,16),
	(1,17),
	(1,22),
	(1,23),
	(2,13),
	(2,14),
	(2,15),
	(2,22);
	
	