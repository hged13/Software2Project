package Utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
/** This creates the DBQuery class*/
public class DBQuery {
    private static Statement statement;

    /**
     * @param conn the coneection to pass in
     * @param sqlstatement the sql statement to pass in
     * @throws SQLException
     */
    public static void setPreparedStatement(Connection conn, String sqlstatement) throws SQLException {
        statement = conn.prepareStatement(sqlstatement);

    }
    /**
     * @return the statement
     */

    public static Statement getPreparedStatement(){
        return statement;
    }
}
