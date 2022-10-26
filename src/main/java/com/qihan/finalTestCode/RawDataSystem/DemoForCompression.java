package com.qihan.finalTestCode.RawDataSystem;

import com.qihan.finalRawDataSystem.Compression;
import com.qihan.finalRawDataSystem.Filtering;
import com.qihan.finalRawDataSystem.LocationAndTagging;

import java.io.IOException;

public class DemoForCompression {
    public static void main(String[] args) throws IOException {
        LocationAndTagging test = new LocationAndTagging();
        test.provideInfo("E:\\科研训练\\基础科研训练相关文档");

        Filtering test2 = new Filtering();
        test2.siftfilesByRE(test.Files, "xlsx",true);
        System.out.println(test2.filesAfterSift);

        System.out.println("===========");

        Compression test3 = new Compression();
        test3.setSelectCompression(true);
        test3.setFilesAfterSift(test2.filesAfterSift);
        test3.compressData("E:\\科研训练\\暑假内容\\test.zip");

    }
}
