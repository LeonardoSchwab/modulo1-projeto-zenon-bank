package br.com.zenon.fraud;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository {

    Map<String, Transaction> transactions;

    public TransactionMapRepository(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);
        this.transactions = transactions.stream()
                .collect(Collectors.toMap(transaction -> transaction.origin().name(), Function.identity(), (transaction, transaction2) -> transaction));
    }

    @Override
    public Optional<Transaction> findTransactionByOriginCustomerName(String name) {
        return Optional.ofNullable(transactions.get(name));
    }
}
