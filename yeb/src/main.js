import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from "./store";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import 'font-awesome/css/font-awesome.css';
import {initMenu} from "./util/menus";
import AdminFunction from "./api/AdminFunction";

Vue.config.productionTip = false;
Vue.use(ElementUI);

router.beforeEach((to, from, next) => {
  let token = localStorage.getItem("token");
  if (token && to.path !== "/login") {
    initMenu(router,store);
    next();
  }else {
    if(to.path === "/" || to.path === "/login") {
      next();
    }else {
      next({path: "/", query: {redirect: to.path}});
    }
  }
});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
