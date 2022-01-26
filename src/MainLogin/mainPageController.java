package MainLogin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mainPageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void customerListAction(ActionEvent event) {
        String str="/customers/customerList.fxml";
        showPage(str);
    }

    @FXML
    void newCustomerAction(ActionEvent event) {
        String str="/customers/createCustomer.fxml";
        showPage(str);
    }

    @FXML
    void onMakeRecieptAction(ActionEvent event) {
        String str="/reciepts/reciept.fxml";
        showPage(str);
    }



    @FXML
    void onRecieptListAction(ActionEvent event) {
        String str="/reciepts/recieptList.fxml";
        showPage(str);
    }

    @FXML
    void onStockEntryAction(ActionEvent event) {
        String str="/MainStock/entry.fxml";
        showPage(str);
    }

    @FXML
    void onStockListAction(ActionEvent event) {
        String str="/StockList/stocklist.fxml";
        showPage(str);
    }

    @FXML
    void onRevenueAction(ActionEvent event) {
        showPage("/viewRevenue/revenue.fxml");
    }
    private void showPage(String str) {
        Parent newScene= null;
        try {
            newScene = FXMLLoader.load(getClass().getResource(str));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage=new Stage();
        stage.setScene(new Scene(newScene));
        stage.show();
    }
    @FXML
    void initialize() {

    }
}
