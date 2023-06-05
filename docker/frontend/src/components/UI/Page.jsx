import React, {useEffect, useState} from "react";
import {Button, Layout, Menu, theme} from "antd";
import Sider from "antd/es/layout/Sider";
import {
    MenuFoldOutlined,
    MenuUnfoldOutlined,
    UserOutlined,
    LogoutOutlined,
    MailOutlined,
    GroupOutlined,
    UnorderedListOutlined,
    FileSearchOutlined
} from "@ant-design/icons";
import {Content, Header} from "antd/es/layout/layout";
import {Link, useNavigate} from "react-router-dom";
import AuthService from "../../services/auth.service";
import EventBus from "../../common/EventBus";

const Page = ({children}) => {
    const [collapsed, setCollapsed] = useState(false);
    const {
        token: {colorBgContainer}
    } = theme.useToken();
    const [menuItems, setMenuItems] = useState([]);
    const [currentMenuItem, setCurrentMenuItem] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        setCurrentMenuItem(window.location.pathname)

        makeMenuItems();

        EventBus.on('authoritiesUpdate', () => {
            makeMenuItems();
        })

        return () => {
            setMenuItems([]);
            EventBus.remove('authoritiesUpdate')
        }
    }, [])

    const makeMenuItems = () => {
        const user = AuthService.getCurrentUser();
        let items = [];
        if (user.authorities.includes('telegram.announcements.send')) {
            items = [...items,
                {
                    key: '/',
                    icon: <MailOutlined/>,
                    label: 'Рассылка'
                },
                {
                    key: '/categories',
                    icon: <UnorderedListOutlined/>,
                    label: 'Категории'
                }]
        }
        if (user.authorities.includes('users.get'))
            items = [...items,
                {
                    key: '/users',
                    icon: <UserOutlined/>,
                    label: 'Пользователи'
                }]
        if (user.authorities.includes('telegramusers.get'))
            items = [...items,
                {
                    key: '/telegramusers',
                    icon: <UserOutlined/>,
                    label: 'Подписчики'
                }]
        if (user.authorities.includes('roles.get'))
            items = [...items,
                {
                    key: '/roles',
                    icon: <GroupOutlined/>,
                    label: 'Группы',
                }]
        if (user.authorities.includes('resource.announcement.get'))
            items = [...items,
                {
                    key: '/announcements',
                    icon: <FileSearchOutlined/>,
                    label: 'Архив новостей'
                }]

        setMenuItems(items);
    }

    const logout = () => {
        AuthService.logout();
        window.location.reload();
    }

    return (
        <Layout style={{minHeight: '100vh'}}>
            <Sider trigger={null} collapsible collapsed={collapsed}>
                <div className="demo-logo-vertical">
                </div>
                <Menu
                    theme="dark"
                    mode="inline"
                    onClick={(e) => {
                        setCurrentMenuItem(e.key);
                        navigate(e.key);
                    }}
                    selectedKeys={[currentMenuItem]}
                    items={menuItems}
                />
                <Link to="/logout" className="logout-button"
                      onClick={logout}>
                    <LogoutOutlined style={{marginRight: "5px"}}/>
                    Выход
                </Link>
            </Sider>
            <Layout>
                <Header
                    style={{
                        padding: 0,
                        background: colorBgContainer
                    }}
                >
                    <Button
                        type="text"
                        icon={collapsed ? <MenuUnfoldOutlined/> : <MenuFoldOutlined/>}
                        onClick={() => setCollapsed(!collapsed)}
                        style={{
                            fontSize: '16px',
                            width: 64,
                            height: 64,
                            float: "left"
                        }}
                    />
                    <h3
                        style={{
                            float: "left",
                            marginLeft: 5
                        }}
                    >
                        {menuItems.filter(item => item.key === currentMenuItem)[0]?.label}
                    </h3>
                </Header>
                <Content
                    style={{
                        margin: '24px 16px',
                        padding: 24,
                        minHeight: 280,
                        background: colorBgContainer,
                    }}
                >
                    {children}
                </Content>
            </Layout>
        </Layout>
    )
}
export default Page;