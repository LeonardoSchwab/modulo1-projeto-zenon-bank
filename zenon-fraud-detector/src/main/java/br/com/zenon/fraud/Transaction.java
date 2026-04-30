package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Transaction(Long step,
                          Payment_type type,
                          BigDecimal amount,
                          TransactionCustomer origin,
                          TransactionCustomer dest,
                          boolean isFraud,
                          boolean isFlaggedFraud) {

    public Transaction {
        if (step <= 0) throw new IllegalArgumentException("Step must be positive");
        if (amount.signum() < 0) throw new IllegalArgumentException("amount must be zero or positive");
    }

    public enum Payment_type { CASH_IN, CASH_OUT, DEBIT, PAYMENT, TRANSFER }
}
