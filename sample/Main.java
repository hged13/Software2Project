package sample;

import Utils.dbconnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../VIew/LoginScreen.fxml"));
        Parent root = fxmlloader.load();
        Object loginscreencntrl = fxmlloader.getController();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(scene);

        primaryStage.show();

    }


    public static void main(String[] args) throws IOException {


        dbconnection.startconnection();


        launch(args);



        dbconnection.closeConnection();





    }
}
