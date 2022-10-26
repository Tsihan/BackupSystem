package com.qihan.finalTestCode.CloudDataSystem;


import com.qihan.finalCloudDataSystem.PutObject;


import java.io.IOException;


public class DemoForPutObject {

    public static void main(String[] args) throws IOException {
        PutObject.selectSaveMeta = true;
        PutObject.putSingObject("user-str245-bucket","C:\\Users\\24539\\Desktop\\计算机硕士--选校表--终稿.xlsx");
    }

}
