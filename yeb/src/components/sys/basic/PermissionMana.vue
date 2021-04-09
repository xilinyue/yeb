<template>
    <div>
        <!--添加角色-->
        <div class="addRole">
            <el-input  size="small" placeholder="请输入角色英文名称" v-model="role.name">
                <template slot="prepend">ROLE_</template>
            </el-input>
            <el-input  size="small" placeholder="请输入角色中文名称" v-model="role.nameZh">
            </el-input>
            <el-button type="primary" size="small" icon="el-icon-plus" @click="addRole">添加角色</el-button>
        </div>
        <!--角色权限展示-->
        <el-collapse v-model="activeName" accordion style="width: 800px;margin-top: 9px" @change="changeCollapse">
            <el-collapse-item :title="item.nameZh" :name="item.id" v-for="(item,index) in roles" :key="item.id">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span>可管理资源</span>
                        <el-button style="float: right; padding: 3px 0;color: red" icon="el-icon-delete" type="text"
                        @click="deleteRoleById(item)"></el-button>
                    </div>
                    <el-tree
                            :data="menuList"
                            :props="props"
                            node-key="id"
                            show-checkbox
                            ref="tree"
                            :default-checked-keys="mIds"
                    >
                    </el-tree>
                    <div class="bottom">
                        <el-button size="small" @click="cancelUpdate">取消修改</el-button>
                        <el-button size="small" type="primary" @click="updateMenusByRoleId(item.id,index)">更新修改</el-button>
                    </div>
                </el-card>
            </el-collapse-item>
        </el-collapse>
    </div>
</template>

<script>
    import SysFunction from "../../../api/SysFunction";
    export default {
        name: "PermissionMana",
        data() {
            return{
                activeName: -1,
                role: {
                    name: '',
                    nameZh: ''
                },
                roles: [],
                menuList: [],
                mIds: [],
                props: {
                    label: 'name',
                    children: 'children'
                },
            }
        },
        mounted() {
            this.initRoles();
        },
        methods: {
            deleteRoleById(item) {
                this.$confirm('此操作将永久删除【'+item.nameZh+'】角色, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    SysFunction.deleteRoleById(item.id).then(res => {
                        if (res.code === 0) {
                            this.$message.success(res.message);
                            this.initRoles();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            getMenuIdsByRoleId(rId) {
                SysFunction.getMenuIdsByRoleId(rId).then(res => {
                    if (res.code === 0){
                        this.mIds = res.data;
                    }
                });
            },
            getMenuList() {
                SysFunction.getMenuList().then(res => {
                    if (res.code === 0){
                        this.menuList = res.data;
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
            addRole() {
                if (this.role.name && this.role.nameZh) {
                    SysFunction.addRole(this.role).then(res => {
                        if (res.code === 0){
                            this.$message(res.message);
                            this.initRoles();
                        }
                    })
                }else {
                    this.$message.warning("请输入角色名称");
                }
            },
            changeCollapse(activeName) {
                if (activeName) {
                    this.getMenuList();
                    this.getMenuIdsByRoleId(activeName);
                }else{
                    this.mIds = [];
                    this.menuList = [];
                }
            },
            cancelUpdate() {
                this.activeName = -1;
            },
            updateMenusByRoleId(rId,index) {
                let trees = this.$refs["tree"];
                let mIds = trees[index].getCheckedKeys(true);
                let param = "?rId=" + rId + "&mIds=";
                mIds.forEach(item => {
                    param += item + "&mIds=";
                });
                SysFunction.updateMenusByRoleId(param).then(res => {
                    if (res.code === 0) {
                        this.$message.success(res.message);
                    }else {
                        this.$message.warning(res.message);
                    }
                    this.activeName = -1;
                })
            }
        }
    }
</script>

<style scoped>
    .addRole{
        display: flex;
        justify-content: left;
    }
    .addRole .el-input {
        width: 300px;
        margin-right: 9px;
    }
    .bottom{
        float: right;
        margin-top: 9px;
        margin-bottom: 9px;
    }

</style>