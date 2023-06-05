# Информационная система с новостным Telegram чат-ботом

## Установка

Для установки необходимо иметь установленный докер на операционной системе с ядром Linux.
Также необходимо иметь установленный Docker и docker-compose.
<br>
Файл `docker-compose.yml` и файлы сборки `Dockerfile` можно найти в директории `/docker` данного репозитория.

### Запуск через docker-compose

Для запуска достаточно выполнить следующую команду

```
sudo docker-compose up -d
```

После выполнения этой команды будут запущены все необходимые сервисы.
Также, для начала работы с системой необходимо проинициализировать начальные данные, выполнив следующий SQL запрос или импортировав данные из директории `/sql-data`.
```mysql
# Создание пользователей
INSERT INTO main.users (id, password_hash, username) VALUES (1, '$2a$05$86i/aBAeKuGsXc8XAXwmKu5UCWUxk.wLd95dltAfHLvtuGdDFAnMu', 'admin');
INSERT INTO main.users (id, password_hash, username) VALUES (2, '$2a$10$TVx.ETuM6sOgGyg8u7RPj.VbNaL9BpLfPV/c/KjTHiS/oT8T94.9i', 'user');

# Создание групп
INSERT INTO main.roles (id, name) VALUES (1, 'Администратор');
INSERT INTO main.roles (id, name) VALUES (2, 'Управляющий ботом');

# Выдача полномочий группам
INSERT INTO main.role_permissions (role_id, permission) VALUES (1, 'roles.get');
INSERT INTO main.role_permissions (role_id, permission) VALUES (1, 'roles.create');
INSERT INTO main.role_permissions (role_id, permission) VALUES (1, 'roles.update');
INSERT INTO main.role_permissions (role_id, permission) VALUES (1, 'roles.delete');
INSERT INTO main.role_permissions (role_id, permission) VALUES (1, 'telegramusers.get');
INSERT INTO main.role_permissions (role_id, permission) VALUES (1, 'telegramusers.update');
INSERT INTO main.role_permissions (role_id, permission) VALUES (1, 'resource.announcement.get');
INSERT INTO main.role_permissions (role_id, permission) VALUES (1, 'users.get');
INSERT INTO main.role_permissions (role_id, permission) VALUES (1, 'telegram.announcements.send');
INSERT INTO main.role_permissions (role_id, permission) VALUES (1, 'users.create');
INSERT INTO main.role_permissions (role_id, permission) VALUES (1, 'users.update');
INSERT INTO main.role_permissions (role_id, permission) VALUES (2, 'telegram.announcements.send');
INSERT INTO main.role_permissions (role_id, permission) VALUES (2, 'telegram.announcements.roles');
INSERT INTO main.role_permissions (role_id, permission) VALUES (1, 'categories.create');
INSERT INTO main.role_permissions (role_id, permission) VALUES (1, 'categories.delete');
INSERT INTO main.role_permissions (role_id, permission) VALUES (1, 'users.delete');

# Выдача групп пользователям
INSERT INTO main.user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO main.user_roles (user_id, role_id) VALUES (2, 2);
```
Посе этого будут созданы необходимые пользователи для работы с приложением.

### Информация о созданных пользователях

| Пользователь | Пароль | Группа            |
|--------------|--------|-------------------|
| admin        | admin  | Администратор     |
| user         | user   | Управляющий ботом |

## Дополнительно

Информацию о существующих в системе полномочиях можно найти в директории `/info` данного репозитория.