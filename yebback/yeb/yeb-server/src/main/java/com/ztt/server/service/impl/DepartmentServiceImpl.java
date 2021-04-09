package com.ztt.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ztt.server.mapper.DepartmentMapper;
import com.ztt.server.pojo.Department;
import com.ztt.server.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> getDepartmentList() {
        return departmentMapper.getDepartmentList(-1);
    }

    @Override
    public String addDepartment(Department department) {
        department.setEnabled(true);
        departmentMapper.addDepartment(department);
        if (department.getResult() == 1) {
            return "添加成功";
        }
        return "添加失败";
    }

    @Override
    public String deleteDepartment(Integer id) {
        Department department = new Department();
        department.setId(id);
        departmentMapper.deleteDepartment(department);
        if (department.getResult() == -2) {
            return "删除失败，该部门存在子部门";
        }else if(department.getResult() == -1) {
            return "删除失败，改部门下含有员工";
        }
        return "删除成功";
    }
}
