package com.example.controller;

import com.example.service.NewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 降临
 */
@RestController
public class downloadFilecon {
    @Autowired
    private NewService newService;

    @RequestMapping("/download")
    public void download(String downfile, Long id,HttpServletResponse response) {
        try {
            String fileurl = "D:\\upload\\";
            fileurl = fileurl+downfile;
// path是指想要下载的文件的路径
            File file = new File(fileurl);

// 获取文件名
            String filename = file.getName();
// 获取文件后缀名
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();


// 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

// 清空response
            response.reset();

            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();

//            newService.downloadnum(id);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
