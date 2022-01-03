package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.classinfo;
import com.example.service.classinfoService;
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
@RequestMapping("/api/classinfo")
public class classinfoController {
    @Resource
    private classinfoService classinfoService;
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
    public Result<?> save(@RequestBody classinfo classinfo) {
        return Result.success(classinfoService.save(classinfo));
    }

    @PutMapping
    public Result<?> update(@RequestBody classinfo classinfo) {
        return Result.success(classinfoService.updateById(classinfo));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        classinfoService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(classinfoService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(classinfoService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<classinfo> query = Wrappers.<classinfo>lambdaQuery().orderByDesc(classinfo::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(classinfo::getInstructor, name);
        }
        return Result.success(classinfoService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<classinfo> all = classinfoService.list();
        for (classinfo obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("", obj.getId());
            row.put("班级编号", obj.getClassid());
            row.put("辅导员", obj.getInstructor());
            row.put("班级人数", obj.getClasssize());
            row.put("专业编号", obj.getMajorid());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("班级信息表信息", "UTF-8");
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
        List<classinfo> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            classinfo obj = new classinfo();
            obj.setClassid((String) row.get(1));
            obj.setInstructor((String) row.get(2));
            obj.setClasssize((String) row.get(3));
            obj.setMajorid((String) row.get(4));

            saveList.add(obj);
        }
        classinfoService.saveBatch(saveList);
        return Result.success();
    }

}
