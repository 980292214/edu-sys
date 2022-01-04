package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.students;
import com.example.mapper.studentsMapper;
import com.example.service.studentsService;
import com.example.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.exception.CustomException;
import cn.hutool.core.util.StrUtil;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/students")
public class studentsController {
    @Resource
    private studentsService studentsService;
    @Resource
    private HttpServletRequest request;

    public User getUser() {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomException("-1", "请登录");
        }
        return user;
    }

    @PostMapping
    public Result<?> save(@RequestBody students students) {
        return Result.success(studentsService.save(students));
    }

    @PutMapping
    public Result<?> update(@RequestBody students students) {
        return Result.success(studentsService.updateById(students));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        studentsService.removeById(id);
        return Result.success();
    }

    @Autowired
    private studentsMapper stuMapper;

    @GetMapping("/stu")
    public Result<?> findById(@RequestParam("id") Long id) {
        System.out.println("hhh");

        return Result.success(stuMapper.getStu(id));

    }

    @GetMapping
    public Result<?> findAll() {
        System.out.println("hhhh");
        return Result.success(studentsService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<students> query = Wrappers.<students>lambdaQuery().orderByDesc(students::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(students::getName, name);
        }
        return Result.success(studentsService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<students> all = studentsService.list();
        for (students obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("班级编号", obj.getClasse());
            row.put("院系", obj.getDepart());
            row.put("邮箱", obj.getEmail());
            row.put("ID", obj.getId());
            row.put("专业", obj.getMajor());
            row.put("联系方式", obj.getPhoto());
            row.put("姓名", obj.getName());
            row.put("密码", obj.getPassword());
            row.put("照片", obj.getPhoto());
            row.put("年龄", obj.getAge());
            row.put("入学时间", obj.getStarttime());
            row.put("学号", obj.getStudentid());
            row.put("性别", obj.getSex());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("学生信息表信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }

    @GetMapping("/upload/{fileId}")
    public Result<?> upload(@PathVariable String fileId) {
        String basePath = System.getProperty("user.dir") + "/src/main/resources/static/file/";
        List<String> fileNames = FileUtil.listFileNames(basePath);
        String file = fileNames.stream().filter(name -> name.contains(fileId)).findAny().orElse("");
        List<List<Object>> lists = ExcelUtil.getReader(basePath + file).read(1);
        List<students> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            students obj = new students();
            obj.setClasse((String) row.get(1));
            obj.setDepart((String) row.get(2));
            obj.setEmail((String) row.get(3));
            obj.setMajor((String) row.get(4));
            obj.setPhone((String) row.get(5));
            obj.setName((String) row.get(6));
            obj.setPassword((String) row.get(7));
            obj.setPhoto((byte) row.get(8));
            obj.setAge((String) row.get(9));
            obj.setStarttime((String) row.get(10));
            obj.setStudentid((String) row.get(11));
            obj.setSex((String) row.get(12));

            saveList.add(obj);
        }
        studentsService.saveBatch(saveList);
        return Result.success();
    }

}
