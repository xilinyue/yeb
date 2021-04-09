import request from "./api";

let SysFunction = {
    // 职位
    getPositions() {
        return request.get("/system/basic/position/listPositions");
    },
    addPosition(param) {
        return request.post("/system/basic/position/addPosition",param);
    },
    deletePosition(param) {
        return request.delete("/system/basic/position/deletePositionById/" + param);
    },
    deletePositions(param) {
        return request.delete("/system/basic/position/deletePositionByIds/" + param);
    },
    updatePosition(param){
        return request.put("/system/basic/position/updatePosition", param);
    },
    // 职称
    getJobLevels() {
        return request.get("/system/basic/jobLevel/listJoblevels");
    },
    addJobLevel(param) {
        return request.post("/system/basic/jobLevel/addJoblevel",param);
    },
    deleteJobLevel(param) {
        return request.delete("/system/basic/jobLevel/deleteJoblevelById/" + param);
    },
    deleteJobLevels(param) {
        return request.delete("/system/basic/jobLevel/deleteJoblevelByIds/" + param);
    },
    updateJobLevel(param){
        return request.put("/system/basic/jobLevel/updateJoblevel", param);
    },
    // 权限组
    // 角色列表获取
    getRoleList() {
        return request.get("/system/basic/permission/roleList");
    },
    // 查询所有菜单列表
    getMenuList() {
        return request.get("/system/basic/permission/getAllMenuList");
    },
    // 根据角色id查询查询菜单列表ids
    getMenuIdsByRoleId(rId) {
        return request.get("/system/basic/permission/getMenuIdsByRoleId/" + rId);
    },
    // 添加角色
    addRole(data) {
        return request.post("/system/basic/permission/addRole",data);
    },
    // 根据id删除角色
    deleteRoleById(rId) {
        return request.delete("/system/basic/permission/deleteRole/" + rId);
    },
    // 根据角色id更新角色menu
    updateMenusByRoleId(param) {
        return request.put("/system/basic/permission/updateMenusByRoleId"+param);
    },
    // 部门
    // 查询部门列表
    departmentList() {
        return request.get("/system/basic/department/list");
    },
    // 添加部门
    addDepartment(data) {
        return request.post("/system/basic/department/add",data);
    },
    // 删除部门
    deleteDepartmentById(dId) {
        return request.delete("/system/basic/department/delete/" + dId);
    },

    //操作员管理
    // 获取所有操作员
    listAdmins(keyWords) {
        return request.get("/system/admin/list?keyWords" + keyWords);
    },
    // 根据id更新管理员角色
    updateRolesByAdminId(param) {
        return request.put("/system/admin/updateRolesByAdminId" + param);
    },
    // 根据id删除管理员
    deleteAdminById(id) {
        return request.delete("/system/admin/delete/" + id);
    }
}

export default SysFunction