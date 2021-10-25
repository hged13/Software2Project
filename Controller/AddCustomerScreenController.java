package Controller;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static Model.Countries.allCountries;
import static Model.Countries.getAllCountries;
import static Model.Customer.allCustomers;
/**This creates the AddCustomerScreenController class*/
public class AddCustomerScreenController implements Initializable {
/** This defines all fxml features*/
    @FXML
    private TextField CustomerIDTextField;

    @FXML
    private TextField CustomerNameTextField;

    @FXML
    private TextField AddressTextField;

    @FXML
    private TextField PostalCodeTextField;

    @FXML
    private TextField PhoneNumberTextField;

    @FXML
    private Button AddButton;

    @FXML
    private Button BackButton;

    @FXML
    private ComboBox<String> CountryComboBox;

    @FXML
    private ComboBox<String> DivisionComboBox;
    /** This creates and initialize variables. */
    public static ObservableList<String> alldivisions = FXCollections.observableArrayList();
    public static String country;
    public static int divisionid;
    public static int countryid;
    public boolean validattempt;





    /**This validate and adds customer to database */
    @FXML
    void AddButtonPushed(ActionEvent event) throws SQLException, IOException {
        validattempt = true;
        int id =0;
        for (Customer c : allCustomers)
            if (c.getCustomer_ID()> id) {
                id = c.getCustomer_ID();
            }
        id++;
        String name = (CustomerNameTextField.getText());
        String address = (AddressTextField.getText());
        String postal = (PostalCodeTextField.getText());
        String phone = (PhoneNumberTextField.getText());
        LocalDateTime time = LocalDateTime.now();
        String username = User.getUserID();
        LocalDateTime time2 = LocalDateTime.now();
        String username2 = User.getUserID();
        Connection conn = dbconnection.getConnection();



      String division = DivisionComboBox.getSelectionModel().getSelectedItem();
        if(name.isEmpty()|| address.isEmpty()||postal.isEmpty()||phone.isEmpty()){
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            String errorMessage = "FIELDS CANNOT BE EMPTY";
            alert2.setContentText(errorMessage);
            alert2.showAndWait();
            validattempt = false;
        }


    if(validattempt== true){
        PreparedStatement userselectstatement2 = conn.prepareStatement("SELECT Division_ID FROM first_level_divisions WHERE Division = ?");
        userselectstatement2.setString(1, division);
        ResultSet rs2 = userselectstatement2.executeQuery();
        while (rs2.next()){
          divisionid = rs2.getInt(1);
        }

        conn = dbconnection.getConnection();
         userselectstatement2 = conn.prepareStatement("INSERT INTO customers" + "(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)"+
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ;
        userselectstatement2.setString(1, String.valueOf(id));
        userselectstatement2.setString(2, name);
        userselectstatement2.setString(3, address);
        userselectstatement2.setString(4, postal);
        userselectstatement2.setString(5, phone);
        userselectstatement2.setString(6, String.valueOf(time));
        userselectstatement2.setString(7, username);
        userselectstatement2.setString(8, String.valueOf(time2));
        userselectstatement2.setString(9, username2);
        userselectstatement2.setString(10, String.valueOf(divisionid));


        userselectstatement2.executeUpdate();
        String country = CountryComboBox.getSelectionModel().getSelectedItem();

        PreparedStatement userselectstatement = conn.prepareStatement("SELECT Country_ID FROM countries WHERE Country = ?");
        userselectstatement.setString(1, country);
        ResultSet rs = userselectstatement.executeQuery();
        while (rs.next()){
            countryid = rs.getInt("Country_ID");
        }
        Customer customer =new Customer(id, name, address, postal, phone, division, countryid);
        allCustomers.add(customer);
        Parent AddScreen = FXMLLoader.load(getClass().getResource("../VIew/CustomerScreen.fxml"));
        Scene scene = new Scene(AddScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();}


    }
/**This returns you to Customer screen*/
    @FXML
    void BackButtonPushed(ActionEvent event) throws IOException {
        Parent LogIn = FXMLLoader.load(getClass().getResource("../VIew/CustomerScreen.fxml"));
        Scene scene = new Scene(LogIn);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

/**This populates the Country drop down box*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            getAllCountries();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        CountryComboBox.setItems(allCountries);


    }
/**
 * @param event country selected
 * @throws SQLException
 * This populates division drop down based on country selected
 */
    public void CountrySelected(ActionEvent event) throws SQLException {
        alldivisions.clear();
        String userselectstatement = null;
       String selection = CountryComboBox.getSelectionModel().getSelectedItem();
        if (selection.equals("U.S")){
            userselectstatement = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 1 ";

        }
        if (selection.equals("UK")){
            userselectstatement = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 2 ";
        }
       if(selection.equals("Canada")) {
           userselectstatement = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 3 ";
       }
        Connection conn = dbconnection.getConnection();


        DBQuery.setPreparedStatement(conn, userselectstatement);
        Statement statement2 = DBQuery.getPreparedStatement();

        ResultSet rs = statement2.executeQuery(userselectstatement);
        while(rs.next()){

           String division = rs.getString("Division");

            alldivisions.add(division);
        }
        DivisionComboBox.setItems(alldivisions);


    }
}

