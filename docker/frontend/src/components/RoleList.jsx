import React, {useState} from 'react';
import {Button, Form, Input, InputNumber, message, Popconfirm, Table, Typography} from "antd";
import {Link} from "react-router-dom";
import {DeleteOutlined, EditOutlined} from "@ant-design/icons";
import Modal from "antd/es/modal/Modal";

const RoleList = ({data, onAdd, onDelete}) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [addGroupName, setAddGroupName] = useState('');

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id'
        },
        {
            title: 'Наименование',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Действия',
            dataIndex: 'id',
            key: 'operations',
            render: (id) => (
                <>
                    <Link to={`/roles/${id}`}><EditOutlined/></Link>
                    <a type="button" style={{marginLeft: 10}} onClick={() => onDelete(id)}><DeleteOutlined/></a>
                </>
            )
        }
    ]

    const showAddGroupModal = () => {
        setIsModalOpen(true);
    }

    const handleOkModal = () => {
        if (addGroupName === '') {
            message.warning('Имя группы не может быть пустым').then();
            return
        }
        onAdd(addGroupName);
        setAddGroupName('')
        setIsModalOpen(false);
    }

    const handleCancelModal = () => {
        setAddGroupName('')
        setIsModalOpen(false);
    }

    return (
        <>
            <Button
                style={{
                    float: "right",
                    marginBottom: 10
                }}
                onClick={showAddGroupModal}
            >
                Добавить группу
            </Button>
            <Table
                columns={columns}
                dataSource={data.map(r => {
                    return {...r, key: r.id}
                })}
            />
            <Modal title="Добавление группы"
                   open={isModalOpen}
                   onOk={handleOkModal}
                   onCancel={handleCancelModal}
                   okText="Сохранить"
                   cancelText="Отмена">
                <Input
                    placeholder="Наименование"
                    maxLength={32}
                    showCount
                    value={addGroupName}
                    onChange={(e) => setAddGroupName(e.target.value)}
                />
            </Modal>
        </>
    );
};

export default RoleList;