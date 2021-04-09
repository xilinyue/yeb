package com.ztt.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ztt.server.pojo.Department;

import java.util.*;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 获取部门列表
     * @return
     */
    List<Department> getDepartmentList();

    /**
     * 添加部门
     * @param department
     * @return
     */
    String addDepartment(Department department);

    /**
     * 删除部门
     * @param id
     * @return
     */
    String deleteDepartment(Integer id);
}
