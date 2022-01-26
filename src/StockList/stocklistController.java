package StockList;

import DatabaseConnection.dbms;
import DatabaseConnection.myAlerts;
import MainStock.entryController;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.time.DateUtils;

public class stocklistController {
    ObservableList<Entry> list = FXCollections.observableArrayList();
    ObservableList<Entry> explist = FXCollections.observableArrayList();
    FilteredList<Entry> filteredList = new FilteredList(list, e -> true);
    SortedList sort =new SortedList(filteredList);
    @FXML
    private TableView<Entry> tableView;
    @FXML
    private JFXTextField searchValue;
    @FXML
    private JFXCheckBox showExpBtn;
    @FXML
    private TableColumn<Entry, String> totalQuantityCol;
    @FXML
    private TableColumn<Entry, String> nameCol;
    @FXML
    private TableColumn<Entry, String> formulaCol;
    @FXML
    private TableColumn<Entry, String> cost;

    @FXML
    private TableColumn<Entry, String> catCol;
    @FXML
    private TableColumn<Entry, String> expCol;
    @FXML
    private TableColumn<Entry, String> mfgCol;
    @FXML
    private TableColumn<Entry, Float> boxqCol;
    @FXML
    private TableColumn<Entry, Float> itemqCol;
    @FXML
    private TableColumn<Entry, Float> priceCol;
    @FXML
    private TableColumn<Entry, String> companyCol;
    @FXML
    private TableColumn<Entry, String> subCat;
    @FXML
    private TableColumn<Entry, String> mg;

    @FXML
    void onEditAction(ActionEvent event) {
        Entry entry=tableView.getSelectionModel().getSelectedItem();
        if(entry==null){
            return;

        }
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/MainStock/entry.fxml"));
        Parent root= null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        entryController ref=(entryController) loader.getController();
        ref.setValues(entry);
        // librarymanageUtil.setIcon(stg);
        Stage stg=new Stage(StageStyle.DECORATED);
        Scene scene =new Scene(root);
        stg.setTitle("details ");
        stg.setScene(scene);
        stg.show();

    }
    @FXML
    void initialize() {

        assert searchValue != null : "fx:id=\"searchValue\" was not injected: check your FXML file 'stocklist.fxml'.";
        assert showExpBtn != null : "fx:id=\"showExpBtn\" was not injected: check your FXML file 'stocklist.fxml'.";
        assert nameCol != null : "fx:id=\"nameCol\" was not injected: check your FXML file 'stocklist.fxml'.";
        assert formulaCol != null : "fx:id=\"formaulCol\" was not injected: check your FXML file 'stocklist.fxml'.";
        assert catCol != null : "fx:id=\"catCol\" was not injected: check your FXML file 'stocklist.fxml'.";
        assert expCol != null : "fx:id=\"expCol\" was not injected: check your FXML file 'stocklist.fxml'.";
        assert mfgCol != null : "fx:id=\"mfgCol\" was not injected: check your FXML file 'stocklist.fxml'.";
        assert boxqCol != null : "fx:id=\"boxqCol\" was not injected: check your FXML file 'stocklist.fxml'.";
        assert itemqCol != null : "fx:id=\"itemqCol\" was not injected: check your FXML file 'stocklist.fxml'.";
        assert priceCol != null : "fx:id=\"priceCol\" was not injected: check your FXML file 'stocklist.fxml'.";
        assert companyCol != null : "fx:id=\"companyCol\" was not injected: check your FXML file 'stocklist.fxml'.";
        initializeData();
        loadData();

    }

    private void initializeData() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nameCol"));
        formulaCol.setCellValueFactory(new PropertyValueFactory<>("formulaCol"));
        companyCol.setCellValueFactory(new PropertyValueFactory<>("companyCol"));
        mg.setCellValueFactory(new PropertyValueFactory<>("mg"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("priceCol"));
        expCol.setCellValueFactory(new PropertyValueFactory<>("expCol"));
        mfgCol.setCellValueFactory(new PropertyValueFactory<>("mfgCol"));
        boxqCol.setCellValueFactory(new PropertyValueFactory<>("boxqCol"));
        itemqCol.setCellValueFactory(new PropertyValueFactory<>("itemqCol"));
        catCol.setCellValueFactory(new PropertyValueFactory<>("catCol"));
        subCat.setCellValueFactory(new PropertyValueFactory<>("subCat"));
        totalQuantityCol.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        showExpBtn.selectedProperty().addListener((ov, old_val, new_val) -> {
            if (new_val) {
                list.clear();
                explist.clear();
                tableView.getItems().clear();

                String q = "SELECT * FROM stock";
                ResultSet rs = dbms.getResultSet(q);
                while (true) {
                    try {
                        if (!rs.next()) break;
                        var nameCol = rs.getString("name");
                        var price = rs.getInt("pricePerItem");
                        var quantityBox = rs.getInt("quantityBox");
                        var itemsInBox = rs.getInt("itemsInBox");
                        var companyName = rs.getString("companyName");
                        var formula = rs.getString("formula");
                        var mg = rs.getInt("mg");
                        var category = rs.getString("category");
                        var mfg = rs.getString("mfgDate");
                        var exp = rs.getString("expDate");
                        var subCat = rs.getString("subCategory");
                        var id=rs.getInt("id");
                        var totalQuantity = rs.getInt("totalQuantity");
                        var cost = rs.getFloat("costPerBox");

                        if (isExpired(exp))
                            explist.add(new Entry(nameCol, formula, category, exp, mfg, quantityBox, itemsInBox, price, companyName, subCat, mg,id,totalQuantity,cost));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                }
               System.out.println(explist.toArray().toString());
                tableView.setItems(explist);
            } else {
                tableView.refresh();
                list.clear();
                loadData();
            }
            System.out.println(new_val);
        });
    }

    private boolean isExpired(String exp) {
        if (!(exp.equals("") || exp == null)) {
            String[] ex = exp.split("/");
            System.out.println("this is time month"+ex[0]+"day"+ex[1]+"year"+ex[2]);
            Date date = new GregorianCalendar(Integer.parseInt(ex[2]),
                    Integer.parseInt(ex[0]) - 1, Integer.parseInt(ex[1])).getTime();
            Date newDate = DateUtils.addMonths(new Date(), 1);

            if (date.compareTo(newDate) < 0) {
                System.out.println("expired" + date+":" +newDate);
                return true;
            }
        }
        return false;
    }


    @FXML
    void searchAction(ActionEvent event) {
        System.out.println("hitted the key");
        searchValue.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate((Predicate<? super Entry>) (Entry std) -> {
                String lowerCase = newValue.toLowerCase();
                if (newValue.isEmpty() || newValue == null) {
                    return true;
                } else if (std.getNameCol().indexOf(lowerCase) != -1) {
                    return true;
                } else if (std.getFormulaCol().indexOf(lowerCase) != -1) {
                    return true;
                } else if (std.subCat.indexOf(lowerCase) != -1) {
                    return true;
                } else if (std.getExpCol().indexOf(lowerCase) != -1) {
                    return true;
                }
                return false;
            });

        });


        SortedList sort = new SortedList(filteredList);
        sort.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sort);
    }

    @FXML
    void onDeleteAction(ActionEvent event) {
        Entry [] entries=tableView.getSelectionModel().getSelectedItems().toArray(new Entry[0]);

        for (int i = 0; i < entries.length; i++) {
            String qr="DELETE FROM stock WHERE id="+entries[0].getId();
            if (dbms.executeQuery(qr)){
                myAlerts.alert("done",entries[0].getNameCol() +" is deleted from database");
                list.remove(entries[i]);
            }else{
                myAlerts.alert("error",entries[0].getNameCol() +" cannot be deleted from database");
            }
        }
        tableView.refresh();

    }
    private void loadData() {
        String q = "SELECT * FROM stock";
        ResultSet rs = dbms.getResultSet(q);
        while (true) {
            try {
                if (!rs.next()) break;
                var nameCol = rs.getString("name");
                var price = rs.getInt("pricePerItem");
                var quantityBox = rs.getInt("quantityBox");
                var itemsInBox = rs.getInt("itemsInBox");
                var id=rs.getInt("id");
                var companyName = rs.getString("companyName");
                var formula = rs.getString("formula");
                var mg = rs.getInt("mg");
                var category = rs.getString("category");
                var mfg = rs.getString("mfgDate");
                var exp = rs.getString("expDate");
                var subCat = rs.getString("subCategory");
                var totalQuantity = rs.getInt("totalQuantity");
                var cost = rs.getFloat("costPerBox");

                list.add(new Entry(nameCol, formula, category, exp, mfg, quantityBox, itemsInBox, price, companyName, subCat, mg,id,totalQuantity,cost));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        tableView.setItems(list);
    }


    public static class Entry {
        int id;
        float cost;
        String nameCol;
        String formulaCol;
        String catCol;
        String expCol;
        String mfgCol;
        int boxqCol;
        int itemqCol;
        int priceCol;
        String companyCol;
        String subCat;
        int mg;
        int totalQuantity;

        public float getCost() {
            return cost;
        }

        public void setCost(float cost) {
            this.cost = cost;
        }

        public Entry(String nameCol, String formaulCol, String catCol,
                     String expCol, String mfgCol, int boxqCol, int itemqCol, int priceCol,
                     String companyCol, String subCat, int mg, int id, int totalQuantity, float cost) {
            this.nameCol = nameCol;
            this.formulaCol = formaulCol;
            this.catCol = catCol;
            this.expCol = expCol;
            this.mfgCol = mfgCol;
            this.boxqCol = boxqCol;
            this.itemqCol = itemqCol;
            this.priceCol = priceCol;
            this.companyCol = companyCol;
            this.subCat = subCat;
            this.mg = mg;
            this.id = id;
            this.totalQuantity = totalQuantity;
            this.cost= cost;
        }

        public String getNameCol() {
            return nameCol;
        }

        public void setNameCol(String nameCol) {
            this.nameCol = nameCol;
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCatCol() {
            return catCol;
        }

        public void setCatCol(String catCol) {
            this.catCol = catCol;
        }
        public int getTotalQuantity() {
            return totalQuantity;
        }

        public void setTotalQuantity(int totalQuantity) {
            this.totalQuantity = totalQuantity;
        }
        public String getExpCol() {
            return expCol;
        }

        public void setExpCol(String expCol) {
            this.expCol = expCol;
        }

        public String getMfgCol() {
            return mfgCol;
        }

        public void setMfgCol(String mfgCol) {
            this.mfgCol = mfgCol;
        }

        public int getBoxqCol() {
            return boxqCol;
        }

        public void setBoxqCol(int boxqCol) {
            this.boxqCol = boxqCol;
        }

        public int getItemqCol() {
            return itemqCol;
        }

        public String getFormulaCol() {
            return formulaCol;
        }

        public void setFormulaCol(String formulaCol) {
            this.formulaCol = formulaCol;
        }

        public int getMg() {
            return mg;
        }

        public void setMg(int mg) {
            this.mg = mg;
        }

        public void setItemqCol(int itemqCol) {
            this.itemqCol = itemqCol;
        }

        public int getPriceCol() {
            return priceCol;
        }

        public void setPriceCol(int priceCol) {
            this.priceCol = priceCol;
        }

        public String getCompanyCol() {
            return companyCol;
        }

        public void setCompanyCol(String companyCol) {
            this.companyCol = companyCol;
        }

        public String getSubCat() {
            return subCat;
        }

        public void setSubCat(String subCat) {
            this.subCat = subCat;
        }
    }


}