package com.qihan.finalFrontEnd;

import com.amazonaws.services.s3.model.Bucket;
import com.qihan.finalCloudDataSystem.DeleteBucket;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class DeleteIrreversiblePageController implements Initializable {

    public static Stage STAGE_NOW;
    @FXML
    private ImageView brandingImageView;

    @FXML
    private ImageView lockImageView;


    @FXML
    private Button deleteUserAndCloudDataButton;

    @FXML
    private Button returnButton;

    @FXML
    private TextField idTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField confirmTextField;


    @FXML
    private Label messageLabel;

    @FXML
    private Label timeNowLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("src\\main\\resources\\com\\qihan\\finalFrontEnd\\Images\\DeleteUserAndCloudDataPageLeft.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("src\\main\\resources\\com\\qihan\\finalFrontEnd\\Images\\DeleteUserAndCloudDataPageRight.jpg");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);


    }


    public void timeNowButtonOnAction(MouseEvent event) {
        DateFormat currentTime = new SimpleDateFormat("yyyy.MM.dd hh:mm");    //设置时间格式

        timeNowLabel.setText(currentTime.format(new Date()));
    }

    public void timeNowButtonNoAction(MouseEvent event) {

        timeNowLabel.setFont(Font.font("Times New Roman"));
        timeNowLabel.setText("Pending your mouse here and you can see the time now.");
    }

    public void deleteUserAndCloudDataButtonOnAction(ActionEvent event) {
        String user_id;
        String user_email;
        if (idTextField.getText().trim().isBlank()) {
            messageLabel.setText("You haven't entered user_id!");
            return;
        }

        if (emailTextField.getText().trim().isBlank()) {
            messageLabel.setText("You haven't entered email_address!");
            return;
        }

        if (!confirmTextField.getText().trim().equals("The past is past")) {
            messageLabel.setText("The confirmation is incorrect!");
            return;
        }

        user_id = idTextField.getText().trim();
        user_email = emailTextField.getText().trim();

        DeleteIrreversiblePageDatabaseConnection connectNow = new DeleteIrreversiblePageDatabaseConnection();

        Boolean hasResult;
        hasResult = connectNow.search(user_id,user_email);
        if(!hasResult){
            messageLabel.setText("The provided information is invalid!");
            idTextField.setText("");
            emailTextField.setText("");
            confirmTextField.setText("");
            return;
        }

        try {

            //这里需要解决模块依赖
            DeleteBucket.deleteBucketAndSql(user_id);
            messageLabel.setText("Fading is true while flowering is past.");
            idTextField.setText("");
            emailTextField.setText("");
            confirmTextField.setText("");


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();

        }

    }

    private void directToDeleteUserAndCloudDataPage() {
        Stage stage = (Stage) deleteUserAndCloudDataButton.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("DeleteUserAndCloudDataPage.fxml"));
            STAGE_NOW = new Stage();

            //Stage registerStage = new Stage();
            STAGE_NOW.initStyle(StageStyle.UNDECORATED);
            // registerStage.initStyle(StageStyle.UNDECORATED);
            Scene newScene = new Scene(root, 520, 475);
            STAGE_NOW.setScene(newScene);
            STAGE_NOW.show();
            //registerStage.setScene(newScene);
            //registerStage.show();

            //        鼠标按下事件
            newScene.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    FrontEndApplication.oldStageX = STAGE_NOW.getX();
                    FrontEndApplication.oldStageY = STAGE_NOW.getY();
//                    FrontEndApplication.oldStageX = registerStage.getX();
//                    FrontEndApplication.oldStageY = registerStage.getY();
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
                    STAGE_NOW.setX(event.getScreenX() - FrontEndApplication.oldScreenX + FrontEndApplication.oldStageX);
                    STAGE_NOW.setY(event.getScreenY() - FrontEndApplication.oldScreenY + FrontEndApplication.oldStageY);
//                    registerStage.setX(event.getScreenX() - FrontEndApplication.oldScreenX + FrontEndApplication.oldStageX);
//                    registerStage.setY(event.getScreenY() - FrontEndApplication.oldScreenY + FrontEndApplication.oldStageY);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }


    public void returnButtonOnAction(ActionEvent event) {

        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("DeleteUserAndCloudDataPage.fxml"));
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
