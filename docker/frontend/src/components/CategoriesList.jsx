import React, {useState} from 'react';
import {DeleteOutlined} from "@ant-design/icons";
import {Button, Form, message, Table} from "antd";
import Modal from "antd/es/modal/Modal";
import CreateCategoryForm from "./CreateCategoryForm";

const CategoriesList = ({data, onCreate, onDelete}) => {
    const [addCategory, setAddCategory] = useState({name: ''})
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [form] = Form.useForm();

    const columns = [
        {
            title: 'Наименование',
            dataIndex: 'name',
            key: 'name'
        },
        {
            title: 'Действия',
            dataIndex: 'id',
            key: 'operations',
            render: (id) => (
                <>
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
        if (addCategory.name === '') {
            message.warning('Имя категории не может быть пустым!!').then()
            setAddCategory({name: ''})
            form.resetFields();
            return
        }
        onCreate(addCategory);
        setAddCategory({name: ''})
        form.resetFields();
        setIsModalOpen(false)
    }

    const handleCancelModal = () => {
        setAddCategory({name: ''})
        setIsModalOpen(false)
        form.resetFields();
    }

    return (
        <div>
            <Button
                style={{
                    float: "right",
                    marginBottom: 10
                }}
                onClick={openModal}
            >
                Добавить категорию
            </Button>
            <Table
                columns={columns}
                dataSource={data.map(o => {return {...o, key: o.id}})}
            />
            <Modal
                title="Добавление категории"
                onOk={handleOkModal}
                onCancel={handleCancelModal}
                okText="Сохранить"
                cancelText="Отмена"
                open={isModalOpen}
            >
                <CreateCategoryForm onCategoryChanged={(values) => setAddCategory(values)} form={form}/>
            </Modal>
        </div>
    );
};

export default CategoriesList;