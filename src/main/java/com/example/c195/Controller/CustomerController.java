package com.example.c195.Controller;

import com.example.c195.Model.*;
import helper.Notify;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import static helper.JDBC.connection;

public class CustomerController implements Initializable {
    @FXML private TextField addressTxt;
    @FXML private Button cancelBtn;
    @FXML private ComboBox<String> countryCombo;
    @FXML private TextField idTxt;
    @FXML private TextField nameTxt;
    @FXML private TextField phoneTxt;
    @FXML private Button saveBtn;
    @FXML private ComboBox<String> divCombo;
    @FXML private TextField zipTxt;
    /** Returns to the main application screen */
    @FXML void onActionCancel(ActionEvent event) throws IOException {
        LoginController.ChangeScene(event, "main.fxml");
    }
    /** Saves the new or modified customer */
    @FXML void onActionSave(ActionEvent event) {
        try {
            String id = idTxt.getText();
            String name = nameTxt.getText();
            String phone = phoneTxt.getText();
            String address = addressTxt.getText();
            String postal = zipTxt.getText();
            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || postal.isEmpty()) {
                Notify.AlertMessage("Please enter valid values into the fields.");
            }
            else {
                String div = divCombo.getValue();
                int divId = Division.divisionNameHash.get(div);
                if (id.contains("Auto-Generated ID")) {
                    insertCust(name, address, postal, phone, divId);
                }
                else {
                    updateCust(name, address, postal, phone, divId, Integer.valueOf(id));
                }
                LoginController.ChangeScene(event, "main.fxml");
            }
        }
        catch (Exception e) {
            Notify.AlertMessage("Please enter valid values into the fields.");
        }
    }
    /** Sets the state/providence combo box based on the country */
    @FXML void onActionCountryCombo(ActionEvent event) {
        try {
            if (countryCombo.getValue().contains("U.S")) divCombo.setItems(Division.usDivisions);
            if (countryCombo.getValue().contains("UK")) divCombo.setItems(Division.ukDivisions);
            if (countryCombo.getValue().contains("Canada")) divCombo.setItems(Division.canadaDivisions);
        }
        catch (Exception e) {
        }
    }
    /** Performs an insert statement to add the customer in the MySQL database */
    public static int insertCust(String name, String address, String postal,
                                 String phone, int divId) throws SQLException {
        String sql = "INSERT INTO CUSTOMERS (Customer_Name, Address, " +
                "Postal_Code, Phone, Division_ID, Create_Date, Created_By, " +
                "Last_Update, Last_Updated_By) VALUES(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pStatement = connection.prepareStatement(sql);
        pStatement.setString(1, name);
        pStatement.setString(2, address);
        pStatement.setString(3, postal);
        pStatement.setString(4, phone);
        pStatement.setInt(5, divId);
        pStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        pStatement.setString(7, LoginController.currentUser);
        pStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        pStatement.setString(9, LoginController.currentUser);
        int rowsAffected = pStatement.executeUpdate();
        return rowsAffected;
    }
    /** Performs an update statement to modify the customer in the MySQL database */
    public static int updateCust(String name, String address, String postal, String phone,
                                 int divId, int custId) throws SQLException {
        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, " +
                "Postal_Code = ?, Phone = ?, Division_ID = ?, Last_Update = ?, " +
                "Last_Updated_By = ? WHERE Customer_ID = " + custId;
        PreparedStatement pStatement = connection.prepareStatement(sql);
        pStatement.setString(1, name);
        pStatement.setString(2, address);
        pStatement.setString(3, postal);
        pStatement.setString(4, phone);
        pStatement.setInt(5, divId);
        pStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        pStatement.setString(7, LoginController.currentUser);
        int rowsAffected = pStatement.executeUpdate();
        return rowsAffected;
    }
    /** Receives the customer details of the customer to be modified.
     * @param cust The customer that was selected on the main screen. */
    public void modifyCustDetails(Customer cust) throws SQLException {
        idTxt.setText(String.valueOf(cust.getCustId()));
        nameTxt.setText(cust.getName());
        phoneTxt.setText(cust.getPhone());
        zipTxt.setText(cust.getPostal());
        addressTxt.setText(cust.getAddress());
        countryCombo.setItems(Division.countryList);
        countryCombo.setValue(cust.getCountry());
        if (countryCombo.getValue().contains("U.S")) divCombo.setItems(Division.usDivisions);
        else if (countryCombo.getValue().contains("UK")) divCombo.setItems(Division.ukDivisions);
        else if (countryCombo.getValue().contains("Canada")) divCombo.setItems(Division.canadaDivisions);
        divCombo.setValue(cust.getDivName());
    }
    /** The first method that is run */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(Division.countryList);
    }
}
