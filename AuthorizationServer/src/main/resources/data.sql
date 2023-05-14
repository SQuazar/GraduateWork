INSERT INTO `roles` (`id`, `name`) VALUES (1,'Администратор');
INSERT INTO `role_permissions` (`role_id`, `permission`) VALUES (1, 'sa');
INSERT INTO `users` (`id`, `username`, `password_hash`) VALUES (1, 'admin', '$2a$05$86i/aBAeKuGsXc8XAXwmKu5UCWUxk.wLd95dltAfHLvtuGdDFAnMu');
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `user_permissions` (`user_id`, `permission`) VALUES (1, 'sa');