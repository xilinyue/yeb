<template>
    <div>
        <el-container>
            <el-header class="home-header">
                <div class="title">ZTT&GJ之家</div>
                <el-dropdown class="right" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{user.name}}
                        <i>
                            <img :src="user.userFace">
                        </i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="personal">个人中心</el-dropdown-item>
                        <el-dropdown-item command="setting">设置</el-dropdown-item>
                        <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </el-header>
            <el-container>
                <el-aside width="200px">
                    <el-menu unique-opened
                                :router="true">
                        <el-submenu :index="index + ''" v-for="(item,index) in routes" :key="index">
                            <template slot="title">
                                <i :class="item.iconCls" style="margin-right: 5px"></i>
                                {{item.name}}
                            </template>
                            <el-menu-item :index="children.path" :key="index2" v-for="(children,index2) in item.children">
                                {{children.name}}
                            </el-menu-item>
                        </el-submenu>
                    </el-menu>
                </el-aside>
                <el-main>
                    <el-breadcrumb separator-class="el-icon-arrow-right" v-if="$router.currentRoute.path !== '/home'">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>{{this.$router.currentRoute.name}}</el-breadcrumb-item>
                    </el-breadcrumb>
                    <div v-else class="homeWelcome">
                        欢迎来到ZTT&GJ之家
                    </div>
                    <router-view />
                </el-main>
            </el-container>
        </el-container>

    </div>
</template>

<script>
    export default {
        name: "Home",
        data() {
            return{
                user: JSON.parse(localStorage.getItem("user"))
            }
        },
        computed: {
            routes() {
                return this.$store.state.routes;
            }
        },
        methods: {
            handleCommand(command) {
                switch (command) {
                    case "personal":
                        break;
                    case "setting":
                        break;
                    case "logout":
                        localStorage.removeItem("token");
                        localStorage.removeItem("user");
                        this.$store.commit("initRoutes",[]);
                        this.$router.replace("/");
                }
            }
        }
    }
</script>

<style>
    .home-header{
        display: flex;
        justify-content: space-between;
        background-color: #409EFF;
        position: relative;
    }
    .home-header .title{
        line-height: 60px;
        font-size: 30px;
        font-family: 华文楷体;
        color: #fff;
        letter-spacing: 5px;
        margin-left: 15px;
    }
    .el-dropdown-link{
        cursor: pointer;
        text-align: center;
        position: absolute;
        right: 20px;
        line-height: 60px;
        height: 60px;
        width: 150px;
        color: #fff;
        font-weight: bold;
    }
    .el-dropdown-link img{
        position: absolute;
        width: 40px;
        height: 40px;
        border-radius: 50%;
        top: 50%;
        margin-top: -20px;
        margin-left: 10px;
    }
    .homeWelcome{
        font-weight: bold;
        font-size: 25px;
        text-align: center;
        font-family: 华文楷体;
        margin-top: 52px;
        color: #409EFF;
    }
    .el-breadcrumb{
        margin-bottom: 10px;
    }
</style>