package com.tzadok.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tzadok.pojo.Employee;
import com.tzadok.service.EmployeeService;
import com.tzadok.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author Tzadok
 * @date 2022/8/23 16:23:53
 * @project reggie_take_out
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param employee
     * @param request
     * @return
     */
    @PostMapping("/login")
    public Result<Employee> login(@RequestBody Employee employee, HttpServletRequest request){

        //用户密码进行MD5加密
        String password = employee.getPassword();

        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据用户名查询数据库
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3.判断用户是否存在，存在再查询密码，否则返回错误提示信息
        if (emp == null) {
            return Result.error("用户不存在,请核对用户名是否错误");
        }
        //4.判断用户密码是否错误
        if (!emp.getPassword().equals(password)){
            return Result.error("用户密码错误");
        }
        //5.判断用户是否被禁用
        if (emp.getStatus() == 1){
            //6.将用户存到session域
            request.getSession().setAttribute("employee",emp.getId());
            return Result.success(emp);
        }
        return Result.error("用户已被禁用，请联系管理员");
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        //清理session中的员工id
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody Employee employee,HttpServletRequest request){
        //设置员工初始密码，进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        //获取当前用户登录的ID
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return Result.success("新增员工成功");
    }

    /**
     * 员工分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
//    http://localhost/employee/page?page=1&pageSize=10
    @GetMapping("/page")
    public Result<Page> getEmployeePage(int page, int pageSize, String name
    ){

        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);

        Page pageInfo = new Page(page,pageSize);

        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.like(StringUtils.isNotEmpty(name),"name",name);

        queryWrapper.orderByDesc("update_time");

        employeeService.page(pageInfo,queryWrapper);

        return Result.success(pageInfo);
    }

    /**
     * 修改员工状态
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public Result<String> update(HttpServletRequest request,@RequestBody Employee employee){

//        Long empId = (Long) request.getSession().getAttribute("employee");

//        employee.setUpdateUser(empId);

//        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);

        return Result.success("员工信息修改成功");
    }

    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> getEmployee(@PathVariable("id") Long id){
        log.info("id:{}",id);

        Employee employee = employeeService.getById(id);

        if (employee != null) {
            return Result.success(employee);
        }
        return Result.error("找不到这个人");
    }
}
