package be.nidel.utils;

import be.nidel.kinomichi.participant.Participant;

import java.math.BigDecimal;

public class BigDecimalFormatter {

    private boolean convertToFree = false;
    private BigDecimal value;
    private String currencyString = "";

    public BigDecimalFormatter(BigDecimal value) {
        this.value = value;
    }

    public BigDecimalFormatter zeroToFree(){
        convertToFree = true;
        return this;
    }

    public BigDecimalFormatter formatEuro(){
        currencyString = " eur";
        return this;
    }

    public String toString(){
        String result = "";
        if(convertToFree && value.compareTo(BigDecimal.ZERO) == 0)
            result = "FREE";
        else{
            result = "%s%s".formatted(value.toString(), currencyString);
        }
        return result;
    }
}
