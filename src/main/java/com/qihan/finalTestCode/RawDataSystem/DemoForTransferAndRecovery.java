package com.qihan.finalTestCode.RawDataSystem;

import com.qihan.finalRawDataSystem.Compression;
import com.qihan.finalRawDataSystem.Filtering;
import com.qihan.finalRawDataSystem.LocationAndTagging;
import com.qihan.finalRawDataSystem.TransferAndRecovery;

import java.io.File;

public class DemoForTransferAndRecovery {
    public static void main(String[] args) throws Exception {
        TransferAndRecovery test = new TransferAndRecovery();
        //test.encryption(new File("E:\\科研训练\\测试.zip"), new File("E:\\科研训练\\暑假内容\\测试.zip"));
//        test.decryption(new File("E:\\科研训练\\暑假内容\\测试.zip"), new File("E:\\科研训练\\测试.zip"));

//        LocationAndTagging test = new LocationAndTagging();
//        test.provideInfo("E:\\科研训练\\基础科研训练相关文档");
//        Filtering test2 = new Filtering();
//        test2.siftfilesByRE(test.Files, "xlsx");
//        Compression test3 = new Compression();
//        test3.setSelectCompression(true);
//        test3.setFilesAfterSift(test2.filesAfterSift);
//        TransferAndRecovery test1 = new TransferAndRecovery();
//        test1.selectSaveMeta = true;
//        test1.setFilesAfterSift(test3.filesAfterSift);
//        test1.Copy("E:\\大四优班答辩");
//        LocationAndTagging test = new LocationAndTagging();
//        test.provideInfo("E:\\科研训练\\基础科研训练相关文档");
//        Filtering test2 = new Filtering();
//        test2.siftfilesByRE(test.Files, "xlsx");
//        Compression test3 = new Compression();
//        test3.setSelectCompression(true);
//        test3.setFilesAfterSift(test2.filesAfterSift);
//        test3.compressData("E:\\科研训练\\基础科研训练相关文档\\test.zip");
//
//        TransferAndRecovery test4 = new TransferAndRecovery();
//
//        test4.setSrcZipFilePath("E:\\科研训练\\基础科研训练相关文档\\test.zip");
//        test4.setFilesAfterSift(test3.filesAfterSift);
//        test4.TransferZip("E:\\科研训练\\test");
//        TransferAndRecovery.encryption(new File("C:\\Users\\24539\\Desktop\\计算机硕士--选校表--终稿.xlsx"),
//                new File("C:\\Users\\24539\\Desktop\\AWS备考\\计算机硕士--选校表--终稿.xlsx"));
//        TransferAndRecovery.decryption(new File("C:\\Users\\24539\\Desktop\\AWS备考\\计算机硕士--选校表--终稿.xlsx"),
//                new File("C:\\Users\\24539\\Desktop\\AWS备考\\计算机硕士--选校表--终稿修复后.xlsx"));
        //TransferAndRecovery.decryption();
        // test4.Copy("E:\\科研训练\\test");

//        for (int i = 0; i < test4.filesAfterSift.size(); i++) {
//            Boolean result = test4.passIdentification(test4.filesAfterSift.get(i),test4.futureFilesPositions.get(i));
//            System.out.println(result);
//        }


    }
}
