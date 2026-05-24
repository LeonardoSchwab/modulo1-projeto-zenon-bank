package br.com.zenon.fraud;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EfficientTransactionIngestor {

    Semaphore semaphore = new Semaphore(80);

    public EfficientTransactionIngestor() {
    }

    public void readAsStream(String filename, int numberOfRowsLimit, Consumer<Transaction> consumer) {

        Path path = Path.of(filename);

        try (Stream<String> lines = Files.lines(path)) {

            if (numberOfRowsLimit > 0)
                lines.skip(1)
                        .limit(10_000)
                        .map(this::parseTransaction)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(consumer);
            else {
                lines.skip(1)
                        .map(this::parseTransaction)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(consumer);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void readAsBatchWithConcurrency(String filename, int numberOfRowsLimit, Consumer<List<Transaction>> consumer) {

        Path path = Path.of(filename);

        try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();
             Stream<String> lines = Files.lines(path)) {

            Iterator<String> iterator;
            if (numberOfRowsLimit > 0)
                iterator = lines.skip(1).limit(numberOfRowsLimit).iterator();
            else {
                iterator = lines.skip(1).iterator();
            }

            int initialCapacity = 2000;
            List<Transaction> transactionListBatch = new ArrayList<>(initialCapacity);
            while (iterator.hasNext()) {
                parseTransaction(iterator.next()).ifPresent(transactionListBatch::add);

                if (transactionListBatch.size() == initialCapacity) {
                    List<Transaction> copiedTransactionListBatch = transactionListBatch.stream().toList();

                    transactionListBatch.clear();

                    service.submit(() -> {
                        try {
                            semaphore.acquire();
                            consumer.accept(copiedTransactionListBatch);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            semaphore.release();
                        }
                    });
                }
            }

            service.close();

            if (!transactionListBatch.isEmpty()) {
                consumer.accept(transactionListBatch);
                transactionListBatch.clear();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readAsBatch(String filename, int numberOfRowsLimit, Consumer<List<Transaction>> consumer) {

        Path path = Path.of(filename);

        try (Stream<String> lines = Files.lines(path)) {

            Iterator<String> iterator;
            if (numberOfRowsLimit > 0)
                iterator = lines.skip(1).limit(numberOfRowsLimit).iterator();
            else {
                iterator = lines.skip(1).iterator();
            }

            int initialCapacity = 1000;
            List<Transaction> transactionListBatch = new ArrayList<>(initialCapacity);
            while (iterator.hasNext()) {
                parseTransaction(iterator.next()).ifPresent(transactionListBatch::add);

                if (transactionListBatch.size() == initialCapacity) {
                    consumer.accept(transactionListBatch);
                    transactionListBatch.clear();
                }
            }

            if (!transactionListBatch.isEmpty()) {
                consumer.accept(transactionListBatch);
                transactionListBatch.clear();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
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
