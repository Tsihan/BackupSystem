package com.qihan.finalFrontEnd;

import com.qihan.finalCloudDataSystem.ListObjects;
import com.qihan.finalCloudDataSystem.PutObjects;
import com.qihan.finalRawDataSystem.Compression;
import com.qihan.finalRawDataSystem.Filtering;
import com.qihan.finalRawDataSystem.LocationAndTagging;
import com.qihan.finalRawDataSystem.TransferAndRecovery;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class CloudDataUploadPageController implements Initializable {

    //用于存储所有的原始文件
    public ArrayList<String> rawFilesArrayList = new ArrayList<String>();
    public ArrayList<String> rawTextFilesArrayList = new ArrayList<String>();
    public ArrayList<String> rawGraphFilesArrayList = new ArrayList<String>();
    public ArrayList<String> rawSoundFilesArrayList = new ArrayList<String>();
    public ArrayList<String> rawVideoFilesArrayList = new ArrayList<String>();
    public ArrayList<String> rawOtherFilesArrayList = new ArrayList<String>();
    //用于保存筛选过后的文件
    public ArrayList<String> filesAfterSift = new ArrayList<String>();
    //压缩文件的路径，包含文件名
    public String SrcZipFilePath = null;
    //临时存储本地操作处理后的文件以及路径

    public static String DesDirectory = "src\\main\\resources\\com\\qihan\\tempDirectory\\";


    public static String USER_NAME;
    public static String USER_ID;

    public boolean executeSuffixSift = false;

    public boolean executeIdentifierSift = false;

    public boolean keepStructure = false;

    public boolean keepMetaData = false;


    public String suffix;


    public boolean selectTextButton = false;
    public boolean selectGraphButton = false;
    public boolean selectSoundButton = false;
    public boolean selectVideoButton = false;
    public boolean selectOtherButton = false;

    public boolean selectEncryption = false;

    public boolean selectCompression = false;

    @FXML
    private Label manipulationResultLabel;

    @FXML
    private TextField suffixTextField;


    @FXML
    private Button returnButton;

    @FXML
    private Label welcomeLabel;


    @FXML
    private Label timeNowLabel;

    @FXML
    private ImageView CloudDataUploadImageView;

    //ok
    @FXML
    private RadioButton selectKeepStructureRadioButton;

    //ok
    @FXML
    private RadioButton selectKeepMetaDataRadioButton;


    //ok
    @FXML
    private RadioButton compressionRadioButton;
    //ok
    @FXML
    private RadioButton encryptionRadioButton;
    //ok
    @FXML
    private RadioButton notSiftRadioButton;
    //ok
    @FXML
    private RadioButton identifierSiftRadioButton;
    //ok
    @FXML
    private RadioButton suffixSiftRadioButton;

    //ok
    @FXML
    private CheckBox graphicCheckBox;
    //ok
    @FXML
    private CheckBox textualCheckBox;
    //ok
    @FXML
    private CheckBox musicalCheckBox;
    //ok
    @FXML
    private CheckBox videoCheckBox;
    //ok
    @FXML
    private CheckBox otherCheckBox;

    @FXML
    private TextArea s3ContentsTextArea;

    //因为需要用户指定加密密钥 这里增加输入
    @FXML
    private TextField secretKeyTextField;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("src\\main\\resources\\com\\qihan\\finalFrontEnd\\Images\\CloudDataSpecificManipulation.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        CloudDataUploadImageView.setImage(brandingImage);
        //定义单选按钮组
        ToggleGroup toggleGroup = new ToggleGroup();
        //将单选按钮统一共同的按钮组
        notSiftRadioButton.setToggleGroup(toggleGroup);
        identifierSiftRadioButton.setToggleGroup(toggleGroup);
        suffixSiftRadioButton.setToggleGroup(toggleGroup);


        welcomeLabel.setText("Welcome Common User " + CommonUserPageController.USER_NAME);
        USER_NAME = MainPageController.USER_NAME;
        USER_ID = MainPageController.USER_ID;

    }

    /**
     * 将S3存储桶所有的内容展示在文本框
     *
     * @param event
     */
    public void showS3ButtonOnAction(ActionEvent event) {
        //初始化
        manipulationResultLabel.setText("");
        String s3_name = "user-" + USER_ID + "-bucket";
        ListObjects test = new ListObjects();
        test.listAllObjects(s3_name, s3ContentsTextArea);


    }

    public void chooseUploadButtonOnAction(ActionEvent event) throws IOException {
        //初始化
        manipulationResultLabel.setText("");

        if (rawFilesArrayList.size() == 0) {
            manipulationResultLabel.setFont(Font.font("Times New Roman"));
            manipulationResultLabel.setText("Haven't designated source!");
            return;
        }


        if ((!notSiftRadioButton.isSelected()) && (!identifierSiftRadioButton.isSelected()) && (!suffixSiftRadioButton.isSelected())) {
            manipulationResultLabel.setFont(Font.font("Times New Roman"));
            manipulationResultLabel.setText("Haven't designated sift mode!");
            return;
        }

        if(selectEncryption){
            try{
                 Integer.parseInt(secretKeyTextField.getText().trim());
            }catch (NumberFormatException e){
                manipulationResultLabel.setFont(Font.font("Times New Roman"));
                manipulationResultLabel.setText("The secret key has to be integer!");
                return;
            }
        }

        //第一步

        LocationAndTagging step1 = new LocationAndTagging();
        for (String singleFile : rawFilesArrayList) {
            step1.provideInfo(singleFile);
        }
        rawOtherFilesArrayList = step1.otherFiles;
        rawVideoFilesArrayList = step1.videoFiles;
        rawSoundFilesArrayList = step1.soundFiles;
        rawGraphFilesArrayList = step1.graphFiles;
        rawTextFilesArrayList = step1.textFiles;
        //第二步 筛选
        Filtering step2 = new Filtering();
        step2.setSelectGraphButton(selectGraphButton);
        step2.setSelectOtherButton(selectOtherButton);
        step2.setSelectSoundButton(selectSoundButton);
        step2.setSelectTextButton(selectTextButton);
        step2.setSelectVideoButton(selectVideoButton);
        if (notSiftRadioButton.isSelected()) {
            //原封不动传递
            step2.setFilesAfterSift(rawFilesArrayList);
        } else if (suffixSiftRadioButton.isSelected()) {
            suffix = suffixTextField.getText().trim();
            step2.siftfilesByRE(rawFilesArrayList, suffix, executeSuffixSift);
        } else {
            step2.siftfiles(rawTextFilesArrayList, rawGraphFilesArrayList, rawSoundFilesArrayList, rawVideoFilesArrayList,
                    rawOtherFilesArrayList, executeIdentifierSift);
        }
        String s3_name = "user-" + USER_ID + "-bucket";


        //第三步 获取元数据并直接保留在目的目录
        TransferAndRecovery step3 = new TransferAndRecovery();
        step3.setFilesAfterSift(step2.filesAfterSift);
        step3.setSelectSaveMeta(keepMetaData);
        step3.addMetaData(DesDirectory);
        //第四步 加密
        TransferAndRecovery step4 = new TransferAndRecovery();
        step4.setFilesAfterSift(step3.getFilesAfterSift());
        step4.setSelectEncryption(selectEncryption);
        //将用户给的密钥作为原先写死在代码中的密钥

        if(selectEncryption){
            step4.setSecret(Integer.parseInt(secretKeyTextField.getText().trim()));
        }


        step4.encryption(DesDirectory);
        //第五步压缩
        Compression step5 = new Compression();
        step5.setKeepDirStructure(keepStructure);
        step5.setSelectCompression(selectCompression);
        step5.setFilesAfterSift(step4.tempEncryptedFiles);
        if (selectCompression) {
            String zipFilePath = DesDirectory + "\\" + "compression.zip";
            step5.compressData(zipFilePath);
            //已经结束
            //记得删除临时的未压缩的加密文件   对于云端数据是需要的
            for (String file : step4.tempEncryptedFiles) {
                File temp = new File(file);
                temp.delete();
            }
            manipulationResultLabel.setFont(Font.font("Times New Roman"));
            manipulationResultLabel.setText("The manipulation is successful!");
            //清零
            rawFilesArrayList.clear();
            rawTextFilesArrayList.clear();
            rawGraphFilesArrayList.clear();
            rawSoundFilesArrayList.clear();
            rawVideoFilesArrayList.clear();
            rawOtherFilesArrayList.clear();
            // DesDirectory = null;

            //将暂存到本地的需要上传的文件全部上传
            LocationAndTagging tempGetAllFiles = new LocationAndTagging();
            tempGetAllFiles.provideInfo(DesDirectory);
            PutObjects.putObjects(tempGetAllFiles.Files, s3_name);
            //将暂存到本地的需要上传的文件全部删除
            tempGetAllFiles.deleteDir(DesDirectory);
            return;
        }

        //第六步 拷贝
        if (selectEncryption) {
            TransferAndRecovery step6 = new TransferAndRecovery();
            step6.setFilesAfterSift(step5.filesAfterSift);
            step6.Transfer(DesDirectory);

            manipulationResultLabel.setFont(Font.font("Times New Roman"));
            manipulationResultLabel.setText("The manipulation is successful!");
            //清零
            rawFilesArrayList.clear();
            rawTextFilesArrayList.clear();
            rawGraphFilesArrayList.clear();
            rawSoundFilesArrayList.clear();
            rawVideoFilesArrayList.clear();
            rawOtherFilesArrayList.clear();
            //  DesDirectory = null;

            //将暂存到本地的需要上传的文件全部上传
            LocationAndTagging tempGetAllFiles = new LocationAndTagging();
            tempGetAllFiles.provideInfo(DesDirectory);
            PutObjects.putObjects(tempGetAllFiles.Files, s3_name);
            //将暂存到本地的需要上传的文件全部删除
            tempGetAllFiles.deleteDir(DesDirectory);

            return;
        }

        //即没有加密也没有压缩
        step3.Copy(DesDirectory);
        manipulationResultLabel.setFont(Font.font("Times New Roman"));
        manipulationResultLabel.setText("The manipulation is successful!");
        //清零
        rawFilesArrayList.clear();
        rawTextFilesArrayList.clear();
        rawGraphFilesArrayList.clear();
        rawSoundFilesArrayList.clear();
        rawVideoFilesArrayList.clear();
        rawOtherFilesArrayList.clear();
        //DesDirectory = null;

        //将暂存到本地的需要上传的文件全部上传
        LocationAndTagging tempGetAllFiles = new LocationAndTagging();
        tempGetAllFiles.provideInfo(DesDirectory);
        PutObjects.putObjects(tempGetAllFiles.Files, s3_name);
        //将暂存到本地的需要上传的文件全部删除
        tempGetAllFiles.deleteDir(DesDirectory);
    }

    public void chooseSourceFilesButtonOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("May use the sift function provided "
                + "by javaFX, but I use my own.", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        List<File> list = fileChooser.showOpenMultipleDialog(MainPageController.STAGE_NOW);


        for (File singleFile : list) {
            rawFilesArrayList.add(singleFile.toString());

        }
        //成功读入到FilesArrayList中，之后去调用其他类对象的方法
        manipulationResultLabel.setFont(Font.font("Times New Roman"));
        manipulationResultLabel.setText("");
    }

    public void chooseSourceDirectoryButtonOnAction(ActionEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();


        File path = directoryChooser.showDialog(MainPageController.STAGE_NOW);

        LocationAndTagging test = new LocationAndTagging();
        test.provideInfo(String.valueOf(path));
        //读取到相应的arraylist中
        for (String singleFile : test.Files) {
            rawFilesArrayList.add(singleFile);
        }
        for (String singleFile : test.textFiles) {
            rawTextFilesArrayList.add(singleFile);
        }
        for (String singleFile : test.graphFiles) {
            rawGraphFilesArrayList.add(singleFile);
        }
        for (String singleFile : test.soundFiles) {
            rawSoundFilesArrayList.add(singleFile);
        }
        for (String singleFile : test.videoFiles) {
            rawVideoFilesArrayList.add(singleFile);
        }
        for (String singleFile : test.otherFiles) {
            rawOtherFilesArrayList.add(singleFile);
        }
        for (String singleFile : rawFilesArrayList) {
            System.out.println(singleFile);
        }
        manipulationResultLabel.setFont(Font.font("Times New Roman"));
        manipulationResultLabel.setText("");

    }

    public void chooseSelectKeepMetaDataRadioButtonOnAction(ActionEvent event) {
        if (selectKeepMetaDataRadioButton.isSelected()) {
            keepMetaData = true;
        } else {
            keepMetaData = false;
        }

    }

    public void chooseSelectKeepStructureRadioButtonOnAction(ActionEvent event) {
        if (selectKeepStructureRadioButton.isSelected()) {
            keepStructure = true;
        } else {
            keepStructure = false;
        }

    }

    public void chooseEncryptionRadioButtonOnAction(ActionEvent event) {
        if (encryptionRadioButton.isSelected()) {
            selectEncryption = true;
        } else {
            selectEncryption = false;
        }

    }

    public void chooseCompressionRadioButtonOnAction(ActionEvent event) {
        if (compressionRadioButton.isSelected()) {
            selectCompression = true;
        } else {
            selectCompression = false;
        }

    }

    public void chooseSuffixTextFieldOnAction(ActionEvent event) {
        if (!suffixTextField.getText().trim().equals("")) {
            //去掉首尾的空格
            suffix = suffixTextField.getText().trim();
            //System.out.println(suffix + "有");
        } else {
            return;
            // System.out.println("没有输入");
        }


    }


    /**
     * 用于筛选的处理
     *
     * @param event
     */
    public void chooseSiftButtonsOnAction(ActionEvent event) {

        if (notSiftRadioButton.isSelected()) {
            executeIdentifierSift = false;
            executeSuffixSift = false;
        }
        if (identifierSiftRadioButton.isSelected()) {
            executeIdentifierSift = true;
            executeSuffixSift = false;

        }
        if (suffixSiftRadioButton.isSelected()) {
            executeIdentifierSift = false;
            executeSuffixSift = true;
        }
    }

    /**
     * 用于按identifier多选框进行筛选
     *
     * @param event
     */
    public void chooseCheckBoxsOnAction(ActionEvent event) {

        if (graphicCheckBox.isSelected()) {
            selectGraphButton = true;
        } else {
            selectGraphButton = false;
        }

        if (textualCheckBox.isSelected()) {
            selectTextButton = true;
        } else {
            selectTextButton = false;
        }

        if (musicalCheckBox.isSelected()) {
            selectSoundButton = true;
        } else {
            selectSoundButton = false;
        }

        if (videoCheckBox.isSelected()) {
            selectVideoButton = true;
        } else {
            selectVideoButton = false;
        }

        if (otherCheckBox.isSelected()) {
            selectOtherButton = true;
        } else {
            selectOtherButton = false;
        }

        System.out.println(selectGraphButton);
        System.out.println(selectTextButton);
        System.out.println(selectSoundButton);
        System.out.println(selectVideoButton);
        System.out.println(selectOtherButton);


    }


    public void timeNowButtonOnAction(MouseEvent event) {
        DateFormat currentTime = new SimpleDateFormat("yyyy.MM.dd hh:mm");    //设置时间格式
        timeNowLabel.setAlignment(Pos.CENTER);
        timeNowLabel.setText(currentTime.format(new Date()));
    }

    public void timeNowButtonNoAction(MouseEvent event) {

        timeNowLabel.setFont(Font.font("Times New Roman"));
        timeNowLabel.setText("Pending your mouse here and you can see the time now.");
    }

    public void returnButtonOnAction(ActionEvent event) {

        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
        USER_NAME = null;
        USER_ID = null;


        try {
            Parent root = FXMLLoader.load(getClass().getResource("CloudDataManipulationPage.fxml"));
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            Scene newScene = new Scene(root, 520, 475);
            registerStage.setScene(newScene);
            registerStage.show();

            //        鼠标按下事件
            newScene.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    FrontEndApplication.oldStageX = registerStage.getX();
                    FrontEndApplication.oldStageY = registerStage.getY();
                    FrontEndApplication.oldScreenX = event.getScreenX();
                    FrontEndApplication.oldScreenY = event.getScreenY();
                }
            });

            //鼠标拖拽
            newScene.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    //新位置
                    //拖拽前后的鼠标差值加上原始窗体坐标值
                    registerStage.setX(event.getScreenX() - FrontEndApplication.oldScreenX + FrontEndApplication.oldStageX);
                    registerStage.setY(event.getScreenY() - FrontEndApplication.oldScreenY + FrontEndApplication.oldStageY);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }
}
