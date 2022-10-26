package com.qihan.finalCloudDataSystem;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.AmazonS3EncryptionClientBuilder;
import com.amazonaws.services.s3.model.*;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
// snippet-end:[s3.java1.s3_encrypt.import]

/**
 * This AWS Code Example is Depreciated. To encrypt S3 content, see the S3EncryptV2 example.
 */
public class S3Encrypt {


    public static void encrypt(String BUCKET_NAME, String ENCRYPTED_KEY, String NON_ENCRYPTED_KEY,String object) {
        System.out.println("calling encryption with customer managed keys");
        S3Encrypt encrypt = new S3Encrypt();

        try {
            //can change to call the other encryption methods
            encrypt.authenticatedEncryption_CustomerManagedKey(BUCKET_NAME, ENCRYPTED_KEY, NON_ENCRYPTED_KEY,object);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Uses AES/GCM with AESWrap key wrapping to encrypt the key. Uses v2 metadata schema. Note that authenticated
     * encryption requires the bouncy castle provider to be on the classpath. Also, for authenticated encryption the size
     * of the data can be no longer than 64 GB.
     */
    // snippet-start:[s3.java1.s3_encrypt.authenticated_encryption]
    public void authenticatedEncryption_CustomerManagedKey(String BUCKET_NAME, String ENCRYPTED_KEY, String NON_ENCRYPTED_KEY,String object) throws NoSuchAlgorithmException {
        // snippet-start:[s3.java1.s3_encrypt.authenticated_encryption_build]

        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
        AmazonS3Encryption s3Encryption = AmazonS3EncryptionClientBuilder
                .standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCryptoConfiguration(new CryptoConfiguration(CryptoMode.AuthenticatedEncryption))
                .withEncryptionMaterials(new StaticEncryptionMaterialsProvider(new EncryptionMaterials(secretKey)))
                .build();

        AmazonS3 s3NonEncrypt = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2).build();
        // snippet-end:[s3.java1.s3_encrypt.authenticated_encryption_build]

        s3Encryption.putObject(BUCKET_NAME, ENCRYPTED_KEY, object);
        s3NonEncrypt.putObject(BUCKET_NAME, NON_ENCRYPTED_KEY, object);
        //System.out.println(s3Encryption.getObjectAsString(BUCKET_NAME, ENCRYPTED_KEY));
        // System.out.println(s3Encryption.getObjectAsString(BUCKET_NAME, NON_ENCRYPTED_KEY));
    }
    // snippet-end:[s3.java1.s3_encrypt.authenticated_encryption]

    /**
     * For ranged GET we do not use authenticated encryption since we aren't reading the entire message and can't produce the
     * MAC. Instead we use AES/CTR, an unauthenticated encryption algorithm. If {@link CryptoMode#StrictAuthenticatedEncryption}
     * is enabled, ranged GETs will not be allowed since they do not use authenticated encryption..
     */



}
