package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.course;
import com.example.entity.students;
import com.example.mapper.studentsMapper;
import com.example.service.courseService;
import com.example.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.service.studentsService;
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
@RequestMapping("/api/course")
public class courseController {
    @Resource
    private courseService courseService;
    @Resource
    private HttpServletRequest request;

    @Autowired
    private studentsService stuservice;



    public User getUser() {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomException("-1", "请登录");
        }
        return user;
    }

    @PostMapping
    public Result<?> save(@RequestBody course course) {
        return Result.success(courseService.save(course));
    }

    @PutMapping
    public Result<?> update(@RequestBody course course) {
        return Result.success(courseService.updateById(course));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        courseService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(courseService.getById(id));
    }

    @GetMapping("/courses/{teacherId}")
    public Result<?> getCourses(@PathVariable String teacherId) {
        return Result.success(courseService.getCourses(teacherId));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(courseService.list());
    }



    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize,HttpSession httpSession) {
        User user = (User)httpSession.getAttribute("user");

        LambdaQueryWrapper<course> query = Wrappers.<course>lambdaQuery().orderByDesc(course::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(course::getCoursename, name);
        }
        return Result.success(courseService.page(new Page<>(pageNum, pageSize), query));
    }

    @Autowired
    private studentsMapper stuMapper;

    @GetMapping("/stupage")
    public Result<?> findstuPage(@RequestParam(required = false, defaultValue = "") String name,
                              @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize,HttpSession httpSession) {

        User user = (User)httpSession.getAttribute("user");

        students stu = stuMapper.getStudentid(user.getUsername());


        LambdaQueryWrapper<course> query = Wrappers.<course>lambdaQuery().orderByDesc(course::getId).eq(course::getLimitmajor,stu.getMajor());

        if (StrUtil.isNotBlank(name)) {
            query.like(course::getCoursename, name);
        }
        return Result.success(courseService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<course> all = courseService.list();
        for (course obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("课程编号", obj.getId());
            row.put("课程名称", obj.getCoursename());
            row.put("教师编号", obj.getTeacherid());
            row.put("学时", obj.getPeriod());
            row.put("学分", obj.getCredit());
            row.put("课程描述", obj.getCoursedesc());
            row.put("上课地点", obj.getPlace());
            row.put("上课时间", obj.getTime());
            row.put("修读方式", obj.getModer());
            row.put("课程人数", obj.getNumber());
            row.put("限选专业", obj.getLimitmajor());
            row.put("审核状态", obj.getState());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("课程信息表信息", "UTF-8");
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
        List<course> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            course obj = new course();
            obj.setId((Long) row.get(1));
            obj.setCoursename((String) row.get(2));
            obj.setTeacherid((String) row.get(3));
            obj.setPeriod((String) row.get(4));
            obj.setCredit((String) row.get(5));
            obj.setCoursedesc((String) row.get(6));
            obj.setPlace((String) row.get(7));
            obj.setTime((String) row.get(8));
            obj.setModer((String) row.get(9));
            obj.setNumber((String) row.get(10));
            obj.setLimitmajor((String) row.get(11));
            obj.setState((String) row.get(12));

            saveList.add(obj);
        }
        courseService.saveBatch(saveList);
        return Result.success();
    }

}
