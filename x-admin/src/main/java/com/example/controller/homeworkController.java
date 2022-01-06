package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.homework;
import com.example.service.homeworkService;
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
@RequestMapping("/api/homework")
public class homeworkController {
    @Resource
    private homeworkService homeworkService;
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
    public Result<?> save(@RequestBody homework homework) {
        return Result.success(homeworkService.save(homework));
    }

    @PutMapping
    public Result<?> update(@RequestBody homework homework) {
        return Result.success(homeworkService.updateById(homework));
    }
    @PutMapping("/commit")
    public Result<?> commit(@RequestBody homework homework) {
        return Result.success(homeworkService.commit(homework));
    }
    @PostMapping("/publish")
    public Result<?> publish(@RequestBody homework homework) {
        return Result.success(homeworkService.publish(homework));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        homeworkService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(homeworkService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(homeworkService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<homework> query = Wrappers.<homework>lambdaQuery().orderByDesc(homework::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(homework::getContent, name);
        }
        return Result.success(homeworkService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<homework> all = homeworkService.list();
        for (homework obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
//            row.put("作业编号", obj.getHomeworkid());
            row.put("课程编号", obj.getCourseid());
            row.put("学号", obj.getStudentid());
            row.put("作业内容", obj.getContent());
            row.put("作业附件", obj.getAttachment());
            row.put("截至日期", obj.getDeadline());
            row.put("提交状态", obj.getState());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("作业表信息", "UTF-8");
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
        List<homework> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            homework obj = new homework();
//            obj.setHomeworkid((String) row.get(1));
            obj.setCourseid((String) row.get(2));
            obj.setStudentid((String) row.get(3));
            obj.setContent((String) row.get(4));
            obj.setAttachment((String) row.get(5));
            obj.setDeadline((String) row.get(6));
            obj.setState((String) row.get(7));

            saveList.add(obj);
        }
        homeworkService.saveBatch(saveList);
        return Result.success();
    }

}
