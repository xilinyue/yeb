<template>
    <div>
        <div class="search">
            <el-input style="width: 700px;margin-right: 9px" size="small" placeholder="请输入操作员名称搜索..." v-model="keyWords"></el-input>
            <el-button size="small" type="primary" icon="el-icon-search" @click="listAdmins">搜索</el-button>
        </div>
        <div class="admin-container">
            <el-card class="box-card" v-for="(admin,index) in admins" :key="index">
                <div slot="header" class="clearfix">
                    <span>{{admin.name}}</span>
                    <el-button @click="deleteAdmin(admin)" style="float: right; padding: 3px 0;color: red" type="text" icon="el-icon-delete"></el-button>
                </div>
                <div class="user_info">
                    <div>
                        <img :src="admin.userFace" />
                    </div>
                    <div>
                        用户名：{{admin.username}}
                    </div>
                    <div>
                        手机号码：{{admin.phone}}
                    </div>
                    <div>
                        移动电话：{{admin.telephone}}
                    </div>
                    <div>
                        地址：{{admin.address}}
                    </div>
                    <div>
                        是否可用：<el-switch v-model="admin.enabled" disabled></el-switch>
                    </div>
                    <div>
                        备注：{{admin.remark}}
                    </div>
                    <div>
                        角色：
                        <el-tag size="mini" type="success"  style="margin-right: 4px"
                                v-for="role in admin.roles" :key="role.id">{{role.nameZh}}</el-tag>
                        <el-popover
                                placement="right"
                                title="修改角色"
                                width="300"
                                @show="show(admin)"
                                trigger="click">
                            <i class="el-icon-more" style="color: #ccc;cursor: pointer" slot="reference"></i>
                            <el-select v-model="rIds" multiple size="small">
                                <el-option v-for="role in roles" :key="role.id" :value="role.id" :label="role.nameZh"></el-option>
                            </el-select>
                            <el-button size="mini" type="primary" @click="updateRolesByAdminId(admin)"
                                style="display: block;margin-top: 5px;">更新修改</el-button>
                        </el-popover>
                    </div>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script>
    import SysFunction from "../../api/SysFunction";

    export default {
        name: "SysAdmin",
        data() {
            return {
                keyWords: "",
                admins: [],
                rIds: [],
                roles: []
            }
        },
        mounted() {
            this.listAdmins();
            this.initRoles();
        },
        methods: {
            updateRolesByAdminId(admin) {
                let rIds = [];
                admin.roles.forEach(role => {
                    rIds.push(role.id);
                });
                let flag = false;
                if (this.rIds.length !== rIds.length){
                    flag = true;
                }else{
                    for(let i = 0; i < rIds.length;i++){
                        for(let j = 0; j < this.rIds.length;j++){
                            if (this.rIds[j] === rIds[i]) {
                                rIds.splice(i,1);
                                i--;
                                break;
                            }
                        }
                    }
                    if (rIds.length !== 0){
                        flag = true;
                    }
                }
                if (flag) {
                    let param = "?adminId=" + admin.id + "&rIds=";
                    this.rIds.forEach(rId => {
                        param += rId + "&rIds=";
                    });
                    SysFunction.updateRolesByAdminId(param).then(res => {
                        if (res.code === 0){
                            this.$message.success(res.message);
                            this.listAdmins();
                        }
                    });
                }else{
                    this.$message.warning("未进行任何修改");
                }
            },
            listAdmins() {
                SysFunction.listAdmins(this.keyWords).then(res => {
                    if (res.code === 0) {
                        this.admins = res.data;
                    }
                })
            },
            initRoles() {
                SysFunction.getRoleList().then(res => {
                    if (res.code === 0){
                        this.roles = res.data;
                    }
                })
            },
            show(admin) {
                this.rIds = [];
                let roles = admin.roles;
                roles.forEach(role => {
                    this.rIds.push(role.id);
                })
            },
            deleteAdmin(admin) {
                this.$confirm('此操作将永久删除【'+admin.name+'】操作员, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    SysFunction.deleteAdminById(admin.id).then(res => {
                        if (res.code === 0) {
                            this.$message.success(res.message);
                            this.listAdmins();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            }
        }
    }
</script>

<style scoped>
    .search {
        width: 100%;
        margin-top: 9px;
        margin-bottom: 9px;
        display: flex;
        justify-content: center;
    }
    .admin-container {
        display: flex;
        justify-content: space-between;
        flex-wrap: wrap;
    }
    .box-card{
        width: 400px;
        margin-bottom: 9px;
    }
    .box-card .user_info{
        font-size: 14px;
        color: cornflowerblue;
    }
    .box-card .user_info div:nth-child(1){
        text-align: center;
    }
    .box-card .user_info img {
        width: 72px;
        height: 72px;
        border-radius: 50%;
    }
</style>