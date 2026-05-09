import br.com.zenon.fraud.*;
import br.com.zenon.fraud.TransactionReport.Statistics;

public static final String TRANSACTIONS_LOG_FILE = "data/PS_20174392719_1491204439457_log.csv";

void main() {
    TransactionReport transactionReport = new TransactionReport();


    long tempoInicio = System.currentTimeMillis();
    Statistics statistics = transactionReport.generateStatistics(TRANSACTIONS_LOG_FILE);
    IO.println("""
            Total de linhas: %d
            Total de fraudes: %d
            Valor total transacionado: %.2f\s
           \s""".formatted(statistics.totalTransactions(), statistics.totalFrauds(), statistics.totalAmount()));
    long tempoFim = System.currentTimeMillis();
    IO.println(tempoFim - tempoInicio + "ms");

}

public static void outputTarefa2() {

    TransactionCustomer origin = new TransactionCustomer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36"));
    TransactionCustomer dest = new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0"));

    TransactionCustomer origin2 = new TransactionCustomer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0"));
    TransactionCustomer dest2 = new TransactionCustomer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63"));

    List<Transaction> transactions = List.of(new Transaction(1L, Transaction.Payment_type.PAYMENT, new BigDecimal("9839.64"), origin,
                                             dest, false, false),
                                             new Transaction(743L, Transaction.Payment_type.CASH_OUT, new BigDecimal("850002.52"), origin2,
                                             dest2, true, false));

    transactions.forEach(IO::println);
}

public static void outputTarefa3() {
    TransactionIngestor transactionIngestor = new TransactionIngestor();
    List<Transaction> transactions = transactionIngestor.getTransactions(TRANSACTIONS_LOG_FILE, 1000);

    IO.println(transactions.size());

    transactions.stream().limit(10).forEach(IO::println);
}

public static void outputTarefa4() {
    TransactionIngestor transactionIngestor = new TransactionIngestor();
    List<Transaction> transactions = transactionIngestor.getTransactions("data/paysim_with_bad_data.txt", 1000);

    IO.println(transactions.size());
    transactions.forEach(IO::println);
}

public static void outputTarefa5() {
    TransactionIngestor transactionIngestor = new TransactionIngestor();

    List<Transaction> transactions = transactionIngestor.getTransactions(TRANSACTIONS_LOG_FILE,50000);

    FraudAnalyzer fraudAnalyzer = new FraudAnalyzer(transactions);

    IO.println("1. Total frauds: " + fraudAnalyzer.countFraudTransactions());

    List<BigDecimal> topFraudTransactions = fraudAnalyzer.findHighestAmountFrauds(3L);

    IO.println();
    IO.println("2. Top 3 frauds by amount: ");
    topFraudTransactions.forEach(amount -> IO.println("%.2f".formatted(amount)));

    IO.println();
    List<Transaction> topSuspectCustomers = fraudAnalyzer.FindMostSuspectCustomers(5L);
    IO.println("3. Suspect customers: ");
    topSuspectCustomers.stream()
            .map(transaction -> transaction.origin().name())
            .forEach(IO::println);

    IO.println();
    IO.println("4. Total fraud amount: " + fraudAnalyzer.totalFraudAmount());

    IO.println();
    IO.println("5. Frauds per type: ");
    Map<Transaction.Payment_type, Long> fraudsPerType = fraudAnalyzer.GetFraudsPerType();
    fraudsPerType.forEach((key, value) -> IO.println(key + ": " +value));
}

public static void outputTarefa06() {
    TransactionIngestor transactionIngestor = new TransactionIngestor();

    long tempoInicio = System.currentTimeMillis();
    List<Transaction> transactions = transactionIngestor.getTransactions(TRANSACTIONS_LOG_FILE,100_000);
    long tempoFim = System.currentTimeMillis();
    IO.println(tempoFim - tempoInicio + "ms");
    IO.println(transactions.size());

    TransactionRepository transactionRepository = new TransactionListRepository(transactions);
    tempoInicio = System.currentTimeMillis();
    Optional<Transaction> optionalTransaction = transactionRepository.findTransactionByOriginCustomerName("C1231006815");
    tempoFim = System.currentTimeMillis();
    optionalTransaction.ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para o cliente: C1231006815"));
    System.out.println(tempoFim - tempoInicio + "ms");

    tempoInicio = System.currentTimeMillis();
    Optional<Transaction> optionalTransaction1 = transactionRepository.findTransactionByOriginCustomerName("C12345");
    tempoFim = System.currentTimeMillis();
    optionalTransaction1.ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para o cliente: C12345"));
    System.out.println(tempoFim - tempoInicio + "ms");

    tempoInicio = System.currentTimeMillis();
    Optional<Transaction> optionalTransaction2 = transactionRepository.findTransactionByOriginCustomerName("C1868032458");
    tempoFim = System.currentTimeMillis();
    optionalTransaction2.ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para o cliente: C1868032458"));
    System.out.println(tempoFim - tempoInicio + "ms");

    IO.println("-----------------------------------------------");

    transactionRepository = new TransactionMapRepository(transactions);
    tempoInicio = System.currentTimeMillis();
    optionalTransaction = transactionRepository.findTransactionByOriginCustomerName("C1231006815");
    tempoFim = System.currentTimeMillis();
    optionalTransaction.ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para o cliente: C1231006815"));
    System.out.println(tempoFim - tempoInicio + "ms");

    tempoInicio = System.currentTimeMillis();
    optionalTransaction1 = transactionRepository.findTransactionByOriginCustomerName("C12345");
    tempoFim = System.currentTimeMillis();
    optionalTransaction1.ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para o cliente: C12345"));
    System.out.println(tempoFim - tempoInicio + "ms");

    tempoInicio = System.currentTimeMillis();
    optionalTransaction2 = transactionRepository.findTransactionByOriginCustomerName("C1868032458");
    tempoFim = System.currentTimeMillis();
    optionalTransaction2.ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para o cliente: C1868032458"));
    System.out.println(tempoFim - tempoInicio + "ms");
}