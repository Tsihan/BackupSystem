package com.qihan.finalFrontEnd;

import com.qihan.finalCloudDataSystem.GetObject;
import com.qihan.finalCloudDataSystem.ListObjects;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class CloudDataDownloadPageController implements Initializable {

    //临时存储本地操作处理后的文件以及路径
    public static String DesDirectory = null;


    public static String USER_NAME;
    public static String USER_ID;

    @FXML
    private Label manipulationResultLabel;

    @FXML
    private TextField fileNameTextField;


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
    private RadioButton downloadOneRadioButton;

    //ok
    @FXML
    private RadioButton downloadAllRadioButton;


    @FXML
    private TextArea s3ContentsTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("src\\main\\resources\\com\\qihan\\finalFrontEnd\\Images\\CloudDataSpecificManipulation.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        CloudDataUploadImageView.setImage(brandingImage);
        //定义单选按钮组
        ToggleGroup toggleGroup = new ToggleGroup();
        //将单选按钮统一共同的按钮组
        downloadOneRadioButton.setToggleGroup(toggleGroup);

        downloadAllRadioButton.setToggleGroup(toggleGroup);


        welcomeLabel.setText("Welcome Common User " + CommonUserPageController.USER_NAME);
        USER_NAME = MainPageController.USER_NAME;
        USER_ID = MainPageController.USER_ID;

    }


    public void chooseDownloadButtonOnAction(ActionEvent event) {
        //初始化
        manipulationResultLabel.setText("");
        //没有选择模式
        if ((!downloadOneRadioButton.isSelected()) && (!downloadAllRadioButton.isSelected())) {
            manipulationResultLabel.setText("You haven't selected download mode!");
            return;
        }

        if (downloadOneRadioButton.isSelected() && fileNameTextField.getText().trim().equals("")) {
            manipulationResultLabel.setText("You haven't entered a file/directory's name!");
            return;
        }

        if (DesDirectory == null) {
            manipulationResultLabel.setText("You haven't selected download mode!");
            return;
        }
        String s3_name = "user-" + USER_ID + "-bucket";
        //下载所有的存储桶中的文件
        if (downloadAllRadioButton.isSelected()) {
            GetObject.getAllObjects(s3_name, DesDirectory, USER_ID, s3_name);
            manipulationResultLabel.setText("The manipulation is successful!");
        } else {
            //下载存储桶中的一个文件/路径
            GetObject.getSingleObject(s3_name, fileNameTextField.getText().trim(), DesDirectory, manipulationResultLabel
                    , USER_ID, s3_name);
            manipulationResultLabel.setText("The manipulation is successful!");
        }

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

        ListObjects.listAllObjects(s3_name, s3ContentsTextArea);


    }

    public void chooseDestinationDirectoryButtonOnAction(ActionEvent event) {

        DirectoryChooser directoryChooser = new DirectoryChooser();

        File path = directoryChooser.showDialog(MainPageController.STAGE_NOW);
        DesDirectory = path.toString();
        System.out.println(DesDirectory);

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


    public void timeNowButtonOnAction(MouseEvent event) {
        DateFormat currentTime = new SimpleDateFormat("yyyy.MM.dd hh:mm");    //设置时间格式
        timeNowLabel.setAlignment(Pos.CENTER);
        timeNowLabel.setText(currentTime.format(new Date()));
    }

    public void timeNowButtonNoAction(MouseEvent event) {

        timeNowLabel.setFont(Font.font("Times New Roman"));
        timeNowLabel.setText("Pending your mouse here and you can see the time now.");
    }


}
