package com.qihan.finalFrontEnd;


import javafx.application.Application;
import javafx.application.Platform;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;


public class FrontEndApplication extends Application {

    public static double oldStageX;
    public static double oldStageY;
    public static double oldScreenX;
    public static double oldScreenY;

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Qihan Zhang's backup system ");



        Scene newScene = new Scene(root, 520, 475);
        primaryStage.setScene(newScene);
        primaryStage.show();



        //        鼠标按下事件
        newScene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                oldStageX = primaryStage.getX();
                oldStageY = primaryStage.getY();
                oldScreenX = event.getScreenX();
                oldScreenY = event.getScreenY();
            }
        });

        //鼠标拖拽
        newScene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //新位置
                //拖拽前后的鼠标差值加上原始窗体坐标值
                primaryStage.setX(event.getScreenX() - oldScreenX + oldStageX);
                primaryStage.setY(event.getScreenY() - oldScreenY + oldStageY);
            }
        });


    }

    public static void main(String[] args) {
        launch(args);
    }
}