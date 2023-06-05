import React from 'react';
import {Link} from "react-router-dom";
import {Table, Tag} from "antd";

const columns = [
    Table.EXPAND_COLUMN,
    {
        title: 'Отправитель',
        dataIndex: 'sender',
        key: 'sender',
        render: (sender) => (
            <Link to={`/users/${sender.id}`}>{sender.username}</Link>
        )
    },
    {
        title: 'Время',
        dataIndex: 'timestamp',
        key: 'timestamp'
    },
    {
        title: 'Категории',
        dataIndex: 'categories',
        key: 'categories',
        render: (_, {categories}) => (
            <>
                {categories && categories.length > 0 ?
                    categories?.map(category => {
                        return <Tag key={category}>{category}</Tag>
                    })
                    : <Tag key="all" color="green">Все</Tag>}
            </>
        )
    },
    {
        title: 'Роли',
        dataIndex: 'roles',
        key: 'roles',
        render: (_, {roles}) => (
            <>
                {roles && roles.length > 0 ?
                    roles?.map(role => {
                        return <Tag key={role}>{role}</Tag>
                    })
                : <Tag key="all" color="green">Все</Tag>}
            </>
        )
    }
]

const AnnouncementsList = ({data}) => {
    return (
        <Table
            dataSource={data.map(o => {
                return {...o, key: o.id}
            })}
            columns={columns}
            expandable={{
                expandedRowRender: (record) => (
                    <p
                        style={{
                            margin: 0,
                            maxWidth: 1200,
                            whiteSpace: "pre-line",
                        }}
                    >
                        {record.text}
                    </p>
                )
            }}
        />
    );
};

export default AnnouncementsList;