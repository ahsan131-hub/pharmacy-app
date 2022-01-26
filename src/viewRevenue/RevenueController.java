package viewRevenue;

import DatabaseConnection.dbms;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class RevenueController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField searchValue;

    @FXML
    private TableView<?> tableView;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TableColumn<?, ?> phoneCol;

    @FXML
    private TableColumn<?, ?> address;

    @FXML
    private TableColumn<?, ?> Balance;
    private Object String;

    @FXML
    void searchAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert searchValue != null : "fx:id=\"searchValue\" was not injected: check your FXML file 'revenue.fxml'.";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'revenue.fxml'.";
        assert idCol != null : "fx:id=\"idCol\" was not injected: check your FXML file 'revenue.fxml'.";
        assert nameCol != null : "fx:id=\"nameCol\" was not injected: check your FXML file 'revenue.fxml'.";
        assert phoneCol != null : "fx:id=\"phoneCol\" was not injected: check your FXML file 'revenue.fxml'.";
        assert address != null : "fx:id=\"address\" was not injected: check your FXML file 'revenue.fxml'.";
        assert Balance != null : "fx:id=\"Balance\" was not injected: check your FXML file 'revenue.fxml'.";
        ////     float cost=getCostPrice();
    }
}

//
//    private float getCostPrice() {
//        //Feb/26/2021 00:00:41
//        String qr="SELECT * FROM receipt WHERE id=(SELECT max(id) FROM receipt)";
//        ResultSet rs= dbms.getResultSet(qr);
//        String date="";
//        while (true) {
//            try {
//                if (!rs.next()) break;
//                date=rs.getString("recieptDate");
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//        }
//        var s=date.split("/");
//        int day= Integer.parseInt(s[2]);
//        day=day-1;
//        String s1=s[0]+"/"+day+"/"+s[2];+
//        String str="SELECT * FROM receipt";
//        rs= dbms.getResultSet(str);
//        while (true){
//            try {
//                if (!rs.next()) break;
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//
//        }
//        return 0;
//    }
//}
