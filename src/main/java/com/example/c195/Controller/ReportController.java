package com.example.c195.Controller;
import com.example.c195.Model.*;
import helper.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.xml.stream.Location;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
/**LAMBDA EXPRESSION 3: please see onActionContactCombo method below */
public class ReportController implements Initializable {
    private FilteredList<Appointment> filteredApptList = new FilteredList<>(MainController.getAllApptList());
    private ObservableList<ReportApptCount> apptCountList = FXCollections.observableArrayList();
    private ObservableList<ReportCustCount> custCountList = FXCollections.observableArrayList();

    @FXML private TableView<Appointment> apptTable;
    @FXML private TableColumn<Appointment, Integer> apptCustCol;
    @FXML private TableColumn<Appointment, String> apptDescCol;
    @FXML private TableColumn<Appointment, Timestamp> apptEndCol;
    @FXML private TableColumn<Appointment, Integer> apptIdCol;
    @FXML private TableColumn<Appointment, Location> apptLocationCol;
    @FXML private TableColumn<Appointment, Timestamp> apptStartCol;
    @FXML private TableColumn<Appointment, String> apptTitleCol;
    @FXML private TableColumn<Appointment, String> apptTypeCol;
    @FXML private ComboBox<Contact> contactCombo;
    @FXML private TableView<ReportApptCount> countApptTable;
    @FXML private TableColumn<ReportApptCount, String> monthCol;
    @FXML private TableColumn<ReportApptCount, String> typeCol;
    @FXML private TableColumn<ReportApptCount, Integer> countCol;
    @FXML private TableView<ReportCustCount> countCustTable;
    @FXML private TableColumn<ReportCustCount, String> divCol;
    @FXML private TableColumn<ReportCustCount, Integer> custCountCol;
    public ReportController() throws SQLException {
    }
    /**LAMBDA EXPRESSION 3: uses a lambda expression as a predicate for the filtered list, so that the list
     * can be filtered based on the contact without needing to execute MySQL queries
     * Displays the appointments in the TableView based on the contact selected */
    @FXML void onActionContactCombo(ActionEvent event) {
        try {
            Contact contact = contactCombo.getValue();
            filteredApptList.setPredicate(appt -> appt.getContactId() == contact.getContactId());
            apptTable.setItems(filteredApptList);
        }
        catch (Exception e) {

        }
    }
    /** Returns to the main screen */
    @FXML void onActionMain(ActionEvent event) throws IOException {
        LoginController.ChangeScene(event, "main.fxml");
    }
    /** Returns the report containing the total count of appointments by month and type*/
    private ObservableList<ReportApptCount> getApptCountList() throws SQLException {
        apptCountList.clear();
        String sql = "SELECT MONTH(Start), Type, COUNT(*) FROM APPOINTMENTS GROUP BY Type, MONTH(Start) ORDER BY MONTH(Start)";
        ResultSet result = Query.selectQuery(sql);
        while (result.next()) {
            String month = ReportApptCount.monthHash.get(result.getInt("MONTH(Start)"));
            String type = result.getString("Type");
            int count = result.getInt("COUNT(*)");
            apptCountList.add(new ReportApptCount(month, type, count));
        }
        return apptCountList;
    }
    /** Returns the report containing the total count of customers by division */
    private ObservableList<ReportCustCount> getCustCountList() throws SQLException {
        custCountList.clear();
        String sql = "SELECT D.Division, COUNT(*) FROM CUSTOMERS AS C JOIN first_level_divisions AS D " +
                "ON C.Division_ID=D.Division_ID GROUP BY Division ORDER BY Division";
        ResultSet result = Query.selectQuery(sql);
        while (result.next()) {
            String div = result.getString("Division");
            int count = result.getInt("COUNT(*)");
            custCountList.add(new ReportCustCount(div, count));
        }
        return custCountList;
    }
    /** the method to run on initialization */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            contactCombo.setItems(AppointmentController.getContactList());
            apptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            apptDescCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
            apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            apptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            apptCustCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            ReportApptCount.getMonthHT();
            countApptTable.setItems(getApptCountList());
            monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            countCol.setCellValueFactory(new PropertyValueFactory<>("count"));

            countCustTable.setItems(getCustCountList());
            divCol.setCellValueFactory(new PropertyValueFactory<>("div"));
            custCountCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
