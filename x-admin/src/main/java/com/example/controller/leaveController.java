package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.leave;
import com.example.service.leaveService;
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
@RequestMapping("/api/leave")
public class leaveController {
    @Resource
    private leaveService leaveService;
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
    public Result<?> save(@RequestBody leave leave) {
        return Result.success(leaveService.save(leave));
    }

    @PutMapping
    public Result<?> update(@RequestBody leave leave) {
        return Result.success(leaveService.updateById(leave));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        leaveService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(leaveService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(leaveService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<leave> query = Wrappers.<leave>lambdaQuery().orderByDesc(leave::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(leave::getReason, name);
        }
        return Result.success(leaveService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<leave> all = leaveService.list();
        for (leave obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("请假编号", obj.getId());
            row.put("请假开始时间", obj.getStarttime());
            row.put("请假结束时间", obj.getEndtime());
            row.put("请假原因", obj.getReason());
            row.put("请假附件", obj.getAttachment());
            row.put("学号", obj.getStudentid());
            row.put("教师编号", obj.getTeacherid());
            row.put("请假审批状态", obj.getState());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("请假信息表信息", "UTF-8");
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
        List<leave> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            leave obj = new leave();
            obj.setId((Long) row.get(1));
            obj.setStarttime((String) row.get(2));
            obj.setEndtime((String) row.get(3));
            obj.setReason((String) row.get(4));
            obj.setAttachment((String) row.get(5));
            obj.setStudentid((String) row.get(6));
            obj.setTeacherid((String) row.get(7));
            obj.setState((String) row.get(8));

            saveList.add(obj);
        }
        leaveService.saveBatch(saveList);
        return Result.success();
    }

}
