package customers;

import DatabaseConnection.dbms;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import reciepts.RecieptListController;

public class customerListController {
    ObservableList<customer> list = FXCollections.observableArrayList();
    // ObservableList<stocklistController.Entry> explist = FXCollections.observableArrayList();
    FilteredList<customer> filteredList = new FilteredList(list, e -> true);
    SortedList sort =new SortedList(filteredList);

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField searchValue;

    @FXML
    private TableView<customer> tableView;

    @FXML
    private TableColumn<customer,String> idCol;

    @FXML
    private TableColumn<customer,String> nameCol;

    @FXML
    private TableColumn<customer,String> phoneCol;

    @FXML
    private TableColumn<customer,String> address;

    @FXML
    private TableColumn<customer,Double> Balance;


    @FXML
    void initialize() {
        assert searchValue != null : "fx:id=\"searchValue\" was not injected: check your FXML file 'customerList.fxml'.";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'customerList.fxml'.";
        assert idCol    != null : "fx:id=\"idCol\" was not injected: check your FXML file 'customerList.fxml'.";
        assert nameCol   != null : "fx:id=\"nameCol\" was not injected: check your FXML file 'customerList.fxml'.";
        assert phoneCol  != null : "fx:id=\"phoneCol\" was not injected: check your FXML file 'customerList.fxml'.";
        assert address   != null : "fx:id=\"address\" was not injected: check your FXML file 'customerList.fxml'.";
        assert Balance  != null : "fx:id=\"Balance\" was not injected: check your FXML file 'customerList.fxml'.";
        initializeCol();
        loadData();
    }

    private void initializeCol() {
        nameCol .setCellValueFactory(new PropertyValueFactory<>("idCol"));
        idCol   .setCellValueFactory(new PropertyValueFactory<>("nameCol"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneCol"));
        address .setCellValueFactory(new PropertyValueFactory<>("address"));
        Balance .setCellValueFactory(new PropertyValueFactory<>("Balance"));

    }
    private void loadData() {
        String q = "SELECT * FROM customers";
        ResultSet rs = dbms.getResultSet(q);
        Date date=null;
        while (true) {
            try {
                if (!rs.next()) break;
                var  name= rs.getString("customerName");
                String  phone = String.valueOf(rs.getString("phoneNO"));
                String  addrs  = String.valueOf(rs.getInt("cnic"));
                Double  balance  = Double.valueOf((rs.getInt("amountRemaining")));
                String id=rs.getString("id");
                list.add(new customer( id,  name,  phone,  addrs, balance));

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
            filteredList.setPredicate((Predicate<? super customer>) (customer std) -> {
                String lowerCase = newValue.toLowerCase();
                if (newValue.isEmpty() || newValue == null) {
                    return true;
                } else if (std.getIdCol().indexOf(lowerCase) != -1) {
                    return true;
                } else if (std.getNameCol().indexOf(lowerCase) != -1) {
                    return true;
                } else if (String.valueOf(std.getBalance()).indexOf(lowerCase) != -1) {
                    return true;
                }
                return false;
            });

        });


        SortedList sort = new SortedList(filteredList);
        sort.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sort);
    }
    public static class customer{

       String idCol   ;
       String nameCol ;
       String phoneCol;
       String address ;
       Double Balance ;

        public customer(String idCol, String nameCol, String phoneCol, String address, Double balance) {
            this.idCol = idCol;
            this.nameCol = nameCol;
            this.phoneCol = phoneCol;
            this.address = address;
            Balance = balance;
        }

        public String getIdCol() {
            return idCol;
        }

        public void setIdCol(String idCol) {
            this.idCol = idCol;
        }

        public String getNameCol() {
            return nameCol;
        }

        public void setNameCol(String nameCol) {
            this.nameCol = nameCol;
        }

        public String getPhoneCol() {
            return phoneCol;
        }

        public void setPhoneCol(String phoneCol) {
            this.phoneCol = phoneCol;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Double getBalance() {
            return Balance;
        }

        public void setBalance(Double balance) {
            Balance = balance;
        }
    }
}
