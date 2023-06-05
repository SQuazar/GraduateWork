import React, {useState} from 'react';
import {Link} from "react-router-dom";
import {Table, Tag} from "antd";
import {useApi} from "../hooks/useApi";

const columns = [
    {
        title: 'ID',
        dataIndex: 'info',
        key: 'id',
        render: (info) => <Link to={`${info.id}`}>{info.id} {info.signature && `(${info.signature})`}</Link>
    },
    {
        title: 'Роли',
        dataIndex: 'roles',
        key: 'roles',
        render: (_, {roles}) => (
            <>
                {roles.map((role) => {
                    return <Tag key={role.id}>{role.name}</Tag>
                })}
            </>
        )
    },
    {
        title: 'Категории',
        dataIndex: 'categories',
        key: 'categories',
        render: (_, {categories}) => (
            <>
                {categories.map((category) => {
                    return <Tag key={category.id}>{category.name.toUpperCase()}</Tag>
                })}
            </>
        )
    }
]
const TelegramUsersList = ({data}) => {
    return (
        <Table columns={columns} dataSource={data.map(user => {
            return {...user, key: user.id, info: {id: user.id, signature: user.signature}}
        })}/>
    );
};

export default TelegramUsersList;