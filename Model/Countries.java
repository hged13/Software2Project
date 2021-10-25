package Model;

import Utils.DBQuery;
import Utils.dbconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**This creates the Countries class*/
public class Countries {
    /**This is the list for the country class */
    public static ObservableList<String> allCountries = FXCollections.observableArrayList();



    /**
     *
     * @return the list of all countries
     * @throws SQLException
     */
    public static ObservableList<String> getAllCountries() throws SQLException {
        allCountries.clear();
        String country = null;

            Connection conn = dbconnection.getConnection();
            String userselectstatement = "SELECT * FROM countries";

            DBQuery.setPreparedStatement(conn, userselectstatement);
            Statement statement2 = DBQuery.getPreparedStatement();

            ResultSet rs = statement2.executeQuery(userselectstatement);
            while(rs.next()) {
                country = rs.getString("Country");

                allCountries.add(country);
                System.out.println(country);

            }

            return allCountries;}}
