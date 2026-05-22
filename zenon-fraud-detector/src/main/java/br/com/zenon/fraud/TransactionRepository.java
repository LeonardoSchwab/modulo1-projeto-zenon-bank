package br.com.zenon.fraud;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Optional<Transaction> findByOriginName(String name);
    boolean save(Transaction transaction);
    boolean save(List<Transaction> transactions);
}
