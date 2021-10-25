package Controller;

import Model.Countries;
import Model.Customer;
import Model.User;
import Utils.DBQuery;
import Utils.dbconnection;
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

import static Controller.AddCustomerScreenController.alldivisions;
import static Controller.CustomerScreenController.id;
import static Controller.CustomerScreenController.tempid;
import static Model.Customer.allCustomers;
import static Model.Customer.getAllCustomers;
/**This creates the EditCustomerViewController Class*/
public class EditCustomerViewController implements Initializable {
/**Defines fxml features*/
    @FXML
    private TextField CustomerIDTextField;

    @FXML
    private TextField NameTextField;

    @FXML
    private TextField AddressTextField;

    @FXML
    private TextField PostalCodeTextField;

    @FXML
    private TextField PhoneNumberTextField;

    @FXML
    private Button UpdateButton;

    @FXML
    private Button BackButton;

    @FXML
    private ComboBox<String> CountryDropDown;

    @FXML
    private ComboBox<String> DivisionDropDown;
/**create and initialize variables*/
    public static String tempcountry;
    public static String division;

    public static int divisionid2;
    public boolean validattempt;

/**switches to main screen */
    @FXML
    void BackButtonPushed(ActionEvent event) throws IOException {
        Parent LogIn = FXMLLoader.load(getClass().getResource("../VIew/MainScreen.fxml"));
        Scene scene = new Scene(LogIn);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();


    }
/** populates division drop down*/
    @FXML
    void CountryDropDownSelected(ActionEvent event) throws SQLException {
        alldivisions.clear();
        String userselectstatement = null;
        String selection = CountryDropDown.getValue();
        if(selection!=null){
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
        DivisionDropDown.setItems(alldivisions);}

    }


/**validates and updates customer in database*/
    @FXML
    void UpdateButtonPushed(ActionEvent event) throws SQLException, IOException {
        validattempt = true;
        String name = (NameTextField.getText());
        String address = (AddressTextField.getText());
        String postal = (PostalCodeTextField.getText());
        String phone = (PhoneNumberTextField.getText());
        LocalDateTime time = LocalDateTime.now();
        String username2 = User.getUserID();
        Customer cust = allCustomers.get(id);
       int id = cust.getCustomer_ID();
        if(name.isEmpty()|| address.isEmpty()||postal.isEmpty()||phone.isEmpty()){
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            String errorMessage = "FIELDS CANNOT BE EMPTY";
            alert2.setContentText(errorMessage);
            alert2.showAndWait();
            validattempt = false;
        }
    if (validattempt == true){

        Connection conn = dbconnection.getConnection();
        PreparedStatement userselectstatement = conn.prepareStatement("SELECT Division_ID FROM first_level_divisions WHERE Division = ?");
        userselectstatement.setString(1, division);
        ResultSet rs = userselectstatement.executeQuery();
        while(rs.next()){
           divisionid2 = rs.getInt(1);


        }




        userselectstatement = conn.prepareStatement("UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?");
        userselectstatement.setString(1, String.valueOf(name));
        userselectstatement.setString(2, address);
        userselectstatement.setString(3, postal);
        userselectstatement.setString(4, phone);;
        userselectstatement.setString(5, String.valueOf(time));
        userselectstatement.setString(6, username2);
        userselectstatement.setString(7, String.valueOf(divisionid2));
        userselectstatement.setString(8, String.valueOf(id));



        userselectstatement.executeUpdate();

        Parent AddScreen = FXMLLoader.load(getClass().getResource("../VIew/CustomerScreen.fxml"));
        Scene scene = new Scene(AddScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();}

    }
/**sets all text fields with selected customer data and populates drop downs*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

      int realid = CustomerScreenController.id;
        Connection conn = dbconnection.getConnection();
        Customer temp = getAllCustomers().get(realid);
        CustomerIDTextField.setText(String.valueOf(temp.getCustomer_ID()));
        NameTextField.setText(temp.getCustomerName());
       AddressTextField.setText(temp.getAddress());
       PostalCodeTextField.setText(temp.getPostal());
       PhoneNumberTextField.setText(temp.getPhoneNumber());
        try {
            Countries.getAllCountries();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        tempid = temp.getCountryID();
         String pstm = "SELECT Country from countries WHERE (Country_ID = ?)";
        PreparedStatement userselect = null;
        try {
            userselect = conn.prepareStatement(pstm);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            userselect.setInt(1, tempid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet rs = null;
        try {
            rs = userselect.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while(true){
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                tempcountry = rs.getString("Country");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }




        CountryDropDown.setValue(tempcountry);
        DivisionDropDown.setPromptText(temp.getDivision());
        CountryDropDown.setItems(Countries.allCountries);
        division = temp.getDivision();

        alldivisions.clear();
        String userstatement = null;
        String selection = CountryDropDown.getValue();
        if (selection.equals("U.S")){
            userstatement = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 1 ";

        }
        if (selection.equals("UK")){
            userstatement = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 2 ";
        }
        if(selection.equals("Canada")) {
            userstatement = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 3 ";
        }


        try {
            userselect = conn.prepareStatement(userstatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            rs = userselect.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        while(true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                division = rs.getString("Division");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            alldivisions.add(division);

        }


        DivisionDropDown.setItems(alldivisions);
        }

/**assigns division to the selected division
 * @param event drop down selected
 * */
    public void DivisionDropDownSelected(ActionEvent event) {
        division = DivisionDropDown.getSelectionModel().getSelectedItem();
    }
}


