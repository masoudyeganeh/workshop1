package repository;

import entity.Costs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CostsDA implements AutoCloseable {

    private Connection connection;

    private PreparedStatement preparedStatement;

    public CostsDA(Connection connection) {
        this.connection = connection;
    }

    public List<Costs> selectAll() throws SQLException {
        preparedStatement = connection.prepareStatement("select * from Costs order by id");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Costs> list = new ArrayList<>();
        while (resultSet.next()) {
            Costs costs = new Costs()
                    .setId(resultSet.getInt("id"))
                    .setFromHour(resultSet.getString("from_hour"))
                    .setToHour(resultSet.getString("to_hour"))
                    .setCostPerHour(resultSet.getDouble("cost_per_hour"));
            list.add(costs);
        }
        return list;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
    }
}
