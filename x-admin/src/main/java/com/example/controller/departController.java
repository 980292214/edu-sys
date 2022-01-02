package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.depart;
import com.example.service.departService;
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
@RequestMapping("/api/depart")
public class departController {
    @Resource
    private departService departService;
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
    public Result<?> save(@RequestBody depart depart) {
        return Result.success(departService.save(depart));
    }

    @PutMapping
    public Result<?> update(@RequestBody depart depart) {
        return Result.success(departService.updateById(depart));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        departService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(departService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(departService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<depart> query = Wrappers.<depart>lambdaQuery().orderByDesc(depart::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(depart::getDepartname, name);
        }
        return Result.success(departService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<depart> all = departService.list();
        for (depart obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("院系编号", obj.getDepartid());
            row.put("院系名称", obj.getDepartname());
            row.put("院系主任", obj.getDirector());
            row.put("院系介绍", obj.getDepartdesc());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("院系信息表信息", "UTF-8");
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
        List<depart> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            depart obj = new depart();
            obj.setDepartid((String) row.get(1));
            obj.setDepartname((String) row.get(2));
            obj.setDirector((String) row.get(3));
            obj.setDepartdesc((String) row.get(4));

            saveList.add(obj);
        }
        departService.saveBatch(saveList);
        return Result.success();
    }

}
