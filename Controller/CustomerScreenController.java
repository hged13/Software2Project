package Controller;
import Model.Customer;
import Utils.dbconnection;
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
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Customer.allCustomers;

/**This creates the CustomerScreenController class*/
public class CustomerScreenController implements Initializable {



  /** This defines fxml features */

    @FXML
    public Button AddButton;
    @FXML
    public Button BackButton;

    @FXML
    public Button EditButton;
    @FXML
    public Button DeleteButton;


    
    public TableView CustomersTable;
    public TableColumn CustomerIDColumn;
    public TableColumn CustomerNameColumn;
    public TableColumn CustomerAddressColumn;
    public TableColumn PostalCodeColumn;
    public TableColumn PhoneNumberColumn;
    public TableColumn DivisionColumn;
    public static int tempid;
    public static int id;

/** This switches to add customer screen*/
    @FXML
    void AddButtonPushed(ActionEvent event) throws IOException {

        Parent AddScreen = FXMLLoader.load(getClass().getResource("../VIew/AddCustomerScreen.fxml"));
        Scene scene = new Scene(AddScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();



        }
        /** This switches to Main Screen */
    @FXML
    void BackButtonPushed(ActionEvent event) throws IOException {
        Parent LogIn = FXMLLoader.load(getClass().getResource("../VIew/MainScreen.fxml"));
        Scene scene = new Scene(LogIn);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }
/** This switches to edit customer screen, passes an ID*/
    @FXML
    void EditButtonPushed(ActionEvent event) throws IOException {
      Customer cust = (Customer) CustomersTable.getSelectionModel().getSelectedItem();
      tempid = cust.getCustomer_ID();
      id = allCustomers.indexOf(cust);


        Parent LogIn = FXMLLoader.load(getClass().getResource("../VIew/EditCustomerScreen.fxml"));
        Scene scene = new Scene(LogIn);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();




    }
/**
 * @throws SQLException
 * This updates customer table view */
    public void updateCustomersTableView() throws SQLException {
        CustomersTable.setItems(Customer.getAllCustomers());

        }








/**This initializes the table view */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CustomersTable.setItems(Customer.getAllCustomers());
        CustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customer_ID"));
        CustomerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        CustomerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        PhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        PostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postal"));
        DivisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));







    }
/**
 * @param event delete button pushed
 * @throws SQLException
 * Removes customer from database, checks to make sure there are no associated appointments
 */
    public void DeleteButtonPushed(ActionEvent event) throws SQLException {
        boolean validattempt = true;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Cancel");
        alert.setContentText("are you sure you want to remove the customer?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            Customer customertodelete = (Customer) CustomersTable.getSelectionModel().getSelectedItem();
            int id = customertodelete.getCustomer_ID();
            Connection conn = dbconnection.getConnection();
            PreparedStatement userselectstatement = conn.prepareStatement("SELECT * from appointments WHERE Customer_ID =" + "?");
            userselectstatement.setString(1, String.valueOf(id));
            ResultSet rs = userselectstatement.executeQuery();
            if (rs.next()){
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                String errorMessage = "CANNOT DELETE CUSTOMER WHILE CUSTOMER HAS APPOINTMENTS";
                alert.setContentText(errorMessage);
                alert.showAndWait();
                validattempt = false;



            }
            if (validattempt == true){
            userselectstatement = conn.prepareStatement("DELETE from customers WHERE Customer_ID =" + "?");
            userselectstatement.setString(1, String.valueOf(id));
            userselectstatement.executeUpdate();
            updateCustomersTableView();
            }

        }

        }}
