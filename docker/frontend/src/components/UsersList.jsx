import React, {useState} from 'react';
import {Button, Input, message, Table, Tag} from "antd";
import CreateUserForm from "./CreateUserForm";
import Modal from "antd/es/modal/Modal";
import {Link} from "react-router-dom";
import {DeleteOutlined, EditOutlined} from "@ant-design/icons";

const UsersList = ({data, onCreate, onDelete}) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [addUser, setAddUser] = useState({username: '', password: ''})

    let columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id'
        },
        {
            title: 'Имя пользователя',
            dataIndex: 'username',
            key: 'username'
        },
        {
            title: 'Роли',
            dataIndex: 'roles',
            key: 'roles',
            render: (roles) => (
                <>
                    {roles?.map(role => {
                        return <Tag key={role.id}>{role.name}</Tag>
                    })}
                </>
            )
        },
        {
            title: 'Действия',
            dataIndex: 'id',
            key: 'operations',
            render: (id) => (
                <>
                    <Link to={`/users/${id}`}><EditOutlined/></Link>
                    <a type="button" style={{marginLeft: 10}} onClick={() => handleDelete(id)}><DeleteOutlined/></a>
                </>
            )
        }
    ]

    const handleDelete = (id) => {
        onDelete(id);
    }

    const openModal = () => {
        setIsModalOpen(true);
    }

    const handleOkModal = () => {
        if (addUser.username === '' || addUser.password === '') {
            message.warning('Имя пользователя или пароль не должны быть пустыми!').then()
            setAddUser({username: '', password: ''})
            return
        }
        onCreate(addUser);
        setAddUser({username: '', password: ''})
        setIsModalOpen(false)
    }

    const handleCancelModal = () => {
        setIsModalOpen(false)
    }

    return (
        <>
            <Button
                style={{
                    float: "right",
                    marginBottom: 10
                }}
                onClick={openModal}
            >
                Добавить пользователя
            </Button>
            <Table
                columns={columns}
                dataSource={data.map(o => {
                    return {...o, key: o.id}
                })}
            />
            <Modal title="Добавление группы"
                   open={isModalOpen}
                   onOk={handleOkModal}
                   onCancel={handleCancelModal}
                   okText="Сохранить"
                   cancelText="Отмена">
                <CreateUserForm onUserChanged={(values) => setAddUser(values)}/>

            </Modal>
        </>
    );
};

export default UsersList;