package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.inform;
import com.example.service.informService;
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
@RequestMapping("/api/inform")
public class informController {
    @Resource
    private informService informService;
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
    public Result<?> save(@RequestBody inform inform) {
        return Result.success(informService.save(inform));
    }

    @PutMapping
    public Result<?> update(@RequestBody inform inform) {
        return Result.success(informService.updateById(inform));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        informService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(informService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(informService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<inform> query = Wrappers.<inform>lambdaQuery().orderByDesc(inform::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(inform::getContent, name);
        }
        return Result.success(informService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<inform> all = informService.list();
        for (inform obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("通知编号", obj.getInformid());
            row.put("通知标题", obj.getTitle());
            row.put("通知内容", obj.getContent());
            row.put("通知时间", obj.getTime());
            row.put("发布人工号", obj.getAdminid());
            row.put("通知附件", obj.getAttachment());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("通知表信息", "UTF-8");
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
        List<inform> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            inform obj = new inform();
            obj.setInformid((String) row.get(1));
            obj.setTitle((String) row.get(2));
            obj.setContent((String) row.get(3));
            obj.setTime((String) row.get(4));
            obj.setAdminid((String) row.get(5));
            obj.setAttachment((String) row.get(6));

            saveList.add(obj);
        }
        informService.saveBatch(saveList);
        return Result.success();
    }

}
