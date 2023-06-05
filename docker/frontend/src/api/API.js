import axios from "axios";
import authHeader from "../services/atuh.header";
import AuthService from "../services/auth.service";
import {message} from "antd";

const API_URL = "/api";

const makeRequest = async (path, method, data) => {
    return await axios.request({
        url: `${API_URL}` + path,
        method: method,
        headers: authHeader(),
        data: data
    }).then(response => {
        return response.data;
    }).catch(err => {
        if (err.response.status === 401) {
            AuthService.logout();
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
    })
}

export default makeRequest;

