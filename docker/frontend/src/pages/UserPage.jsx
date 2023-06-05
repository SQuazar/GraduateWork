import React, {useEffect, useState} from 'react';
import {Card, Input, message, Result, Skeleton} from "antd";
import {useNavigate, useParams} from "react-router-dom";
import makeRequest from "../api/API";
import {useApi} from "../hooks/useApi";
import RolesSelect from "../components/RolesSelect";
import AuthoritiesList from "../components/AuthoritiesList";

const tabList = [
    {
        key: 'info',
        tab: 'Информация о пользователе'
    },
    {
        key: 'roles',
        tab: 'Роли пользователя'
    },
    {
        key: 'authorities',
        tab: 'Полномочия пользователя'
    }
]
const UserPage = () => {
    const route = useNavigate();
    const {id} = useParams();

    const [activeTabKey, setActiveTabKey] = useState('info');

    const [userName, setUserName] = useState('');
    const [userPassword, setUserPassword] = useState('');

    const [user, setUser] = useState({id: 0, username: ''});
    const [userRoles, setUserRoles] = useState([]);
    const [userAuthorities, setUserAuthorities] = useState([]);

    const [roles, setRoles] = useState([]);

    const [loadUser, isLoading, error] = useApi((data) => {
        setUser(data.response);
        setUserName(data.response.username)
    }, `/users/${id}`, 'GET')

    const [loadUserRoles] = useApi((data) => {
        setUserRoles(data.response);
    }, `/users/${id}/roles`, 'GET')

    const [loadUserAuthorities] = useApi((data) => {
        setUserAuthorities(data.response)
    }, `/users/${id}/permissions`, 'GET')

    const [loadRoles] = useApi((data) => {
        setRoles(data.response)
    }, '/roles', 'GET')

    useEffect(() => {
        loadUser();
        loadUserRoles();
        loadUserAuthorities();
        loadRoles();
    }, [])

    const tabContent = {
        info:
            <div>
                <p>ID: {user.id}</p>
                <Input
                    style={{
                        marginTop: 10,
                        maxWidth: "25%",
                        display: "flex"
                    }}
                    placeholder="Имя пользователя"
                    value={userName}
                    onChange={(e) => setUserName(e.target.value)}
                    maxLength={32}
                    showCount
                />
                <Input.Password
                    style={{
                        marginTop: 10,
                        maxWidth: "25%",
                        display: "flex"
                    }}
                    placeholder="Пароль"
                    value={userPassword}
                    onChange={(e) => setUserPassword(e.target.value)}
                    maxLength={24}
                    showCount
                />
            </div>,
        roles:
            <RolesSelect roles={roles} selectedRoles={userRoles} onChange={(values) => setUserRoles(values)}/>,
        authorities:
            <AuthoritiesList authorities={userAuthorities} onChange={(values) => setUserAuthorities(values)}/>,
    }

    const handleTabChange = (key) => {
        setActiveTabKey(key)
    }

    const save = () => {
        makeRequest(`/users/${id}`, 'POST', {username: userName, password: userPassword, roles: userRoles.map(r => r.id), authorities: userAuthorities}).then((data) => {
            route(-1);
            message.success('Пользователь сохранён').then();
        })
    }

    if (isLoading)
        return <Skeleton/>
    if (error)
        return <Result
            status="403"
            title="403"
            subTitle="У вас нет прав для просмотра этой страницы"
        />
    return (
        <Card
            title={`Пользователь`}
            tabList={tabList}
            activeTabKey={activeTabKey}
            onTabChange={handleTabChange}
            extra={<div><a style={{marginRight: 20}} onClick={() => route(-1)}>Назад</a><a onClick={save}>Сохранить</a></div>}
        >
            {tabContent[activeTabKey]}
        </Card>
    );
};

export default UserPage;