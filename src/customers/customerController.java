package customers;

import DatabaseConnection.dbms;
import DatabaseConnection.myAlerts;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class customerController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField custName;

    @FXML
    private JFXTextField phoneNo;

    @FXML
    private JFXTextField cnicNo;

    @FXML
    private JFXTextField address;

    @FXML
    void onAddAction(ActionEvent event) {
        String qr="Insert into customers(" +
                "customerName   ," +
                "phoneNO        ," +
                "cnic           ," +
                "Address        ," +
                "amountRemaining)" +
                "values(" +
                "'" + custName.getText()+"',"+
                "'" + phoneNo.getText()+"',"+
                "'" + cnicNo.getText()+"',"+
                "'" + address.getText()+"',"+
                "'" + 0+"'"+
                ")";


        if(dbms.executeQuery(qr)){
            myAlerts.alert("done" ,"Cusomer is added to the list");
            custName.clear();
            phoneNo.clear();
            cnicNo.clear();
            address.clear();
        }else {
            myAlerts.alert("error" ,"Cusomer is added to the list");
        }
    }

    @FXML
    void oncloseAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert custName != null : "fx:id=\"custName\" was not injected: check your FXML file 'createCustomer.fxml'.";
        assert phoneNo != null : "fx:id=\"phoneNo\" was not injected: check your FXML file 'createCustomer.fxml'.";
        assert cnicNo != null : "fx:id=\"cnicNo\" was not injected: check your FXML file 'createCustomer.fxml'.";
        assert address != null : "fx:id=\"address\" was not injected: check your FXML file 'createCustomer.fxml'.";

    }
}
