package com.qihan.finalFrontEnd;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
    public static String USER_ID;
    public static String USER_NAME;
    public static Stage STAGE_NOW;
    @FXML
    private Button loginButton;

    @FXML
    private Button exitButton;
    @FXML
    private Button registerButton;
    @FXML
    private Button userManagementButton;
    @FXML
    private Button guestModeButton;


    @FXML
    private Label loginMessageLabel;

    @FXML
    private Label timeNowLabel;
    @FXML
    private Label helpLabel;

    @FXML
    private ImageView brandingImageView;

    @FXML
    private ImageView lockImageView;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField enterPasswordField;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       //

        File brandingFile = new File("src\\main\\resources\\com\\qihan\\finalFrontEnd\\Images\\LoginLeft.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("src\\main\\resources\\com\\qihan\\finalFrontEnd\\Images\\LoginRight.jpg");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);

    }

    public void registerButtonOnAction(ActionEvent event) {
        directToRegisterPage();
    }

    public void guestModeButtonOnAction(ActionEvent event) {
        directToGuestPage();
    }

    public void userManagementButtonOnAction(ActionEvent event) {
        directToUserManagementPage();
    }

    public void loginButtonOnAction(ActionEvent event) {

        if (!usernameTextField.getText().isBlank() && !enterPasswordField.getText().isBlank()) {
            String user_name = usernameTextField.getText();
            String user_password = enterPasswordField.getText();
            USER_NAME = usernameTextField.getText();
            validateLogin(user_name, user_password);

        } else {
            loginMessageLabel.setFont(Font.font("Times New Roman"));
            loginMessageLabel.setText("Please enter username and password");
        }
    }

    public void exitButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    public void helpButtonOnAction(MouseEvent event) {
        helpLabel.setFont(Font.font("Times New Roman"));
        helpLabel.setText("you can do whatever you like since it's a toy");
    }

    public void timeNowButtonOnAction(MouseEvent event) {
        DateFormat currentTime = new SimpleDateFormat("yyyy.MM.dd hh:mm");    //设置时间格式

        timeNowLabel.setText(currentTime.format(new Date()));
    }

    public void timeNowButtonNoAction(MouseEvent event) {

        timeNowLabel.setFont(Font.font("Times New Roman"));
        timeNowLabel.setText("Pending your mouse here and you can see the time now.");
    }

    public static String buildMD5(String plainText) {

        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        //System.out.println(md5code);
        return md5code;
    }

    public void validateLogin(String s1, String s2) {
        MainPageDatabaseConnection connectNow = new MainPageDatabaseConnection();


        String ENCRYPTEDPASSWORD = MainPageController.buildMD5(s2);
        String verifyLogin = "SELECT * FROM `users` WHERE user_name = '" + s1 + "' AND user_encryptedpassword = '"
                + ENCRYPTEDPASSWORD + "'";
        System.out.println(s1);
        System.out.println(s2);
        Boolean hasResult;

        hasResult = connectNow.search(verifyLogin, true, true, true);

        if (hasResult) {
            //loginMessageLabel.setText("Congratulations!");
            directToCommonUserPage();
            System.out.println("aaa");
        } else {
            loginMessageLabel.setText("Invalid login!");
            System.out.println("bbb");

        }


    }

    private void directToCommonUserPage() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("CommonUserPage.fxml"));
            STAGE_NOW= new Stage();

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

    public void directToRegisterPage() {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("RegisterPage.fxml"));
            Stage registerStage = new Stage();
            USER_ID = null;
            USER_NAME = null;
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

    public void directToUserManagementPage() {
        Stage stage = (Stage) userManagementButton.getScene().getWindow();
        stage.close();

        try {
            USER_ID = null;
            USER_NAME = null;
            Parent root = FXMLLoader.load(getClass().getResource("UserManagementPage.fxml"));
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


    public void directToGuestPage() {
        Stage stage = (Stage) guestModeButton.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("GuestPage.fxml"));
            USER_ID = null;
            USER_NAME = null;
            STAGE_NOW= new Stage();
            STAGE_NOW.initStyle(StageStyle.UNDECORATED);
           // Stage registerStage = new Stage();
           // registerStage.initStyle(StageStyle.UNDECORATED);
            Scene newScene = new Scene(root, 520, 475);
            STAGE_NOW.setScene(newScene);
            STAGE_NOW.show();
           // registerStage.setScene(newScene);
           // registerStage.show();

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


}