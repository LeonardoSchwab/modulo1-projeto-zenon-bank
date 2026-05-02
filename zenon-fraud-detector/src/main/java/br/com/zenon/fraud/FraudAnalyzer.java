package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FraudAnalyzer {

    List<Transaction> transactions;

    public FraudAnalyzer(List<Transaction> transactionsList) {
        Objects.requireNonNull(transactionsList);
        transactions = transactionsList;
    }

    public Long countFraudTransactions() {
        return getFraudStream()
                .count();
    }

    public List<BigDecimal> findHighestAmountFrauds(Long limit) {
        return getFraudStream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(Transaction::amount)
                .limit(limit)
                .toList();
    }

    public List<Transaction> FindMostSuspectCustomers(Long limit) {
        return getFraudStream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .distinct()
                .limit(limit)
                .toList();
    }

    public BigDecimal totalFraudAmount() {
        return getFraudStream()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<Transaction.Payment_type, Long> GetFraudsPerType() {
        return getFraudStream()
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));
    }

    private Stream<Transaction> getFraudStream() {
        return transactions.stream()
                .filter(Transaction::isFraud);
    }
}
