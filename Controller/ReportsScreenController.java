package Controller;
import Model.Appointment;
import Model.Customer;
import Utils.DBQuery;

import Utils.ReportsInteface;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;

import static Model.Appointment.allAppointments;
import static Model.Appointment.getAllAppointments;
/**This creates the ReportsScreenController Class*/
public class ReportsScreenController implements Initializable {

    public static ObservableList<Appointment> DanielAppointments = FXCollections.observableArrayList();


/**defines fxml featuers*/
    public TableView<Appointment> ScheduleTableView;
    public Label MostLabel;
    public Button BackButton;
    public Label AppointmentsLabel;
    public Label CustomersLabel;
    @FXML
    private TableColumn AppointmentIDColumn;

    @FXML
    private TableColumn TitleColumn;

    @FXML
    private TableColumn TypeColumn;

    @FXML
    private TableColumn DescriptionColumn;

    @FXML
    private TableColumn StartColumn;

    @FXML
    private TableColumn EndColumn;

    @FXML
    private TableColumn CustIDColumn;

    @FXML
    private Label MonthLabel;

    @FXML
    private Button ThisMonthButton;

    @FXML
    private Button NextMonthButton;

    @FXML
    private Button PlanningButton;

    @FXML
    private Button DebriefingButton;

    @FXML
    private Button OtherButton;

    @FXML
    private Label TypeLabel;

    @FXML
    private Button LileeButton;

    @FXML
    private Button DanielButton;

    @FXML
    private Button OtherContactButton;
    /**create variables*/
    int debriefcount = 0;
    int matchingappointments = 0;
    int danielapp = 0;
    int aapp = 0;
    int liapp = 0;
/**set appointments to daniel's appointments*/
    @FXML
    void DanielButtonPushed(ActionEvent event) throws SQLException {
        DanielAppointments.clear();
        for (Appointment a : getAllAppointments()) {

            if (a.getContact().contains("Da")) {
                DanielAppointments.add(a);

            }

        } ScheduleTableView.setItems(DanielAppointments);
    }
/** set label to number of debriefing appointments*/
        @FXML
        void DebriefingButtonPushed (ActionEvent event){
        debriefcount = 0;
            for (Appointment a : getAllAppointments()) {

                if (a.getType().contains("De")) {
                    debriefcount++;

                }
                TypeLabel.setText(String.valueOf(debriefcount));

        }}
/** sets tableview to Li Lee's appointments */
        @FXML
        void LileeButtonPushed (ActionEvent event){
        DanielAppointments.clear();
            for (Appointment a : getAllAppointments()) {
                System.out.println(a.getContact());
                if (a.getContact().contains("Li")) {
                    DanielAppointments.add(a);

                }

            } ScheduleTableView.setItems(DanielAppointments);

        }
/**displays number of appointments next month on label*/
        @FXML
        void NextMonthButtonPushed (ActionEvent event){
            matchingappointments = 0;
            int now = LocalDateTime.now().getMonthValue();
            for (Appointment a: allAppointments){
                int test = a.getStartTime().getMonthValue();
                if(test - now == 1){
                    matchingappointments++;

                }}
            MonthLabel.setText(String.valueOf(matchingappointments));

        }
/** displays number of other appointments*/
        @FXML
        void OtherButtonPushed (ActionEvent event) {
            debriefcount = 0;
            for (Appointment a : getAllAppointments()) {
                if (a.getType().contains("ot")) {
                    debriefcount++;

                }
                TypeLabel.setText(String.valueOf(debriefcount));

            }}
/**Displays appointments for anita*/
        @FXML
        void OtherContactButtonPushed (ActionEvent event) {DanielAppointments.clear();
            for (Appointment a : getAllAppointments()) {
        if (a.getContact().contains("An")) {
            DanielAppointments.add(a);

        }

    } ScheduleTableView.setItems(DanielAppointments);

}
/**Displays number of 'planning' appointments*/
        @FXML
        void PlanningButtonPushed (ActionEvent event){
            debriefcount = 0;
            for (Appointment a : getAllAppointments()) {
                if (a.getType().contains("Pl")) {
                    debriefcount++;

                }
                TypeLabel.setText(String.valueOf(debriefcount));

            }}


/**Displays number of appointments this month*/
        @FXML
        void ThisMonthButtonPushed (ActionEvent event){
        matchingappointments = 0;
            Month now = LocalDateTime.now().getMonth();
            for (Appointment a: allAppointments){
                Month test = a.getStartTime().getMonth();
                if(now == test){
                    matchingappointments++;

                }}
            MonthLabel.setText(String.valueOf(matchingappointments));

        }




/**initializes tableview, contains lambdas */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**Lambda that sets label with total number of appointments*
         * This lambda makes it easy to manipulate fxml features to show values of other manipulated functions.
         * It improves the code because it is easy to read and easy to reuse effectively by changing it a little
         * and still getting a clean result.
         */
        ReportsInteface w1 = () -> AppointmentsLabel.setText(String.valueOf(getAllAppointments().size()));
        w1.customers();
        /**Lambda that sets label with the total number of customers*/
        ReportsInteface w2 = () -> CustomersLabel.setText(String.valueOf(Customer.getAllCustomers().size()));
        w2.customers();
        AppointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointment_ID"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        StartColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        EndColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        CustIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerid"));

        for (Appointment a: getAllAppointments()){
            if (a.getContact().contains("D")){
                danielapp++;
            }
            else if(a.getContact().contains("A")){
                aapp++;
            }
            else{
                liapp++;
            }
        }
        if (danielapp>aapp&& danielapp>liapp){
            MostLabel.setText("Daniel");

        }
        else if(aapp>danielapp&& danielapp>liapp){
            MostLabel.setText("Anika");
        }
        else if(liapp>danielapp&&liapp>aapp){
            MostLabel.setText("Li");
        }
        else if(liapp==danielapp&& liapp>aapp){
            MostLabel.setText("Daniel and Li");
        }
        else if(danielapp==aapp&& danielapp>liapp){
            MostLabel.setText("Daniel and Anika");
        }
        else if(aapp==liapp && aapp> danielapp){
            MostLabel.setText("Anika and Li");
        }
        else{
            MostLabel.setText("All have the same number");
        }

    }
/**
 * @param event back button pushed
 * @throws IOException
 * returns to main screen
 */
    public void BackButtonPushed(ActionEvent event) throws IOException {
        Parent LogIn = FXMLLoader.load(getClass().getResource("../VIew/MainScreen.fxml"));
        Scene scene = new Scene(LogIn);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();}
    }

