import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import {useApi} from "../hooks/useApi";
import {Button, Card, Form, Input, Skeleton} from "antd";
import {MinusCircleOutlined, PlusOutlined} from "@ant-design/icons";
import AuthoritiesList from "../components/AuthoritiesList";

const tabList = [
    {
        key: 'info',
        tab: 'Информация о группе'
    },
    {
        key: 'authorities',
        tab: 'Полномочия группы'
    }
]

const RolePage = () => {
    const {id} = useParams();
    const [activeTabKey, setActiveTabKey] = useState('info')

    const route = useNavigate();

    const [role, setRole] = useState({id: 0, name: ''});
    const [authorities, setAuthorities] = useState([])

    const [roleName, setRoleName] = useState('');

    const [loadRole, isLoading] = useApi((data) => {
        setRole(data.response)
        setRoleName(data.response.name)
    }, `/roles/${id}`, 'GET')

    const [loadAuthorities] = useApi((data) => {
        setAuthorities(data.response)
    }, `/roles/${id}/authorities`, 'GET')

    const [saveRole] = useApi(() => {
        route(-1);
    }, `/roles/${id}`, 'POST', {name: roleName, authorities: authorities})

    useEffect(() => {
        loadRole();
        loadAuthorities();
    }, [])

    const tabContent = {
        info:
            <div>
                <p>ID: {role.id}</p>
                <Input
                    style={{
                        marginTop: 10,
                        width: "auto",
                    }}
                    placeholder="Наименование"
                    value={roleName}
                    onChange={(e) => setRoleName(e.target.value)}
                    maxLength={32}
                    showCount
                />
            </div>,
        authorities:
            <div>
                <AuthoritiesList authorities={authorities} onChange={setAuthorities}/>
            </div>
    }

    const onTabChange = (key) => {
        setActiveTabKey(key);
    }

    const save = () => {
        saveRole();
    }

    if (isLoading)
        return <Skeleton/>
    return (
        <Card
            title={`Группа ${role.name}`}
            tabList={tabList}
            activeTabKey={activeTabKey}
            onTabChange={onTabChange}
            extra={<div><a style={{marginRight: 20}} onClick={() => route(-1)}>Назад</a><a onClick={save}>Сохранить</a></div>}
        >
            {tabContent[activeTabKey]}
        </Card>
    );
};

export default RolePage;