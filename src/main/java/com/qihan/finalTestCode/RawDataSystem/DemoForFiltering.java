package com.qihan.finalTestCode.RawDataSystem;

import com.qihan.finalRawDataSystem.Filtering;
import com.qihan.finalRawDataSystem.LocationAndTagging;

import java.io.IOException;

public class DemoForFiltering {
    public static void main(String[] args) throws IOException {
        LocationAndTagging test = new LocationAndTagging();
        test.provideInfo("E:\\科研训练\\基础科研训练相关文档");

//        Filtering test2 = new Filtering();
//        test2.siftfilesByRE(test.Files,"xlsx");
//        System.out.println(test2.filesAfterSift);

        Filtering test2 = new Filtering();
        test2.setSelectOtherButton(true);
        test2.siftfiles(test.textFiles,test.graphFiles,test.soundFiles,test.videoFiles,test.otherFiles,true);
        System.out.println(test2.filesAfterSift);
    }
}
