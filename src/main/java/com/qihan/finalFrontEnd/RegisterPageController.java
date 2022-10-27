package com.qihan.finalFrontEnd;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import javafx.application.Platform;
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
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class RegisterPageController implements Initializable {

    @FXML
    private ImageView registerImageView;

    @FXML
    private Button closeButton;

    @FXML
    private Button returnButton;

    @FXML
    private Label registrationMessageLabel;

    @FXML
    private Label timeNowLabel;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private PasswordField confirmpasswordTextField;

    @FXML
    private Label confirmPasswordLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField useremailTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File registerFile = new File("src\\main\\resources\\com\\qihan\\finalFrontEnd\\Images\\Register.jpg");
        Image registerImage = new Image(registerFile.toURI().toString());
        registerImageView.setImage(registerImage);

    }


    public void registerButtonOnAction(ActionEvent event) {

        if (usernameTextField.getText().trim().isBlank()) {
            registrationMessageLabel.setText("You haven't enter a username!");
            return;
        }
        //包含数字字母的6-32位字符串
        if (!Pattern.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", usernameTextField.getText().trim())) {
            registrationMessageLabel.setText("You haven't enter a valid username form!");
            return;
        }

        if (useremailTextField.getText().trim().isBlank()) {

            registrationMessageLabel.setText("You haven't enter an email!");
            return;
        }

        if (!Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", useremailTextField.getText().trim())) {
            registrationMessageLabel.setText("You haven't enter a valid email form!");
            return;
        }

        if (passwordTextField.getText().trim().isBlank()) {

            registrationMessageLabel.setText("You haven't enter a password!");
            return;
        }

        //至少10个字符，至少1个字母，1个数字和1个特殊字符
        if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{10,}$", passwordTextField.getText().trim())) {
            registrationMessageLabel.setText("You haven't enter a valid password form!");
            return;
        }


        if (passwordTextField.getText().equals(confirmpasswordTextField.getText())) {
            String user_id = registerUser();
            passwordTextField.setText("");
            confirmpasswordTextField.setText("");
            usernameTextField.setText("");
            useremailTextField.setText("");
            usernameTextField.setText(user_id);

            registrationMessageLabel.setText("Successfully! keep a record of your user_id: " + user_id
                    + ". You can copy it in the user_name. ");
        } else {
            passwordTextField.setText("");
            confirmpasswordTextField.setText("");
            confirmPasswordLabel.setText("Password does not match!");
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

    public void closeButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);
    }

    public static String getRandomString(int length) {

        Random random = new Random();

        StringBuffer valSb = new StringBuffer();

        String charStr = "0123456789abcdefghijklmnopqrstuvwxyz";

        int charLength = charStr.length();


        for (int i = 0; i < length; i++) {

            int index = random.nextInt(charLength);

            valSb.append(charStr.charAt(index));

        }

        return valSb.toString();

    }

    public static Bucket getBucket(String bucket_name) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2).build();
        Bucket named_bucket = null;
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket b : buckets) {
            if (b.getName().equals(bucket_name)) {
                named_bucket = b;
            }
        }
        return named_bucket;
    }

    public static Bucket createBucket(String bucket_name) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2).build();
        Bucket b = null;
        if (s3.doesBucketExistV2(bucket_name)) {
            System.out.format("Bucket %s already exists.\n", bucket_name);
            b = getBucket(bucket_name);
        } else {
            try {
                b = s3.createBucket(bucket_name);
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
        return b;
    }


    public String registerUser() {
        RegisterPageDatabaseConnection connectNow = new RegisterPageDatabaseConnection();
        String user_name = usernameTextField.getText().trim();
        String user_email = useremailTextField.getText().trim();
        String user_password = passwordTextField.getText().trim();
        String user_encryptedpassword = MainPageController.buildMD5(user_password);
        String user_id = getRandomString(6);
        String s3_name = "user-" + user_id + "-bucket";
        String s3_lastmodifiedtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //同时需要写数据库并新增存储桶
        String sql1 = "Insert INTO `users` VALUES ('" + user_id + "','" + user_name + "','" + user_encryptedpassword +
                "','" + user_email + "')";
        String sql2 = "Insert INTO `s3s` VALUES ('" + s3_name + "','" + s3_lastmodifiedtime + "','" + user_id + "')";
        System.out.println(sql1);
        System.out.println(sql2);

        try {
            System.out.format("\nCreating S3 bucket: %s\n", s3_name);
            //这里需要解决模块依赖
            Bucket b = createBucket(s3_name);
            if (b == null) {
                System.out.println("Error creating bucket!\n");
            } else {
                System.out.println("Done!\n");
            }

            connectNow.update(sql1);
            connectNow.update(sql2);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();

        }
        return user_id;

    }


}
