package DatabaseConnection;

import java.sql.*;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class dbms {
    static Connection conn = null;
    static Statement stmt =null;
    public dbms() {
        connection();
    }

    private void connection() {

        try {
            // db parameters
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:pharmacy.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            setTables();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setTables() {
        createStockTable();
        createRecieptTable();
        creatCustomerTable();
        creatItemTable();
    }

    private void creatItemTable() {
        try {
            ResultSet tables = conn.getMetaData().getTables(null,"main" , "items", null);
            if (tables.next()) {
                System.out.println("Table already exists Items");
            } else {
                String query = "CREATE TABLE IF NOT EXISTS items(" +
                        "id          integer PRIMARY KEY," +
                        "item      REFERENCES stock(id)," +
                        "receiptNo REFERENCES receipt(id)," +
                        "quantity   int" +
                        ")";
                if (executeQuery(query)) {
                    myAlerts.alert("done", "items Table is Created successfully");
                } else {
                    myAlerts.alert("error", "items cannot Table is Created ");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void creatCustomerTable() {
        try {
            ResultSet tables = conn.getMetaData().getTables(null,"main" , "customers", null);
            if (tables.next()) {
                System.out.println("Table already exists customers");
            } else {
                String query = "CREATE TABLE IF NOT EXISTS customers(" +
                        "id         integer PRIMARY KEY," +
                        "customerName    text NOT NULL ," +
                        "phoneNO         text ," +
                        "cnic            text ," +
                        "Address         text," +
                        "amountRemaining DOUBLE" +
                        ")";
                if (executeQuery(query)) {
                    myAlerts.alert("done", "Customer Table is Created successfully");
                } else {
                    myAlerts.alert("error", "Customer cannot Table is Created ");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void createRecieptTable() {
        try {
            ResultSet tables = conn.getMetaData().getTables(null,"main" , "receipt", null);
            if (tables.next()) {
                System.out.println("Table already exists receipts");
            } else {
                String query = "CREATE TABLE IF NOT EXISTS receipt(" +
                        "id integer PRIMARY KEY," +
                        "customerName REFERENCES customers(id)," +
                        "itemsInBox integer," +
                        "discountPercent integer," +
                        "recieptDate text," +
                        "totalReturn integer," +
                        "totalRec    integer," +
                        "totalAmount integer," +
                        "noOfITem integer," +
                        "cost double" +

                        ")";
                if (executeQuery(query)) {
                    myAlerts.alert("done", "Stock Table is Created successfully");
                } else {
                    myAlerts.alert("error", "Stock cannot Table is Created ");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void createStockTable() {
        try {
            ResultSet tables = conn.getMetaData().getTables(null,"main" , "stock", null);
            if (tables.next()) {
                System.out.println("Table already exists Stock");
            } else {
                String query = "CREATE TABLE IF NOT EXISTS stock(" +
                        "id integer PRIMARY KEY," +
                        "name text NOT NULL," +
                        "price REAL NOT NULL," +
                        "quantityBox integer NOT NULL," +
                        "itemsInBox integer," +
                        "companyName text," +
                        "formula text," +
                        "mg integer," +
                        "category text NOT NULL," +
                        "subCategory text," +
                        "pricePerItem REAL," +
                        "expDate text," +
                        "mfgDate text," +
                        "totalQuantity integer," +
                        "costPerBox double NOT NULL" +
                        ")";
                if (executeQuery(query)) {
                    myAlerts.alert("done", "Stock Table is Created successfully");
                } else {
                    myAlerts.alert("error", "Stock cannot Table is Created ");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Boolean  executeQuery(String query){
         try {
              stmt =conn.createStatement();
              stmt.execute(query);
              return true;
         } catch (SQLException ex) {
             System.out.println(ex.getStackTrace());
             showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", ERROR_MESSAGE);
             System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
             return false;
         }

        }

    public static ResultSet getResultSet(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        } finally {
        }
        return result;
    }


}
