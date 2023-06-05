import {Alert, Button, message, Result, Select, Skeleton} from "antd";
import React, {useEffect, useState} from "react";
import {useApi} from "../hooks/useApi";
import TextArea from "antd/es/input/TextArea";

const HomePage = () => {
    const [botState, setBotState] = useState({code: 0, message: 'Получаем информацию о состоянии бота'})
    const [categories, setCategories] = useState([{}]);
    const [roles, setRoles] = useState([{}]);
    const [text, setText] = useState('')
    const [selectedCategory, setSelectedCategory] = useState(0);
    const [selectedRoles, setSelectedRoles] = useState([]);

    const [check, isLoading, error] = useApi(() => {}, '/check/telegram.announcements.send')

    const [loadBotState] = useApi((data) => {
        setBotState(data)
    }, "/telegram/state", "GET")

    const [loadCategories] = useApi((data) => {
        setCategories(data.response);
    }, "/categories", "GET")

    const [loadRoles] = useApi((data) => {
        setRoles(data.response);
    }, "/roles", "GET")

    const [startMailing] = useApi(() => {
        message.info('Рассылка отправлена').then()
    }, "/telegram/sendAnnouncements", "POST", {category_id: selectedRoles.length > 0 ? 0 : selectedCategory, roles: selectedRoles.map(r => r.id), text: text})

    useEffect(() => {
        if (sessionStorage.getItem('login')) {
            message.success('Успешный вход!').then(() => {
                sessionStorage.removeItem('login')
            })
        }

        check();
    }, [])

    useEffect(() => {
        if (!isLoading && !error) {
            loadBotState();
            loadCategories();
            loadRoles();

            const interval = setInterval(() => {
                loadBotState();
            }, 10000)

            return () => {
                clearInterval(interval);
            }
        }
    }, [isLoading])

    const selectCategory = (value) => {
        if (!value)
            setSelectedCategory(0);
        else setSelectedCategory(value);
    }

    const selectRoles = (values) => {
        setSelectedRoles(values.map(o => {return {id: o.value, name: o.label}}));
    }

    const handleStartMailing = () => {
        if (text === '') {
            message.warning('Сообщение для рассылки не может быть пустым').then()
            return;
        }
        startMailing().then(() => {
            loadBotState();
            setText('')
            setSelectedRoles([])
            setSelectedCategory(0)
        }).catch(e => {
            if (e.status === 423)
                message.warning('В данный момент бот занят рассылкой').then()
        })
    }

    if (isLoading)
        return <Skeleton/>
    if (error)
        return <Result
            status="403"
            title="403"
            subTitle="У вас нет прав для просмотра этой страницы"
        />
    return (
        <>
            <Alert message={botState.message}
                   type={botState.code === 0 ? 'info' : botState.code === 200 ? 'success' : 'warning'}/>
            <TextArea style={{marginTop: 20}}
                      autoSize={{minRows: 4, maxRows: 12}}
                      placeholder="Ваше объявление"
                      maxLength={1024}
                      value={text}
                      onChange={(e) => setText(e.target.value)}
                      showCount/>
            <Select
                style={{
                    width: '100%',
                    marginTop: 25
                }}
                placeholder='Выберите категорию для рассылки'
                allowClear
                disabled={selectedRoles.length > 0}
                onChange={selectCategory}
                value={categories.filter(c => c.id === selectedCategory).map(c => {return {label: c.name, value: c.id}})}
                options={categories.map(c => {
                    return {
                        label: c.name,
                        value: c.id
                    }
                })}/>
            <Select
                style={{
                    width: '100%',
                    marginTop: 10
                }}
                mode="multiple"
                placeholder="Выберите роли для рассылки"
                allowClear
                onChange={(value, option) => selectRoles(option)}
                value={selectedRoles.map(role => {
                    return {
                        label: role.name,
                        value: role.id
                    }
                })}
                options={roles.map(role => {
                    return {
                        label: role.name,
                        value: role.id
                    }
                })}
            />
            <Button
                style={{
                    width: '100%',
                    marginTop: 10
                }}
                onClick={handleStartMailing}
            >
                Сделать рассылку
            </Button>
        </>
    )
}
export default HomePage;