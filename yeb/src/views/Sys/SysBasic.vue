<template>
    <div>
        <el-tabs v-model="activeName" type="card">
            <el-tab-pane label="部门管理" name="first">
                <department-mana></department-mana>
            </el-tab-pane>
            <el-tab-pane label="职位管理" name="second">
                <position-mana></position-mana>
            </el-tab-pane>
            <el-tab-pane label="职称管理" name="third">
                <job-level-mana></job-level-mana>
            </el-tab-pane>
            <el-tab-pane label="奖惩规则" name="fourth">
                <ec-mana></ec-mana>
            </el-tab-pane>
            <el-tab-pane label="权限组" name="five">
                <permission-mana></permission-mana>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    import DepartmentMana from "../../components/sys/basic/DepartmentMana";
    import EcMana from "../../components/sys/basic/EcMana";
    import JobLevelMana from "../../components/sys/basic/JobLevelMana";
    import PermissionMana from "../../components/sys/basic/PermissionMana";
    import PositionMana from "../../components/sys/basic/PositionMana";

    export default {
        name: "SalMonth",
        components: {
            DepartmentMana,
            EcMana,
            JobLevelMana,
            PermissionMana,
            PositionMana
        },
        data() {
            return{
                activeName: "first"
            }
        }
    }
</script>

<style scoped>

</style>