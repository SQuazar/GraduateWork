import {useState} from "react";
import axios from "axios";
import authHeader from "../services/atuh.header";
import {useNavigate} from "react-router-dom";
import AuthService from "../services/auth.service";
import {message} from "antd";

const API_URL = '/api'

export const useApi = (callback, path, method, data) => {
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState();
    const route = useNavigate();

    const callApi = async (...args) => {
        return await axios.request({
            url: `${API_URL}` + path,
            method: method,
            headers: authHeader(),
            data: data
        }).then(response => {
            callback(response.data);
        }).catch(err => {
            setError(err.response)
            if (err.response.status === 401) {
                AuthService.logout();
                route('/');
                window.location.reload();
            }
            if (err.response.status === 503) {
                message.error(`503 /api${path}: Сервис недоступен`)
            }
            if (err.response.status === 403) {
                message.error(`403 /api${path}: Ошибка доступа`)
            }
            if (err.response.status === 400)
                message.error(`400 /api${path}: Ошибка запроса (${err.response.data.message})`)
            throw err.response;
        }).finally(() => {
            setIsLoading(false)
        })
    }

    return [callApi, isLoading, error]
}