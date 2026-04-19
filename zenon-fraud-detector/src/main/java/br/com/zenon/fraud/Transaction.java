package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Transaction(Long step,
                          Payment_type type,
                          BigDecimal amount,
                          String nameOrig,
                          BigDecimal oldbalanceOrg,
                          BigDecimal newbalanceOrig,
                          String nameDest,
                          BigDecimal oldbalanceDest,
                          BigDecimal newbalanceDest,
                          int isFraud,
                          int isFlaggedFraud) {

    public enum Payment_type { CASH_IN, CASH_OUT, DEBIT, PAYMENT, TRANSFER}
}
