package br.com.zenon.fraud;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionReport {

    private record ReportTransaction(BigDecimal amount, boolean isFraud){}

    public record Statistics(long totalTransactions, long totalFrauds, BigDecimal totalAmount){

        private static final Statistics STATISTICS_ZERO = new Statistics(0,0,BigDecimal.ZERO);

        private Statistics addReportTransaction(ReportTransaction reportTransaction) {
            return new Statistics(totalTransactions + 1L,
                    totalFrauds + (reportTransaction.isFraud ? 1L : 0L),
                    totalAmount.add(reportTransaction.amount));
        }

        private Statistics add(Statistics other){
            return new Statistics(totalTransactions + other.totalTransactions,
                    totalFrauds + other.totalFrauds,
                    totalAmount.add(other.totalAmount));
        }
    }

    public TransactionReport() {
    }

    public Statistics generateStatistics(String filename) {
        Path path = Path.of(filename);

       try(Stream<String> lines = Files.lines(path)) {
            return lines
                    .skip(1)
                    .map(this::parseTransaction)
                     .filter(Optional::isPresent)
                    .map(Optional::get)
                    .reduce(Statistics.STATISTICS_ZERO,
                            Statistics::addReportTransaction,
                            Statistics::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<ReportTransaction> parseTransaction(String line) {
        try {
            String[] split = line.split(",");

            if (split[2] == null || split[2].isBlank())
                throw new IllegalArgumentException("amount can't be null or blank");

            BigDecimal amount = new BigDecimal(split[2]);

            boolean isFraud = "1".equals(split[9]);

            return Optional.of(new ReportTransaction(amount, isFraud));
        } catch (IllegalArgumentException e) {
            System.err.println("Error on line: " + line + " " + e);
            return Optional.empty();
        }
    }
}
