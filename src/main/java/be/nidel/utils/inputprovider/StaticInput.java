package be.nidel.utils.inputprovider;

import java.math.BigDecimal;

public class StaticInput implements InputProvider{
    String value;
    public StaticInput(String value) {
        this.value = value;
    }

    @Override
    public Integer nextInt() {
        return Integer.parseInt(value);
    }

    @Override
    public BigDecimal nextBigDecimal() {
        return new BigDecimal(value);
    }

    @Override
    public String nextLine() {
        return value;
    }
}
