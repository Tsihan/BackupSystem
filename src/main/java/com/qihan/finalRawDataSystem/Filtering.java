package com.qihan.finalRawDataSystem;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public class Filtering {
    public ArrayList<String> getFilesAfterSift() {
        return filesAfterSift;
    }

    public void setFilesAfterSift(ArrayList<String> filesAfterSift) {
        this.filesAfterSift = filesAfterSift;
    }

    //输出字符串数组列表
    public ArrayList<String> filesAfterSift = new ArrayList<String>();
    //用户输入二：是否选择这些筛选过后的文件 到时候会有两个选择：根据文件后缀名进行筛选或者根据文件的二进制标签进行筛选
    boolean selectTextButton = false;
    boolean selectGraphButton = false;
    boolean selectSoundButton = false;
    boolean selectVideoButton = false;
    boolean selectOtherButton = false;

    public boolean isSelectTextButton() {
        return selectTextButton;
    }

    public void setSelectTextButton(boolean selectTextButton) {
        this.selectTextButton = selectTextButton;
    }

    public boolean isSelectGraphButton() {
        return selectGraphButton;
    }

    public void setSelectGraphButton(boolean selectGraphButton) {
        this.selectGraphButton = selectGraphButton;
    }

    public boolean isSelectSoundButton() {
        return selectSoundButton;
    }

    public void setSelectSoundButton(boolean selectSoundButton) {
        this.selectSoundButton = selectSoundButton;
    }

    public boolean isSelectVideoButton() {
        return selectVideoButton;
    }

    public void setSelectVideoButton(boolean selectVideoButton) {
        this.selectVideoButton = selectVideoButton;
    }

    public boolean isSelectOtherButton() {
        return selectOtherButton;
    }

    public void setSelectOtherButton(boolean selectOtherButton) {
        this.selectOtherButton = selectOtherButton;
    }

    /**
     * 根据LocationAndTagging子系统传递过来的分类数据进行筛选
     *
     * @param textFs
     * @param graphFs
     * @param soundFs
     * @param videoFs
     * @param otherFs
     * @throws IOException
     */
    public void siftfiles(ArrayList<String> textFs, ArrayList<String> graphFs, ArrayList<String> soundFs, ArrayList<String> videoFs,
                          ArrayList<String> otherFs, boolean execute) throws IOException {
        if (execute == true) {

            if (isSelectGraphButton()) {
                //System.out.println(graphFs);
                this.filesAfterSift.addAll(graphFs);
            }
            if (isSelectOtherButton()) {
                //System.out.println(otherFs);
                this.filesAfterSift.addAll(otherFs);
            }
            if (isSelectSoundButton()) {
                //System.out.println(soundFs);
                this.filesAfterSift.addAll(soundFs);
            }
            if (isSelectVideoButton()) {
                //System.out.println(videoFs);
                this.filesAfterSift.addAll(videoFs);
            }
            if (isSelectTextButton()) {
                //System.out.println(textFs);
                this.filesAfterSift.addAll(textFs);
            }
            if (filesAfterSift.isEmpty()) {
                throw new InvalidParameterException("No file has been selected!");
            }
        } else {
            return;
        }

    }

    /**
     * 根据文件拓展名进行筛选
     *
     * @param Fs
     * @param aimedExtension
     * @throws IOException
     */
    public void siftfilesByRE(ArrayList<String> Fs, String aimedExtension, boolean execute) throws IOException {
        if (execute) {
            for (String f : Fs) {
                String extension = "";
                int j = f.lastIndexOf('.');
                if (j > 0) {
                    extension = f.substring(j + 1);
                }
                if (aimedExtension.equals(extension)) {
                    this.filesAfterSift.add(f);
                }
            }
        }


    }

}
