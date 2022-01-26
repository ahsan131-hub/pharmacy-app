package DatabaseConnection;

import javafx.scene.control.Alert;

public class myAlerts {

    public static void alert(String type, String message) {
        Alert alert = null;
        if (type.equals("done")) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");

        } else if (type.equals("error")) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
        } else {
            return;
        }
        alert.setContentText(message);
        alert.show();
    }
}
