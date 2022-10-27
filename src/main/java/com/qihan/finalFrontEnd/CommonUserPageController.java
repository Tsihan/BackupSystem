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

public class CommonUserPageController implements Initializable {

    @FXML
    private ImageView brandingImageView;

    @FXML
    private ImageView lockImageView;

    @FXML
    private Button localDataManipulationButton;

    @FXML
    private Button cloudDataManipulationButton;

    @FXML
    private Button logOutButton;


    @FXML
    private Label timeNowLabel;

    @FXML
    private Label welcomeLabel;
    //不能复用GuestPage.fxml 因为退出逻辑需要重新返回用户界面
    public static String USER_NAME;
    public static String USER_ID;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("src\\main\\resources\\com\\qihan\\finalFrontEnd\\Images\\CommonUserLeft.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("src\\main\\resources\\com\\qihan\\finalFrontEnd\\Images\\CommonUserRight.jpg");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);

        welcomeLabel.setText("Welcome Common User " + MainPageController.USER_NAME);
        USER_NAME = MainPageController.USER_NAME;
        USER_ID = MainPageController.USER_ID;

    }



    public void directToGuestPage() {

        Stage stage = (Stage) logOutButton.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("GuestPage.fxml"));
            Stage GuestStage = new Stage();
            GuestStage.initStyle(StageStyle.UNDECORATED);
            Scene newScene = new Scene(root, 520, 475);
            GuestStage.setScene(newScene);

           // GuestStage.welcomeLabel.setText("Welcome Common User " + USER_NAME);
            GuestStage.show();

            //        鼠标按下事件
            newScene.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    FrontEndApplication.oldStageX = GuestStage.getX();
                    FrontEndApplication.oldStageY = GuestStage.getY();
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
                    GuestStage.setX(event.getScreenX() - FrontEndApplication.oldScreenX + FrontEndApplication.oldStageX);
                    GuestStage.setY(event.getScreenY() - FrontEndApplication.oldScreenY + FrontEndApplication.oldStageY);
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


    public void logOutButtonOnAction(ActionEvent event) {

        Stage stage = (Stage) logOutButton.getScene().getWindow();
        stage.close();
        USER_ID = null;
        USER_NAME = null;

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }


    public void localDataManipulationButtonOnAction(ActionEvent actionEvent) {
        directToGuestPage();
    }

    public void cloudDataManipulationButtonOnAction(ActionEvent actionEvent) {
        directToCloudDataManipulationPage();
    }

    private void directToCloudDataManipulationPage() {


        Stage stage = (Stage) logOutButton.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("CloudDataManipulationPage.fxml"));
            Stage GuestStage = new Stage();
            GuestStage.initStyle(StageStyle.UNDECORATED);
            Scene newScene = new Scene(root, 520, 475);
            GuestStage.setScene(newScene);

            // GuestStage.welcomeLabel.setText("Welcome Common User " + USER_NAME);
            GuestStage.show();

            //        鼠标按下事件
            newScene.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    FrontEndApplication.oldStageX = GuestStage.getX();
                    FrontEndApplication.oldStageY = GuestStage.getY();
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
                    GuestStage.setX(event.getScreenX() - FrontEndApplication.oldScreenX + FrontEndApplication.oldStageX);
                    GuestStage.setY(event.getScreenY() - FrontEndApplication.oldScreenY + FrontEndApplication.oldStageY);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
