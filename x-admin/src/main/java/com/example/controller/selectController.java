package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.course;
import com.example.entity.select;
import com.example.entity.students;
import com.example.mapper.studentsMapper;
import com.example.service.selectService;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/select")
public class selectController {
    @Resource
    private selectService selectService;
    @Resource
    private HttpServletRequest request;

    public User getUser() {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomException("-1", "请登录");
        }
        return user;
    }

    @GetMapping("/noselect/{studentId}")
    public Result<?> noselectedCourse(@PathVariable String studentId){
        List courses = selectService.noselectedCourse(studentId);
        return Result.success(courses);
    }

    @GetMapping("/select/{studentId}")
    public Result<?> selectedCourse(@PathVariable String studentId){
        List courses = selectService.selectedCourse(studentId);
        return Result.success(courses);
    }

    @GetMapping("/delete/{courseId}/{studentId}")
    public Result<?> deleteSelectedCourse(@PathVariable String courseId,@PathVariable String studentId){
        int rows = selectService.deleteSelectedCourse(courseId,studentId);
        return Result.success(rows);
    }

    @PostMapping
    public Result<?> save(@RequestBody select select) {
        return Result.success(selectService.save(select));
    }

    @PutMapping
    public Result<?> update(@RequestBody select select) {
        return Result.success(selectService.updateById(select));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        selectService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(selectService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(selectService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<select> query = Wrappers.<select>lambdaQuery().orderByDesc(select::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(select::getId, name);
        }
        return Result.success(selectService.page(new Page<>(pageNum, pageSize), query));
    }
    @Autowired
    private studentsMapper stuMapper;

    @GetMapping("/stupage")
    public Result<?> findstuPage(@RequestParam(required = false, defaultValue = "") String name,
                                 @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize, HttpSession httpSession) {
        User user = (User)httpSession.getAttribute("user");

        students stu = stuMapper.getStudentid(user.getUsername());
        LambdaQueryWrapper<select> query = Wrappers.<select>lambdaQuery().orderByDesc(select::getId).eq(select::getStudentid,stu.getStudentid());
        if (StrUtil.isNotBlank(name)) {
            query.like(select::getId, name);
        }
        return Result.success(selectService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<select> all = selectService.list();
        for (select obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("学号", obj.getStudentid());
            row.put("课程编号", obj.getCourseid());
            row.put("教师编号", obj.getTeacherid());
            row.put("课程成绩", obj.getGrade());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("选课信息表信息", "UTF-8");
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
        List<select> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            select obj = new select();
            obj.setStudentid((String) row.get(1));
            obj.setCourseid((String) row.get(2));
            obj.setTeacherid((String) row.get(3));
            obj.setGrade((String) row.get(4));

            saveList.add(obj);
        }
        selectService.saveBatch(saveList);
        return Result.success();
    }

    @GetMapping("/student/{studentId}")
    public Result<?> findByStudentId(@PathVariable String studentId) {
        return Result.success(selectService.findByStudentId(studentId));
    }

}
