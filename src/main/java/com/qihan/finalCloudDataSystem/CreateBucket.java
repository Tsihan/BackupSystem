package com.qihan.finalCloudDataSystem;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;

import java.util.List;
import java.util.Random;

/**
 * Create an Amazon S3 bucket.
 *
 * This code expects that you have AWS credentials set up per:
 * http://docs.aws.amazon.com/java-sdk/latest/developer-guide/setup-credentials.html
 */
public class CreateBucket {
    public static Bucket getBucket(String bucket_name) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2).build();
        Bucket named_bucket = null;
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket b : buckets) {
            if (b.getName().equals(bucket_name)) {
                named_bucket = b;
            }
        }
        return named_bucket;
    }

    public static Bucket createBucket(String bucket_name) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2).build();
        Bucket b = null;
        if (s3.doesBucketExistV2(bucket_name)) {
            System.out.format("Bucket %s already exists.\n", bucket_name);
            b = getBucket(bucket_name);
        } else {
            try {
                b = s3.createBucket(bucket_name);
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
        return b;
    }

    public static String getRandomString(int length) {

        Random random = new Random();

        StringBuffer valSb = new StringBuffer();

        String charStr = "0123456789abcdefghijklmnopqrstuvwxyz";

        int charLength = charStr.length();



        for (int i = 0; i < length; i++) {

            int index = random.nextInt(charLength);

            valSb.append(charStr.charAt(index));

        }

        return valSb.toString();

    }

    public static void createBucketAndSql(String user_name, String raw_password,String user_email) {
        final String USAGE = "\n" +
                "CreateBucket - create an S3 bucket\n\n" +
                "Usage: CreateBucket <bucketname>\n\n" +
                "Where:\n" +
                "  bucketname - the name of the bucket to create.\n\n" +
                "The bucket name must be unique, or an error will result.\n";

        if (user_name == null || raw_password == null || user_email == null) {
            System.out.println(USAGE);
            System.exit(1);
        }
        ManipulateUsersRecord testBuildUser = new ManipulateUsersRecord();
        ManipulateS3sRecord testBuildS3 = new ManipulateS3sRecord();
        String PASSWORD = ManipulateUsersRecord.buildMD5(raw_password);
        String user_id = getRandomString(6);
        String s3_name = "user-" + user_id + "-bucket";
        String sql1 = "Insert INTO `users` VALUES ('"+user_id+"','"+user_name+"','" + PASSWORD + "','"+user_email+"')";
        String sql2 = "Insert INTO `s3s` VALUES ('"+s3_name+"',null,'"+user_id+"')";

        testBuildUser.update(sql1);
        testBuildS3.update(sql2);

        System.out.format("\nCreating S3 bucket: %s\n", s3_name);
        Bucket b = createBucket(s3_name);


        if (b == null) {
            System.out.println("Error creating bucket!\n");
        } else {
            System.out.println("Done!\n");
        }
    }
}
