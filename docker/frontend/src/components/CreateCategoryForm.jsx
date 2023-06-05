import React, {useState} from 'react';
import {Form, Input} from "antd";

const CreateCategoryForm = ({onCategoryChanged, form}) => {
    const [name, setName] = useState('')

    const handleChanges = (values) => {
        onCategoryChanged(values);
    }

    return (
        <Form
            form={form}
            onValuesChange={(changedValues, values) => handleChanges(values)}
            name="userForm"
            labelCol={{
                span: 8,
            }}
            wrapperCol={{
                span: 16,
            }}
            style={{
                maxWidth: 600,
            }}
        >
            <Form.Item
                label="Наименование"
                name="name"
                rules={[
                    {
                        required: true,
                        message: 'Введите имя категории!'
                    }
                ]}
            >
                <Input
                    placeholder="Наименование категории"
                    maxLength="32"
                    showCount
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
            </Form.Item>
        </Form>
    );
};

export default CreateCategoryForm;