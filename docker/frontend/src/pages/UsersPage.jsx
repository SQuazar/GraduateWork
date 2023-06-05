import React, {useEffect, useState} from 'react';
import {useApi} from "../hooks/useApi";
import makeRequest from "../api/API";
import {message, Result, Skeleton} from "antd";
import UsersList from "../components/UsersList";

const UsersPage = () => {
    const [users, setUsers] = useState([]);

    const [loadUsers, isLoading, error] = useApi((data) => {
        let find = data.response;
        Promise.all(
            find.map(async user => {
                const roles = await makeRequest(`/users/${user.id}/roles`, 'GET').then(data => {
                    return data.response;
                })
                return {...user, roles: roles};
            })
        ).then((res) => {
            setUsers(res)
        })
    }, '/users', 'GET')

    useEffect(() => {
        loadUsers();
    }, [])

    const handleCreateUser = (user) => {
        makeRequest(`/users`, 'PUT', user).then((data) => {
            if (data.code === 200) {
                message.success('Пользователь добавлен в систему').then()
                loadUsers();
            }
        })
    }

    const handleDeleteUser = (id) => {
        makeRequest(`/users/${id}`, 'DELETE').then((data) => {
            if (data.code === 200) {
                message.success('Пользователь удалён из системы').then();
                loadUsers();
            }
        })
    }

    if (isLoading || users.length === 0)
        return <Skeleton/>
    if (error)
        return <Result
            status="403"
            title="403"
            subTitle="У вас нет прав для просмотра этой страницы"
        />
    return (
        <UsersList data={users} onCreate={handleCreateUser} onDelete={handleDeleteUser}/>
    );
};

export default UsersPage;