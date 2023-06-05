import React from 'react';
import {Button, Form, Input} from "antd";
import {MinusCircleOutlined, PlusOutlined} from "@ant-design/icons";

const AuthoritiesList = ({authorities, onChange}) => {
    return (
        <Form
            initialValues={{authorities: authorities}}
            onValuesChange={(changedValues, values) => onChange(values.authorities)}
        >
            <Form.List
                name="authorities"
            >
                {(fields, {add, remove}, {errors}) => (
                    <>
                        {fields.map((field, index) => (
                            <Form.Item
                                required={false}
                                key={field.key}
                            >
                                <Form.Item
                                    {...field}
                                    validateTrigger={['onChange', 'onBlur']}
                                    rules={[
                                        {
                                            required: true,
                                            whitespace: true,
                                            message: 'Введите полномочие или удалите это поле'
                                        }
                                    ]}
                                    noStyle
                                >
                                    <Input
                                        placeholder="Полномочие"
                                        style={{
                                            width: "auto",
                                            minWidth: "25%"
                                        }}
                                    />
                                </Form.Item>
                                <MinusCircleOutlined
                                    className="dynamic-delete-button"
                                    onClick={() => remove(field.name)}
                                />
                            </Form.Item>
                        ))}
                        <Form.Item>
                            <Button
                                type="dashed"
                                onClick={() => add()}
                                style={{
                                    width: "auto",
                                    minWidth: "25%"
                                }}
                                icon={<PlusOutlined/>}
                            >
                                Добавить полномочие
                            </Button>
                        </Form.Item>
                    </>
                )}
            </Form.List>
        </Form>
    );
};

export default AuthoritiesList;