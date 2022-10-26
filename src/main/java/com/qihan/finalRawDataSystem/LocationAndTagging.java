package com.qihan.finalRawDataSystem;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;

public class LocationAndTagging {
    public ArrayList<String> textFiles = new ArrayList<String>();
    public ArrayList<String> graphFiles = new ArrayList<String>();
    public ArrayList<String> soundFiles = new ArrayList<String>();
    public ArrayList<String> videoFiles = new ArrayList<String>();
    public ArrayList<String> otherFiles = new ArrayList<String>();
    public ArrayList<String> Files = new ArrayList<String>();

    public void  deleteDir(String path) {
        File file = new File(path);


        String[] content = file.list();//取得当前目录下所有文件和文件夹
        for (String name : content) {
            File temp = new File(path, name);
            if (temp.isDirectory()) {//判断是否是目录
                deleteDir(temp.getAbsolutePath());//递归调用，删除目录里的内容
                temp.delete();//删除空目录
            } else {
                if (!temp.delete()) {//直接删除文件
                    System.err.println("Failed to delete " + name);
                }
            }
        }


    }


    /**
     * 用户输入一：指定一个目录或一(多)个文件
     * 展示指定目录下所有子文件、子目录信息
     *
     * @param path 用户给定的目录
     * @throws IOException
     */
    public void provideInfo(String path) throws IOException {
        File newFile = new File(path);

        long wholeSizeBytes = 0;
        String wholeSize;

        if (newFile.isFile()) {

            String fileSize;
            String Type = FileTypeUtil.getType(newFile);
            if (Type.equals("jpg") || Type.equals("png") || Type.equals("gif") || Type.equals("tif") || Type.equals("bmp")
                    || Type.equals("dwg") || Type.equals("psd")) {
                this.graphFiles.add(newFile.toString());
            } else if (Type.equals("txt") || Type.equals("rtf") || Type.equals("ini") || Type.equals("log")) {
                this.textFiles.add(newFile.toString());
            } else if (Type.equals("mp3") || Type.equals("wma") || Type.equals("wav")) {
                this.soundFiles.add(newFile.toString());
            } else if (Type.equals("mp4") || Type.equals("rmvb") || Type.equals("avi")) {
                this.videoFiles.add(newFile.toString());
            } else {
                this.otherFiles.add(newFile.toString());
            }


            if (newFile.length() < 1024) {
                fileSize = newFile.length() + "B";
            } else if (newFile.length() < 1024 * 1024) {
                fileSize = newFile.length() / 1024 + "KB";
            } else if (newFile.length() < 1024 * 1024 * 1024) {
                fileSize = newFile.length() / 1024 / 1024 + "MB";
            } else if (newFile.length() / 1024 / 1024 / 1024 < 4) {
                fileSize = newFile.length() / 1024 / 1024 / 1024 + "GB";
            } else {
                throw new InvalidParameterException("The single file exceeds the size bound!");
            }
            wholeSizeBytes += newFile.length();

            Date lastModified = new Date(newFile.lastModified());
            System.out.println("File：" + newFile + " The last modified date：" + lastModified + " The type of the file："
                    + Type + " The size of the file：" + fileSize);
            this.Files.add(newFile.toString());

        } else {
            //是目录
            File file = FileUtil.file(path);
            File[] tempList = file.listFiles();


            for (int i = 0; i < tempList.length; i++) {
                if (tempList[i].isFile()) {
                    String fileSize;
                    String Type = FileTypeUtil.getType(tempList[i]);
                    if (Type.equals("jpg") || Type.equals("png") || Type.equals("gif") || Type.equals("tif") || Type.equals("bmp")
                            || Type.equals("dwg") || Type.equals("psd")) {
                        this.graphFiles.add(tempList[i].toString());
                    } else if (Type.equals("txt") || Type.equals("rtf") || Type.equals("ini") || Type.equals("log")) {
                        this.textFiles.add(tempList[i].toString());
                    } else if (Type.equals("mp3") || Type.equals("wma") || Type.equals("wav")) {
                        this.soundFiles.add(tempList[i].toString());
                    } else if (Type.equals("mp4") || Type.equals("rmvb") || Type.equals("avi")) {
                        this.videoFiles.add(tempList[i].toString());
                    } else {
                        this.otherFiles.add(tempList[i].toString());
                    }


                    if (tempList[i].length() < 1024) {
                        fileSize = tempList[i].length() + "B";
                    } else if (tempList[i].length() < 1024 * 1024) {
                        fileSize = tempList[i].length() / 1024 + "KB";
                    } else if (tempList[i].length() < 1024 * 1024 * 1024) {
                        fileSize = tempList[i].length() / 1024 / 1024 + "MB";
                    } else if (tempList[i].length() / 1024 / 1024 / 1024 < 4) {
                        fileSize = tempList[i].length() / 1024 / 1024 / 1024 + "GB";
                    } else {
                        throw new InvalidParameterException("The single file exceeds the size bound!");
                    }
                    wholeSizeBytes += tempList[i].length();

                    Date lastModified = new Date(file.lastModified());
                    System.out.println("File：" + tempList[i] + " The last modified date：" + lastModified + " The type of the file："
                            + Type + " The size of the file：" + fileSize);
                    this.Files.add(tempList[i].toString());
                }
                if (tempList[i].isDirectory()) {
                    System.out.println("Directory：" + tempList[i]);
                    this.provideInfo(String.valueOf(tempList[i]));

                }
            }

            if (wholeSizeBytes < 1024) {
                wholeSize = wholeSizeBytes + "B";
            } else if (wholeSizeBytes < 1024 * 1024) {
                wholeSize = wholeSizeBytes / 1024 + "KB";
            } else if (wholeSizeBytes < 1024 * 1024 * 1024) {
                wholeSize = wholeSizeBytes / 1024 / 1024 + "MB";
            } else if (wholeSizeBytes / 1024 / 1024 / 1024 < 4) {
                wholeSize = wholeSizeBytes / 1024 / 1024 / 1024 + "GB";
            } else {
                throw new InvalidParameterException("The whole files exceed the size bound 4 GB!");
            }
            System.out.println("The whole size is: " + wholeSize);
        }


    }
}
