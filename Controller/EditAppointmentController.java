package Controller;

import Model.Appointment;
import Utils.dbconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.ResourceBundle;


import static Controller.AppointmentsScreenController.allusers;
import static Controller.AppointmentsScreenController.typecombo;
import static Model.Appointment.allAppointments;
import static Model.Appointment.getAllAppointments;


/**This creates the EditAppointmentController class*/
public class EditAppointmentController implements Initializable {
    /**
     * defines fxml features
     */
    public DatePicker StartDatePicker;
    public ComboBox<LocalTime> StartTimeComboBOx;
    public ComboBox<LocalTime> EndTimeComboBox;
    @FXML
    private TextField AppointmentIDTextField;

    @FXML
    private TextField TitleTextField;

    @FXML
    private TextField DescriptionTextField;

    @FXML
    private TextField LocationTextField;

    @FXML
    private TextField ContactTextField;

    @FXML
    private ComboBox<String> TypeComboBox;


    @FXML
    private TextField CustomerIDTextField;

    @FXML
    private TextField UserIDTextField;

    @FXML
    private Button UpdateButton;

    @FXML
    private Button BackButton;

    @FXML
    private ComboBox<String> CustomerBox;


    public static int userid;
    public boolean isvalid;

    /**
     * switches to appointments screen
     */
    @FXML
    void BackButtonPushed(ActionEvent event) throws IOException {
        Parent LogIn = FXMLLoader.load(getClass().getResource("../VIew/AppointmentsScreen.fxml"));
        Scene scene = new Scene(LogIn);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    /**
     * sets contact text field with the chosen customer
     */
    @FXML
    void CustomerChosen(ActionEvent event) {
        ContactTextField.setText(CustomerBox.getValue());

    }
    /** updates customer in database*/
    @FXML
    void UpdateButtonPushed(ActionEvent event) throws SQLException, IOException {
        isvalid = true;
        String title = (TitleTextField.getText());
        String descritption = (DescriptionTextField.getText());
        String location = (LocationTextField.getText());
        String contact = (ContactTextField.getText());
        String type = TypeComboBox.getValue();


        LocalDateTime start1 = LocalDateTime.of(StartDatePicker.getValue(), StartTimeComboBOx.getValue());
        ZonedDateTime startzone = start1.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime utc = startzone.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime start = utc.toLocalDateTime();

        LocalDateTime end1 = LocalDateTime.of(StartDatePicker.getValue(), EndTimeComboBox.getValue());
        ZonedDateTime endzone = end1.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime endutc = endzone.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime end = endutc.toLocalDateTime();

        LocalDateTime time3 = LocalDateTime.now();
        ZonedDateTime ttdt = time3.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime tttc = ttdt.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime time2 = tttc.toLocalDateTime();

        ZoneId easternid = ZoneId.of("US/Eastern");
        ZonedDateTime setz = startzone.withZoneSameInstant(easternid);
        LocalDateTime easternstart = setz.toLocalDateTime();
        LocalTime starttest = easternstart.toLocalTime();

        ZonedDateTime eetz = endzone.withZoneSameInstant(easternid);
        LocalDateTime easternend = eetz.toLocalDateTime();
        LocalTime endtest = easternend.toLocalTime();
        LocalTime openinghr = LocalTime.parse("08:00");
        LocalTime closinghr = LocalTime.parse("22:00");

        int id3 = AppointmentsScreenController.id;

        Appointment cust = Appointment.allAppointments.get(id3);
        int id = cust.getAppointment_ID();


        if (starttest.isBefore(openinghr)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            String errorMessage = "APPOINTMENT MUST BE WITHIN BUSINESS HOURS. 8:00 AM - 10:00 PM EASTERN";
            alert.setContentText(errorMessage);
            alert.showAndWait();
            isvalid = false;

        }
        if (endtest.isAfter(closinghr)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            String errorMessage = "APPOINTMENT MUST BE WITHIN BUSINESS HOURS. 8:00 AM - 10:00 PM EASTERN";
            alert.setContentText(errorMessage);
            alert.showAndWait();
            isvalid = false;
        }
        if( title.isEmpty()|| descritption.isEmpty()||location.isEmpty()|| type.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            String errorMessage = "TEXT FIELD CANNOT BE EMPTY";
            alert.setContentText(errorMessage);
            alert.showAndWait();
            isvalid = false;

        }
        for (Appointment a : allAppointments) {
            LocalTime atime = a.getStartTime().toLocalTime();
            LocalTime aetime = a.getEndTime().toLocalTime();
            LocalTime stime = start.toLocalTime();
            LocalDate adate = a.getStartTime().toLocalDate();
            LocalDate ndate = start.toLocalDate();


            if (adate.equals(ndate) && stime.isAfter(atime) && stime.isBefore(aetime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                String errorMessage = "APPOINTMENTS CANNOT OVERLAP";
                alert.setContentText(errorMessage);
                alert.showAndWait();
                isvalid = false;

            }
        }
        if (isvalid == true) {


            Connection conn = dbconnection.getConnection();
            String sqlstring = "SELECT Contact_ID FROM contacts WHERE (Contact_Name = ?)";
            PreparedStatement userselectstatement = conn.prepareStatement(sqlstring);
            userselectstatement.setString(1, contact);
            ResultSet rs = userselectstatement.executeQuery();
            while (rs.next()) {
                userid = rs.getInt("Contact_ID");

            }
            userselectstatement = conn.prepareStatement("UPDATE appointments SET Title = ?, Description = ?, Location = ?,  Type = ?, Start = ?, End = ?, Contact_ID = ? WHERE Appointment_ID = ?");
            userselectstatement.setString(1, title);
            userselectstatement.setString(2, descritption);
            userselectstatement.setString(3, location);
            userselectstatement.setString(4, type);
            userselectstatement.setTimestamp(5, Timestamp.valueOf(start));
            userselectstatement.setTimestamp(6, Timestamp.valueOf(end));
            userselectstatement.setString(7, String.valueOf(userid));
            userselectstatement.setString(8, String.valueOf(id));


            userselectstatement.executeUpdate();

            Parent AddScreen = FXMLLoader.load(getClass().getResource("../VIew/AppointmentsScreen.fxml"));
            Scene scene = new Scene(AddScreen);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }


    }

    /**sets text boxes with appointment information
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TypeComboBox.setItems(typecombo);
        int id2 = AppointmentsScreenController.id;
        Appointment temp = getAllAppointments().get(id2);
        AppointmentIDTextField.setText(String.valueOf(temp.getAppointment_ID()));
        TitleTextField.setText(temp.getTitle());
        DescriptionTextField.setText(temp.getDescription());
        LocationTextField.setText(temp.getLocation());
        ContactTextField.setText(temp.getContact());
        TypeComboBox.setValue(temp.getType());
        LocalDateTime starttime = temp.getStartTime();
       // ZonedDateTime uzdt = starttime.atZone(ZoneId.of("UTC"));
        ZonedDateTime zdt = starttime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        LocalDateTime start = zdt.toLocalDateTime();
        LocalDate date = start.toLocalDate();
        StartDatePicker.setValue(date);
        LocalTime time = start.toLocalTime();
        StartTimeComboBOx.setValue(time);
        LocalDateTime endtime = temp.getEndTime();
        //ZonedDateTime eszdt = endtime.atZone(ZoneId.of("UTC"));
        ZonedDateTime ezdt = endtime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        LocalDateTime end = ezdt.toLocalDateTime();
        LocalTime etime = end.toLocalTime();
        EndTimeComboBox.setValue(etime);
        CustomerIDTextField.setText(String.valueOf(temp.getCustomerid()));
        UserIDTextField.setText(String.valueOf(temp.getUserid()));
        CustomerBox.setItems(allusers);
        CustomerBox.setPromptText(temp.getContact());


        LocalTime thestart = LocalTime.of(7, 0);
        LocalTime theend = LocalTime.of(23, 0);

        while (thestart.isBefore(theend)) {
            StartTimeComboBOx.getItems().add(thestart);
            EndTimeComboBox.getItems().add(thestart);
            thestart = thestart.plusMinutes(10);
        }
    }
}