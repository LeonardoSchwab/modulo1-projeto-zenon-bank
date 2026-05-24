package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;

public class TransactionSQLRepository implements TransactionRepository {

    public TransactionSQLRepository() {
    }

    @Override
    public Optional<Transaction> findByOriginName(String name) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT step,paymentType,amount,nameOrig,oldbalanceOrig,newbalanceOrig,nameDest,oldbalanceDest,newbalanceDest,isFraud,isFlaggedFraud from TRANSACTIONS WHERE nameOrig = ? LIMIT 1");)
        {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                Transaction transaction = mapResultSetToTransaction(resultSet);
                return  Optional.of(transaction);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) {
        try {
            long step = rs.getLong("step");
            String type = rs.getString("paymentType");
            BigDecimal amount = rs.getBigDecimal("amount");
            String nameOrig = rs.getString("nameOrig");
            BigDecimal oldbalanceOrig = rs.getBigDecimal("oldbalanceOrig");
            BigDecimal newbalanceOrig = rs.getBigDecimal("newbalanceOrig");
            String nameDest = rs.getString("nameDest");
            BigDecimal oldbalanceDest = rs.getBigDecimal("oldbalanceDest");
            BigDecimal newbalanceDest = rs.getBigDecimal("newbalanceDest");
            boolean isFraud = rs.getBoolean("isFraud");
            boolean isFlaggedFraud = rs.getBoolean("isFlaggedFraud");

            TransactionCustomer origin = new TransactionCustomer(nameOrig, oldbalanceOrig, newbalanceOrig);
            TransactionCustomer dest = new TransactionCustomer(nameDest, oldbalanceDest, newbalanceDest);

            return new Transaction(step,Transaction.Payment_type.valueOf(type),amount,origin,dest,isFraud,isFlaggedFraud);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public boolean save(Transaction transaction) {
        String sql = "INSERT INTO TRANSACTIONS (step,paymentType,amount,nameOrig,oldbalanceOrig,newbalanceOrig,nameDest,oldbalanceDest,newbalanceDest,isFraud,isFlaggedFraud) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            mapTransactionToPreparedStatement(transaction, preparedStatement);

            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean saveAll(List<Transaction> transactions) {
        String sql = "INSERT INTO TRANSACTIONS (step,paymentType,amount,nameOrig,oldbalanceOrig,newbalanceOrig,nameDest,oldbalanceDest,newbalanceDest,isFraud,isFlaggedFraud) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            short countBatch = 0;
            for (Transaction transaction : transactions) {
                mapTransactionToPreparedStatement(transaction, preparedStatement);

                preparedStatement.addBatch();

                countBatch++;
                if (countBatch == 1000) {
                    preparedStatement.executeBatch();
                    countBatch = 0;
                }
            }
            if (countBatch > 0) preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public void mapTransactionToPreparedStatement(Transaction transaction, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, transaction.step());
        preparedStatement.setString(2, transaction.type().name());
        preparedStatement.setBigDecimal(3, transaction.amount());

        preparedStatement.setString(4, transaction.origin().name());
        preparedStatement.setBigDecimal(5, transaction.origin().oldBalance());
        preparedStatement.setBigDecimal(6, transaction.origin().newBalance());

        preparedStatement.setString(7, transaction.dest().name());
        preparedStatement.setBigDecimal(8, transaction.dest().oldBalance());
        preparedStatement.setBigDecimal(9, transaction.dest().newBalance());

        preparedStatement.setBoolean(10, transaction.isFraud());
        preparedStatement.setBoolean(11, transaction.isFlaggedFraud());
    }
}
