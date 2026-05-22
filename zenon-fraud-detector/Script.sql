create table TRANSACTIONS (
      id bigint,
      step integer,
      paymentType enum('CASH_IN', 'CASH_OUT', 'DEBIT', 'PAYMENT', 'TRANSFER'),
      amount numeric(15,2),
      nameOrig varchar(100),
      oldbalanceOrig numeric(15,2),
      newbalanceOrig numeric(15,2),
      nameDest varchar(100),
      oldbalanceDest numeric(15,2),
      newbalanceDest numeric(15,2),
      isFraud boolean,
      isFlaggedFraud boolean
);