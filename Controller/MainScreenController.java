package Controller;
import Model.Appointment;
import Model.Customer;
import Utils.ReportsInteface;
import Utils.UserInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import static Controller.LoginScreenController.UserName;
import static Model.Appointment.getAllAppointments;
/**This creates the MainScreenController class*/
public class MainScreenController implements Initializable {

    /** defines fxml features*/
    public Button ReportButton;
    public Label userlabel;
    @FXML
    private Label NameLabel;

    @FXML
    private Label AppointmentAlertLabel;

    @FXML
    private Button CustomerInfoButton;

    @FXML
    private Button AppointmentsButton;

    /**switches to appointments screen */
    @FXML
    void AppointmentsButtonPushed(ActionEvent event) throws IOException {
        Parent LogIn = FXMLLoader.load(getClass().getResource("../VIew/AppointmentsScreen.fxml"));
        Scene scene = new Scene(LogIn);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }
/**switches to customer screen*/
    @FXML
    void CustomerInfoButtonPushed(ActionEvent event) throws IOException {
        Parent LogIn = FXMLLoader.load(getClass().getResource("../VIew/CustomerScreen.fxml"));
        Scene scene = new Scene(LogIn);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }


/**Checks for appointments coming in next 15 minutes, contains lambda*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**Lambda that sets text with the username of who logged in.
         * This lambda makes it easy to assign the label to more complicated variables, it looks clean and
         * improves the code by making it easy to pick out. */
        UserInterface w1 = () -> userlabel.setText(UserName);
        w1.user();

        AppointmentAlertLabel.setText("No upcoming appointments");
        LocalDateTime currentdatetime = LocalDateTime.now();
        LocalDate currentDate = currentdatetime.toLocalDate();
        LocalTime currenttime = currentdatetime.toLocalTime();
        System.out.println(currentDate);
        for(Appointment a: getAllAppointments()){
            LocalDateTime start1 = a.getStartTime();
            ZonedDateTime uzdt = start1.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
            LocalDateTime start2 = uzdt.toLocalDateTime();
            LocalTime start = start2.toLocalTime();
            LocalDate startdate = start2.toLocalDate();
            int id = a.getAppointment_ID();
            Long timedifference = ChronoUnit.MINUTES.between(start, currenttime);
            Long realtime = (timedifference -1) * -1;
                if(currentDate.equals(startdate) && realtime > 0 && realtime<= 15) {

                    AppointmentAlertLabel.setText("There is an appointment for today:" + " " + currentDate + " " + " in" + " " + realtime + " "+ "minutes. Appointment ID: " + id);
                }
            }

        }
/** switches to reports screen
 * @param event report button pushed
 * @throws IOException
 * */
    public void ReportButtonPushed(ActionEvent event) throws IOException {
        Parent LogIn = FXMLLoader.load(getClass().getResource("../VIew/Reportsview.fxml"));
        Scene scene = new Scene(LogIn);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}




