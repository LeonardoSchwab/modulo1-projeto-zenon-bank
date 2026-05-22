package br.com.zenon.fraud;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {

    List<Transaction> transactions;

    public TransactionListRepository(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);
        this.transactions = transactions;
    }

    @Override
    public Optional<Transaction> findByOriginName(String name) {
        return transactions.stream()
                .filter(transaction -> transaction.origin().name().equals(name))
                .findFirst();
    }

    @Override
    public boolean save(Transaction transaction) {
        this.transactions.add(transaction);
        return true;
    }

    @Override
    public boolean save(List<Transaction> transactions) {
        this.transactions.addAll(transactions);
        return true;
    }
}
