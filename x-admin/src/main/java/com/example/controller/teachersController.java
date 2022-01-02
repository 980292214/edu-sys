package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.teachers;
import com.example.service.teachersService;
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
@RequestMapping("/api/teachers")
public class teachersController {
    @Resource
    private teachersService teachersService;
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
    public Result<?> save(@RequestBody teachers teachers) {
        return Result.success(teachersService.save(teachers));
    }

    @PutMapping
    public Result<?> update(@RequestBody teachers teachers) {
        return Result.success(teachersService.updateById(teachers));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        teachersService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(teachersService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(teachersService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<teachers> query = Wrappers.<teachers>lambdaQuery().orderByDesc(teachers::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(teachers::getName, name);
        }
        return Result.success(teachersService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<teachers> all = teachersService.list();
        for (teachers obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("年龄", obj.getAge());
            row.put("院系", obj.getDepart());
            row.put("邮箱", obj.getEmail());
            row.put("职称", obj.getJob());
            row.put("名字", obj.getName());
            row.put("密码", obj.getPassword());
            row.put("联系方式", obj.getPhone());
            row.put("照片", obj.getPhoto());
            row.put("性别", obj.getSex());
            row.put("教师编号", obj.getTeacherid());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("教师信息表信息", "UTF-8");
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
        List<teachers> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            teachers obj = new teachers();
            obj.setAge((String) row.get(1));
            obj.setDepart((String) row.get(2));
            obj.setEmail((String) row.get(3));
            obj.setJob((String) row.get(4));
            obj.setName((String) row.get(5));
            obj.setPassword((String) row.get(6));
            obj.setPhone((String) row.get(7));
            obj.setPhoto((byte) row.get(8));
            obj.setSex((String) row.get(9));
            obj.setTeacherid((Long) row.get(10));

            saveList.add(obj);
        }
        teachersService.saveBatch(saveList);
        return Result.success();
    }

}
