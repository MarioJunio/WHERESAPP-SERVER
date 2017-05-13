package util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by MarioJ on 06/04/15.
 */
public class JDBCUtil implements Serializable {

    private final String URL = "jdbc:mysql://localhost:3306/";
    private final String DB_NAME = "wheresapp";
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String USER = "root";
    private final String PASSWD = "";

    @Produces
    @RequestScoped
    public Connection getConnection() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class.forName(DRIVER).newInstance();
        return DriverManager.getConnection(URL + DB_NAME, USER, PASSWD);
    }

    public void close(@Disposes java.sql.Connection connection) throws SQLException {
        connection.close();
    }

}
