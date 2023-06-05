import React, {useEffect, useState} from 'react';
import {useApi} from "../hooks/useApi";
import {Result, Skeleton} from "antd";
import TelegramUsersList from "../components/TelegramUsersList";

const TelegramUsersPage = () => {
    const [users, setUsers] = useState([{id: 0, name: ''}]);

    const [loadUsers, isUsersLoading, usersLoadError] = useApi((data) => {
        setUsers(data.response);
    }, "/telegram/users?roles=true&categories=true", "GET")

    useEffect(() => {
        loadUsers();
    }, [])

    if (usersLoadError)
        return <Result
            status="403"
            title="403"
            subTitle="У вас нет прав для просмотра этой страницы"
        />
    return (
        <>
            {isUsersLoading ?
                <Skeleton/>
                :
                <>
                    <TelegramUsersList data={users}/>
                </>
            }
        </>
    );
};

export default TelegramUsersPage;