package repository;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionProvider implements AutoCloseable{

    private Connection connection;

    public ConnectionProvider() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@avdf01.rh:2019/rayannav21c.rh", "N12KRP209", "n");
            connection.setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws Exception {
        connection.commit();
        connection.close();
    }
}
