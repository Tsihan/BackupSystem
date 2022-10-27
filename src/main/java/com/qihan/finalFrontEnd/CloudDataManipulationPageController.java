package com.qihan.finalFrontEnd;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class CloudDataManipulationPageController implements Initializable {

    @FXML
    private ImageView CloudDataManipulationDogImageView;

    @FXML
    private ImageView CloudDataManipulationS3ImageView;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label timeNowLabel;


    @FXML
    private Button returnButton;

    @FXML
    private Button uploadButton;
    @FXML
    private Button downloadButton;
    @FXML
    private Button transferButton;


    public static String USER_NAME;
    public static String USER_ID;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("src\\main\\resources\\com\\qihan\\finalFrontEnd\\Images\\CloudDataManipulationPage.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        CloudDataManipulationDogImageView.setImage(brandingImage);

        File lockFile = new File("src\\main\\resources\\com\\qihan\\finalFrontEnd\\Images\\CloudDataManipulationPageS3.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        CloudDataManipulationS3ImageView.setImage(lockImage);

        welcomeLabel.setText("Welcome Common User " + CommonUserPageController.USER_NAME);
        USER_NAME = MainPageController.USER_NAME;
        USER_ID = MainPageController.USER_ID;

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
        USER_ID = null;
        USER_NAME = null;

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }

    public void chooseUploadButtonOnAction(ActionEvent actionEvent) {

        Stage stage = (Stage) uploadButton.getScene().getWindow();
        stage.close();
        CloudDataUploadPageController.USER_ID = USER_ID;
        CloudDataUploadPageController.USER_NAME = USER_NAME;


        try {
            Parent root = FXMLLoader.load(getClass().getResource("CloudDataUploadPage.fxml"));
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

    public void chooseDownloadButtonOnAction(ActionEvent actionEvent) {

        Stage stage = (Stage) uploadButton.getScene().getWindow();
        stage.close();
        CloudDataDownloadPageController.USER_ID = USER_ID;
        CloudDataDownloadPageController.USER_NAME = USER_NAME;


        try {
            Parent root = FXMLLoader.load(getClass().getResource("CloudDataDownloadPage.fxml"));
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


    public void chooseTransferButtonOnAction(ActionEvent actionEvent) {

        Stage stage = (Stage) uploadButton.getScene().getWindow();
        stage.close();
        CloudDataTransferPageController.USER_ID = USER_ID;
        CloudDataTransferPageController.USER_NAME = USER_NAME;


        try {
            Parent root = FXMLLoader.load(getClass().getResource("CloudDataTransferPage.fxml"));
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
