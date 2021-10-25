package Controller;
import Model.Appointment;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Appointment.allAppointments;
import static Model.Appointment.getAllAppointments;
/**This creates the AppointmentsScreenController class*/
public class AppointmentsScreenController implements Initializable {
/** This defines fxml features*/
    public TableView AppointmentsTableView;
    public TableColumn IDColumn;
    public TableColumn TitleColumn;
    public TableColumn DescriptionColumn;
    public TableColumn LocationColumn;
    public TableColumn ContactColumn;
    public static ObservableList<Appointment> matchingappointments = FXCollections.observableArrayList();

    public TableColumn StartColumn;
    public TableColumn EndColumn;

    public TableColumn TypeColumn;
    public TableColumn CustomerIDColumn;
    @FXML
    private Button MonthButton;

    @FXML
    private Button WeekButton;

    @FXML
    private Button AddButton;

    @FXML
    private Button UpdateButton;


    @FXML
    private Button BackButton;
    /** create and initialize variables*/
    static int id;
    public static ObservableList<String> typecombo = FXCollections.observableArrayList();
    public static ObservableList<String> allusers = FXCollections.observableArrayList();


/** This switches to add appointment screen */
    @FXML
    void AddButtonPushed(ActionEvent event) throws IOException {

        Parent LogIn = FXMLLoader.load(getClass().getResource("../VIew/AddAppointmentScreen.fxml"));
        Scene scene = new Scene(LogIn);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

/** This switches to MainScreen */
    @FXML
    void BackButtonPushed(ActionEvent event) throws IOException {
    Parent LogIn = FXMLLoader.load(getClass().getResource("../VIew/MainScreen.fxml"));
    Scene scene = new Scene(LogIn);
    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();}
/**This populates tableview with appointments occuring that month*/
    @FXML
    void MonthButtonPushed(ActionEvent event) {
        matchingappointments.clear();

        Month now = LocalDateTime.now().getMonth();
        for (Appointment a: allAppointments){
            Month test = a.getStartTime().getMonth();
            if(now == test){
                matchingappointments.add(a);

            }}
            AppointmentsTableView.setItems(matchingappointments);




        }








/**This switches to edit appointment screen and passes on an id variable*/
    @FXML
    void UpdateButtonPushed(ActionEvent event) throws IOException {
        int temp = 0;
        Appointment tempid = (Appointment) AppointmentsTableView.getSelectionModel().getSelectedItem();
        for (Appointment a: allAppointments){
            if( tempid == a){

                id = temp;
            }
            temp++;
        }
        Parent LogIn = FXMLLoader.load(getClass().getResource("../VIew/EditAppointmentScreen.fxml"));
        Scene scene = new Scene(LogIn);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }
/**This populates tableview with appointments occurring during that week */
    @FXML
    void WeekButtonPushed(ActionEvent event) {
        matchingappointments.clear();
        Month now = LocalDateTime.now().getMonth();
        for (Appointment a: allAppointments){
            Month test = a.getStartTime().getMonth();
            if(now == test){
                if(a.getStartTime().getDayOfMonth() - LocalDateTime.now().getDayOfMonth() <7)
                matchingappointments.add(a);

            }}
        AppointmentsTableView.setItems(matchingappointments);

    }



/** This initialize the tableview and sets the items*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allusers.clear();

        IDColumn.setCellValueFactory(new PropertyValueFactory<>("appointment_ID"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        ContactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        StartColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        EndColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        CustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerid"));

        AppointmentsTableView.setItems(getAllAppointments());
        Connection conn = dbconnection.getConnection();
        String userselectstatement = "SELECT * FROM contacts";

        try {
            DBQuery.setPreparedStatement(conn, userselectstatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Statement statement2 = DBQuery.getPreparedStatement();

        ResultSet rs = null;
        try {
            rs = statement2.executeQuery(userselectstatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while(true){
            try {
                if (!rs.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            String division = null;
            try {
                String user = rs.getString("Contact_Name");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            String user = null;
            try {
                user = rs.getString("Contact_Name");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            allusers.add(user);
        }



       }
/**
 * @param event event button pushed
 * @throws SQLException
 * This removes appointmnet from database and updates the tableview*/
    public void DeleteButtonPushed(ActionEvent event)  throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Cancel");
        alert.setContentText("are you sure you want to remove the appointmnent?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            Appointment appointment = (Appointment) AppointmentsTableView.getSelectionModel().getSelectedItem();
            int id = appointment.getAppointment_ID();
            Connection conn = dbconnection.getConnection();
            PreparedStatement userselectstatement = conn.prepareStatement("DELETE from appointments WHERE Appointment_ID =" + "?");
            userselectstatement.setString(1, String.valueOf(id));
            userselectstatement.executeUpdate();
            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
            alert2.setTitle("Deleted");
            alert2.setHeaderText("Deleted");
            alert2.setContentText("appointment with ID" + appointment.getAppointment_ID() + "has been deleted. Appointment Type:" + appointment.getType());
            alert2.showAndWait();
            AppointmentsTableView.setItems(getAllAppointments());


        }

    }

}
