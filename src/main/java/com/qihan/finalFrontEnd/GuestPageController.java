package com.qihan.finalFrontEnd;

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

import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GuestPageController implements Initializable {
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
    //需要未来放置的文件地址arraylist 即原始文件的文件名与DesDirectory的拼接
    public ArrayList<String> futureFilesPositions = new ArrayList<>();
    //未来所移动到的目录
    public String DesDirectory = null;

    public String firstCompareFile = null;
    public String secondCompareFile = null;

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


    //OK
    @FXML
    private ImageView GuestPageImageView;


    //ok
    @FXML
    private Button returnButton;


    //OK
    @FXML
    private Label timeNowLabel;

    //OK
    @FXML
    public Label welcomeLabel;


    //ok
    @FXML
    private TextField suffixTextField;


    //ok
    @FXML
    private Button chooseSourceFilesButton;
    //ok
    @FXML
    private Button chooseSourceDirectoryButton;
    //ok
    @FXML
    private Button chooseDestinationButton;

    //ok
    @FXML
    private Button decryptButton;
    //ok
    @FXML
    private Button compareFirstButton;
    //ok
    @FXML
    private Button compareSecondButton;
    //ok
    @FXML
    private Button compareExecuteButton;


    @FXML
    private Button copyButton;
    @FXML
    private Button cutButton;

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
    private Label guestLabel;

    @FXML
    private Label manipulationResultLabel;
    //新增用户需要将密钥作为输入
    @FXML
    private TextField secretKeyTextField;
    @FXML
    private TextField previousKeyTextField;


    /**
     * 深拷贝
     *
     * @param
     * @param
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
//    public static <T> ArrayList<T> deepCopy(ArrayList<T> src) throws IOException, ClassNotFoundException {
//        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//        ObjectOutputStream out = new ObjectOutputStream(byteOut);
//        out.writeObject(src);
//
//        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
//        ObjectInputStream in = new ObjectInputStream(byteIn);
//        @SuppressWarnings("unchecked")
//        List<T> dest = (List<T>) in.readObject();
//        return (ArrayList<T>) dest;
//    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File registerFile = new File("src\\main\\resources\\com\\qihan\\finalFrontEnd\\Images\\GuestPage.jpg");
        Image registerImage = new Image(registerFile.toURI().toString());
        GuestPageImageView.setImage(registerImage);

        //定义单选按钮组
        ToggleGroup toggleGroup = new ToggleGroup();
        //将单选按钮统一共同的按钮组
        notSiftRadioButton.setToggleGroup(toggleGroup);
        identifierSiftRadioButton.setToggleGroup(toggleGroup);
        suffixSiftRadioButton.setToggleGroup(toggleGroup);
        USER_NAME = CommonUserPageController.USER_NAME;
        USER_ID = CommonUserPageController.USER_ID;
        if (!(USER_NAME == null)) {
            welcomeLabel.setText("Welcome common user: " + USER_NAME);
            guestLabel.setText("");

        } else {
            welcomeLabel.setText("");
            guestLabel.setText("Guest has the only right to access local data");

        }


    }


    public void chooseCopyButtonOnAction(ActionEvent event) throws IOException {

        previousKeyTextField.setText("");
        if (rawFilesArrayList.size() == 0) {
            manipulationResultLabel.setFont(Font.font("Times New Roman"));
            manipulationResultLabel.setText("Haven't designated source!");
            return;
        }

        if (DesDirectory == null) {
            manipulationResultLabel.setFont(Font.font("Times New Roman"));
            manipulationResultLabel.setText("Haven't designated destination!");
            return;
        }
        if ((!notSiftRadioButton.isSelected()) && (!identifierSiftRadioButton.isSelected()) && (!suffixSiftRadioButton.isSelected())) {
            manipulationResultLabel.setFont(Font.font("Times New Roman"));
            manipulationResultLabel.setText("Haven't designated sift mode!");
            return;
        }

        //将用户给的密钥作为原先写死在代码中的密钥

        if (selectEncryption) {
            try {
                Integer.parseInt(secretKeyTextField.getText().trim());
            } catch (NumberFormatException e) {
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
        //第三步 获取元数据并直接保留在目的目录
        TransferAndRecovery step3 = new TransferAndRecovery();
        step3.setFilesAfterSift(step2.filesAfterSift);
        step3.setSelectSaveMeta(keepMetaData);
        step3.addMetaData(DesDirectory);
        //第四步 加密
        TransferAndRecovery step4 = new TransferAndRecovery();
        step4.setFilesAfterSift(step3.getFilesAfterSift());
        step4.setSelectEncryption(selectEncryption);

        if (selectEncryption) {
            step4.setSecret(Integer.parseInt(secretKeyTextField.getText().trim()));
        }


        step4.encryption(DesDirectory);
        //第五步压缩
        Compression step5 = new Compression();
        step5.setKeepDirStructure(keepStructure);
        step5.setSelectCompression(selectCompression);
        step5.setFilesAfterSift(step4.tempEncryptedFiles);
        if (selectCompression) {
            String zipFilePath = DesDirectory + "\\" + "test.zip";
            step5.compressData(zipFilePath);
            //已经结束
            //记得删除临时的未压缩的加密文件
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
            DesDirectory = null;


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
            DesDirectory = null;
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
        DesDirectory = null;

        suffixTextField.setText("");
        secretKeyTextField.setText("");
    }

    public void chooseCutButtonOnAction(ActionEvent event) throws IOException {

        previousKeyTextField.setText("");
        if (rawFilesArrayList.size() == 0) {
            manipulationResultLabel.setFont(Font.font("Times New Roman"));
            manipulationResultLabel.setText("Haven't designated source!");
            return;
        }

        if (DesDirectory == null) {
            manipulationResultLabel.setFont(Font.font("Times New Roman"));
            manipulationResultLabel.setText("Haven't designated destination!");
            return;
        }
        if ((!notSiftRadioButton.isSelected()) && (!identifierSiftRadioButton.isSelected()) && (!suffixSiftRadioButton.isSelected())) {
            manipulationResultLabel.setFont(Font.font("Times New Roman"));
            manipulationResultLabel.setText("Haven't designated sift mode!");
            return;
        }

        //将用户给的密钥作为原先写死在代码中的密钥
        int secret = 0;
        if (selectEncryption) {
            try {
                secret = Integer.parseInt(secretKeyTextField.getText().trim());
            } catch (NumberFormatException e) {
                manipulationResultLabel.setFont(Font.font("Times New Roman"));
                manipulationResultLabel.setText("The secret key has to be integer!");
                return;
            }
        }

        //第一步{

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
        //第三步 获取元数据并直接保留在目的目录
        TransferAndRecovery step3 = new TransferAndRecovery();
        step3.setFilesAfterSift(step2.filesAfterSift);
        step3.setSelectSaveMeta(keepMetaData);
        step3.addMetaData(DesDirectory);
        //第四步 加密
        TransferAndRecovery step4 = new TransferAndRecovery();
        step4.setFilesAfterSift(step3.getFilesAfterSift());
        step4.setSelectEncryption(selectEncryption);

        if (selectEncryption) {
            step4.setSecret(secret);
        }

        step4.encryption(DesDirectory);
        //第五步压缩
        Compression step5 = new Compression();
        step5.setKeepDirStructure(keepStructure);
        step5.setSelectCompression(selectCompression);
        step5.setFilesAfterSift(step4.tempEncryptedFiles);
        if (selectCompression) {
            String zipFilePath = DesDirectory + "\\" + "test.zip";
            step5.compressData(zipFilePath);
            //删除原先的文件与目录
            for (String file : step2.filesAfterSift) {
                File temp = new File(file);
                temp.delete();
            }

            //已经结束
            //记得删除临时的未压缩的加密文件
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
            DesDirectory = null;


            return;
        }

        //第六步 剪贴
        if (selectEncryption) {
            TransferAndRecovery step6 = new TransferAndRecovery();
            step6.setFilesAfterSift(step5.filesAfterSift);
            step6.Transfer(DesDirectory);
            //删除原先的文件与目录
            for (String file : step2.filesAfterSift) {
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
            DesDirectory = null;
            return;
        }

        //即没有加密也没有压缩
        step3.Transfer(DesDirectory);
        manipulationResultLabel.setFont(Font.font("Times New Roman"));
        manipulationResultLabel.setText("The manipulation is successful!");
        //清零
        rawFilesArrayList.clear();
        rawTextFilesArrayList.clear();
        rawGraphFilesArrayList.clear();
        rawSoundFilesArrayList.clear();
        rawVideoFilesArrayList.clear();
        rawOtherFilesArrayList.clear();
        DesDirectory = null;

        suffixTextField.setText("");
        secretKeyTextField.setText("");

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

    public void chooseCompareExecuteButtonOnAction(ActionEvent event) {
        suffixTextField.setText("");
        secretKeyTextField.setText("");
        previousKeyTextField.setText("");
        if (firstCompareFile != null && secondCompareFile != null) {
            TransferAndRecovery test = new TransferAndRecovery();
            if (test.passIdentification(firstCompareFile, secondCompareFile)) {
                manipulationResultLabel.setFont(Font.font("Times New Roman"));
                manipulationResultLabel.setText("The Two files are same!");
            } else {
                manipulationResultLabel.setFont(Font.font("Times New Roman"));
                manipulationResultLabel.setText("The Two files are different!");
            }
        } else {
            manipulationResultLabel.setFont(Font.font("Times New Roman"));
            manipulationResultLabel.setText("You have to choose two files!");
        }

    }


    public void chooseCompareFirstButtonOnAction(ActionEvent event) {
        suffixTextField.setText("");
        secretKeyTextField.setText("");
        previousKeyTextField.setText("");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Select the first file", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        File f = fileChooser.showOpenDialog(MainPageController.STAGE_NOW);
        firstCompareFile = f.toString();

    }

    public void chooseCompareSecondButtonOnAction(ActionEvent event) {
        suffixTextField.setText("");
        secretKeyTextField.setText("");
        previousKeyTextField.setText("");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Select the second file", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        File f = fileChooser.showOpenDialog(MainPageController.STAGE_NOW);
        secondCompareFile = f.toString();

    }


    public void chooseDecryptButtonOnAction(ActionEvent event) throws IOException {


        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("You must select encrypted files, or "
                + "decryption becomes encryption automatically.", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        List<File> list = fileChooser.showOpenMultipleDialog(MainPageController.STAGE_NOW);
        ArrayList<String> srcFiles = new ArrayList();

        for (File singleFile : list) {
            srcFiles.add(singleFile.toString());
        }
        TransferAndRecovery test = new TransferAndRecovery();
        try {
            test.setSecret(Integer.parseInt(previousKeyTextField.getText().trim()));
        } catch (NumberFormatException e) {
            manipulationResultLabel.setText("The previous key is pure number!");
            return;
        }

        for (String singleFile : srcFiles) {
            //第一部分获取不带文件后缀的文件名
            String part1 = singleFile.substring(0, singleFile.lastIndexOf("."));
            //第二部分
            String part2 = "_Decrypted.";
            //第三部分 获取文件后缀名
            // 获取文件后缀类型
            String part3 = "";
            int i = singleFile.lastIndexOf('.');
            if (i > 0) {
                part3 = singleFile.substring(i + 1);
            }

            String target = part1 + part2 + part3;
            test.decryption(new File(singleFile), new File(target));
        }
        previousKeyTextField.setText("");

        manipulationResultLabel.setFont(Font.font("Times New Roman"));
        manipulationResultLabel.setText("The chosen files have been decrypted!");

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

    public void chooseDestinationButtonOnAction(ActionEvent event) {
        //如果还没有选择文件，不能进行目的目录的选择
        if (rawFilesArrayList.size() == 0) {
            manipulationResultLabel.setFont(Font.font("Times New Roman"));
            manipulationResultLabel.setText("You have not chosen any source files!");
            return;
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File path = directoryChooser.showDialog(MainPageController.STAGE_NOW);
        DesDirectory = path.toString();
        System.out.println(DesDirectory);
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

        try {

            if (USER_ID == null) {
                Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
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
            } else {
                Parent root = FXMLLoader.load(getClass().getResource("CommonUserPage.fxml"));
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
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }


    }
}
