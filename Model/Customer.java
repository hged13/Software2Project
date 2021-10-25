package Model;

import Utils.DBQuery;
import Utils.dbconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**This creates the Customer class */
public class Customer {
    /**these are the variables for the customer class*/
    public Integer customer_ID;
    public String customerName;
    public String address;
    public String postal;
    public String phoneNumber;
    public int Country_ID;

    public String division;
    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**
     *
     * @return the countryID
     */
    public int getCountryID(){
        return Country_ID;
    }


    /**
     *
     * @return the division
     */
    public String getDivision() {
        return division;
    }


    /**
     *The customer constructor
     * @param customer_ID
     * @param customerName
     * @param address
     * @param postal
     * @param phoneNumber
     * @param division
     * @param Country_ID
     */
    public Customer(int customer_ID, String customerName, String address, String postal, String phoneNumber, String division, int Country_ID){
        this.customer_ID = customer_ID;
        this.customerName = customerName;
        this.address = address;
        this.postal = postal;
        this.phoneNumber = phoneNumber;
        this.division = division;
        this.Country_ID = Country_ID;

    }
    /**
     *
     * @return the customer ID
     */
    public Integer getCustomer_ID() {
        return customer_ID;
    }

    /**
     *
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @return the postal code
     */
    public String getPostal() {
        return postal;
    }

    /**
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @return the list of all customers
     */
    public static ObservableList<Customer> getAllCustomers(){

        allCustomers.clear();
                try{
                    Connection conn = dbconnection.getConnection();
                    String userselectstatement = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address,customers.Postal_Code, customers.Phone, first_level_divisions.Division, first_level_divisions.Country_ID" +
                            " FROM customers LEFT JOIN first_level_divisions " +
                            " ON customers.Division_ID = first_level_divisions.Division_ID";

                    DBQuery.setPreparedStatement(conn, userselectstatement);
                    Statement statement = DBQuery.getPreparedStatement();

                    ResultSet rs = statement.executeQuery(userselectstatement);
                   while(rs.next()) {
                        int id =rs.getInt("Customer_ID");
                        String name = rs.getString("Customer_Name");
                        String address = rs.getString("Address");
                        String postal = rs.getString("Postal_Code");
                        String phone = rs.getString("Phone");
                        String division = rs.getString("Division");
                        int country = rs.getInt("Country_ID");

                       Customer customer = new Customer(id, name, address, postal, phone, division, country);


                        allCustomers.add(customer);




                    }
                    statement.close();
                   return allCustomers;


    } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    return null;
                }


    }}

