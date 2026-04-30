import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionIngestor;

void main() {
    TransactionIngestor transactionIngestor = new TransactionIngestor();
    List<Transaction> transactions = transactionIngestor.getTransactions("data/paysim_with_bad_data.txt", 1000);

    System.out.println(transactions.size());
    transactions.forEach(System.out::println);
}

public static void outputTarefa2() {
//    List<Transaction> transactions = List.of(new Transaction(1L, Transaction.Payment_type.PAYMENT, new BigDecimal("9839.64"), "C1231006815",
//                    new BigDecimal("170136.0"), new BigDecimal("160296.36"), "M1979787155", new BigDecimal("0.0"),
//                    new BigDecimal("0.0"), 0, 0),
//
//            new Transaction(743L, Transaction.Payment_type.CASH_OUT, new BigDecimal("850002.52"), "C1280323807",
//                    new BigDecimal("850002.52"), new BigDecimal("0.0"), "C873221189", new BigDecimal("6510099.11"),
//                    new BigDecimal("7360101.63"), 1, 0));

//    transactions.forEach(System.out::println);
}

public static void outputTarefa3() {
//    TransactionIngestor transactionIngestor = new TransactionIngestor();
//    List<Optional<Transaction>> transactions = transactionIngestor.getTransactions("data/PS_20174392719_1491204439457_log.csv", 1000);
//
//    IO.println(transactions.size());
//
//    transactions.stream().limit(10).forEach(System.out::println);
}
