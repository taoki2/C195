package com.example.c195.Controller;

import com.example.c195.Model.*;
import helper.Notify;
import helper.Query;
import helper.Time;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import static helper.JDBC.connection;

public class AppointmentController implements Initializable {
    private static ObservableList<Contact> contactList = FXCollections.observableArrayList();
    private static ObservableList<String> timeList = FXCollections.observableArrayList();

    @FXML private ComboBox<Contact> contactCombo;
    @FXML private ComboBox<Customer> custIdCombo;
    @FXML private TextField descTxt;
    @FXML private ComboBox<String> endCombo;
    @FXML private DatePicker endDP;
    @FXML private TextField locTxt;
    @FXML private ComboBox<String> startCombo;
    @FXML private DatePicker startDP;
    @FXML private TextField titleTxt;
    @FXML private TextField typeTxt;
    @FXML private ComboBox<User> userIdCombo;
    @FXML private TextField idTxt;
    /** Returns to the main application screen */
    @FXML void onActionCancel(ActionEvent event) throws IOException {
        LoginController.ChangeScene(event, "main.fxml");
    }
    /** Saves the new or modified appointment */
    @FXML void onActionSave(ActionEvent event) {
        try {
            String id = idTxt.getText();
            String title = titleTxt.getText();
            String desc = descTxt.getText();
            String location = locTxt.getText();
            String type = typeTxt.getText();
            if (title.isEmpty() || desc.isEmpty() || location.isEmpty() || type.isEmpty()) {
                Notify.AlertMessage("Please enter valid values into the fields.");
            }
            else {
                int contactId = contactCombo.getValue().getContactId();
                int custId = custIdCombo.getValue().getCustId();
                int userId = userIdCombo.getValue().getUserId();
                LocalDate startDate = startDP.getValue();
                LocalTime startTime = LocalTime.parse(startCombo.getValue());
                LocalDate endDate = endDP.getValue();
                LocalTime endTime = LocalTime.parse(endCombo.getValue());
                LocalDateTime start = LocalDateTime.of(startDate, startTime);
                LocalDateTime end = LocalDateTime.of(endDate, endTime);
                LocalDateTime now = LocalDateTime.now();

                if (isDuringBusinessHours(startTime, endTime)) {
                    if (id.contains("Auto-Generated ID")) {
                        if (isOverlappingAppt(start, end)) {
                            Notify.AlertMessage("This appointment time overlaps with an existing appointment. Please select a new time.");
                        }
                        else {
                            insertAppt(title, desc, location, type, start, end, now, custId, userId, contactId);
                            LoginController.ChangeScene(event, "main.fxml");
                        }
                    }
                    else {
                        if (isOverlappingAppt(start, end, Integer.valueOf(id))) {
                            Notify.AlertMessage("This appointment time overlaps with an existing appointment. Please select a new time.");
                        }
                        else {
                            updateAppt(title, desc, location, type, start, end, now, custId, userId, contactId, Integer.valueOf(id));
                            LoginController.ChangeScene(event, "main.fxml");
                        }
                    }
                }
                else
                    Notify.AlertMessage("Please select a time during business hours (0800-2200 EST)");
            }
        }
        catch (Exception e) {
            Notify.AlertMessage("Please enter valid values into the fields.");
        }
    }
    /** returns a list of all the contacts */
    public static ObservableList<Contact> getContactList() throws SQLException {
        contactList.clear();
        String sql = "SELECT * FROM CONTACTS";
        ResultSet result = Query.selectQuery(sql);
        while (result.next()) {
            int contactId = result.getInt("Contact_ID");
            String contactName = result.getString("Contact_Name");
            contactList.add(new Contact(contactId, contactName));
        }
        return contactList;
    }
    /** returns a list of appointment time slots to be used to populate the combo boxes */
    public static ObservableList<String> getTimeList() {
        timeList.clear();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j+=15) {
                timeList.add(String.format("%02d",i)+":"+String.format("%02d",j));
            }
        }
        return timeList;
    }
    /** Performs an insert statement to add the appointment in the MySQL database */
    public static int insertAppt(String title, String desc, String location, String type,
                                 LocalDateTime start, LocalDateTime end, LocalDateTime now,
                                 int custId, int userId, int contactId) throws SQLException {
        String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, " +
                "Start, End, Last_Update, Last_Updated_By, Create_Date, Created_By, " +
                "Customer_ID, User_ID, Contact_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pStatement = connection.prepareStatement(sql);
        pStatement.setString(1, title);
        pStatement.setString(2, desc);
        pStatement.setString(3, location);
        pStatement.setString(4, type);
        pStatement.setTimestamp(5, Timestamp.valueOf(start));
        pStatement.setTimestamp(6, Timestamp.valueOf(end));
        pStatement.setTimestamp(7, Timestamp.valueOf(now));
        pStatement.setString(8, LoginController.currentUser);
        pStatement.setTimestamp(9, Timestamp.valueOf(now));
        pStatement.setString(10, LoginController.currentUser);
        pStatement.setInt(11, custId);
        pStatement.setInt(12, userId);
        pStatement.setInt(13, contactId);
        int rowsAffected = pStatement.executeUpdate();
        return rowsAffected;
    }
    /** Performs an update statement to modify the appointment in the MySQL database */
    public static int updateAppt(String title, String desc, String location, String type,
                                 LocalDateTime start, LocalDateTime end, LocalDateTime now,
                                 int custId, int userId, int contactId, int apptId) throws SQLException {
        String sql = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Type = ?, " +
                "Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, " +
                "Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = " + apptId;
        PreparedStatement pStatement = connection.prepareStatement(sql);
        pStatement.setString(1, title);
        pStatement.setString(2, desc);
        pStatement.setString(3, location);
        pStatement.setString(4, type);
        pStatement.setTimestamp(5, Timestamp.valueOf(start));
        pStatement.setTimestamp(6, Timestamp.valueOf(end));
        pStatement.setTimestamp(7, Timestamp.valueOf(now));
        pStatement.setString(8, LoginController.currentUser);
        pStatement.setInt(9, custId);
        pStatement.setInt(10, userId);
        pStatement.setInt(11, contactId);
        int rowsAffected = pStatement.executeUpdate();
        return rowsAffected;
    }
    /** Checks if the new appointment's start and end date/time overlap with an existing appointment
     * @return true if the appointment overlaps with another appointment*/
    public static boolean isOverlappingAppt(LocalDateTime start, LocalDateTime end) throws SQLException {
        for (Appointment appt : MainController.getAllApptList()) {
                if ((start.isEqual(appt.getStart()) || start.isAfter(appt.getStart())) && start.isBefore(appt.getEnd())) {
                    return true;
                }
                else if (end.isAfter(appt.getStart()) && end.isBefore(appt.getEnd())) {
                    return true;
                }
            }
        return false;
    }
    /** Checks if the modified appointment's start and end date/time overlap with an existing appointment
     * @return true if the appointment overlaps with another appointment*/
    public static boolean isOverlappingAppt(LocalDateTime start, LocalDateTime end, int apptId) throws SQLException {
        for (Appointment appt : MainController.getAllApptList()) {
            if (!(appt.getApptId() == apptId)) {
                if ((start.isEqual(appt.getStart()) || start.isAfter(appt.getStart())) && start.isBefore(appt.getEnd())) {
                    return true;
                }
                else if (end.isAfter(appt.getStart()) && end.isBefore(appt.getEnd())) {
                    return true;
                }
            }
        }
        return false;
    }
    /** Checks if the appointment's start and end time are during business hours
     * @return true if the appointment is during business hours*/
    public static boolean isDuringBusinessHours(LocalTime localStartTime, LocalTime localEndTime) {
        ZoneId zoneEST = ZoneId.of("EST5EDT");
        ZonedDateTime businessStart = ZonedDateTime.of(LocalDate.now(), LocalTime.parse("07:59:00"), zoneEST);
        ZonedDateTime businessEnd = ZonedDateTime.of(LocalDate.now(), LocalTime.parse("22:00:00"), zoneEST);
        ZonedDateTime businessStartLocal = businessStart.withZoneSameInstant(Time.getLocalZone());
        ZonedDateTime businessEndLocal = businessEnd.withZoneSameInstant(Time.getLocalZone());
        LocalTime businessStartLocalTime = businessStartLocal.toLocalTime();
        LocalTime businessEndLocalTime = businessEndLocal.toLocalTime();
        if (localStartTime.isAfter(businessStartLocalTime) && localStartTime.isBefore(businessEndLocalTime)) {
            if (localEndTime.isAfter(businessStartLocalTime) && localEndTime.isBefore(businessEndLocalTime)) {
                return true;
            }
        }
        return  false;
    }
    /** Checks if the customer has an associated appointment
     * @return true if the customer has an associated appointment*/
    public static boolean hasScheduledAppt(Customer cust) throws SQLException {
        for (Appointment appt : MainController.getAllApptList()) {
            if (appt.getCustomerId() == cust.getCustId()) {
                return true;
            }
        }
        return false;
    }

    /** Receives the appointment details of the appointment to be modified.
     * @param appt The appointment that was selected on the main screen. */
    public void modifyApptDetails(Appointment appt) throws SQLException {
        idTxt.setText(String.valueOf(appt.getApptId()));
        titleTxt.setText(appt.getTitle());
        descTxt.setText(appt.getDesc());
        locTxt.setText(appt.getLocation());
        typeTxt.setText(appt.getType());
        startDP.setValue(appt.getStart().toLocalDate());
        endDP.setValue(appt.getEnd().toLocalDate());
        startCombo.setValue(String.valueOf(appt.getStart().toLocalTime()));
        endCombo.setValue(String.valueOf(appt.getEnd().toLocalTime()));
        for (Contact contact : AppointmentController.getContactList()) {
            if (contact.getContactId() == appt.getContactId()) {
                contactCombo.setValue(contact);
            }
        }
        for (Customer customer : MainController.getAllCustList()) {
            if (customer.getCustId() == appt.getCustomerId()) {
                custIdCombo.setValue(customer);
            }
        }
        for (User user : LoginController.getUserList()) {
            if (user.getUserId() == appt.getUserId()) {
                userIdCombo.setValue(user);
            }
        }
    }
    /** the method to run on initialization */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            contactCombo.setItems(getContactList());
            custIdCombo.setItems(MainController.getAllCustList());
            userIdCombo.setItems(LoginController.getUserList());
            startCombo.setItems(getTimeList());
            endCombo.setItems(getTimeList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
