import axios from "axios";
import authHeader from "./atuh.header";

const API_URL = '/api/check';

class UserService {
    hasAccess(authority) {
        return axios.get(`${API_URL}/${authority}`, {headers: authHeader()});
    }
}

export default new UserService();