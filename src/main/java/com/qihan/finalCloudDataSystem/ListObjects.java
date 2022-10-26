package com.qihan.finalCloudDataSystem;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import javafx.scene.control.TextArea;

import java.util.List;

/**
 * List objects within an Amazon S3 bucket.
 *
 * This code expects that you have AWS credentials set up per:
 * http://docs.aws.amazon.com/java-sdk/latest/developer-guide/setup-credentials.html
 */
public class ListObjects {
    /**
     * 递归展示存储桶中的全部对象
     * @param bucket_name
     */
    public static void listAllObjects(String bucket_name) {
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
            System.out.println("* " + os.getKey());
        }
    }

    public static void listAllObjects(String bucket_name, TextArea s3ContentsTextArea) {
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
        String finalResult = "The contents are: \n";
        for (S3ObjectSummary os : objects) {
            finalResult = finalResult.concat("* " + os.getKey() + "\n");


        }
        s3ContentsTextArea.setText(finalResult);
    }
}