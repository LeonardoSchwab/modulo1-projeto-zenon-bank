package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

        ArrayList<Transaction> transactions = new ArrayList<>();
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {

            int numberOfRows = 0;

            String line = bufferedReader.readLine();
            if (line.startsWith("step"))
                line = bufferedReader.readLine();
            while (line != null && (numberOfRowsLimit == -1 || numberOfRows < numberOfRowsLimit)) {
                String[] split = line.split(",");

                try {
                    transactions.add(new Transaction(Long.parseLong(split[0]), Transaction.Payment_type.valueOf(split[1]),
                            new BigDecimal(split[2]), split[3], new BigDecimal(split[4]), new BigDecimal(split[5]),
                            split[6], new BigDecimal(split[7]), new BigDecimal(split[8]),
                            Integer.parseInt(split[9]), Integer.parseInt(split[10])));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Incorrect file structure!" + "\r\n" + e);
                }

                line = bufferedReader.readLine();
                numberOfRows++;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return transactions;
    }
}
