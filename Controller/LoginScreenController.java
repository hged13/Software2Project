package Controller;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**This creates the LoginScreenController class*/
public class LoginScreenController implements Initializable {




/**defines fxml features*/
    @FXML
    private TextField UserNameTextField;

    @FXML
    private TextField PasswordTextField;

    @FXML
    private Button LoginButton;

    @FXML
    private Label LabelID;
/**create variables*/
    private String erortext = "INVALID ATTEMPT";
public static int userid = 2;
public static String UserName;



/**check for valid login, add attempt to text file, switch to main screen if successful*/
    @FXML
    void LoginButtonPushed(ActionEvent event) throws SQLException, IOException {
       Connection conn =  dbconnection.getConnection();
        String userselectstatement = "SELECT * FROM users";
       DBQuery.setPreparedStatement(conn, userselectstatement);
        Statement statement = DBQuery.getPreparedStatement();

        statement.execute(userselectstatement);
        ResultSet rs = statement.getResultSet();
        boolean validlogin = false;
        while(rs.next()) {
            String User_Name = rs.getString("User_Name");
            String Password = rs.getString("Password");
            if (User_Name.equals("test")){
                userid = 1;
            }
            else {
                userid = 2;
            }


            if(User_Name.equals(UserNameTextField.getText())&& Password.equals(PasswordTextField.getText()) ){
                String filename = "src/login_activity.txt";
                FileWriter fWriter = new FileWriter(filename, true);
                PrintWriter outputFile = new PrintWriter(fWriter);
                outputFile.println(UserNameTextField.getText() + " logged in on " + LocalDateTime.now());
                System.out.println(UserNameTextField.getText() + " logged in on " + LocalDateTime.now());
                outputFile.close();
                UserName = UserNameTextField.getText();
                Parent LogIn = FXMLLoader.load(getClass().getResource("../VIew/MainScreen.fxml"));
                Scene scene = new Scene(LogIn);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                validlogin = true;
                window.show();



            }




    }
        if(validlogin == false){
            String filename = "src/login_activity.txt";
            FileWriter fWriter = new FileWriter(filename, true);
            PrintWriter outputFile = new PrintWriter(fWriter);
            outputFile.println(UserNameTextField.getText() + " unsuccessful login attempt on " + LocalDateTime.now());
            System.out.println(UserNameTextField.getText() + " unsuccessful login attempt on " + LocalDateTime.now());
            outputFile.close();


                Alert alert = new Alert(Alert.AlertType.ERROR);
                String errorMessage = erortext;
                alert.setContentText(errorMessage);
                alert.showAndWait();


            }
        }

        /**sets langauge according to computer settings*/
        @Override
public void initialize(URL url, ResourceBundle rb){
        try{
                rb = ResourceBundle.getBundle("Controller/resourcecbundle", Locale.getDefault());





            if(Locale.getDefault().getLanguage().equals("fr")){
                UserNameTextField.setText(rb.getString("Username"));
                PasswordTextField.setText(rb.getString("Password"));
                LoginButton.setText(rb.getString("enter"));
                erortext = rb.getString("invalid");
            }


}catch (Exception e){
        System.out.println(e.getMessage());



        }
            System.out.println(Locale.getDefault().getLanguage());
            LabelID.setText(ZoneId.systemDefault().toString());

        }}
