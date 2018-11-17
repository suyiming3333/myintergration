package com.corn.myintergration.controller.file;

import com.corn.myintergration.entity.FileInfo;
import com.corn.myintergration.utils.FileUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Auther: suyiming
 * @Date: 18-11-17 10:07
 * @Description:
 */

@Controller()
@RequestMapping("/file")
public class FileController {

    @RequestMapping("/uploadFile")
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        FileInfo fileInfo = FileUtil.saveFile(file);
        System.out.println(fileInfo);
        return fileInfo.toString();
    }

    @RequestMapping("/getFile")
    public void getFile(HttpServletRequest request, HttpServletResponse response){
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName("test");
        fileInfo.setPath("/data/upload/201811/logo.jpg");
        FileUtil.downloadFile(fileInfo,response);
    }

    @RequestMapping(value = "/getImage",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public BufferedImage getImage() throws IOException {
        return ImageIO.read(new FileInputStream(new File("/data/upload/201811/logo.jpg")));
    }

}
