package com.qihan.finalFrontEnd;

import com.qihan.finalCloudDataSystem.CopyObject;
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

public class CloudDataTransferPageController implements Initializable {


    public static String USER_NAME;
    public static String USER_ID;

    @FXML
    private Label manipulationResultLabel;

    @FXML
    private TextField DestinationTextField;

    @FXML
    private TextField ContentTextField;


    @FXML
    private Button returnButton;

    @FXML
    private Label welcomeLabel;


    @FXML
    private Label timeNowLabel;

    @FXML
    private ImageView CloudDataTransferImageView;


    @FXML
    private TextArea s3ContentsTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("src\\main\\resources\\com\\qihan\\finalFrontEnd\\Images\\CloudDataSpecificManipulation.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        CloudDataTransferImageView.setImage(brandingImage);


        welcomeLabel.setText("Welcome Common User " + CommonUserPageController.USER_NAME);
        USER_NAME = MainPageController.USER_NAME;
        USER_ID = MainPageController.USER_ID;

    }


    public void chooseTransferButtonOnAction(ActionEvent event) {


        if (ContentTextField.getText().trim().equals("")) {
            manipulationResultLabel.setText("You haven't entered your content!");
            return;
        }

        if (DestinationTextField.getText().trim().equals("")) {
            manipulationResultLabel.setText("You haven't entered your friend's user_id!");
            return;
        }


        String Src_s3name = "user-" + USER_ID + "-bucket";
        String Des_s3name = "user-" + DestinationTextField.getText().trim() + "-bucket";
        //拷贝存储桶中的文件到另一个存储桶
        manipulationResultLabel.setText("");
        CopyObject.copy(ContentTextField.getText().trim(), Src_s3name, Des_s3name);
        manipulationResultLabel.setText("The manipulation is successful!");
        ContentTextField.setText("");
        DestinationTextField.setText("");


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
