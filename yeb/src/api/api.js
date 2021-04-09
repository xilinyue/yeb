import axios from "axios";
import {Message} from "element-ui";

var instance = axios.create({
    // 本地地址
    // baseURL: 'http://localhost:9090',
    // withCredentials: false
});
// application/x-www-form-urlencoded; charset=UTF-8
// application/json;charset=utf-8
// 添加请求拦截器
instance.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8'
    config.headers['Access-Control-Allow-Origin'] = '*'
    let token = localStorage.getItem("token")
    if (token) {
        config.headers.Authorization = token
    }
    return config
}, error => {

    console.log(error) // for debug
    return Promise.reject(error);
});
instance.interceptors.response.use(response => {
    let data = response.data;
    if (response.status === 200) {
        if (data.code !== 0) {
            Message.error(data.message);
        }
        return data;
    } else {
        let message = data.message;
        Message.error(message !== undefined && message !== null ? message : "系统发生错误")
        return data;
    }
}, error => {
    let status = error.response.status
    switch (status) {
        case 401:
            if (localStorage.getItem('token')) {
                Message.warning('身份已过期')
            }
            localStorage.removeItem('token')
            router.push('/login')
            break;
        case  403:
            Message.warning('无访问权限')
            break;
    }
    return Promise.reject(error);
});
export default instance;