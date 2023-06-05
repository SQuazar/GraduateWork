import axios from "axios";
import authHeader from "./atuh.header";
import {useApi} from "../hooks/useApi";

const API_URL = "/api"

class AuthService {
    login(username, password) {
        let data;
        return axios.post(`${API_URL}/auth/login`, null, {
            params: {
                username: username,
                password: password
            }
        }).then(response => {
            let user = {
                accessToken: response.data.accessToken,
                refreshToken: response.data.refreshToken,
            }
            data = user;
            localStorage.setItem('user', JSON.stringify(user));
            return axios.get(`${API_URL}/profile`, {headers: authHeader()});
        }).then(response => {
            if (response.data.code === 200)
                data = {...response.data.response, ...data};
            return axios.get(`${API_URL}/profile/authorities`, {headers: authHeader()})
        }).then(response => {
            if (response.data.code === 200)
                data = {...data, 'authorities': response.data.response}
            return data;
        }).then(result => {
            localStorage.setItem('user', JSON.stringify(result));
            return result;
        })
    }

    getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'))
    }

    updateAuthorities() {
        return axios.get(`${API_URL}/profile/authorities`, {headers: authHeader()}).then(response => {
            if (response.data.code === 200) {
                let user = this.getCurrentUser();
                user = {...user, 'authorities': response.data.response};
                localStorage.setItem('user', JSON.stringify(user));
                return user;
            }
        }).catch(e => {
            if (e.response.status === 401) {
                this.logout();
                window.location.reload();
            }
        })
    }

    logout() {
        localStorage.clear()
    }
}

export default new AuthService();