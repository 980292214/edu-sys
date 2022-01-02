package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.New;
import com.example.service.NewService;
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
@RequestMapping("/api/new")
public class NewController {
    @Resource
    private NewService newService;
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
    public Result<?> save(@RequestBody New wew) {
        return Result.success(newService.save(wew));
    }

    @PutMapping
    public Result<?> update(@RequestBody New wew) {
        return Result.success(newService.updateById(wew));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        newService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        System.out.println("**********");
        return Result.success(newService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(newService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<New> query = Wrappers.<New>lambdaQuery().orderByDesc(New::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(New::getTitle, name);
        }
        return Result.success(newService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<New> all = newService.list();
        for (New obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("内容", obj.getContent());
            row.put("序号", obj.getId());
            row.put("标题", obj.getTitle());
            row.put("下载量",obj.getDownload());
            row.put("下载文件名",obj.getName());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("资源上传信息", "UTF-8");
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
        List<New> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            New obj = new New();
            obj.setContent((String) row.get(1));
            obj.setTitle((String) row.get(2));

            saveList.add(obj);
        }
        newService.saveBatch(saveList);
        return Result.success();
    }
    @GetMapping("/downloadnum")
    public Result<?> downloadnum(){
        System.out.println("用户下载");
        List<New> download = newService.download();
        return Result.success(download);
    }

}
