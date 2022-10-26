package com.qihan.finalTestCode.CloudDataSystem;

import com.qihan.finalCloudDataSystem.PutObjects;
import com.qihan.finalRawDataSystem.Compression;
import com.qihan.finalRawDataSystem.Filtering;
import com.qihan.finalRawDataSystem.LocationAndTagging;

import java.io.IOException;


public class DemoForPutObjects {
    public static void main(String[] args) throws IOException {
        PutObjects.selectSaveMeta = true;
        LocationAndTagging test = new LocationAndTagging();
        test.provideInfo("E:\\美研申请&托福GRE\\托福参考资料\\冲刺班\\口语作业\\day4已看");
        Filtering test2 = new Filtering();
        //上传该目录下所有pdf文件
        test2.siftfilesByRE(test.Files, "pdf",false);
        Compression test3 = new Compression();
        test3.setSelectCompression(true);
        test3.setFilesAfterSift(test2.filesAfterSift);

        PutObjects.putObjects(test3.filesAfterSift,"user-str245-bucket");




    }
}
