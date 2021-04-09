import request from "./api";

let AdminFunction = {
    // 登录
    login(params) {
        return request.post("/login",params);
    },
    logout() {
        return request.post("/logout");
    },
    getOwn() {
        return request.get("/getOwn");
    }
}

export default AdminFunction