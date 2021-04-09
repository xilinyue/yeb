<template>
    <div style="width: 800px">
        <el-input
                placeholder="输入部门名称进行筛选..."
                v-model="filterText">
        </el-input>

        <el-tree
                class="filter-tree"
                :data="depList"
                :filter-node-method="filterNode"
                node-key="id"
                :expand-on-click-node="false"
                ref="tree">
            <span slot-scope="{ node, data }" class="treeNode">
                <span>{{ data.name }}</span>
                <span>
                  <el-button
                          class="function"
                          type="primary"
                          size="mini"
                          @click="() => showAdd(data)">
                    添加部门
                  </el-button>
                  <el-button
                          class="function"
                          type="danger"
                          size="mini"
                          @click="() => deleteDep(node, data)">
                    删除部门
                  </el-button>
                </span>
            </span>
        </el-tree>
        <el-dialog
                title="添加部门"
                :visible.sync="dialogVisible"
                :before-close="showClose"
                width="30%">
            <table>
                <tr>
                    <td><el-tag size="mini">父级部门</el-tag></td>
                    <td><el-input size="mini" disabled v-model="parentName"/></td>
                </tr>
                <tr>
                    <td><el-tag size="mini">部门名称</el-tag></td>
                    <td><el-input size="mini" placeholder="请输入部门名称..." v-model="addForm.name"/></td>
                </tr>
            </table>
            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelAdd" size="mini">取 消</el-button>
                <el-button type="primary" @click="submitAdd" size="mini">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import SysFunction from "../../../api/SysFunction";

    export default {
        name: "DepartmentMana",
        data() {
            return{
                depList: [],
                filterText: "",
                // defaultProps: {
                //     children: 'children',
                //     label: 'name'
                // },
                dialogVisible: false,
                parentName: "",
                addForm: {
                    name: "",
                    parentId: -1
                }
            }
        },
        watch: {
            filterText(val) {
                this.$refs.tree.filter(val);
            }
        },
        mounted() {
            this.initDepartment();
        },
        methods: {
            filterNode(value, data) {
                if (!value) return true;
                return data.name.indexOf(value) !== -1;
            },
            initDepartment() {
                SysFunction.departmentList().then(res => {
                    if (res.code === 0) {
                        this.depList = res.data;
                    }
                })
            },
            showAdd(node) {
                this.dialogVisible = true;
                this.parentName = node.name;
                this.addForm.parentId = node.id;
            },
            showClose(done) {
                this.parentName = "";
                this.addForm.name = "";
                this.addForm.parentId = -1;
                done();
            },
            cancelAdd() {
                this.dialogVisible = false;
                this.parentName = "";
                this.addForm.name = "";
                this.addForm.parentId = -1;
            },
            submitAdd() {
                if (this.addForm.name.trim()) {
                    SysFunction.addDepartment(this.addForm).then(res => {
                        if (res.code === 0){
                            this.$message.success(res.message);
                            this.initDepartment();
                            this.cancelAdd();
                        }
                    })
                }else {
                    this.$message.warning("部门名称不能为空");
                }
            },
            deleteDep(node,data) {
                this.$confirm('此操作将永久删除【'+data.name+'】部门, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    SysFunction.deleteDepartmentById(data.id).then(res => {
                        if (res.code === 0) {
                            this.$message.success(res.message);
                            this.initDepartment();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },

        },
    }
</script>

<style scoped>
    .treeNode{
        width: 100%;
        display: flex;
        justify-content: space-between;
    }
    .function{
        padding: 3px;
    }
</style>