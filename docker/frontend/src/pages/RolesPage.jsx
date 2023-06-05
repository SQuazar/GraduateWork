import React, {useEffect, useState} from 'react';
import RoleList from "../components/RoleList";
import {useApi} from "../hooks/useApi";
import {useNavigate} from "react-router-dom";
import {Button, Result, Skeleton} from "antd";
import makeRequest from "../api/API";

const RolesPage = () => {
    const route = useNavigate();

    const [roles, setRoles] = useState([{id: 0, name: ''}]);

    useEffect(() => {
        loadRoles();
    }, [])

    const [loadRoles, isLoading, error] = useApi((data) => {
        if (data.code === 200)
            setRoles(data.response)
    }, '/roles', 'GET')

    const handleAddRole = (name) => {
        makeRequest(`/roles?name=${name}`, 'PUT').then(async () => {
            await loadRoles();
        })
    }

    const handleDeleteRole = (id) => {
        makeRequest(`/roles/${id}`, 'DELETE').then(async () => {
            await loadRoles();
        })
    }

    if (error)
        return <Result
            status="403"
            title="403"
            subTitle="У вас нет прав для просмотра этой страницы"
        />
    return (
        <>
            {isLoading ?
                <Skeleton/>
                : <RoleList data={roles} onAdd={handleAddRole} onDelete={handleDeleteRole}/>}
        </>
    );
};

export default RolesPage;