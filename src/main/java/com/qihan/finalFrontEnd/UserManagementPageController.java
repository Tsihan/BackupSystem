package com.qihan.finalFrontEnd;

import com.qihan.finalCloudDataSystem.DeleteBucket;
import com.qihan.finalCloudDataSystem.ManipulateDownloadsRecord;
import com.qihan.finalCloudDataSystem.ManipulateUsersRecord;
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
import javafx.scene.control.PasswordField;
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
import java.util.regex.Pattern;

public class UserManagementPageController implements Initializable {

    public static Stage STAGE_NOW;

    @FXML
    private ImageView brandingImageView;

    @FXML
    private ImageView lockImageView;

    @FXML
    private Button resetPasswordButton;

    @FXML
    private Button resetUserNameButton;

    @FXML
    private Button resetUserEmailButton;

    @FXML
    private Button deleteUserAndCloudDataButton;

    @FXML
    private Button returnButton;


    @FXML
    private Label timeNowLabel;

    @FXML
    private Label PromptMessageLabel;


    @FXML
    private TextField useridTextField;


    @FXML
    private PasswordField passwordTextField;

    @FXML
    private PasswordField newpasswordPasswordField;


    @FXML
    private TextField newWordTextField;


    @FXML
    private TextField confirmTextField;


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
        timeNowLabel.setAlignment(Pos.CENTER);
        timeNowLabel.setText(currentTime.format(new Date()));
    }

    public void timeNowButtonNoAction(MouseEvent event) {

        timeNowLabel.setFont(Font.font("Times New Roman"));
        timeNowLabel.setText("Pending your mouse here and you can see the time now.");
    }

    public void deleteUserAndCloudDataButtonOnAction(ActionEvent event) {

//        deleteUserAndCloudDataButton.setFont(Font.font("Times New Roman"));
//        deleteUserAndCloudDataButton.setText("The function has not been realization");
        directToDeleteIrreversiblePage();

    }

    private void directToDeleteIrreversiblePage() {
        Stage stage = (Stage) deleteUserAndCloudDataButton.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("DeleteIrreversible.fxml"));
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

    public void resetUserEmailButtonOnAction(ActionEvent event) {
        //初始化
        PromptMessageLabel.setText("");
        if (useridTextField.getText().trim().isBlank()) {
            PromptMessageLabel.setText("You haven't entered user_id!");
            return;
        }

        if (passwordTextField.getText().trim().isBlank()) {
            PromptMessageLabel.setText("You haven't entered password!");
            return;
        }

        if (newWordTextField.getText().trim().isBlank()) {
            PromptMessageLabel.setText("You haven't entered a new email!");
            return;
        }

        //邮箱的格式
        if (!Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", newWordTextField.getText().trim())) {
            PromptMessageLabel.setText("You haven't enter a valid new email!");
            return;
        }
        String user_NEWemail = newWordTextField.getText().trim();
        String user_id = useridTextField.getText().trim();
        String user_encryptedpassword = ManipulateUsersRecord.buildMD5(passwordTextField.getText().trim());

        if (!confirmTextField.getText().trim().equals("reset")) {
            PromptMessageLabel.setText("The confirmation is incorrect!");
            return;
        } else {
            UserManagementPageDatabaseConnection connectNow = new UserManagementPageDatabaseConnection();
            Boolean hasResult;
            hasResult = connectNow.search(user_id, user_encryptedpassword);
            if (!hasResult) {
                PromptMessageLabel.setText("The provided information is invalid!");
                useridTextField.setText("");
                passwordTextField.setText("");
                newpasswordPasswordField.setText("");
                confirmTextField.setText("");
                newWordTextField.setText("");

            } else {
                //找到相关的记录
                try {
                    //更新记录
                    ManipulateUsersRecord user_infoUpdate = new ManipulateUsersRecord();

                    String sql = "update `users` set user_email ='" + user_NEWemail
                            + "' where user_id = '" + user_id + "'";
                    user_infoUpdate.update(sql);

                    PromptMessageLabel.setText("The username has been changed!");
                    useridTextField.setText("");
                    passwordTextField.setText("");
                    newpasswordPasswordField.setText("");
                    confirmTextField.setText("");
                    newWordTextField.setText("");


                } catch (Exception e) {
                    e.printStackTrace();
                    e.getCause();

                }
            }

        }

    }

    public void resetUserNameButtonOnAction(ActionEvent event) {

        //初始化
        PromptMessageLabel.setText("");
        if (useridTextField.getText().trim().isBlank()) {
            PromptMessageLabel.setText("You haven't entered user_id!");
            return;
        }

        if (passwordTextField.getText().trim().isBlank()) {
            PromptMessageLabel.setText("You haven't entered password!");
            return;
        }

        if (newWordTextField.getText().trim().isBlank()) {
            PromptMessageLabel.setText("You haven't entered a new username!");
            return;
        }

        //包含数字字母的6-32位字符串
        if (!Pattern.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", newWordTextField.getText().trim())) {
            PromptMessageLabel.setText("You haven't enter a valid new password!");
            return;
        }
        String user_NEWname = newWordTextField.getText().trim();
        String user_id = useridTextField.getText().trim();
        String user_encryptedpassword = ManipulateUsersRecord.buildMD5(passwordTextField.getText().trim());

        if (!confirmTextField.getText().trim().equals("reset")) {
            PromptMessageLabel.setText("The confirmation is incorrect!");
            return;
        } else {
            UserManagementPageDatabaseConnection connectNow = new UserManagementPageDatabaseConnection();
            Boolean hasResult;
            hasResult = connectNow.search(user_id, user_encryptedpassword);
            if (!hasResult) {
                PromptMessageLabel.setText("The provided information is invalid!");
                useridTextField.setText("");
                passwordTextField.setText("");
                newpasswordPasswordField.setText("");
                confirmTextField.setText("");
                newWordTextField.setText("");

            } else {
                //找到相关的记录
                try {
                    //更新记录
                    ManipulateUsersRecord user_infoUpdate = new ManipulateUsersRecord();

                    String sql = "update `users` set user_name ='" + user_NEWname
                            + "' where user_id = '" + user_id + "'";
                    user_infoUpdate.update(sql);

                    PromptMessageLabel.setText("The username has been changed!");
                    useridTextField.setText("");
                    passwordTextField.setText("");
                    newpasswordPasswordField.setText("");
                    confirmTextField.setText("");
                    newWordTextField.setText("");


                } catch (Exception e) {
                    e.printStackTrace();
                    e.getCause();

                }
            }

        }

    }

    public void resetPasswordButtonOnAction(ActionEvent event) {


        //初始化
        PromptMessageLabel.setText("");
        if (useridTextField.getText().trim().isBlank()) {
            PromptMessageLabel.setText("You haven't entered user_id!");
            return;
        }

        if (passwordTextField.getText().trim().isBlank()) {
            PromptMessageLabel.setText("You haven't entered password!");
            return;
        }

        if (newpasswordPasswordField.getText().trim().isBlank()) {
            PromptMessageLabel.setText("You haven't entered a new password!");
            return;
        }

        //至少10个字符，至少1个字母，1个数字和1个特殊字符
        if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{10,}$", newpasswordPasswordField.getText().trim())) {
            PromptMessageLabel.setText("You haven't enter a valid new password!");
            return;
        }


        String user_id = useridTextField.getText().trim();
        String user_encryptedpassword = ManipulateUsersRecord.buildMD5(passwordTextField.getText().trim());
        String user_NEWencryptedpassword = ManipulateUsersRecord.buildMD5(newpasswordPasswordField.getText().trim());


        if (!confirmTextField.getText().trim().equals("reset")) {
            PromptMessageLabel.setText("The confirmation is incorrect!");
            return;
        } else {
            UserManagementPageDatabaseConnection connectNow = new UserManagementPageDatabaseConnection();
            Boolean hasResult;
            hasResult = connectNow.search(user_id, user_encryptedpassword);
            if (!hasResult) {
                PromptMessageLabel.setText("The provided information is invalid!");
                useridTextField.setText("");
                passwordTextField.setText("");
                newpasswordPasswordField.setText("");
                confirmTextField.setText("");
                newWordTextField.setText("");

            } else {
                //找到相关的记录
                try {
                    //更新记录
                    ManipulateUsersRecord user_infoUpdate = new ManipulateUsersRecord();

                    String sql = "update `users` set user_encryptedpassword ='" + user_NEWencryptedpassword
                            + "' where user_id = '" + user_id + "'";
                    user_infoUpdate.update(sql);

                    PromptMessageLabel.setText("The password has been changed!");
                    useridTextField.setText("");
                    passwordTextField.setText("");
                    newpasswordPasswordField.setText("");
                    confirmTextField.setText("");
                    newWordTextField.setText("");


                } catch (Exception e) {
                    e.printStackTrace();
                    e.getCause();

                }
            }

        }


    }

    public void returnButtonOnAction(ActionEvent event) {

        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();

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


}
