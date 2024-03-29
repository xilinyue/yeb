import Vue from 'vue';
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    redirect: "/login"
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("../views/Login")
  },{
    path: "/home",
    name: "Home",
    component: () => import("../views/Home")
  }
];

const router = new VueRouter({
  routes
});

export default router;