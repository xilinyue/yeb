<template>
    <div>
        <div>
            <el-input size="small"  placeholder="请输入职位名称"
                      suffix-icon="el-icon-plus" v-model="position.name"
                    class="inputPosition"
                    @keydown.enter.native="addPosition"></el-input>
            <el-button type="primary" icon="el-icon-plus" size="small" @click="addPosition">新增</el-button>
        </div>
        <div>
            <el-table
                    :data="positionList"
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
                        label="职位名称"
                        width="300">
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
                    title="编辑职位"
                    :visible.sync="dialogVisible"
                    width="30%">
                <el-tag>职位名称</el-tag>
                <el-input size="small" style="width: 300px; margin-left: 8px" v-model="updatePos.name"></el-input>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="updatePosition">确 定</el-button>
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
        name: "PositionMana",
        data() {
            return{
                position: {
                    name: ''
                },
                updatePos: {},
                positionList: [],
                multipleSelection: [],
                dialogVisible: false
            }
        },
        mounted() {
            this.getPositions();
        },
        methods: {
            updatePosition() {
                this.updatePos.createDate = "";
                SysFunction.updatePosition(this.updatePos).then(res => {
                   if (res.code === 0){
                       this.getPositions();
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
                    SysFunction.deletePositions(param).then(res => {
                        if (res.code === 0) {
                            this.$message.success(res.message);
                            this.getPositions();
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
                this.updatePos = {...data};
            },
            handleDelete(index,data) {
                this.$confirm('此操作将永久删除【'+data.name+'】职位, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    SysFunction.deletePosition(data.id).then(res => {
                        if (res.code === 0) {
                            this.$message.success(res.message);
                            this.getPositions();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            addPosition() {
                if (this.position.name.trim()) {
                    SysFunction.addPosition(this.position).then(res => {
                       if (res.code === 0) {
                           this.$message.success("添加成功");
                           this.position.name = '';
                           this.getPositions();
                       }
                    });
                }else {
                    this.$message.error("职位名称不能为空");
                }
            },
            getPositions() {
                SysFunction.getPositions().then(res => {
                    if (res.code === 0){
                        this.positionList = res.data;
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .inputPosition{
        width: 300px;
        margin-right: 10px;
        margin-bottom: 15px;
    }

</style>