package be.nidel.kinomichi.pricing;

import be.nidel.kinomichi.session.SessionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PricingGroup implements Serializable {
    List<Pricing> pricingList = new ArrayList<>();
    SessionType sessionType;

    public PricingGroup(List<Pricing> pricingList, SessionType sessionType) {
        this.pricingList = pricingList;
        this.sessionType = sessionType;
    }

    public void add(int index, Pricing element) {
        pricingList.add(index, element);
    }

    public Pricing set(int index, Pricing element) {
        return pricingList.set(index, element);
    }

    public List<Pricing> getPricingList() {
        return pricingList;
    }

    public void setPricingList(List<Pricing> pricingList) {
        this.pricingList = pricingList;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public String pricesToString() {
        StringBuilder sb = new StringBuilder();

        for (Pricing pricing : pricingList) {
            sb.append(pricing.getPrice());
            sb.append(";");
        }

        return sb.substring(0, sb.length()-1);
    }
}
