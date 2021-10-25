package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**This creates the dbconnection class*/
public class dbconnection {

    // jdbc url parts
    private static final String protocol = "jdbc";
    private static final String vendorname = ":mysql:";
    private static final String ipaddress =  "//wgudb.ucertify.com:3306/";
    private static final String dbname =  "WJ07NQz";

    private static final String jdbcurl = protocol + vendorname + ipaddress + dbname;
    private static final String Jdbcdriver = "com.mysql.cj.jdbc.Driver";
    private static  Connection conn = null;
    private static final String username = "U07NQz";
    private static final String password = "53689076413";
/**connects to the database
 * @return the connection
  */
    public static Connection startconnection()  {
        try{
        Class.forName(Jdbcdriver);
        conn = DriverManager.getConnection(jdbcurl, username, password);
        System.out.println("Connection Successful");
    }
        catch(ClassNotFoundException e){
        e.printStackTrace();
    }
        catch(SQLException e){
           e.printStackTrace();
        }

        return conn;

}
/**
 * @return returns the connection
 */
public static Connection getConnection(){
        return (Connection) conn;
}
/**this closes the connection */
public static void closeConnection(){
    try {
        conn.close();
    }
    catch(Exception e){
    }
}
}
