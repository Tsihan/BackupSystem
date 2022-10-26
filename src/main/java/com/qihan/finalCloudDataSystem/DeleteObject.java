package com.qihan.finalCloudDataSystem;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Delete an object from an Amazon S3 bucket.
 * <p>
 * This code expects that you have AWS credentials set up per:
 * http://docs.aws.amazon.com/java-sdk/latest/developer-guide/setup-credentials.html
 * <p>
 * ++ Warning ++ This code will actually delete the object that you specify!
 */
public class DeleteObject {
    public static void deleteSingleObject(String bucket_name, String object_key) {
        final String USAGE = "\n" +
                "To run this example, supply the name of an S3 bucket and object\n" +
                "name (key) to delete.\n" +
                "\n" +
                "Ex: DeleteObject <bucketname> <objectname>\n";

        if (bucket_name == null || object_key == null) {
            System.out.println(USAGE);
            System.exit(1);
        }


        System.out.format("Deleting object %s from S3 bucket: %s\n", object_key,
                bucket_name);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2).build();
        try {
            s3.deleteObject(bucket_name, object_key);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        System.out.println("Done!");
    }
}