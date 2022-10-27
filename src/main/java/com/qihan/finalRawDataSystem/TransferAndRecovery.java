package com.qihan.finalRawDataSystem;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.util.ArrayList;

public class TransferAndRecovery {


    public int getSecret() {
        return secret;
    }

    public void setSecret(int secret) {
        this.secret = secret;
    }

    /**
     * 用于文件内容加密的密钥  由于是用户指定需要修改为输入
     */
    public int secret;
    //用户输入五：是否选择文件加密
    public boolean SelectEncryption = true;
    //用户输入六：是否选择对指定文件解密
    public boolean SelectDecryption = true;
    //作为临时的arraylist存储加密后的零时文件的地址
    public ArrayList<String> tempEncryptedFiles = new ArrayList<String>();


    public boolean isSelectEncryption() {
        return SelectEncryption;
    }

    public void setSelectEncryption(boolean selectEncryption) {
        SelectEncryption = selectEncryption;
    }

    public boolean isSelectDecryption() {
        return SelectDecryption;
    }

    public void setSelectDecryption(boolean selectDecryption) {
        SelectDecryption = selectDecryption;
    }

    public boolean isSelectSaveMeta() {
        return selectSaveMeta;
    }

    public void setSelectSaveMeta(boolean selectSaveMeta) {
        this.selectSaveMeta = selectSaveMeta;
    }

    //用户输入七：是否选择保存文件元数据
    public boolean selectSaveMeta = false;


    //作为输入，不包含压缩过程
    ArrayList<String> filesAfterSift = new ArrayList<>();
    //作为输入，包含压缩过程
    String SrcZipFilePath = null;


    //存储新的移动后的文件的位置
    //切记这种写法，要新建一个对象!
    ArrayList<String> futureFilesPositions = new ArrayList<>();


    public ArrayList<String> getFutureFilesPositions() {
        return futureFilesPositions;
    }

    public void setFutureFilesPositions(ArrayList<String> futureFilesPositions) {
        this.futureFilesPositions = futureFilesPositions;
    }


    public ArrayList<String> getFilesAfterSift() {
        return filesAfterSift;
    }

    public void setFilesAfterSift(ArrayList<String> filesAfterSift) {
        this.filesAfterSift = filesAfterSift;
    }

    public String getSrcZipFilePath() {
        return SrcZipFilePath;
    }

    public void setSrcZipFilePath(String srcZipFilePath) {
        SrcZipFilePath = srcZipFilePath;
    }


    public void addMetaData(String DesDirectory) {
        if (selectSaveMeta) {
            for (int i = 0; i < filesAfterSift.size(); i++) {
                try {

                    File oldFile = new File(filesAfterSift.get(i));
                    Path oldFilePath = Path.of(filesAfterSift.get(i));
                    if (oldFile.exists()) { //文件存在时
                        BasicFileAttributes attr = Files.readAttributes(oldFilePath, BasicFileAttributes.class);
                        if (selectSaveMeta) {
                            System.out.println("creationTime: " + attr.creationTime());
                            System.out.println("lastAccessTime: " + attr.lastAccessTime());
                            System.out.println("lastModifiedTime: " + attr.lastModifiedTime());
                            System.out.println("isDirectory: " + attr.isDirectory());
                            System.out.println("isOther: " + attr.isOther());
                            System.out.println("isRegularFile: " + attr.isRegularFile());
                            System.out.println("isSymbolicLink: " + attr.isSymbolicLink());
                            System.out.println("size: " + attr.size());

                            FileWriter writeMeta = new FileWriter(DesDirectory + "\\" + oldFile.getName() + ".Meta");
                            writeMeta.write("creationTime: " + attr.creationTime() + "\n");
                            writeMeta.write("lastAccessTime: " + attr.lastAccessTime() + "\n");
                            writeMeta.write("lastModifiedTime: " + attr.lastModifiedTime() + "\n");
                            writeMeta.write("isDirectory: " + attr.isDirectory() + "\n");
                            writeMeta.write("isOther: " + attr.isOther() + "\n");
                            writeMeta.write("isRegularFile: " + attr.isRegularFile() + "\n");
                            writeMeta.write("isSymbolicLink: " + attr.isSymbolicLink() + "\n");
                            writeMeta.write("size: " + attr.size() + "\n");
                            writeMeta.flush();
                            writeMeta.close();
                        }

                    }
                } catch (Exception e) {
                    System.out.println("When add the metadata of single file: " + filesAfterSift.get(i) + ", an error occurs!");
                    e.printStackTrace();

                }
            }
        } else {
            return;
        }


    }

    /**
     * 使用者给出目的目录，将现有的文件拷贝至目的目录
     *
     * @param DesDirectory
     */
    public void Copy(String DesDirectory) {
        for (int i = 0; i < filesAfterSift.size(); i++) {
            try {
                int bytesum = 0;
                int byteread = 0;
                File oldFile = new File(filesAfterSift.get(i));
                Path oldFilePath = Path.of(filesAfterSift.get(i));
                if (oldFile.exists()) { //文件存在时

                    InputStream inStream = new FileInputStream(filesAfterSift.get(i)); //读入原文件

                    FileOutputStream fs = new FileOutputStream(DesDirectory + "\\" + oldFile.getName());
                    //测试输出
                    System.out.println(DesDirectory + "\\" + oldFile.getName());
                    this.futureFilesPositions.add(DesDirectory + "\\" + oldFile.getName());
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((byteread = inStream.read(buffer)) != -1) {
                        bytesum += byteread; //字节数 文件大小

                        fs.write(buffer, 0, byteread);
                    }
                    //记得缓冲区以及关闭文件
                    fs.flush();
                    fs.close();

                    inStream.close();
                    System.out.println("copy file: " + fs + " successfully!");
                }
            } catch (Exception e) {
                System.out.println("When copy the single file: " + filesAfterSift.get(i) + ", an error occurs!");
                e.printStackTrace();

            }
        }

    }

    /**
     * 使用者给出目的目录，将现有的文件剪切过去
     *
     * @param DesDirectory
     */
    public void Transfer(String DesDirectory) throws IOException {
        for (int i = 0; i < filesAfterSift.size(); i++) {
            File oldFile = new File(filesAfterSift.get(i));
            Path oldFilePath = Path.of(filesAfterSift.get(i));

            File tempFile = new File(filesAfterSift.get(i));
            File newFile = new File(DesDirectory + "\\" + tempFile.getName());
            this.futureFilesPositions.add(DesDirectory + "\\" + tempFile.getName());
            if (tempFile.renameTo(newFile)) {
                System.out.println("The file: " + filesAfterSift.get(i) + " has been moved!");
            } else {
                System.out.println("The file: " + filesAfterSift.get(i) + " moved failed!");
            }
        }
    }

    /**
     * 将压缩之后的文件存放在指定位置，原先存放压缩文件的位置是暂时的，故是剪切操作
     *
     * @param DesDirectory
     */
    public void TransferZip(String DesDirectory) throws IOException {
        if (SrcZipFilePath != null) {

            File tempFile = new File(SrcZipFilePath);
            File newFile = new File(DesDirectory + "\\" + tempFile.getName());
            if (tempFile.renameTo(newFile)) {
                System.out.println("The file: " + SrcZipFilePath + " has been moved!");
            } else {
                System.out.println("The file: " + SrcZipFilePath + " moved failed!");
            }
        }


    }

    /**
     * 根据SHA1算法判断两个文件是否完全一致，用于备份验证功能
     *
     * @param file
     * @return
     */
    public String getFileSha1(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断两个文件的内容是否一致
     *
     * @param des
     * @param src
     * @return
     */
    public Boolean passIdentification(String des, String src) {
        File Des = new File(des);
        File Src = new File(src);
        if (getFileMD5(Des).equals(getFileMD5(Src))) {
            return true;
        }
        return false;
    }

    /**
     * 用于文件的加密。是内容层面的加密 需要写全路径名以及文件名
     *
     * @param
     * @param DesDirectory
     * @throws IOException
     */
    public void encryption(String DesDirectory) throws IOException {
        if (!SelectEncryption) {
            //不选择加密则直接跳出
            tempEncryptedFiles = filesAfterSift;
            return;
        }

        for (String singleFile : filesAfterSift) {
            File temp = new File(singleFile);
            tempEncryptedFiles.add(DesDirectory + "\\" + temp.getName());

            // System.out.println(DesDirectory +"\\"+temp.getName());

            InputStream in = new BufferedInputStream(new FileInputStream(singleFile));
            OutputStream out = new BufferedOutputStream(new FileOutputStream(DesDirectory + "\\" + temp.getName()));
            int data = -1;
            while ((data = in.read()) > -1) {
                out.write(data + secret);
            }
            out.flush();
            out.close();
            in.close();
        }


    }

    /**
     * 用于加密文件的解密 需要写全路径名以及文件名
     *
     * @param src
     * @param target
     * @throws IOException
     */
    public void decryption(File src, File target) throws IOException {
        if (!SelectDecryption) {
            //不选择解密则直接跳出
            return;
        }


        InputStream in = new BufferedInputStream(new FileInputStream(src));
        OutputStream out = new BufferedOutputStream(new FileOutputStream(target));
        int data = -1;
        while ((data = in.read()) > -1) {
            out.write(data - secret);
        }
        out.flush();
        out.close();
        in.close();
    }
}
