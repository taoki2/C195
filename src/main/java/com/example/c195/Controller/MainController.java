package com.example.c195.Controller;

import com.example.c195.Model.Appointment;
import com.example.c195.Model.Customer;
import com.example.c195.Model.Division;
import helper.Notify;
import helper.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.xml.stream.Location;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
/**
 * LAMBDA EXPRESSION 1: please see onActionDeleteAppt method below
 * LAMBDA EXPRESSION 2: please see onActionDeleteCust method below
 * */
public class MainController implements Initializable {
    private static ObservableList<Appointment> apptList = FXCollections.observableArrayList();
    private static ObservableList<Appointment> apptWeek = FXCollections.observableArrayList();
    private static ObservableList<Appointment> apptMonth = FXCollections.observableArrayList();
    private static ObservableList<Customer> custList = FXCollections.observableArrayList();

    @FXML private TableView<Appointment> apptTable;
    @FXML private TableColumn<Appointment, String> apptContactCol;
    @FXML private TableColumn<Appointment, Integer> apptCustCol;
    @FXML private TableColumn<Appointment, String> apptDescCol;
    @FXML private TableColumn<Appointment, Integer> apptUserCol;
    @FXML private TableColumn<Appointment, Timestamp> apptEndCol;
    @FXML private TableColumn<Appointment, Integer> apptIdCol;
    @FXML private TableColumn<Appointment, Location> apptLocationCol;
    @FXML private TableColumn<Appointment, Timestamp> apptStartCol;
    @FXML private TableColumn<Appointment, String> apptTitleCol;
    @FXML private TableColumn<Appointment, String> apptTypeCol;
    @FXML private TableView<Customer> custTable;
    @FXML private TableColumn<Customer, String> custAddressCol;
    @FXML private TableColumn<Customer, String> custDivisionCol;
    @FXML private TableColumn<Customer, Integer> custIdCol;
    @FXML private TableColumn<Customer, String> custNameCol;
    @FXML private TableColumn<Customer, String> custPhoneCol;
    @FXML private TableColumn<Customer, String> custPostalCol;

    /** Logs the user out and returns to login screen*/
    @FXML void onActionLogout(ActionEvent event) throws IOException {
        LoginController.ChangeScene(event, "login.fxml");
    }
    /** Opens the report screen */
    @FXML void onActionReport(ActionEvent event) throws IOException {
        LoginController.ChangeScene(event, "report.fxml");
    }
    /** Displays all appointments in the TableView */
    @FXML void onActionAllRBtn(ActionEvent event) {
        apptTable.setItems(apptList);
        apptTable.getSortOrder().addAll(apptIdCol);
        apptTable.sort();
    }
    /** Displays the appointments for the current month in the TableView */
    @FXML void onActionMonthRBtn(ActionEvent event) {
        apptTable.setItems(apptMonth);
        apptTable.getSortOrder().addAll(apptIdCol);
        apptTable.sort();
    }
    /** Displays the appointments for the current week in the TableView */
    @FXML void onActionWeekRBtn(ActionEvent event) {
        apptTable.setItems(apptWeek);
        apptTable.getSortOrder().addAll(apptIdCol);
        apptTable.sort();
    }
    /** Opens the appointment screen */
    @FXML void onActionAddAppt(ActionEvent event) throws IOException {
        LoginController.ChangeScene(event, "appointment.fxml");
    }
    /**
     * LAMBDA EXPRESSION 1: this lambda expression creates an alert that takes a string parameter for the message.
     * This allows the implementation of a functional interface.
     * deletes the selected appointment
     * */
    @FXML void onActionDeleteAppt(ActionEvent event) throws SQLException {
        if (apptTable.getSelectionModel().isEmpty()) {
            LambdaInterface notifyAlert = s -> {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setContentText(s);
                alert.showAndWait();
            };
            notifyAlert.getNotification("You must first select an appointment to delete.");
        }
        else {
            Appointment appt = apptTable.getSelectionModel().getSelectedItem();
            String confirmationMessage = "Are you sure you want to delete this appointment?";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, confirmationMessage);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = " + appt.getApptId();
                Query.updateQuery(sql);
                getAllApptList();
                getWeekApptList();
                getMonthApptList();
                apptTable.getSortOrder().addAll(apptIdCol);
                apptTable.sort();
                Notify.InfoMessage(MessageFormat.format("The following appointment was deleted:" +
                        "\n\tAppointment ID: {0}" +
                        "\n\tAppointment Type: {1}", appt.getApptId(), appt.getType()));
            }
        }
    }
    /** Opens the appointment screen to modify an existing appointment */
    @FXML void onActionModifyAppt(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/c195/View/appointment.fxml"));
            loader.load();
            AppointmentController ADMController = loader.getController();
            ADMController.modifyApptDetails(apptTable.getSelectionModel().getSelectedItem());
            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (NullPointerException e) {
            Notify.AlertMessage("You must first select an appointment to modify.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /** Opens the customer screen */
    @FXML void onActionAddCust(ActionEvent event) throws IOException {
        LoginController.ChangeScene(event, "customer.fxml");
    }
    /** Opens the customer screen to modify an existing customer */
    @FXML void onActionModifyCust(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/c195/View/customer.fxml"));
            loader.load();
            CustomerController ADMController = loader.getController();
            ADMController.modifyCustDetails(custTable.getSelectionModel().getSelectedItem());
            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (NullPointerException e) {
            Notify.AlertMessage("You must first select a customer to modify.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * LAMBDA EXPRESSION 2: this lambda expression takes no parameters, but executes two methods, to
     * create cleaner code when running these methods multiple times
     * This method deletes the selected customer if the customer does not have an associated appointment
     * */
    @FXML void onActionDeleteCust(ActionEvent event) throws SQLException {
        LambdaInterface2 apptLambda =  () -> {
            apptTable.getSortOrder().addAll(apptIdCol);
            apptTable.sort();
        };
        if (custTable.getSelectionModel().isEmpty()) {
            Notify.AlertMessage("You must first select a customer to delete.");
        }
        else {
            Customer cust = custTable.getSelectionModel().getSelectedItem();

            if (AppointmentController.hasScheduledAppt(cust)) {
                Notify.AlertMessage("Unable to delete this customer. Please delete all appointments associated with this customer first.");
                apptLambda.sortTable();
            }
            else {
                String confirmationMessage = "Are you sure you want to delete this customer?";
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, confirmationMessage);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = " + cust.getCustId();
                    Query.updateQuery(sql);
                    getAllCustList();
                    custTable.getSortOrder().addAll(custIdCol);
                    custTable.sort();
                    apptLambda.sortTable();
                    Notify.InfoMessage(MessageFormat.format("The following customer was deleted:" +
                            "\n\tCustomer ID: {0}" +
                            "\n\tCustomer Name: {1}", cust.getCustId(), cust.getName()));
                }
                else
                    apptLambda.sortTable();
            }
        }
    }
    /** @return a list of all the appointments */
    public static ObservableList<Appointment> getAllApptList() throws SQLException {
        String sql = "SELECT * FROM APPOINTMENTS AS A JOIN CONTACTS AS C ON A.Contact_ID=C.Contact_ID ORDER BY A.Appointment_ID";
        return getApptList(apptList, sql);
    }
    /** @return a list of all the customers */
    public static ObservableList<Customer> getAllCustList() throws SQLException {
        String sql = "SELECT * FROM CUSTOMERS AS C JOIN FIRST_LEVEL_DIVISIONS AS D ON C.Division_ID=D.Division_ID " +
                "JOIN COUNTRIES AS C2 ON D.Country_ID=C2.Country_ID";
        return getCustList(custList, sql);
    }
    /** @return a list of all the appointments for the current week */
    public ObservableList<Appointment> getWeekApptList() throws SQLException {
        String sql = "SELECT * FROM APPOINTMENTS AS A JOIN CONTACTS AS C ON A.Contact_ID=C.Contact_ID " +
                "WHERE WEEK(START) = WEEK(NOW())";
        return getApptList(apptWeek, sql);
    }
    /** @return a list of all the appointments for the current month */
    public ObservableList<Appointment> getMonthApptList() throws SQLException {
        String sql = "SELECT * FROM APPOINTMENTS AS A JOIN CONTACTS AS C ON A.Contact_ID=C.Contact_ID " +
                "WHERE MONTH(START) = MONTH(NOW())";
        return getApptList(apptMonth, sql);
    }
    /** @return a list of the appointments based on the MySQL query
     * @param  sql the MySQL query to execute */
    public static ObservableList<Appointment> getApptList(ObservableList<Appointment> list, String sql) throws SQLException {
        list.clear();
        ResultSet result = Query.selectQuery(sql);
        while (result.next()) {
            int apptId = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String desc = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            LocalDateTime start = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = result.getTimestamp("End").toLocalDateTime();
            int customerId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            int contactId = result.getInt("Contact_ID");
            String contactName = result.getString("Contact_Name");
            list.add(new Appointment(apptId, title, desc, location, type, start, end, customerId, userId, contactId, contactName));
        }
        return list;
    }
    /** @return a list of the customers based on the MySQL query
     * @param  sql the MySQL query to execute */
    public static ObservableList<Customer> getCustList(ObservableList<Customer> list, String sql) throws SQLException {
        list.clear();
        ResultSet result = Query.selectQuery(sql);
        while (result.next()) {
            int custId = result.getInt("Customer_ID");
            String name = result.getString("Customer_Name");
            String phone = result.getString("Phone");
            String address = result.getString("Address");
            String divName = result.getString("Division");
            String country = result.getString("Country");
            String postal = result.getString("Postal_Code");
            list.add(new Customer(custId, name, address, postal, phone, divName, country));
        }
        return list;
    }
    /** the method to run on initialization */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            apptTable.setItems(getAllApptList());
            apptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            apptDescCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
            apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            apptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            apptCustCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            apptUserCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
            apptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
            apptTable.getSortOrder().addAll(apptIdCol);
            apptTable.sort();

            custTable.setItems(getAllCustList());
            custIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
            custNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
            custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            custDivisionCol.setCellValueFactory(new PropertyValueFactory<>("divName"));
            custPostalCol.setCellValueFactory(new PropertyValueFactory<>("postal"));
            custTable.getSortOrder().addAll(custIdCol);
            custTable.sort();

            getMonthApptList();
            getWeekApptList();
            Division.getDivisionHT();
            Division.getUsDivisions();
            Division.getUkDivisions();
            Division.getCanadaDivisions();
            Division.getCountries();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
