import request from "../api/api";

export const initMenu = (router,store)=> {
    if (store.state.routes.length > 0) {
        return;
    }

    request.get("/menu/getByAdminId").then(data => {
        if (data.data) {
            let fmtRoutes = formatRoutes(data.data);
            router.addRoutes(fmtRoutes);
            store.commit("initRoutes",fmtRoutes);
        }
    });
};

export const formatRoutes = (routes) => {
    let fmtRoutes = [];
    routes.forEach(route => {
        let {
            path,component,name,iconCls,children
        } = route;
        if (children && children instanceof  Array) {
            // 递归
            children = formatRoutes(children);
        }
        let fmtRouter = {
            path: path,
            name: name,
            iconCls: iconCls,
            children: children,
            component(resolve) {
               if (component.startsWith("Home")) {
                   require(["../views/" + component + ".vue"],resolve);
               } else if (component.startsWith("Emp")) {
                   require(["../views/Emp/" + component + ".vue"],resolve);
               } else if (component.startsWith("Per")){
                   require(["../views/Per/" + component + ".vue"],resolve);
               } else if (component.startsWith("Sal")){
                   require(["../views/Sal/" + component + ".vue"],resolve);
               } else if (component.startsWith("Sta")){
                   require(["../views/Sta/" + component + ".vue"],resolve);
               } else if (component.startsWith("Sys")){
                   require(["../views/Sys/" + component + ".vue"],resolve);
               }
            }
        }
        fmtRoutes.push(fmtRouter)
    });
    return fmtRoutes;
};