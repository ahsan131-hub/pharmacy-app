package reciepts;

import DatabaseConnection.dbms;
import MainStock.entryController;
import StockList.stocklistController;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RecieptListController {
    ObservableList<recieptList> list = FXCollections.observableArrayList();
   // ObservableList<stocklistController.Entry> explist = FXCollections.observableArrayList();
    FilteredList<recieptList> filteredList = new FilteredList(list, e -> true);
    SortedList sort =new SortedList(filteredList);
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField searchValue;

    @FXML
    private TableView<recieptList> tableView;

    @FXML
    private TableColumn<recieptList,String> nameCol;

    @FXML
    private TableColumn<recieptList,String> id;

    @FXML
    private TableColumn<recieptList,Date> dateCol;

    @FXML
    private TableColumn<recieptList,String> totalQuantityCol;

    @FXML
    private TableColumn<recieptList,String> amountCol;

    @FXML
    void onEditAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert searchValue  != null : "fx:id=\"searchValue\" was not injected: check your FXML file 'recieptList.fxml'.";
        assert tableView    != null : "fx:id=\"tableView\" was not injected: check your FXML file 'recieptList.fxml'.";
        assert nameCol      != null : "fx:id=\"nameCol\" was not injected: check your FXML file 'recieptList.fxml'.";
        assert id   != null : "fx:id=\"formulaCol\" was not injected: check your FXML file 'recieptList.fxml'.";
        assert dateCol      != null : "fx:id=\"dateCol\" was not injected: check your FXML file 'recieptList.fxml'.";
        assert totalQuantityCol != null : "fx:id=\"totalQuantityCol\" was not injected: check your FXML file 'recieptList.fxml'.";
        assert amountCol    != null : "fx:id=\"amountCol\" was not injected: check your FXML file 'recieptList.fxml'.";
        initializeCol();
        loadData();
    }
    @FXML
    void viewMore(ActionEvent event) {
//        stocklistController.Entry entry=tableView.getSelectionModel().getSelectedItem();
//        if(entry==null){
//            return;
//
//        }
//        FXMLLoader loader=new FXMLLoader(getClass().getResource("/MainStock/entry.fxml"));
//        Parent root= null;
//        try {
//            root = loader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        entryController ref=(entryController) loader.getController();
//        ref.setValues(entry);
//        // librarymanageUtil.setIcon(stg);
//        Stage stg=new Stage(StageStyle.DECORATED);
//        Scene scene =new Scene(root);
//        stg.setTitle("details ");
//        stg.setScene(scene);
//        stg.show();
    }
    private void initializeCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nameCol"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        totalQuantityCol.setCellValueFactory(new PropertyValueFactory<>("totalQuantityCol"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateCol"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amountCol"));

    }
    private void loadData() {
        String q = "SELECT * FROM receipt";
        ResultSet rs = dbms.getResultSet(q);
        Date date=null;
        while (true) {
            try {
                if (!rs.next()) break;
                var  nameCol= rs.getString("customerName");
                String  dateCol = String.valueOf(rs.getString("recieptDate"));
                String  totalQuantityCol  = String.valueOf(rs.getInt("noOfITem"));
                String  amountCol  = String.valueOf(rs.getInt("totalAmount"));
                String id=rs.getString("id");
                list.add(new recieptList(id,nameCol,dateCol,totalQuantityCol,amountCol));

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        tableView.setItems(list);
    }
    @FXML
    void searchAction(ActionEvent event) {
        System.out.println("hitted the key");
        searchValue.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate((Predicate<? super recieptList>) (recieptList std) -> {
                String lowerCase = newValue.toLowerCase();
                if (newValue.isEmpty() || newValue == null) {
                    return true;
                } else if (std.getId().indexOf(lowerCase) != -1) {
                    return true;
                } else if (std.getNameCol().indexOf(lowerCase) != -1) {
                    return true;
                } else if (std.getDateCol().indexOf(lowerCase) != -1) {
                    return true;
                }
                return false;
            });

        });


        SortedList sort = new SortedList(filteredList);
        sort.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sort);
    }


    public static class recieptList{
        String nameCol;
        String id;

        String   dateCol;
        String totalQuantityCol;
        String amountCol;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public recieptList(String id, String nameCol, String dateCol, String totalQuantityCol, String amountCol) {
            this.nameCol = nameCol;
            this.id = id;

            this.dateCol = dateCol;
            this.totalQuantityCol = totalQuantityCol;
            this.amountCol = amountCol;
        }

        public String getNameCol() {
            return nameCol;
        }

        public void setNameCol(String nameCol) {
            this.nameCol = nameCol;
        }


        public String getDateCol() {
            return dateCol;
        }

        public void setDateCol(String dateCol) {
            this.dateCol = dateCol;
        }

        public String getTotalQuantityCol() {
            return totalQuantityCol;
        }

        public void setTotalQuantityCol(String totalQuantityCol) {
            this.totalQuantityCol = totalQuantityCol;
        }

        public String getAmountCol() {
            return amountCol;
        }

        public void setAmountCol(String amountCol) {
            this.amountCol = amountCol;
        }
    }
}
