import br.com.zenon.fraud.*;
import br.com.zenon.fraud.TransactionReport.Statistics;

import java.util.List;

public static final String TRANSACTIONS_LOG_FILE = "data/PS_20174392719_1491204439457_log.csv";

void main() {
    EfficientTransactionIngestor efficientTransactionIngestor = new EfficientTransactionIngestor();
    TransactionRepository transactionSQLRepository = new TransactionSQLRepository();

    long tempoInicio = System.currentTimeMillis();
//    efficientTransactionIngestor.readAsStream(TRANSACTIONS_LOG_FILE, 0, transactionSQLRepository::save);
    long tempoFim = System.currentTimeMillis();
    IO.println(tempoFim - tempoInicio + "ms");

    tempoInicio = System.currentTimeMillis();
    efficientTransactionIngestor.readAsBatch(TRANSACTIONS_LOG_FILE, 0, transactionSQLRepository::saveAll);
    tempoFim = System.currentTimeMillis();
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
    Optional<Transaction> optionalTransaction = transactionRepository.findByOriginName("C1231006815");
    tempoFim = System.currentTimeMillis();
    optionalTransaction.ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para o cliente: C1231006815"));
    System.out.println(tempoFim - tempoInicio + "ms");

    tempoInicio = System.currentTimeMillis();
    Optional<Transaction> optionalTransaction1 = transactionRepository.findByOriginName("C12345");
    tempoFim = System.currentTimeMillis();
    optionalTransaction1.ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para o cliente: C12345"));
    System.out.println(tempoFim - tempoInicio + "ms");

    tempoInicio = System.currentTimeMillis();
    Optional<Transaction> optionalTransaction2 = transactionRepository.findByOriginName("C1868032458");
    tempoFim = System.currentTimeMillis();
    optionalTransaction2.ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para o cliente: C1868032458"));
    System.out.println(tempoFim - tempoInicio + "ms");

    IO.println("-----------------------------------------------");

    transactionRepository = new TransactionMapRepository(transactions);
    tempoInicio = System.currentTimeMillis();
    optionalTransaction = transactionRepository.findByOriginName("C1231006815");
    tempoFim = System.currentTimeMillis();
    optionalTransaction.ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para o cliente: C1231006815"));
    System.out.println(tempoFim - tempoInicio + "ms");

    tempoInicio = System.currentTimeMillis();
    optionalTransaction1 = transactionRepository.findByOriginName("C12345");
    tempoFim = System.currentTimeMillis();
    optionalTransaction1.ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para o cliente: C12345"));
    System.out.println(tempoFim - tempoInicio + "ms");

    tempoInicio = System.currentTimeMillis();
    optionalTransaction2 = transactionRepository.findByOriginName("C1868032458");
    tempoFim = System.currentTimeMillis();
    optionalTransaction2.ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para o cliente: C1868032458"));
    System.out.println(tempoFim - tempoInicio + "ms");
}

public static void outputTarefa08() {
    String languageOption;
    try (Scanner scanner = new Scanner(System.in)) {
        System.out.println("Selecione o idioma que deseja exportar o relatório.");
        System.out.println("ptBR(português do Brasil), enUS(inglês do Estados Unidos)");
        languageOption = scanner.nextLine();
    }

    Locale locale;
    if (languageOption.equalsIgnoreCase("ptBR")) {
        locale = Locale.of("pt", "BR");
    } else if (languageOption.equalsIgnoreCase("enUS")) {
        locale = Locale.US;
    } else {
        throw new RuntimeException("Idioma selecionado inválido!");
    }

    ResourceBundle reportBundle = ResourceBundle.getBundle("report", locale);
    NumberFormat currencyFormatter = DecimalFormat.getCurrencyInstance(locale);
    NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);

    currencyFormatter.setCurrency(Currency.getInstance("USD"));

    TransactionReport transactionReport = new TransactionReport();

    long tempoInicio = System.currentTimeMillis();
    Statistics statistics = transactionReport.generateStatistics(TRANSACTIONS_LOG_FILE);
    IO.println();
    IO.println("""
           %s: %s
           %s: %s
           %s: %s\s
           \s""".formatted(reportBundle.getString("label.total.lines"), numberFormatter.format(statistics.totalTransactions()),
            reportBundle.getString("label.total.frauds"), numberFormatter.format(statistics.totalFrauds()),
            reportBundle.getString("label.total.amount"), currencyFormatter.format(statistics.totalAmount())));
    long tempoFim = System.currentTimeMillis();
    IO.println(tempoFim - tempoInicio + "ms");
}

public static void outputTarefa09() {
    TransactionIngestor transactionIngestor = new TransactionIngestor();
    List<Transaction> transactions = transactionIngestor.getTransactions(TRANSACTIONS_LOG_FILE, 10_000);

    TransactionRepository transactionRepository = null;
    try {
        transactionRepository = new TransactionSQLRepository();

        long tempoInicio = System.currentTimeMillis();
        transactionRepository.saveAll(transactions);
        long tempoFim = System.currentTimeMillis();
        IO.println(tempoFim - tempoInicio + "ms");

        Optional<Transaction> transactionByName = transactionRepository.findByOriginName("C1231006815");
        Optional<Transaction> transactionByName2 = transactionRepository.findByOriginName("C12345");

        transactionByName.ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para a origem: C1231006815"));
        transactionByName2.ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para a origem C12345"));

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}