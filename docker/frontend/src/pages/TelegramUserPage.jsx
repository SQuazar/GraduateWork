import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import {useApi} from "../hooks/useApi";
import {Card, Input, Skeleton} from "antd";
import RolesSelect from "../components/RolesSelect";
import CategorySelect from "../components/CategorySelect";

const tabList = [
    {
        key: 'info',
        tab: 'Информация о подписчике'
    },
    {
        key: 'roles',
        tab: 'Роли'
    },
    {
        key: 'categories',
        tab: 'Категории'
    }
]

const TelegramUserPage = () => {
    const {id} = useParams();
    const route = useNavigate();

    const [user, setUser] = useState({id: 0, name: '', signature: ''});
    const [signature, setSignature] = useState('')
    const [userRoles, setUserRoles] = useState([{id: 0, name: ''}])
    const [userCategories, setUserCategories] = useState([{id: 0, name: ''}])

    const [roles, setRoles] = useState([{id: 0, name: ''}])
    const [categories, setCategories] = useState([{id: 0, name: ''}])

    const [activeTabKey, setActiveTabKey] = useState('info')

    const [loadUser, isLoading] = useApi((data) => {
        setUser(data.response)
        setSignature(data.response.signature)
    }, `/telegram/users/${id}`, 'GET')

    const [loadUserRoles] = useApi((data) => {
        setUserRoles(data.response)
    }, `/telegram/users/${id}/roles`, 'GET')

    const [loadUserCategories] = useApi((data) => {
        setUserCategories(data.response)
    }, `/telegram/users/${id}/categories`, 'GET')

    const [loadRoles] = useApi((data) => {
        setRoles(data.response)
    }, '/roles', 'GET')

    const [loadCategories] = useApi((data) => {
        setCategories(data.response)
    }, '/categories', 'GET')

    const [saveUser] = useApi(() => {
        route(-1)
    }, `/telegram/users/${id}`, 'PATCH', {signature: signature, roles: userRoles.map(r => r.id), categories: userCategories.map(c => c.id)})

    useEffect(() => {
        loadUser();
        loadUserRoles();
        loadUserCategories();
        loadRoles();
        loadCategories();
    }, [])

    const tabContent = {
        info:
            <div>
                <p>ID {user.id}</p>
                <p>Подписан с: {user.subscription_date}</p>
                <Input
                    key="info"
                    style={{
                        marginTop: 10,
                        width: "auto"
                    }}
                    placeholder="Подпись"
                    value={signature}
                    onChange={(e) => setSignature(e.target.value)}
                    maxLength={16}
                    showCount
                />
            </div>,
        roles:
            <RolesSelect roles={roles} selectedRoles={userRoles} onChange={(values) => setUserRoles(values)}/>,
        categories:
            <CategorySelect categories={categories} selectedCategories={userCategories} onChange={(values) => setUserCategories(values)}/>
    }

    const onTabChange = (key) => {
        setActiveTabKey(key);
    }

    const save = () => {
        saveUser();
    }

    if (isLoading)
        return <Skeleton/>
    return (
        <Card
            title={`Подписчик ${user.id}`}
            tabList={tabList}
            activeTabKey={activeTabKey}
            onTabChange={onTabChange}
            extra={<div><a style={{marginRight: 20}} onClick={() => route(-1)}>Назад</a><a onClick={save}>Сохранить</a></div>}
        >
            {tabContent[activeTabKey]}
        </Card>
    );
};

export default TelegramUserPage;