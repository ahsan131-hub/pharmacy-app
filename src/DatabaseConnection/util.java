package DatabaseConnection;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class util {

    public static void validateTextField(JFXTextField field, NumberValidator numberValidator, JFXButton saveBtn) {
        field.getValidators().add(numberValidator);

        field.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (!t1) {
                    if (field.validate()) {
                        saveBtn.setDisable(false);
                    } else

                        saveBtn.setDisable(true);

                }
            }
        });
    }

    public static void setAutoCompleteCustomer(JFXTextField item_name, String qr) {
        ObservableList<String> data = FXCollections.observableArrayList();
        //ObservableList<String> filteredList = FXCollections.observableArrayList();
        var rs = dbms.getResultSet(qr);
        while (true) {
            try {
                if (!rs.next()) break;
                var str = rs.getString("customerName");
                var id = rs.getInt("id");
                data.add(id + " | " + str);
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
            } else {
                try {
                    autoCompletePopup.show(item_name);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

    }

    public static void setAutoCompleteItems(JFXTextField item_name,
                                            String qr,
                                            JFXTextField formulaField,
                                            JFXTextField companyField,
                                            JFXTextField subCategoryField) {
        ObservableList<String> data = FXCollections.observableArrayList();
        ObservableList<String> formulaList = FXCollections.observableArrayList();
        ObservableList<String> companyList = FXCollections.observableArrayList();
        ObservableList<String> subCategoryList = FXCollections.observableArrayList();

        //ObservableList<String> filteredList = FXCollections.observableArrayList();
        var rs = dbms.getResultSet(qr);
        while (true) {
            try {
                if (!rs.next()) break;
                var str = rs.getInt("id") + " | " + rs.getString("name") +
                        "  :[" + rs.getString("mg") + "mg" +
                        "  exp : " + rs.getString("expDate") + "]";
                var formula = rs.getString("formula");
                var companyName = rs.getString("companyName");
                var subCategory = rs.getString("subCategory");
                data.add(str);
                if (!formulaList.contains(formula)) {
                    formulaList.add(formula);
                }
                if (!companyList.contains(companyName)) {
                    companyList.add(companyName);
                }
                if (!subCategoryList.contains(subCategory)) {
                    subCategoryList.add(subCategory);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        makeAutoCompletion(item_name, data);
        if (!(formulaField == null && companyField == null && subCategoryField == null)) {
            makeAutoCompletion(formulaField, formulaList);
            makeAutoCompletion(companyField, companyList);
            makeAutoCompletion(subCategoryField, subCategoryList);

        }
    }

    static void makeAutoCompletion(JFXTextField field, ObservableList<String> data) {

        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.getSuggestions().addAll(data);
        autoCompletePopup.setSelectionHandler(event -> {
            if (field.getId().equals("Item_Name") || field.getId().equals("ItemName")) {
                field.setText(event.getObject().split(":")[0]);
            } else {
                field.setText(event.getObject());
            }

        });
        field.textProperty().addListener(observable -> {
            //The filter method uses the Predicate to filter the Suggestions defined above
            //I choose to use the contains method while ignoring cases
            autoCompletePopup.filter(item -> item.toLowerCase().contains(field.getText().toLowerCase()));
            //Hide the autocomplete popup if the filtered suggestions is empty or when the box's original popup is open
            if (autoCompletePopup.getFilteredSuggestions().isEmpty()) {
                autoCompletePopup.hide();
            } else {
                try {
                    autoCompletePopup.show(field);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

    }

}
