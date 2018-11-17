package com.corn.myintergration.utils;

/**
 * @Auther: suyiming
 * @Date: 18-11-17 10:32
 * @Description:
 */

import com.corn.myintergration.entity.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;


@Component
public class FileUtil {
    private static String fileSize;
    private static String fileType;
    private static String filePath;


    private static final int M=1024;

    private static final String DOT=".";
    /**
     * 自动定义路径，使用随机不重复文件名的
     *
     * @param request
     * @param path
     * @return
     */
    public static FileInfo saveFile(HttpServletRequest request, String path) {
        return saveFile(request, path, UUID.randomUUID().toString().replace("-", ""));
    }

    /**
     * 用yml配置的路径
     *
     * @param request
     * @return
     */
    public static FileInfo saveFile(HttpServletRequest request) {
        return saveFile(request, filePath, UUID.randomUUID().toString().replace("-", ""));
    }

    /**
     * 使用srpingmvc上传，用yml配置的路径
     * @param file
     * @return
     */
    public  static FileInfo saveFile(MultipartFile file) throws Exception {
        FileInfo fileInfo=saveFile(file, filePath,UUID.randomUUID().toString().replace("-", ""));
        return  fileInfo;
    }
    /**
     * 使用srpingmvc上传，自定义路径
     * @param file
     * @return
     */
    public  static FileInfo saveFile(MultipartFile file,String path) throws Exception {
        FileInfo fileInfo=saveFile(file, path,UUID.randomUUID().toString().replace("-", ""));
        return  fileInfo;
    }

    /**
     * 下载网络资源
     *
     * @param downloadUrl 下载地址
     * @return
     */
    public static String downloadNetWorkFile(String downloadUrl) {
        String data = createFileDir();
        String savePath = filePath + File.separator + data;
        createDir(savePath);
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        int indexOfDot = downloadUrl.indexOf(".");
        String type = downloadUrl.substring(indexOfDot);
        String newName = fileName + type;
        // 实际保存路径
        String savePathReal = savePath + newName;
        // 开始下载远程文件
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File f = new File(savePathReal);
        try {
            urlfile = new URL(downloadUrl);
            httpUrl = (HttpURLConnection) urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);

            }
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null){
                    bis.close();
                }

                if (bos != null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return savePathReal;
    }



    /**
     * @param fileInfo 传入path和fileName即可
     * @param response
     */
    public static void downloadFile(FileInfo fileInfo, HttpServletResponse response) {
        String fileName = null;
        try {
            fileName = new String(fileInfo.getFileName().getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File file = new File(fileInfo.getPath());
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /***
     * springmvc的保存文件
     * @param file
     * @return
     */
    private static FileInfo saveFile(MultipartFile file, String path,String fileName) throws Exception {
        FileInfo fileInfo=new FileInfo();
        // 判断文件是否为空
        if (!file.isEmpty()) {
            File filepath = new File(path);
            String originalFileName = file.getOriginalFilename();
            int indexOfDot = originalFileName.indexOf(".");
            long fileSize=file.getSize();
            String type = originalFileName.substring(indexOfDot);
            if(!verifyFile( type,fileSize/ M,fileInfo)){
                return fileInfo;
            }
            if (!filepath.exists()){
                filepath.mkdirs();
            }
            String data = createFileDir();
            // 文件保存路径
            String savePath =path+File.separator+data+ File.separator +fileName+ type;
            fileInfo.setPath(savePath);
            fileInfo.setFileSize((fileSize/ M) + "kb");
            fileInfo.setFileName(originalFileName);
            fileInfo.setFileType(type.replace(".", ""));
            // 转存文件
            try {
                file.transferTo(new File(savePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileInfo;

        }
        return fileInfo;
    }

    /**
     * 校验文件是否符合规格
     * @param type
     * @param l
     * @param fileInfo
     * @return
     */
    public static boolean verifyFile(String type,long l,FileInfo fileInfo) throws Exception {
        if (!isExceedFileType(type.replace(DOT, ""))) {
            throw new Exception("文件类型不符合规格");
        }
        if (!isExceedFileSize(l)) {
            throw new Exception("文件大小不符合规格,最大为" + fileSize + "KB");
        }
        return true;
    }



    /**
     * 自定义文件名，路径
     *
     * @param request
     * @param path
     * @return
     */
    public static FileInfo saveFile(HttpServletRequest request, String path, String fileName) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        FileInfo fileInfo = new FileInfo();
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile uploadFile = entity.getValue();
            try {
                InputStream inputStream = uploadFile.getInputStream();
                // 获取自己数组
                byte[] getData = readInputStream(inputStream);
                String originalFileName = uploadFile.getOriginalFilename();

                int indexOfDot = originalFileName.indexOf(".");
                String type = originalFileName.substring(indexOfDot);
                if(!verifyFile( type, getData.length / 1024, fileInfo)){
                    return fileInfo;
                }
                String data = createFileDir();
                String savePath = path + File.separator + data;
                createDir(savePath);
                fileInfo.setFileName(originalFileName);
                String fileSave = savePath + File.separator + fileName + type;
                File file = new File(fileSave);
                fileInfo.setFileType(type.replace(".", ""));
                fileInfo.setPath(fileSave);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(getData);
                if (fos != null) {
                    fos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                fileInfo.setPath(fileSave);
                fileInfo.setFileSize((getData.length / 1024) + "kb");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    uploadFile.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileInfo;
    }

    /**
     * 按日期创建文件，方便日后操作
     * @return
     */
    private static String createFileDir() {
        // 可以对每个时间域单独修改
        Calendar c = Calendar.getInstance();
        String data = c.get(Calendar.YEAR) + "" + (c.get(Calendar.MONTH) + 1);
        return data;
    }

    /**
     * 创建文件目录
     *
     * @param destDirName
     * @return
     */
    private static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            return true;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = (new StringBuilder(String.valueOf(destDirName)))
                    .append(File.separator).toString();
        }
        return dir.mkdirs();
    }

    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    private static boolean isExceedFileSize(long file) {
        return file <= Long.parseLong(fileSize);
    }

    private static boolean isExceedFileType(String type) {
        return fileType.contains(type.trim().toLowerCase());
    }

    public String getFileSize() {
        return fileSize;
    }

    @Value("${uploadfile.size}")
    public void setFileSize(String fileSize) {
        FileUtil.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    @Value("${uploadfile.type}")
    public void setFileType(String fileType) {
        FileUtil.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    @Value("${uploadfile.path}")
    public void setFilePath(String filePath) {
        FileUtil.filePath = filePath;
    }
}

