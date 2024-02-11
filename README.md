# Информационная система с новостным Telegram чат-ботом

## Установка

Для установки необходимо иметь установленный Docker и docker-compose на операционной системе с ядром Linux.
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
INSERT INTO main.roles (id, name) VALUES (1, 'Admin');
INSERT INTO main.roles (id, name) VALUES (2, 'Manager');

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

## Скрины
![Рассылка](https://github.com/SQuazar/GraduateWork/assets/67743907/464752ff-33ad-4698-b351-19c8890bdfba)
![Категории](https://github.com/SQuazar/GraduateWork/assets/67743907/d9c4a77a-3d27-4af9-ac2d-c3fb68664461)
![Пользователи](https://github.com/SQuazar/GraduateWork/assets/67743907/d97de40f-2462-4112-a221-3a925fd3ec71)
![Пользователи-Редактирование-Общее](https://github.com/SQuazar/GraduateWork/assets/67743907/be2787e5-28af-4fb4-b55d-6d2b94b5cae9)
![Пользователи-Редактирование-Роли](https://github.com/SQuazar/GraduateWork/assets/67743907/af9c692c-7e70-40dd-9cd8-ea88cd2798a6)
![Пользователи-Редактирование-Полномочия](https://github.com/SQuazar/GraduateWork/assets/67743907/99032e61-58fc-4421-8748-3aeab2f838fd)
![Подписчики](https://github.com/SQuazar/GraduateWork/assets/67743907/7c7b3b74-2605-4834-a456-b30ea24ac87b)
![Подписчики-Редактирование-Общее](https://github.com/SQuazar/GraduateWork/assets/67743907/576b17e8-746a-4437-af81-30f1ceace7b6)
![Подписчики-Редактирование-Роли](https://github.com/SQuazar/GraduateWork/assets/67743907/8dfc8eec-a234-434e-be30-a3f9bda64a43)
![Подписчики-Редактирование-Категории](https://github.com/SQuazar/GraduateWork/assets/67743907/82b17d50-0d76-4c30-9322-5ab0a5f9cba7)
![Группы](https://github.com/SQuazar/GraduateWork/assets/67743907/97222bd6-a9d8-46b4-8b8a-de8fed8a31e5)
![Группы-Редактирование-Общее](https://github.com/SQuazar/GraduateWork/assets/67743907/97b11c93-34af-44f4-a47f-b995129a52a2)
![Группы-Редактирование-Полномочия](https://github.com/SQuazar/GraduateWork/assets/67743907/f5f62499-5a6a-40e7-9128-a182eed071c3)
![Архив](https://github.com/SQuazar/GraduateWork/assets/67743907/04a5687a-0061-407c-893b-feda8b2ea911)
![Бот-Моя-Подписка](https://github.com/SQuazar/GraduateWork/assets/67743907/94cb7c64-eb3c-489a-9b91-f9e78f947a2c)
![Бот-Категории](https://github.com/SQuazar/GraduateWork/assets/67743907/db329660-4c4c-4bdf-93bf-a9d49fcebef9)
![Бот-Категории-Подписка](https://github.com/SQuazar/GraduateWork/assets/67743907/f2879254-fae4-4832-a8a4-3f41ebacf296)

![Бот-Категории-Отписка](https://github.com/SQuazar/GraduateWork/assets/67743907/0127a26d-3db8-40ab-a3b6-1aa4c72e0004)
