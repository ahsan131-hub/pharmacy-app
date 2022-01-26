package reciepts;

import DatabaseConnection.dbms;
import DatabaseConnection.myAlerts;
import DatabaseConnection.util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.validation.NumberValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

public class recieptController {
    ObservableList<recieptEntry> list = FXCollections.observableArrayList();
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private JFXTextField ItemName;
    @FXML
    private JFXTextField quantity;
    @FXML
    private JFXButton addBtn;
    @FXML
    private TableView<recieptEntry> TableView;
    @FXML
    private TableColumn<recieptEntry, String> nameCol;
    @FXML
    private TableColumn<recieptEntry, String> pricePerCol;
    @FXML
    private TableColumn<recieptEntry, String> quantityCol;
    @FXML
    private TableColumn<recieptEntry, String> TotalPriceCol;
    @FXML
    private JFXTextField customerName;
    @FXML
    private JFXRadioButton radioBtn1;
    @FXML
    private JFXRadioButton radioBtn2;
    @FXML
    private JFXRadioButton radioBtn3;
    @FXML
    private JFXRadioButton radioBtn0;
    @FXML
    private Label amount;
    @FXML
    private Label oldAmount;
    @FXML
    private JFXTextField recieveAmount;
    @FXML
    private Label returnAmount;
    @FXML
    private JFXButton printBtn;
    @FXML
    private JFXButton cancel;
    @FXML
    private JFXDatePicker date;
    ToggleGroup group = new ToggleGroup();
    @FXML
    void initialize() {
        assert ItemName != null : "fx:id=\"ItemName\" was not injected: check your FXML file 'reciept.fxml'.";
        assert quantity != null : "fx:id=\"quantity\" was not injected: check your FXML file 'reciept.fxml'.";
        assert addBtn != null : "fx:id=\"addBtn\" was not injected: check your FXML file 'reciept.fxml'.";
        assert TableView != null : "fx:id=\"TableView\" was not injected: check your FXML file 'reciept.fxml'.";
        assert nameCol != null : "fx:id=\"nameCol\" was not injected: check your FXML file 'reciept.fxml'.";
        assert pricePerCol != null : "fx:id=\"pricePerCol\" was not injected: check your FXML file 'reciept.fxml'.";
        assert quantityCol != null : "fx:id=\"quantityCol\" was not injected: check your FXML file 'reciept.fxml'.";
        assert TotalPriceCol != null : "fx:id=\"TotalPriceCol\" was not injected: check your FXML file 'reciept.fxml'.";
        assert customerName != null : "fx:id=\"customerName\" was not injected: check your FXML file 'reciept.fxml'.";
        assert radioBtn1 != null : "fx:id=\"radioBtn1\" was not injected: check your FXML file 'reciept.fxml'.";
        assert radioBtn2 != null : "fx:id=\"radioBtn2\" was not injected: check your FXML file 'reciept.fxml'.";
        assert radioBtn3 != null : "fx:id=\"radioBtn3\" was not injected: check your FXML file 'reciept.fxml'.";
        assert amount != null : "fx:id=\"amount\" was not injected: check your FXML file 'reciept.fxml'.";
        assert oldAmount != null : "fx:id=\"oldAmount\" was not injected: check your FXML file 'reciept.fxml'.";
        assert recieveAmount != null : "fx:id=\"recieveAmount\" was not injected: check your FXML file 'reciept.fxml'.";
        assert returnAmount != null : "fx:id=\"returnAmount\" was not injected: check your FXML file 'reciept.fxml'.";
        assert printBtn != null : "fx:id=\"printBtn\" was not injected: check your FXML file 'reciept.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'reciept.fxml'.";

        setToggleGroup();
        intializeCol();
        util.setAutoCompleteItems(ItemName,"Select * from stock",null,null,null);
        util.setAutoCompleteCustomer(customerName,"select * from customers");
        NumberValidator numberValidator=new NumberValidator();
        numberValidator.setMessage("Only Number");
        util.validateTextField(quantity,numberValidator,addBtn);
        printBtn.setDisable(true);
    }

    private void intializeCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Item_name"));
        pricePerCol.setCellValueFactory(new PropertyValueFactory<>("price_item"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TotalPriceCol.setCellValueFactory(new PropertyValueFactory<>("total_price"));
        TableView.setItems(list);
    }

    private void setToggleGroup() {

        radioBtn1.setToggleGroup(group);
        radioBtn1.setUserData("1");
        radioBtn0.setToggleGroup(group);
        radioBtn0.setUserData("0");
        radioBtn2.setToggleGroup(group);
        radioBtn2.setUserData("5");
        radioBtn3.setToggleGroup(group);
        radioBtn3.setUserData("10");
        radioBtn1.setSelected(true);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    System.out.println(group.getSelectedToggle().getUserData().toString());
                    int dis= Integer.parseInt(group.getSelectedToggle().getUserData().toString());
                    float price=getPrice(dis);


                    amount.setText(String.valueOf(price));

                }
            }
        });

        recieveAmount.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                // do what is to do
                float revc= Float.parseFloat(recieveAmount.getText());
                float amountText= Float.parseFloat(amount.getText());
                returnAmount.setText(String.valueOf(revc-amountText));
                printBtn.setDisable(false);
                if (revc-amountText<0){
                    //insert remaining Balance
                    if(ValidateCustomer(customerName.getText())==null){
                        printBtn.setDisable(true);
                        myAlerts.alert("error","Provide Customer Name");
                        recieveAmount.clear();
                    }

                }
            }
        });
    }

    @FXML
    void onAddAction(ActionEvent event) {
        if(ItemName.getText().equals("")||(!ItemName.getText().contains("|"))||quantity.getText().equals("")||quantity.getText().equals("0")){
            return;
        }
        var Item_name=ItemName.getText();
        String[] arr=Item_name.split("|");
        int itemId= Integer.parseInt(arr[0]);
        int q= Integer.parseInt(quantity.getText());
        String qr="SELECT * FROM stock where id="+itemId;
        ResultSet rs= dbms.getResultSet(qr);
        while (true){
            try {
                if (!rs.next()) break;
            //    var total_box=rs.getInt("total_box");
                var total_item=rs.getInt("totalQuantity");
                var price=(rs.getInt("pricePerItem"));
                var totalPrice=q*price;
                list.add(new recieptEntry(Item_name,String.valueOf(q),String.valueOf(price),String.valueOf(totalPrice)));
                String updateQuery="";
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        float index = 0;
        amount.setText(String.valueOf(getPrice((int) index)));
        ItemName.clear();
        quantity.clear();
    }


    @FXML
    void onPrintAction(ActionEvent event) {
        var Item_name  =ItemName.getText();
        var quantityText  =quantity.getText();
        var custName   ="Regular";
        float recvAmount   = Float.parseFloat(recieveAmount.getText());
        var rAmount   =returnAmount.getText();
        var amountText    =amount.getText();
        custName   =customerName.getText();


        var oldamount    =getOldAmount(custName);
        int indexDis=0;
        int Dis= Integer.parseInt(group.getSelectedToggle().getUserData().toString());
        float totalAmount = getPrice(Dis);
        float ramount=recvAmount-totalAmount;

        amount.setText(String.valueOf(totalAmount));

        String creatRecipt="INSERT INTO receipt(" +
                "customerName," +
                "itemsInBox," +
                "discountPercent," +
                "recieptDate," +
                "totalReturn," +
                "totalRec," +
                "totalAmount," +
                "noOfITem)  VALUES(" +
                   "'" + custName+"'," +
                   "'" +quantityText+"'," +
                   "'" + Dis+"'," +
                   "'" +getDateTime()+"'," +
                   "'" + ramount+"'," +
                   "'" + recvAmount+"'," +
                   "'" + totalAmount+"'," +
                   "'" + list.size()+"'" +
                ")";
        if(dbms.executeQuery(creatRecipt)){
            if(ramount<0){
                updateCustomerBalance(ValidateCustomer(custName),ramount);
            }

            String qr="SELECT * FROM receipt WHERE id=(SELECT max(id) FROM receipt)";
            ResultSet rs=dbms.getResultSet(qr);
            int id=-1;
            while (true) {
                try {
                    if (!rs.next()) break;
                    id=rs.getInt("id");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            float cost=0;
            for (int i = 0; i < list.size(); i++) {
                int Itemid= Integer.parseInt(list.get(i).getItem_name().split("|")[0]);
                var q=Integer.parseInt(list.get(i).getQuantity());
                String creatItem="INSERT INTO items(" +
                        "item," +
                        "receiptNo," +
                        "quantity" +
                        ")  VALUES(" +
                        "'" +Itemid+"'," +
                        "'" +id+"'," +
                        "'" +q+"'" +
                        ")";
                if(dbms.executeQuery(creatItem)){
                    System.out.println("creatte item " + Itemid +" "  + id);
                    cost=cost+updateItem(Itemid,q);
                }
            }
            if(dbms.executeQuery("update receipt set cost='"+cost+"' where id="+id)){
                System.out.println("cost of reciept updated");
            }
            myAlerts.alert("done","successfully " + ramount);
            TableView.getItems().clear();
        }else {
            myAlerts.alert("error","Unsuccessfully " + ramount);
        }
    }

    private float updateItem(int itemid, int q) {
        String qr="select * from stock where id="+itemid;
        ResultSet rs=dbms.getResultSet(qr);
        int newQuantity=-1;
        float cost = 0;
        while (true) {
            try {
                if (!rs.next()) break;
                int quantity= Integer.parseInt(rs.getString("totalQuantity"));
                cost=rs.getFloat("costPerBox");
                newQuantity=quantity-q;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        String qr2="update stock set totalQuantity='"+newQuantity+"' where id="+itemid;
        if (dbms.executeQuery(qr2)){
            System.out.println("customer updated" + itemid);
            return cost*q;
        }else {
            System.out.println("customer NOT updated" + itemid);
        }
        return cost;
    }

    private void updateCustomerBalance(ResultSet resultSet, float ramount) {
        float newAmount= 0.0F;
        int id=-1;
        while (true){
            try {
                if (!resultSet.next()) break;
                var oldamount=resultSet.getInt("amountRemaining");
                id=resultSet.getInt("id");
                newAmount=oldamount+ramount;

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        String qr="update customers set amountRemaining='"+newAmount+"' where id="+id;
        if (dbms.executeQuery(qr)){
            System.out.println("customer updated" + id);
        }else {
            System.out.println("customer NOT updated" + id);
        }
    }

    private ResultSet ValidateCustomer(String custName) {
        if(!custName.trim().equals("")){
            custName=custName.split("|")[0];
            String qr="Select * from customers where id="+custName;
            ResultSet rs=dbms.getResultSet(qr);
            if(rs!=null){
                return rs;
            }

        }

        return null;
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("MMM/dd/yyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    private float getOldAmount(String custName) {
        String qr="SELECT * FROM customers";
        ResultSet rs=dbms.getResultSet(qr);
        float amount = 0;
        while (true){
            try {
                if (!rs.next()) break;
                amount=rs.getInt("amountRemaining");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return amount;
    }

    private float getPrice(float Dis) {
        float totalAmount=0;
        for (int i = 0; i < list.size(); i++) {

            totalAmount+=Integer.parseInt(list.get(i).total_price);
        }
        totalAmount=totalAmount-(totalAmount/100)*Dis;
        return totalAmount;
    }

    @FXML
    void mergePreviousBalance(ActionEvent event) {

    }
    public static class recieptEntry {
      String Item_name ="";
      String quantity="";
      String price_item="";
      String total_price="";

        public recieptEntry(String item_name, String quantity, String price_item, String total_price) {
            Item_name = item_name;
            this.quantity = quantity;
            this.price_item = price_item;
            this.total_price = total_price;
        }

        public String getItem_name() {
            return Item_name;
        }

        public void setItem_name(String item_name) {
            Item_name = item_name;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getPrice_item() {
            return price_item;
        }

        public void setPrice_item(String price_item) {
            this.price_item = price_item;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }
    }

}
