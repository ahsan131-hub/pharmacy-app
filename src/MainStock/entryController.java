package MainStock;

import DatabaseConnection.*;
import DatabaseConnection.myAlerts;
import StockList.stocklistController;
import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import com.jfoenix.controls.*;
import com.jfoenix.validation.NumberValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class entryController {

    @FXML
    private JFXComboBox<String> category_list;

    @FXML
    private JFXTextField Item_Name;

    @FXML
    private Label     result;
    @FXML
    private JFXTextField mg;
//    @FXML
//    private JFXTextField totalCostPrice;

    @FXML
    private JFXTextField profit_percent;

    @FXML
    private JFXTextField formula;

    @FXML
    private JFXTextField price_box;

    @FXML
    private JFXTextField no_of_box;

    @FXML
    private JFXTextField company_name;

    @FXML
    private JFXTextField no_of_item;

    @FXML
    private JFXTextField sub_category;

    @FXML
    private JFXDatePicker mfg_date;

    @FXML
    private JFXDatePicker exp_date;
    @FXML
    private JFXButton saveBtn;
    private int onEditeId=-1;

    @FXML
    void onCancel(ActionEvent event) {
        Stage stage= (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onCatSelection(ActionEvent event) {

    }

    @FXML
    void onSave(ActionEvent event) {
        var category=category_list.getSelectionModel().getSelectedItem();
        var name = Item_Name.getText();
        String str[]=name.split(" ");
        name=str[2];
        float box_price=0;
        var sub_cat=sub_category.getText();
        var company=company_name.getText();
        int noOfBox = 0;
        int noOfItem=0;
        var tabletMg=mg.getText();
        float costPerBox=0.0F;
        var expDate=exp_date.getEditor().getText();
        var mfgDate=mfg_date.getEditor().getText();
        float percent=0.0F;
        var tabletFormula = formula.getText();
        if(!(price_box.getText().equals("")&&profit_percent.getText().equals(""))){
            costPerBox=Float.parseFloat(price_box.getText());
            percent=Float.parseFloat(profit_percent.getText());
            box_price=getPercent(costPerBox,percent)+costPerBox;
            System.out.println("cost Price" + costPerBox);
            System.out.println("box Price" + box_price);
        }else {
            return;
        }
        if(!no_of_box.getText().equals("")){
            noOfBox = Integer.parseInt(no_of_box.getText());
        }else {
            noOfBox=0;
        }
        if(!no_of_item.getText().equals("")){
            noOfItem= Integer.parseInt(no_of_item.getText());
        }else {
            noOfItem=0;
        }
        float price_per_item=0;

        if(noOfBox==0  && noOfItem==0){
            price_per_item=0;
        }else  if(noOfItem==0){
            price_per_item=(float) (box_price/noOfBox);
        }else if(noOfItem!=0){
            price_per_item=(float)box_price/noOfItem;
        }
        int totalItem=noOfBox*noOfItem;
        //for cost price for on each item
        costPerBox=price_per_item-getPercent(price_per_item,percent);
        System.out.println(price_per_item);
        if(category==""||name.equals("")||price_per_item==0){
            myAlerts.alert("error","Fill Name, Price and Select category");
            result.setText("ERROR");
            return;
        }

        String query="insert into stock(" +
                "name ," +
                "price ," +
                "quantityBox," +
                "itemsInBox," +
                "companyName," +
                "formula," +
                "mg," +
                "category," +
                "subCategory," +
                "pricePerItem," +
                "expDate, " +
                "mfgDate," +
                "totalQuantity," +
                "costPerBox) values(" +
                "'" + name+"'," +
                "'" +box_price+"'," +
                "'" + noOfBox+"'," +
                "'" + noOfItem+"'," +
                "'" + company+"'," +
                "'" + tabletFormula+"'," +
                "'" + tabletMg+"'," +
                "'" +category+"'," +
                "'" + sub_cat+"'," +
                "'" + price_per_item+"'," +
                "'" + expDate+"'," +
                "'" + mfgDate+"'," +
                "'" + totalItem+"'," +
                "'" + costPerBox+"'" +

                ")";
        if (onEditeId!=-1){
            query="update stock set " +
                    "name="+  "'" + name+"',"  +
                    "price="+"'"+box_price+"',"+
                    "quantityBox="+"'"+noOfBox+"',"+
                    "itemsInBox="+"'"+noOfItem+"',"+
                    "companyName="+"'"+company+"',"+
                    "formula="+"'"+tabletFormula+"',"+
                    "mg="+"'"+tabletMg+"',"+
                    "category="+"'"+category+"',"+
                    "subCategory="+"'"+sub_cat+"',"+
                    "pricePerItem="+"'"+price_per_item+"',"+
                    "expDate="+"'"+expDate+"',"+
                    "mfgDate="+"'"+mfgDate+"',"+
                    "totalQuantity="+"'"+totalItem+"',"+
                    "costPerBox="+"'"+costPerBox+"'"+

                    "where id="+onEditeId;
        }

        if(dbms.executeQuery(query)){
             result.setText("Entry Added");
            category_list.getSelectionModel().clearSelection();
            Item_Name.setText("");
            company_name.setText("");
            mg.setText("");
            exp_date.getEditor().setText("");
            mfg_date.getEditor().setText("");
            formula.setText("");
            price_box.setText("");
            no_of_box.setText("");
            no_of_item.setText("");
            sub_category.setText("");
            result.setText("Entry Added");
        }
        else {
            result.setText("ERROR");
        }
    }

    private float getPercent(float c, float p) {
        return (c*p/100);
    }


    @FXML
    void initialize() {
        category_list.setPromptText("Select Item");
        category_list.getItems().add("Medicine");
        category_list.getItems().add("Surgical");
        category_list.getItems().add("Cosmetics");
        //select data from stock to show autocompletion
        String qr="SELECT * FROM stock";
        util.setAutoCompleteItems(Item_Name,qr,formula,company_name,sub_category);

        NumberValidator numberValidator=new NumberValidator();
        numberValidator.setMessage("Only Number");
        util.validateTextField(price_box,numberValidator,saveBtn);
        util.validateTextField(no_of_box,numberValidator,saveBtn);
        util.validateTextField(no_of_item,numberValidator,saveBtn);
        util.validateTextField(mg,numberValidator,saveBtn);
       // util.validateTextField(totalCostPrice,numberValidator,saveBtn);
        util.validateTextField(profit_percent,numberValidator,saveBtn);
    }

/*
    private void setAutoCompleteItems(JFXTextField item_name,String qr) {
        ObservableList<String> data = FXCollections.observableArrayList();
        //ObservableList<String> filteredList = FXCollections.observableArrayList();


        var rs=dbms.getResultSet(qr);
        while (true){
            try {
                if (!rs.next()) break;
                var str=rs.getString("name")+ " (" +rs.getString("mg") +")mg";
                data.add(str);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.getSuggestions().addAll(data);
        autoCompletePopup.setSelectionHandler(event -> {
            item_name.setText(event.getObject());
        });
        item_name.textProperty().addListener(observable -> {
            //The filter method uses the Predicate to filter the Suggestions defined above
            //I choose to use the contains method while ignoring cases
            autoCompletePopup.filter(item -> item.toLowerCase().contains(item_name.getText().toLowerCase()));
            //Hide the autocomplete popup if the filtered suggestions is empty or when the box's original popup is open
            if (autoCompletePopup.getFilteredSuggestions().isEmpty()) {
                autoCompletePopup.hide();
            }else {
                try{
                    autoCompletePopup.show(item_name);
                }catch(Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
        });

    }
**/
    
    public void setValues(stocklistController.Entry entry) {

        category_list.getSelectionModel().select(entry.getCatCol());
        company_name.setText(entry.getCompanyCol());
        no_of_item.setText(String.valueOf(entry.getItemqCol()));
        no_of_box.setText(String.valueOf(entry.getBoxqCol()));
        mg.setText(String.valueOf(entry.getMg()));
        formula.setText(entry.getFormulaCol());
        exp_date.getEditor().setText(entry.getExpCol());
        mfg_date.getEditor().setText(entry.getMfgCol());
        Item_Name.setText(entry.getNameCol());
        sub_category.setText(entry.getSubCat());
        onEditeId= entry.getId();

    }
}
