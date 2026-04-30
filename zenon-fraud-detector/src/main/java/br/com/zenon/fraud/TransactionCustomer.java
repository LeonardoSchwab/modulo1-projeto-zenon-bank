package br.com.zenon.fraud;

import java.math.BigDecimal;

public record TransactionCustomer(String name, BigDecimal newBalance, BigDecimal oldBalance) {

    public TransactionCustomer {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name can't be blank or null");
        if (newBalance.signum() < 0) throw new IllegalArgumentException("new balance should be zero or positive");
        if (oldBalance.signum() < 0) throw new IllegalArgumentException("old balance should be zero or positive");
    }
}
