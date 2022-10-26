package com.qihan.finalTestCode.CloudDataSystem;

import com.qihan.finalCloudDataSystem.S3Encrypt;


public class DemoForS3Encrypt {
    public static void main(String[] args) {
        S3Encrypt.encrypt("user-str245-bucket", "enc-key", "some-key"
                , "C:\\Users\\24539\\Desktop\\计算机硕士--选校表--终稿.xlsx");
    }
}
