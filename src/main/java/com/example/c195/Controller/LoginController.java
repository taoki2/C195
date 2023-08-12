package com.example.c195.Controller;

import com.example.c195.Model.Appointment;
import com.example.c195.Model.User;
import helper.Notify;
import helper.Query;
import helper.Time;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class LoginController implements Initializable {
    public static String currentUser;
    public static ObservableList<User> userList = FXCollections.observableArrayList();
    private static ObservableList<Appointment> upcomingAppt = FXCollections.observableArrayList();
    private static String notify15Minutes;

    @FXML private TextField passwordTxt;
    @FXML private Label timezoneTxt;
    @FXML private TextField usernameTxt;
    @FXML private Label timezoneLbl;
    @FXML private Label loginLbl;
    @FXML private Label usernameLbl;
    @FXML private Label passwordLbl;
    @FXML private Button loginBtn;
    @FXML private Button cancelBtn;

    /** Logs the user in if the username/password combination exists in the MySQL database
     * Displays an alert if there is an appointment in the next 15 minutes or alerts that there is no appointment
     * Saves all login attempts to login_activity.txt*/
    @FXML void onActionLogin(ActionEvent event) throws SQLException, IOException {
        String userName = usernameTxt.getText();
        String password = passwordTxt.getText();
        String filename = "login_activity.txt";
        FileWriter fileWriter = new FileWriter(filename, true);
        PrintWriter outputFile = new PrintWriter(fileWriter);
        if (verifyPassword(userName, password)) {
            outputFile.println(MessageFormat.format("Successful login at {0} by user: {1}", LocalDateTime.now(), userName));
            currentUser = userName;
            ChangeScene(event, "main.fxml");
            LocalTime nowMinus1 = LocalTime.now().minusMinutes(1);
            LocalTime nowPlus16 = LocalTime.now().plusMinutes(16);
            upcomingAppt.clear();
            notify15Minutes = "The following appointment(s) start in the next 15 minutes:";
            for (Appointment appt : MainController.getAllApptList()) {
                if (LocalDate.from(appt.getStart()).isEqual(LocalDate.now())) {
                    if (LocalTime.from(appt.getStart()).isAfter(nowMinus1) && LocalTime.from(appt.getStart()).isBefore(nowPlus16)) {
                        upcomingAppt.add(appt);
                        notify15Minutes = MessageFormat.format("{0}\n\tAppointment ID: {1} at {2}", notify15Minutes, appt.getApptId(), appt.getStart());
                    }
                }
            }
            if (upcomingAppt.isEmpty()) {
                Notify.InfoMessage("There are no upcoming appointments in the next 15 minutes.");
            }
            else {
                Notify.InfoMessage(notify15Minutes);
            }
        }
        else {
            outputFile.println(MessageFormat.format("Failed login attempt at {0} by user: {1}", LocalDateTime.now(), userName));
            ResourceBundle rb = ResourceBundle.getBundle("/prop", Locale.getDefault());
            Notify.AlertMessage(rb.getString("incorrect"));
        }
        outputFile.close();
    }

    /** Exits the application when the cancel button is clicked. */
    @FXML void onActionCancel(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Checks if username/password combination is in the Users table.
     * @param userName login username
     * @param password login password
     * @return true if username/password combination exists in Users table
     */
    private boolean verifyPassword(String userName, String password) throws SQLException {
        for (User user : getUserList()) {
            if (userName.equals(user.getUserName()) && password.equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }
    /** @return list of all users */
    public static ObservableList<User> getUserList() throws SQLException {
        userList.clear();
        String sql = "SELECT * FROM USERS";
        ResultSet result = Query.selectQuery(sql);
        while (result.next()) {
            int userId = result.getInt("User_ID");
            String userName = result.getString("User_Name");
            String password = result.getString("Password");
            userList.add(new User(userId, userName, password));
        }
        return userList;
    }

    /**
     * Changes the scene.
     * @param fxml location of the fxml view to switch to
     */
    public static void ChangeScene(ActionEvent event, String fxml) throws IOException {
        fxml = "/com/example/c195/View/" + fxml;
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(LoginController.class.getResource(fxml));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /** the method to run on initialization */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timezoneTxt.setText(Time.getLocalZone().toString());
        try {
            ResourceBundle rb = ResourceBundle.getBundle("/prop", Locale.getDefault());
            loginLbl.setText(rb.getString("login"));
            usernameLbl.setText(rb.getString("username"));
            passwordLbl.setText(rb.getString("password"));
            loginBtn.setText(rb.getString("login"));
            cancelBtn.setText(rb.getString("cancel"));
            timezoneLbl.setText(rb.getString("timezone"));
        }
        catch (Exception e) {

        }
    }
}