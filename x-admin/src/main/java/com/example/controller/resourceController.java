package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.resource;
import com.example.service.resourceService;
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
@RequestMapping("/api/resource")
public class resourceController {
    @Resource
    private resourceService resourceService;
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
    public Result<?> save(@RequestBody resource resource) {
        return Result.success(resourceService.save(resource));
    }

    @PutMapping
    public Result<?> update(@RequestBody resource resource) {
        return Result.success(resourceService.updateById(resource));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        resourceService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(resourceService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(resourceService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<resource> query = Wrappers.<resource>lambdaQuery().orderByDesc(resource::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(resource::getName, name);
        }
        return Result.success(resourceService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<resource> all = resourceService.list();
        for (resource obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("资源分类编号", obj.getCategoryId());
            row.put("资源描述", obj.getDescn());
            row.put("资源编号", obj.getId());
            row.put("运行效果图", obj.getImage());
            row.put("资源名称", obj.getName());
            row.put("上传者", obj.getUploader());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("资源分类信息", "UTF-8");
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
        List<resource> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            resource obj = new resource();
            obj.setCategoryId(Integer.valueOf((String) row.get(1)));
            obj.setDescn((String) row.get(2));
            obj.setImage((String) row.get(3));
            obj.setName((String) row.get(4));
            obj.setUploader((String) row.get(5));

            saveList.add(obj);
        }
        resourceService.saveBatch(saveList);
        return Result.success();
    }

}
