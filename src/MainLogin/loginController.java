package MainLogin;

import com.jfoenix.controls.JFXButton;
import DatabaseConnection.dbms;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField username_id;

    @FXML
    private JFXButton close_btn;

    @FXML
    private PasswordField password_id;

    @FXML
    void onClose(ActionEvent event) {

    }

    @FXML
    void onLogin(ActionEvent event) {
        try {
            new dbms();
            Parent newScene= FXMLLoader.load(getClass().getResource("/MainLogin/mainPage.fxml"));
            Stage stage=new Stage();
            stage.setScene(new Scene(newScene,600,400));
            stage.show();

            Stage stage3= (Stage) password_id.getScene().getWindow();
            stage3.close();
   } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert username_id != null : "fx:id=\"username_id\" was not injected: check your FXML file 'login.fxml'.";
        assert close_btn != null : "fx:id=\"close_btn\" was not injected: check your FXML file 'login.fxml'.";
        assert password_id != null : "fx:id=\"password_id\" was not injected: check your FXML file 'login.fxml'.";

    }
}
