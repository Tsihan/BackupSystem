package com.qihan.finalCloudDataSystem;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import javafx.scene.control.Label;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Get an object within an Amazon S3 bucket.
 * <p>
 * This code expects that you have AWS credentials set up per:
 * http://docs.aws.amazon.com/java-sdk/latest/developer-guide/setup-credentials.html
 */
public class GetObject {
    /**
     * 将云端的单个文件存储到本地的指定文件夹
     *
     * @param bucket_name
     * @param key_name
     * @param Download_Des
     */
    public static void getSingleObject(String bucket_name, String key_name, String Download_Des,String user_id,String s3_id) {
        final String USAGE = "\n" +
                "To run this example, supply the name of an S3 bucket and object to\n" +
                "download from it.\n" +
                "\n" +
                "Ex: GetObject <bucket-name> <filename> <Download-destination>\n";

        if (bucket_name == null || key_name == null || Download_Des == null) {
            System.out.println(USAGE);
            System.exit(1);
        }


        System.out.format("Downloading %s from S3 bucket %s...\n", key_name, bucket_name);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2).build();
        try {
            S3Object o = s3.getObject(bucket_name, key_name);
            S3ObjectInputStream s3is = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(Download_Des + "\\" + key_name);
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        String DOWNLOAD_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String sql = "Insert INTO `downloads` VALUES ('"+user_id+"','"+s3_id+"','" + DOWNLOAD_TIME + "')";
        ManipulateDownloadsRecord testdownload = new ManipulateDownloadsRecord();
        testdownload.update(sql);

        String s3_name = "user-" + user_id + "-bucket";
        String sql2 = "update `s3s` set s3_lastmodifiedtime ='"+DOWNLOAD_TIME+"' where s3_name = '"+s3_name+"'";
        testdownload.update(sql2);


        System.out.println("Done!");
    }


    public static void getSingleObject(String bucket_name, String key_name, String Download_Des, Label TextLabel,String user_id,String s3_id) {
        final String USAGE = "\n" +
                "To run this example, supply the name of an S3 bucket and object to\n" +
                "download from it.\n" +
                "\n" +
                "Ex: GetObject <bucket-name> <filename> <Download-destination>\n";

        if (bucket_name == null || key_name == null || Download_Des == null) {
            System.out.println(USAGE);
            System.exit(1);
        }


        System.out.format("Downloading %s from S3 bucket %s...\n", key_name, bucket_name);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2).build();
        try {
            S3Object o = s3.getObject(bucket_name, key_name);
            S3ObjectInputStream s3is = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(Download_Des + "\\" + key_name);
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            TextLabel.setText("Connection error!");
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            TextLabel.setText("Content not found!");
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            TextLabel.setText("IO error!");
            System.exit(1);
        }

        String DOWNLOAD_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String sql1 = "Insert INTO `downloads` VALUES ('"+user_id+"','"+s3_id+"','" + DOWNLOAD_TIME + "')";
        ManipulateDownloadsRecord testdownload = new ManipulateDownloadsRecord();
        testdownload.update(sql1);

        String s3_name = "user-" + user_id + "-bucket";
        String sql2 = "update `s3s` set s3_lastmodifiedtime ='"+DOWNLOAD_TIME+"' where s3_name = '"+s3_name+"'";
        testdownload.update(sql2);


        System.out.println("Done!");
    }

    public static void getAllObjects(String bucket_name, String Download_Des,String user_id,String s3_id) {
        final String USAGE = "\n" +
                "To run this example, supply the name of a bucket to list!\n" +
                "\n" +
                "Ex: ListObjects <bucket-name>\n";

        if (bucket_name == null) {
            System.out.println(USAGE);
            System.exit(1);
        }

        System.out.format("Objects in S3 bucket %s:\n", bucket_name);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2).build();
        ListObjectsV2Result result = s3.listObjectsV2(bucket_name);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        for (S3ObjectSummary os : objects) {
            getSingleObject(bucket_name, os.getKey(), Download_Des,user_id,s3_id);
        }
    }
}