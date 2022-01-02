package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.admin;
import com.example.service.adminService;
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
@RequestMapping("/api/admin")
public class adminController {
    @Resource
    private adminService adminService;
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
    public Result<?> save(@RequestBody admin admin) {
        return Result.success(adminService.save(admin));
    }

    @PutMapping
    public Result<?> update(@RequestBody admin admin) {
        return Result.success(adminService.updateById(admin));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        adminService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(adminService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(adminService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<admin> query = Wrappers.<admin>lambdaQuery().orderByDesc(admin::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(admin::getName, name);
        }
        return Result.success(adminService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<admin> all = adminService.list();
        for (admin obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("管理员工号", obj.getAdminid());
            row.put("年龄", obj.getAge());
            row.put("电子邮件", obj.getEmail());
            row.put("姓名", obj.getName());
            row.put("密码", obj.getPassword());
            row.put("联系方式", obj.getPhone());
            row.put("照片", obj.getPhoto());
            row.put("性别", obj.getSex());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("管理员信息表信息", "UTF-8");
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
        List<admin> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            admin obj = new admin();
            obj.setAdminid((String) row.get(1));
            obj.setAge((String) row.get(2));
            obj.setEmail((String) row.get(3));
            obj.setName((String) row.get(4));
            obj.setPassword((String) row.get(5));
            obj.setPhone((String) row.get(6));
            obj.setPhoto((byte) row.get(7));
            obj.setSex((String) row.get(8));

            saveList.add(obj);
        }
        adminService.saveBatch(saveList);
        return Result.success();
    }

}
