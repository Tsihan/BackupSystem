package com.qihan.finalCloudDataSystem;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Upload a file to an Amazon S3 bucket.
 * 必须是单个文件而不是目录
 * <p>
 * This code expects that you have AWS credentials set up per:
 * http://docs.aws.amazon.com/java-sdk/latest/developer-guide/setup-credentials.html
 */
public class PutObject {

    public static boolean selectSaveMeta = false;

    public static void putSingObject(String bucket_name, String file_path) throws IOException {
        final String USAGE = "\n" +
                "To run this example, supply the name of an S3 bucket and a file to\n" +
                "upload to it.\n" +
                "\n" +
                "Ex: PutObject <bucket_name> <file_path>\n";

        if (bucket_name == null || file_path == null) {
            System.out.println(USAGE);
            System.exit(1);
        }
        String UPLOAD_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String user_id = bucket_name.substring(5, 11);




        //这里主要写元数据
        BasicFileAttributes attr = Files.readAttributes(Path.of(file_path), BasicFileAttributes.class);
        String file_path_meta = file_path + ".Meta";
        if (selectSaveMeta) {
            FileWriter writeMeta = new FileWriter(file_path_meta);
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
        //增加meta
        String key_name_meta = Paths.get(file_path_meta).getFileName().toString();
        String key_name = Paths.get(file_path).getFileName().toString();

        System.out.format("Uploading %s to S3 bucket %s...\n", file_path, bucket_name);
        if (selectSaveMeta) {
            System.out.format("Uploading %s meta data to S3 bucket %s...\n", file_path_meta, bucket_name);
        }
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2).build();
        try {
            s3.putObject(bucket_name, key_name, new File(file_path));
            if (selectSaveMeta) {
                s3.putObject(bucket_name, key_name_meta, new File(file_path_meta));
                File tempFile = new File(file_path_meta);
                tempFile.delete();
            }

        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

        String sql1 = "Insert INTO `uploads` VALUES ('" + user_id + "','" + bucket_name + "','" + UPLOAD_TIME + "')";
        ManipulateUploadsRecord updateUploadsRecord = new ManipulateUploadsRecord();
        updateUploadsRecord.update(sql1);

        String s3_name = "user-" + user_id + "-bucket";
        String sql2 = "update `s3s` set s3_lastmodifiedtime ='"+UPLOAD_TIME+"' where s3_name = '"+s3_name+"'";
        updateUploadsRecord.update(sql2);
        System.out.println("Done!");

    }
}


