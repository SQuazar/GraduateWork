import React, {useState} from 'react';
import {Form, Input} from "antd";

const CreateUserForm = ({onUserChanged}) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleChanges = (values) => {
        onUserChanged(values)
    }

    return (
        <Form
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
                label="Имя пользователя"
                name="username"
                rules={[
                    {
                        required: true,
                        message: 'Введите имя пользователя!'
                    }
                ]}
            >
                <Input
                    placeholder="Имя пользователя"
                    maxLength="32"
                    showCount
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
            </Form.Item>
            <Form.Item
                label="Пароль"
                name="password"
                rules={[
                    {
                        required: true,
                        message: 'Введите пароль!'
                    }
                ]}
            >
                <Input.Password
                    placeholder="Пароль"
                    maxLength="24"
                    showCount
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
            </Form.Item>
        </Form>
    );
};

export default CreateUserForm;