import {Button, Checkbox, Form, Input, Layout, message} from "antd";
import {LockOutlined, UserOutlined} from "@ant-design/icons";
import {Content} from "antd/es/layout/layout";
import {useNavigate} from "react-router-dom";
import AuthService from "../services/auth.service";

const LoginPage = () => {
    const [messageApi, contextHolder] = message.useMessage();
    const router = useNavigate();

    const doLogin = async (values) => {
        AuthService.login(values.username, values.password).then(() => {
            router('/');
            sessionStorage.setItem('login', 'true')
            window.location.reload();
        }).catch(err => {
            if (err.response.status === 403)
                messageApi.error('Неверный логин или пароль')
            if (err.response.status === 500)
                messageApi.error('Ошибка сервера. Попробуйте позже')
        });
    };
    return (
        <div>
            {contextHolder}
            <Layout style={{minHeight: '100vh'}}>
                <Content style={{display: "flex", justifyContent: "center", alignItems: "center"}}>
                    <Form
                        name="normal_login"
                        className="login-form"
                        onFinish={doLogin}
                    >
                        <Form.Item
                            name="username"
                            rules={[{required: true, message: 'Введите имя пользователя!'}]}
                        >
                            <Input prefix={<UserOutlined className="site-form-item-icon"/>}
                                   placeholder="Имя пользователя"/>
                        </Form.Item>
                        <Form.Item
                            name="password"
                            rules={[{required: true, message: 'Введите пароль!'}]}
                        >
                            <Input
                                prefix={<LockOutlined className="site-form-item-icon"/>}
                                type="password"
                                placeholder="Пароль"
                            />
                        </Form.Item>

                        <Form.Item>
                            <Button type="primary" htmlType="submit" className="login-form-button"
                                    style={{width: "100%"}}>
                                Войти
                            </Button>
                        </Form.Item>
                    </Form>
                </Content>
            </Layout>
        </div>
    );
}
export default LoginPage;