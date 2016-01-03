--delete existing permission
delete from role_permissions 
where permission_id in 
(select id from permissions where permission_key in ('ROLE_EDIT', 'PERMISSION_EDIT'))

INSERT INTO role (id, createdDateTime, deleted, deletedDateTime, lastUpdatedDateTime, version, role_description, role_name)
VALUES
	(3, '2016-01-03 02:11:54', 00000000, NULL, NULL, 0, 'System administrator, has standard admin privileges but can edit roles and permissions on top of this.', 'SysAdmin');

INSERT INTO role_permissions (role_id, permission_id)
VALUES
	(3, 1),
	(3, 2),
	(3, 3),
	(3, 4),
	(3, 5),
	(3, 6),
	(3, 7),
	(3, 8),
	(3, 9),
	(3, 10),
	(3, 11),
	(3, 12),
	(3, 13),
	(3, 14),
	(3, 15),
	(3, 16),
	(3, 17),
	(3, 22),
	(3, 23);

insert into user_roles values(1,3);