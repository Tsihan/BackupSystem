package com.qihan.finalRawDataSystem;

import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compression {
    //作为输入
    public ArrayList<String> filesAfterSift;
    //用户也可以不选择将筛选后的文件压缩到一个zip文件夹
    //用户输入三：是否选择压缩成一个zip
    boolean selectCompression = false;
    //用户输入四：是否选择压缩成一个zip用户自行决定是否保持压缩后的目录结构
    boolean keepDirStructure = false;

    public ArrayList<String> getFilesAfterSift() {
        return filesAfterSift;
    }

    public void setFilesAfterSift(ArrayList<String> filesAfterSift) {
        this.filesAfterSift = filesAfterSift;
    }

    public boolean isKeepDirStructure() {
        return keepDirStructure;
    }

    public void setKeepDirStructure(boolean keepDirStructure) {
        this.keepDirStructure = keepDirStructure;
    }

    public boolean isSelectCompression() {
        return selectCompression;
    }

    public void setSelectCompression(boolean selectCompression) {
        this.selectCompression = selectCompression;
    }

    /**
     * 将筛选之后的全部文件打包压缩到一个固定的zip文件夹
     *
     * @param zipFilePath 需要以“XXX.zip”结尾
     * @throws IOException
     */
    public void compressData(String zipFilePath) throws IOException {
        byte[] buf = new byte[1024];
        File zipFile = new File(zipFilePath);
        //zip文件不存在，则创建文件，用于压缩
        if (!zipFile.exists())
            zipFile.createNewFile();

        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            for (String relativePath : filesAfterSift) {
                if (StringUtils.isEmpty(relativePath)) {
                    continue;
                }
                File sourceFile = new File(relativePath);//绝对路径找到file
                if (!sourceFile.exists()) {
                    continue;
                }
                FileInputStream fis = new FileInputStream(sourceFile);
                if (this.keepDirStructure) {
                    //保持目录结构
                    zos.putNextEntry(new ZipEntry(relativePath));
                } else {
                    //直接放到压缩包的根目录
                    zos.putNextEntry(new ZipEntry(sourceFile.getName()));
                }
                //System.out.println("压缩当前文件："+sourceFile.getName());
                int len;
                while ((len = fis.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
                zos.flush();
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            //System.out.println("压缩完成");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
