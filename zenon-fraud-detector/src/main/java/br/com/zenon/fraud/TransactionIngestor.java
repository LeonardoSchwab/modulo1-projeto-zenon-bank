package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TransactionIngestor {

    public TransactionIngestor() {
    }

    /**
     * Reads a file of transactions with the following structure separated by comma:
     * step,type,amount,nameOrig,oldbalanceOrg,newbalanceOrig,nameDest,oldbalanceDest,newbalanceDest,isFraud,isFlaggedFraud
     *
     * @param filename the file path as String
     * @param numberOfRowsLimit the number of rows to read or -1 to read the whole file
     * @return A list of transactions from the file
     * @Author Leonardo Schwab
     */
    public List<Transaction> getTransactions(String filename, int numberOfRowsLimit){
        Path path = Path.of(filename);

        try {
            List<String> lines = Files.readAllLines(path);

            if (numberOfRowsLimit > 0)
                return lines.stream()
                        .skip(1)
                        .limit(numberOfRowsLimit)
                        .map(this::parseTransaction)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();
            else
                return lines.stream()
                        .skip(1)
                        .map(this::parseTransaction)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();
            
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo " + filename + ": " + e);
        }
    }

    private Optional<Transaction> parseTransaction(String line) {
        try {
            String[] split = line.split(",");

            Long step = Long.parseLong(split[0]);

            Transaction.Payment_type type = Transaction.Payment_type.valueOf(split[1]);

            if (split[2] == null || split[2].isBlank()) throw new IllegalArgumentException("amount can't be null or blank");
            BigDecimal amount = new BigDecimal(split[2]);

            TransactionCustomer origin = new TransactionCustomer(split[3], new BigDecimal(split[4]), new BigDecimal(split[5]));

            TransactionCustomer dest = new TransactionCustomer(split[6], new BigDecimal(split[7]), new BigDecimal(split[8]));

            boolean isFraud = "1".equals(split[9]);

            boolean isFlaggedFraud = "1".equals(split[10]);

            return Optional.of(new Transaction(step, type, amount, origin, dest, isFraud, isFlaggedFraud));
        } catch (IllegalArgumentException e) {
            System.err.println("Error on line: " + line + " " + e);
            return Optional.empty();
        }
    }
}
