<template>
    <div>
        <div>
            <el-input size="small"  placeholder="请输入职称名称"
                      suffix-icon="el-icon-plus" v-model="jobLevel.name"
                      class="inputJobLevel"
                      @keydown.enter.native="addJobLevel"></el-input>
            <el-select size="small" v-model="jobLevel.titleLevel" placeholder="请选择职称等级" class="inputJobLevel">
                <el-option
                        v-for="item in options"
                        :key="item"
                        :label="item"
                        :value="item">
                </el-option>
            </el-select>
            <el-button type="primary" icon="el-icon-plus" size="small" @click="addJobLevel">新增</el-button>
        </div>
        <div>
            <el-table
                    :data="jobLevelList"
                    stripe
                    border
                    style="width: 70%"
                    @selection-change="handleSelectionChange">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="id"
                        label="编号"
                        width="50">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="职称名称"
                        width="200">
                </el-table-column>
                <el-table-column
                        prop="titleLevel"
                        label="职称等级"
                        width="200">
                </el-table-column>
                <el-table-column
                        label="是否启用"
                        width="100">
                    <template slot-scope="scope">
                        <el-tag type="success" v-if="scope.row.enabled" size="small">已启用</el-tag>
                        <el-tag type="warning" v-else size="small">未启用</el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        label="创建时间"
                        width="200">
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                @click="showEidt(scope.$index, scope.row)">编辑</el-button>
                        <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-dialog
                    title="编辑职称"
                    :visible.sync="dialogVisible"
                    width="30%">
                <el-form>
                    <el-form-item>
                        <el-tag>职称名称</el-tag>
                        <el-input size="small" style="width: 300px; margin-left: 8px" v-model="updateJL.name"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-tag>职称等级</el-tag>
                        <el-select size="small" style="width: 300px; margin-left: 8px"  v-model="updateJL.titleLevel" placeholder="请选择职称等级">
                            <el-option
                                    v-for="item in options"
                                    :key="item"
                                    :label="item"
                                    :value="item">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-tag>是否启用</el-tag>
                        <el-switch
                                v-model="updateJL.enabled"
                                style="margin-left: 8px"
                                active-color="#13ce66"
                                inactive-color="#E6A23C">
                        </el-switch>
                    </el-form-item>
                </el-form>

                <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="updateJobLevel">确 定</el-button>
                </span>
            </el-dialog>
            <el-button
                    size="mini"
                    type="danger"
                    style="margin-top: 10px"
                    :disabled="this.multipleSelection.length <= 0"
                    @click="handleMultipleDelete">批量删除</el-button>
        </div>
    </div>
</template>

<script>
    import SysFunction from "../../../api/SysFunction";
    export default {
        name: "JobLevelMana",
        data() {
            return{
                options: ["正高级","副高级","中级","初级"],
                jobLevel: {
                    name: '',
                    titleLevel: '正高级'
                },
                updateJL: {},
                jobLevelList: [],
                multipleSelection: [],
                dialogVisible: false
            }
        },
        mounted() {
            this.getJobLevels();
        },
        methods: {
            updateJobLevel() {
                this.updateJL.createDate = "";
                SysFunction.updateJobLevel(this.updateJL).then(res => {
                    if (res.code === 0){
                        this.getJobLevels();
                        this.dialogVisible = false;
                    }
                });
            },
            handleMultipleDelete() {
                this.$confirm('此操作将永久删除【'+this.multipleSelection.length+'】个职位, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let param = "?";
                    this.multipleSelection.forEach(item => {
                        param += "ids=" + item.id + "&";
                    });
                    SysFunction.deleteJobLevels(param).then(res => {
                        if (res.code === 0) {
                            this.$message.success(res.message);
                            this.getJobLevels();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            showEidt(index,data) {
                this.dialogVisible = true;
                this.updateJL = {...data};
            },
            handleDelete(index,data) {
                this.$confirm('此操作将永久删除【'+data.name+'】职位, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    SysFunction.deleteJobLevel(data.id).then(res => {
                        if (res.code === 0) {
                            this.$message.success(res.message);
                            this.getJobLevels();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            addJobLevel() {
                if (this.jobLevel.name.trim()) {
                    SysFunction.addJobLevel(this.jobLevel).then(res => {
                        if (res.code === 0) {
                            this.$message.success("添加成功");
                            this.jobLevel.name = '';
                            this.getJobLevels();
                        }
                    });
                }else {
                    this.$message.error("职位名称不能为空");
                }
            },
            getJobLevels() {
                SysFunction.getJobLevels().then(res => {
                    if (res.code === 0){
                        this.jobLevelList = res.data;
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .inputJobLevel{
        width: 300px;
        margin-right: 10px;
        margin-bottom: 15px;
    }
</style>