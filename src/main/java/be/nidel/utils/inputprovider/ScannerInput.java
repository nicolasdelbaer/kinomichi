package be.nidel.utils.inputprovider;

import java.math.BigDecimal;
import java.util.Scanner;

public class ScannerInput implements InputProvider {
    Scanner scanner;
    public ScannerInput(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Integer nextInt() {
        return scanner.nextInt();
    }

    @Override
    public BigDecimal nextBigDecimal() {
        return scanner.nextBigDecimal();
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }
}
