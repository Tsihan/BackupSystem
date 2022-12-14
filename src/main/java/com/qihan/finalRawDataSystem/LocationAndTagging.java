package com.qihan.finalRawDataSystem;

import cn.hutool.core.io.FileTypeUtil;

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

    public void deleteDir(String path) {
        File file = new File(path);


        String[] content = file.list();//取得当前目录下所有文件和文件夹
        assert content != null;
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
            switch (Type) {
                case "jpg":
                case "png":
                case "gif":
                case "tif":
                case "bmp":
                case "dwg":
                case "psd":
                    this.graphFiles.add(newFile.toString());
                    break;
                case "txt":
                case "rtf":
                case "ini":
                case "log":
                    this.textFiles.add(newFile.toString());
                    break;
                case "mp3":
                case "wma":
                case "wav":
                    this.soundFiles.add(newFile.toString());
                    break;
                case "mp4":
                case "rmvb":
                case "avi":
                    this.videoFiles.add(newFile.toString());
                    break;
                default:
                    this.otherFiles.add(newFile.toString());
                    break;
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
            File[] tempList = newFile.listFiles();

            assert tempList != null;
            for (File value : tempList) {
                if (value.isFile()) {
                    String fileSize;
                    String Type = FileTypeUtil.getType(value);
                    switch (Type) {
                        case "jpg":
                        case "png":
                        case "gif":
                        case "tif":
                        case "bmp":
                        case "dwg":
                        case "psd":
                            this.graphFiles.add(value.toString());
                            break;
                        case "txt":
                        case "rtf":
                        case "ini":
                        case "log":
                            this.textFiles.add(value.toString());
                            break;
                        case "mp3":
                        case "wma":
                        case "wav":
                            this.soundFiles.add(value.toString());
                            break;
                        case "mp4":
                        case "rmvb":
                        case "avi":
                            this.videoFiles.add(value.toString());
                            break;
                        default:
                            this.otherFiles.add(value.toString());
                            break;
                    }

                    if (value.length() < 1024) {
                        fileSize = value.length() + "B";
                    } else if (value.length() < 1024 * 1024) {
                        fileSize = value.length() / 1024 + "KB";
                    } else if (value.length() < 1024 * 1024 * 1024) {
                        fileSize = value.length() / 1024 / 1024 + "MB";
                    } else if (value.length() / 1024 / 1024 / 1024 < 4) {
                        fileSize = value.length() / 1024 / 1024 / 1024 + "GB";
                    } else {
                        throw new InvalidParameterException("The single file exceeds the size bound!");
                    }
                    wholeSizeBytes += value.length();

                    Date lastModified = new Date(newFile.lastModified());
                    System.out.println("File：" + value + " The last modified date：" + lastModified + " The type of the file："
                            + Type + " The size of the file：" + fileSize);
                    this.Files.add(value.toString());
                }
                if (value.isDirectory()) {
                    System.out.println("Directory：" + value);
                    this.provideInfo(String.valueOf(value));

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
