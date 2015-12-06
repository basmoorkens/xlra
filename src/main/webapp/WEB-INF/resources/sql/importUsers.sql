INSERT INTO `user` (`id`, `createdDateTime`, `deleted`, `deletedDateTime`, `lastUpdatedDateTime`, `version`, `email`, `firstName`, `name`, `password`, `validTo`, `verificationToken`, `userName`, `userStatus`)
VALUES
	(1,'2015-10-27 22:51:37',00000000,NULL,'2015-12-05 20:35:31',2,'bas_moorkens@hotmail.com','bas','moorkens','c05e198412a3608e7a626e473180472d170f0f9c95c158eb0a43583e286799f3',NULL,NULL,'bmoork','IN_OPERATION');
	
	INSERT INTO `user_roles` (`user_id`, `role_id`)
VALUES
	(1,1);