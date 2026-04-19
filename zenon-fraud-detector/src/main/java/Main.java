import br.com.zenon.fraud.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    static void main() {
        List<Transaction> transactions = List.of(new Transaction(1L, Transaction.Payment_type.PAYMENT, new BigDecimal("9839.64"), "C1231006815",
                        new BigDecimal("170136.0"), new BigDecimal("160296.36"), "M1979787155", new BigDecimal("0.0"),
                        new BigDecimal("0.0"), 0, 0),

                new Transaction(743L, Transaction.Payment_type.CASH_OUT, new BigDecimal("850002.52"), "C1280323807",
                        new BigDecimal("850002.52"), new BigDecimal("0.0"), "C873221189", new BigDecimal("6510099.11"),
                        new BigDecimal("7360101.63"), 1, 0));

        transactions.forEach(System.out::println);
    }
}
