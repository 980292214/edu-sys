package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.exam;
import com.example.service.examService;
import com.example.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@RequestMapping("/api/exam")
public class examController {
    @Resource
    private examService examService;
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
    public Result<?> save(@RequestBody exam exam) {
        return Result.success(examService.save(exam));
    }

    @PutMapping
    public Result<?> update(@RequestBody exam exam) {
        return Result.success(examService.updateById(exam));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        examService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(examService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(examService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<exam> query = Wrappers.<exam>lambdaQuery().orderByDesc(exam::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(exam::getId, name);
        }
        return Result.success(examService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<exam> all = examService.list();
        for (exam obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("考试编号", obj.getExamid());
            row.put("考试时间", obj.getTime());
            row.put("考试地点", obj.getPlace());
            row.put("课程编号", obj.getCourseid());
            row.put("班级编号", obj.getClassid());
            row.put("教师编号", obj.getTeacherid());
            row.put("审核状态", obj.getState());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("考试信息表信息", "UTF-8");
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
        List<exam> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            exam obj = new exam();
            obj.setExamid((String) row.get(1));
            obj.setTime((String) row.get(2));
            obj.setPlace((String) row.get(3));
            obj.setCourseid((String) row.get(4));
            obj.setClassid((String) row.get(5));
            obj.setTeacherid((String) row.get(6));
            obj.setState((String) row.get(7));

            saveList.add(obj);
        }
        examService.saveBatch(saveList);
        return Result.success();
    }

}
