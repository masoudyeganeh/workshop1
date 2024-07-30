package repository;

import entity.Costs;
import entity.FixedCost;
import entity.RecordNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class FixedCostDA implements AutoCloseable{

    private Connection connection;

    private PreparedStatement preparedStatement;

    public FixedCostDA(Connection connection) {this.connection = connection;}

    public FixedCost selectAll() throws Exception{
        preparedStatement = connection.prepareStatement("select * from Fixed_cost");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new RecordNotFoundException();
        }
        FixedCost fixedCost = new FixedCost().setId(resultSet.getInt("id")).setFixedCost(resultSet.getDouble("fixed_cost"));
        return fixedCost;
    }

    @Override
    public void close() throws Exception {

    }
}
