package com.qihan.finalTestCode.RawDataSystem;

import com.qihan.finalRawDataSystem.LocationAndTagging;

import java.io.IOException;

public class DemoForLocationAndTagging {
    public static void main(String[] args) throws IOException {
        LocationAndTagging test = new LocationAndTagging();
       // test.provideInfo("E:\\科研训练\\基础科研训练相关文档");
        test.provideInfo( "E:\\科研训练\\图数据库介绍v1.2.docx");
        test.deleteDir("E:\\非学习内容\\test");

       // System.out.println(test.otherFiles.get(0));
    }
}
