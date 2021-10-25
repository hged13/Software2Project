package Model;

import Utils.DBQuery;
import Utils.dbconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;


/** This creates the appointment class*/

public class Appointment {
    /** these are the appointment class variables */
    private int userid;
    private  int customerid;
    private int appointment_ID;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    /** this is the constructor
     * @param appointment_ID
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param type
     * @param startTime
     * @param endTime
     * @param customerid
     * @param userid*/

   public Appointment(int appointment_ID, String title, String description, String location, String contact, String type, LocalDateTime startTime, LocalDateTime endTime, int customerid, int userid){
       this.appointment_ID = appointment_ID;
       this.title = title;
       this.description = description;
       this.location = location;
       this.contact = contact;
       this.type = type;
       this.startTime = startTime;
       this.endTime = endTime;
       this.customerid = customerid;
       this.userid = userid;


   }

    /**
     *
     * @return the appointment ID
     */
    public int getAppointment_ID() {
        return appointment_ID;
    }

    /**
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     *
     * @return the appointment type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     *
     * @return the end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     *
     * @return the list of all appointments
     */
    public static ObservableList<Appointment> getAllAppointments() {
        allAppointments.clear();
        try {

            Connection conn = dbconnection.getConnection();
            String userselectstatement = "SELECT Appointment_ID, Title, Description, Location, contacts.Contact_Name, Type, Start, End, User_ID, Customer_ID " +
                    "from appointments INNER JOIN contacts" +
                    " ON contacts.Contact_ID = appointments.Contact_ID ORDER BY Appointment_ID;";

            DBQuery.setPreparedStatement(conn, userselectstatement);
            Statement statement = DBQuery.getPreparedStatement();


            ResultSet rs = statement.executeQuery(userselectstatement);
            while (rs.next()) {
                int id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String user = rs.getString("Contact_Name");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int userid2 = rs.getInt("User_ID");
                int custid = rs.getInt("Customer_ID");


                Appointment appointment = new Appointment(id, title, description, location, user, type, start, end, userid2, custid);


                allAppointments.add(appointment);


            }

            statement.close();
            return allAppointments;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

    }


    /**
     *
     * @return the userid
     */
    public int getUserid() {
        return userid;
    }

    /**
     *
     * @return the customerid
     */
    public int getCustomerid() {
        return customerid;
    }


}


