package com.qihan.finalCloudDataSystem;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copy an object from one Amazon S3 bucket to another.
 * <p>
 * This code expects that you have AWS credentials set up per:
 * http://docs.aws.amazon.com/java-sdk/latest/developer-guide/setup-credentials.html
 */


public class CopyObject {
    /**
     * 将一个存储桶中的特定一个对象拷贝到另一个存储桶
     *
     * @param object_key
     * @param from_bucket
     * @param to_bucket
     */
    public static boolean copy(String object_key, String from_bucket, String to_bucket, Label TextLabel) {

        final String USAGE = "\n" +
                "To run this example, supply the name (key) of an S3 object, the bucket name\n" +
                "that it's contained within, and the bucket to copy it to.\n" +
                "\n" +
                "Ex: CopyObject <objectname> <frombucket> <tobucket>\n";
        if (object_key == null || from_bucket == null || to_bucket == null) {
            System.out.println(USAGE);
            System.exit(1);
        }


        System.out.format("Copying object %s from bucket %s to %s\n",
                object_key, from_bucket, to_bucket);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2).build();
        try {
            s3.copyObject(from_bucket, object_key, to_bucket, object_key);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
           // System.exit(1);
            TextLabel.setText("Either the use_id or the content doesn't exist!");
            return false;
        }

        ManipulateTransfersRecord testtransfer = new ManipulateTransfersRecord();
        String CLOUD_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String from_userid = from_bucket.substring(5, 11);
        String to_userid = to_bucket.substring(5, 11);
        String sql1 = "Insert INTO `transfers` VALUES ('" + from_userid + "','" + to_userid + "','" + from_bucket + "','"
                + CLOUD_TIME + "')";
        testtransfer.update(sql1);

        String TRANSFER_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String sql2 = "update `s3s` set s3_lastmodifiedtime ='" + TRANSFER_TIME + "' where s3_name = '" + from_bucket + "'";
        testtransfer.update(sql2);

        String sql3 = "update `s3s` set s3_lastmodifiedtime ='" + TRANSFER_TIME + "' where s3_name = '" + to_bucket + "'";
        testtransfer.update(sql3);
        System.out.println("Done!");
        return true;
    }
}