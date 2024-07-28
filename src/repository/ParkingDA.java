package repository;

import entity.ExceptionWrapper;
import entity.Parking;
import entity.RecordNotFoundException;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.*;

public class ParkingDA implements AutoCloseable {
    private Connection connection;

    private PreparedStatement preparedStatement;

    public ParkingDA(Connection connection) {
        this.connection = connection;
    }

    public ParkingDA() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        this.connection = DriverManager.getConnection("jdbc:oracle:thin:@avdf01.rh:2019/rayannav21c.rh","N12KRP209","n");
        connection.setAutoCommit(false);
    }

    public void insert(Parking parking) throws Exception {
        preparedStatement = connection.prepareStatement("insert into Parking (id, brand, model, car_id, enter_time, exit_time, cost) values (?, ?, ?, ?, ?, null, ?)");
        preparedStatement.setInt(1, parking.getId());
        preparedStatement.setString(2, parking.getBrand());
        preparedStatement.setString(3, parking.getModel());
        preparedStatement.setInt(4, parking.getCarId());
        preparedStatement.setTimestamp(5, parking.getEnterTime());
        preparedStatement.setDouble(6, parking.getCost());
        preparedStatement.executeUpdate();
    }

    public void update(Parking parking) throws Exception {
        preparedStatement = connection.prepareStatement("update Parking set exit_time = ?, cost = ? where id = ?");
        preparedStatement.setTimestamp(1, parking.getExitTime());
        preparedStatement.setDouble(2, parking.getCost());
        preparedStatement.setInt(3, parking.getId());
        int ii = parking.getId();
        int i = parking.getCarId();
        double d = parking.getCost();
        Timestamp t = parking.getExitTime();
        preparedStatement.executeUpdate();
    }

    public void selectOneByCarId(Parking parking) throws Exception, RecordNotFoundException {
        preparedStatement = connection.prepareStatement("select * from Parking where car_id = ?");
        preparedStatement.setInt(1, parking.getCarId());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()) {
            throw new RecordNotFoundException();
        }
        parking.setModel(resultSet.getString("model"))
                .setBrand(resultSet.getString("brand"))
                .setId(resultSet.getInt("id"))
                .setEnterTime(resultSet.getTimestamp("enter_time"));
        }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
    }
}


