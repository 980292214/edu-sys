package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.feedback;
import com.example.service.feedbackService;
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
@RequestMapping("/api/feedback")
public class feedbackController {
    @Resource
    private feedbackService feedbackService;
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
    public Result<?> save(@RequestBody feedback feedback) {
        return Result.success(feedbackService.save(feedback));
    }

    @PutMapping
    public Result<?> update(@RequestBody feedback feedback) {
        return Result.success(feedbackService.updateById(feedback));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        feedbackService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(feedbackService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(feedbackService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<feedback> query = Wrappers.<feedback>lambdaQuery().orderByDesc(feedback::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(feedback::getContent, name);
        }
        return Result.success(feedbackService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<feedback> all = feedbackService.list();
        for (feedback obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("课程编号", obj.getCourseid());
            row.put("学号", obj.getStudentid());
            row.put("反馈内容", obj.getContent());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("反馈信息表信息", "UTF-8");
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
        List<feedback> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            feedback obj = new feedback();
            obj.setCourseid((String) row.get(1));
            obj.setStudentid((String) row.get(2));
            obj.setContent((String) row.get(3));

            saveList.add(obj);
        }
        feedbackService.saveBatch(saveList);
        return Result.success();
    }

}
