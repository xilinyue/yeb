<template>
    <div  v-loading="loading">
        <el-form :rules="rules" ref="loginForm" class="loginContainer" :model="loginForm">
            <h3 class="loginTitle">系统登录</h3>
            <el-form-item prop="username">
                <el-input type="text" v-model="loginForm.username" auto-complete="false" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input type="password" v-model="loginForm.password" auto-complete="false" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item prop="code">
                <el-input style="width: 75%"  type="text" v-model="loginForm.code" auto-complete="false" placeholder="点击图片刷新验证码"></el-input>
                <img style="margin-left: 8px" :src="captchaUrl" alt="图片验证码" @click="updateCaptcha">
            </el-form-item>
            <el-checkbox style="margin: 0 0 10px" :checked="true">记住我</el-checkbox>
            <el-button style="width: 100%" type="primary" @click="submitLogin">登录</el-button>
        </el-form>
    </div>
</template>

<script>
    import AdminFunction from "../api/AdminFunction";
    export default {
        name: "Login",
        data() {
            return{
                loading: false,
                captchaUrl: "/captcha?time=" + new Date(),
                loginForm: {
                    username: "admin",
                    password: "123456",
                    code: ""
                },
                rules: {
                    username: [
                        {required: true, message: "请输入用户名", trigger: "blur"},
                        {min:3, max: 8, message: "用户名长度在3~8个之间", trigger: "blur"}
                    ],
                    password: [
                        {required: true, message: "请输入密码", trigger: "blur"}
                    ],
                    code: [
                        {required: true, message: "请输入验证码", trigger: "blur"}
                    ]
                }
            }
        },
        methods: {
            updateCaptcha() {
                this.captchaUrl = "/captcha?time=" + new Date();
            },
            submitLogin() {
                this.$refs.loginForm.validate(valid => {
                    if (valid) {
                        this.loading = true;
                        AdminFunction.login(this.loginForm).then(res => {
                            this.loading = false;
                            if (res.code === 0){
                                localStorage.setItem("token", res.data.tokenHead + res.data.token);
                                if (!localStorage.getItem("user") || localStorage.getItem("user") === undefined) {
                                    AdminFunction.getOwn().then(res0 => {
                                        if (res0.data) {
                                            localStorage.setItem("user",JSON.stringify(res0.data));
                                            let redirect = this.$route.query.redirect;
                                            if (redirect === undefined || redirect === "/") {
                                                redirect = "/home";
                                            }
                                            this.$router.replace(redirect);
                                        }
                                    });
                                }else{
                                    let redirect = this.$route.query.redirect;
                                    if (redirect === undefined || redirect === "/") {
                                        redirect = "/home";
                                    }
                                    this.$router.replace(redirect);
                                }
                            }
                        });
                    }else{
                        this.$message.error("请完善表格！！！");
                    }
                });
            }
        }
    }
</script>

<style>
    .loginContainer {
        margin: 180px auto;
        padding: 10px 20px 20px;
        width: 400px;
        border-radius: 15px;
        border: 1px solid #eaeaea;
        box-shadow: 0 0 20px #eaeaea;
    }
    .loginTitle {
        text-align: center;
    }
    .el-form-item__content{
        display: flex;
    }
</style>