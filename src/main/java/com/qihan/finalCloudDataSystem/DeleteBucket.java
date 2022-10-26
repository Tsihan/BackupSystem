package com.qihan.finalCloudDataSystem;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.util.Iterator;

/**
 * Delete an Amazon S3 bucket.
 * <p>
 * This code expects that you have AWS credentials set up per:
 * http://docs.aws.amazon.com/java-sdk/latest/developer-guide/setup-credentials.html
 * <p>
 * ++ Warning ++ This code will actually delete the bucket that you specify, as
 * well as any objects within it!
 */
public class DeleteBucket {
    public static void deleteBucketAndSql(String user_id) {

        final String USAGE = "\n" +
                "To run this example, supply the name of an S3 bucket\n" +
                "\n" +
                "Ex: DeleteBucket <user_id>\n";

        if (user_id == null) {
            System.out.println(USAGE);
            System.exit(1);
        }

        ManipulateS3sRecord deleteS3Record = new ManipulateS3sRecord();
        //有级联约束，注意顺序

        ManipulateDownloadsRecord deleteDownloadRecord = new ManipulateDownloadsRecord();
        String sql3 = "Delete FROM `downloads` WHERE download_userid = '" + user_id + "'";
        deleteDownloadRecord.update(sql3);

        ManipulateUploadsRecord deleteUploadRecord = new ManipulateUploadsRecord();
        String sql4 = "Delete FROM `uploads` WHERE upload_userid = '" + user_id + "'";
        deleteUploadRecord.update(sql4);

        ManipulateTransfersRecord deleteTransferRecord = new ManipulateTransfersRecord();
        String sql5 = "Delete FROM `transfers` WHERE cloud_srcuserid = '" + user_id + "'";
        deleteTransferRecord.update(sql5);


        String s3_name = "user-" + user_id + "-bucket";
        String sql1 = "Delete FROM `s3s` WHERE s3_name = '" + s3_name + "'";
        deleteS3Record.update(sql1);

        ManipulateUsersRecord deleteUserRecord = new ManipulateUsersRecord();
        String sql2 = "Delete FROM `users` WHERE user_id = '" + user_id + "'";
        deleteUserRecord.update(sql2);


        System.out.println("Deleting S3 bucket: " + s3_name);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2).build();
        try {
            System.out.println(" - removing objects from bucket");
            ObjectListing object_listing = s3.listObjects(s3_name);
            while (true) {
                for (Iterator<?> iterator =
                     object_listing.getObjectSummaries().iterator();
                     iterator.hasNext(); ) {
                    S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
                    s3.deleteObject(s3_name, summary.getKey());
                }

                // more object_listing to retrieve?
                if (object_listing.isTruncated()) {
                    object_listing = s3.listNextBatchOfObjects(object_listing);
                } else {
                    break;
                }
            }

            System.out.println(" - removing versions from bucket");
            VersionListing version_listing = s3.listVersions(
                    new ListVersionsRequest().withBucketName(s3_name));
            while (true) {
                for (Iterator<?> iterator =
                     version_listing.getVersionSummaries().iterator();
                     iterator.hasNext(); ) {
                    S3VersionSummary vs = (S3VersionSummary) iterator.next();
                    s3.deleteVersion(
                            s3_name, vs.getKey(), vs.getVersionId());
                }

                if (version_listing.isTruncated()) {
                    version_listing = s3.listNextBatchOfVersions(
                            version_listing);
                } else {
                    break;
                }
            }

            System.out.println(" OK, bucket ready to delete!");
            s3.deleteBucket(s3_name);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        System.out.println("Done!");
    }
}