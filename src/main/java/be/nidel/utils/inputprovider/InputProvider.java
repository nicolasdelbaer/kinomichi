package be.nidel.utils.inputprovider;

import java.math.BigDecimal;

public interface InputProvider {
    Integer nextInt();
    BigDecimal nextBigDecimal();
    String nextLine();
}
