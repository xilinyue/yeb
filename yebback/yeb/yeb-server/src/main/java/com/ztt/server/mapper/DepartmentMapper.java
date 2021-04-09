package com.ztt.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztt.server.pojo.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    List<Department> getDepartmentList(Integer id);

    Department addDepartment(Department department);

    Department deleteDepartment(Department department);
}
