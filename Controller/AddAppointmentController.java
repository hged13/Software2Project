package Controller;
import Model.Appointment;
import Model.Customer;
import Model.User;
import Utils.DBQuery;
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
import java.lang.reflect.Type;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.ResourceBundle;

import static Controller.AppointmentsScreenController.allusers;
import static Controller.AppointmentsScreenController.typecombo;
import static Model.Appointment.allAppointments;
import static java.time.ZoneOffset.UTC;
/**This creates the AddAppointmentController class*/
public class AddAppointmentController implements Initializable {

   /** Defines fxml features*/
    public DatePicker StartDatePicker;
    public ComboBox<LocalTime> StartTimeComboBox;
    public ComboBox <LocalTime> EndTimeComboBox;
    @FXML
    private ComboBox<String> ContactNameDropDown;

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
    private Button AddButton;

    @FXML
    private Button BackButton;
    public static ObservableList<String> typecombo = FXCollections.observableArrayList("Debriefing", "Planning-session", "other");

/** this defines class variables*/
    public static int contactid;
    boolean isvalid = true;





/**
 * @param event
 * populates the text field withe the selected contact
 */
    @FXML
    public void ContactSelected(ActionEvent event)  {
        String customer =   ContactNameDropDown.getSelectionModel().getSelectedItem();
        ContactTextField.setText(customer);
        ContactTextField.setText(String.valueOf(customer));


    }

/** This adds an appointment to database and checks for errors with time and empty text fields */
    @FXML
    void AddButtonPushed(ActionEvent event) throws SQLException, IOException {
        isvalid = true;
        Connection conn = dbconnection.getConnection();
        String sqlstring = "SELECT Contact_ID FROM contacts WHERE (Contact_Name = ?)";

        PreparedStatement userselectstatement = conn.prepareStatement(sqlstring);
        userselectstatement.setString(1, ContactNameDropDown.getValue());
        userselectstatement.executeQuery();
        ResultSet rs = userselectstatement.executeQuery();
        while (rs.next()) {
             contactid = rs.getInt("Contact_ID");

        }



        String title = (TitleTextField.getText());
        String description = (DescriptionTextField.getText());
        String location = (LocationTextField.getText());
        String type =  TypeComboBox.getValue();
        LocalDate startdate = StartDatePicker.getValue();
        LocalTime starttime = StartTimeComboBox.getValue();
        LocalTime endtime = EndTimeComboBox.getValue();
        if (startdate!=null|| starttime!=null|| endtime!=null){


            LocalDateTime std = LocalDateTime.of(startdate, starttime);
           ZonedDateTime zdt = std.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
            ZonedDateTime utc = std.atZone(UTC);
            LocalDateTime start = utc.toLocalDateTime();


            LocalDateTime edt = LocalDateTime.of(startdate, endtime);
          ZonedDateTime ezdt = edt.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
            ZonedDateTime eutc = edt.atZone(UTC);
            LocalDateTime end = eutc.toLocalDateTime();


            LocalDateTime time1 = LocalDateTime.now();
            ZonedDateTime tdt = time1.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
            ZonedDateTime ttc = tdt.withZoneSameInstant(ZoneId.of("UTC"));
            LocalDateTime time = ttc.toLocalDateTime();

            String username = User.getUserID();

            LocalDateTime time3 = LocalDateTime.now();
            ZonedDateTime ttdt = time3.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
            ZonedDateTime tttc = ttdt.withZoneSameInstant(ZoneId.of("UTC"));
            LocalDateTime time2 = tttc.toLocalDateTime();

            String username2 = User.getUserID();
            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty()|| CustomerIDTextField.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                String errorMessage = "TEXT FIELD CANNOT BE EMPTY";
                alert.setContentText(errorMessage);
                alert.showAndWait();
                isvalid = false;

            }
            if (isvalid == true){

            int userid = Integer.parseInt(UserIDTextField.getText());
            int customerid = Integer.parseInt(CustomerIDTextField.getText());

            ZoneId easternid = ZoneId.of("US/Eastern");
            ZonedDateTime setz = zdt.withZoneSameInstant(easternid);
            LocalDateTime easternstart = setz.toLocalDateTime();
            LocalTime starttest = easternstart.toLocalTime();

            ZonedDateTime eetz = ezdt.withZoneSameInstant(easternid);
            LocalDateTime easternend = eetz.toLocalDateTime();
            LocalTime endtest = easternend.toLocalTime();
            LocalTime openinghr = LocalTime.parse("08:00");
            LocalTime closinghr = LocalTime.parse("22:00");


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






        if (isvalid == true){
            sqlstring = "INSERT INTO appointments( Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, appointments.Contact_ID) VALUES(? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            userselectstatement = conn.prepareStatement(sqlstring, Statement.RETURN_GENERATED_KEYS);

            userselectstatement.setString(1, title);
            userselectstatement.setString(2, description);
            userselectstatement.setString(3, location);
            userselectstatement.setString(4, type);
            userselectstatement.setTimestamp(5, Timestamp.valueOf(start));
            userselectstatement.setTimestamp(6, Timestamp.valueOf(end));
            userselectstatement.setTimestamp(7, Timestamp.valueOf(time));
            userselectstatement.setString(8, username);
            userselectstatement.setTimestamp(9, Timestamp.valueOf(time2));
            userselectstatement.setString(10, username2);
            userselectstatement.setString(11, String.valueOf(customerid));
            userselectstatement.setString(12, String.valueOf(userid));
            userselectstatement.setString(13, String.valueOf(contactid));

            userselectstatement.execute();
            rs = userselectstatement.getGeneratedKeys();
            rs.next();
            int realid2 = rs.getInt(1);


            Appointment appointment = new Appointment(realid2, title, description, location, username, type, start, end, customerid, userid);
            allAppointments.add(appointment);
            Parent AddScreen = FXMLLoader.load(getClass().getResource("../VIew/AppointmentsScreen.fxml"));
            Scene scene = new Scene(AddScreen);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();}}}
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            String errorMessage = "DATES AND/OR TIMES CANNOT BE NULL";
            alert.setContentText(errorMessage);
            alert.showAndWait();
            isvalid = false;

        }
        }



/**This returns you to the appointment screen*/
    @FXML
    void BackButtonPushed(ActionEvent event) throws IOException {
        Parent LogIn = FXMLLoader.load(getClass().getResource("../VIew/AppointmentsScreen.fxml"));
        Scene scene = new Scene(LogIn);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();}


/**This sets all fields and populates combo boxes*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {

        TypeComboBox.setItems(typecombo);


        UserIDTextField.setText(String.valueOf(LoginScreenController.userid));



        ContactNameDropDown.setItems(allusers);

        LocalTime thestart = LocalTime.of(7,0);
        LocalTime end = LocalTime.of(23,0);

        while(thestart.isBefore(end)){
            StartTimeComboBox.getItems().add(thestart);
            EndTimeComboBox.getItems().add(thestart);
            thestart = thestart.plusMinutes(10);


        }
    }



}








