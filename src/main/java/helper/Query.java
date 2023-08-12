package helper;

import java.sql.*;
import static helper.JDBC.connection;

public class Query {
    private static ResultSet result;
    /** Executes a MySQL statement.
     * @return the number of rows affected
     * @param sql the MySQL statement
     */
    public static int updateQuery(String sql) throws SQLException {
        PreparedStatement pStatement = connection.prepareStatement(sql);
        int rowsAffected = pStatement.executeUpdate();
        return rowsAffected;
    }

    /** Returns the result set of the select query.
     * @return the result set of the select query
     * @param sql the select statement
     */
    public static ResultSet selectQuery(String sql) throws SQLException {
        PreparedStatement pStatement = connection.prepareStatement(sql);
        result = pStatement.executeQuery();
        return result;
    }


    /** Returns the result set.
     * @return the result set
     */
    public static ResultSet getResults() {
        return result;
    }
}
