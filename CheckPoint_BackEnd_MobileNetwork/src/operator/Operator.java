package operator;

import java.util.Map;
import java.util.TreeMap;

public class Operator {
    public int ID ;
    public String name ;

    private double standardSubscriptionCost ;
    public Operator setStandardSubscriptionCost(double standardSubscription) {
        this.standardSubscriptionCost = standardSubscription ;
        return this ;
    }
    public double getStandardSubscriptionCost() {
        return standardSubscriptionCost ;
    }

    private double premiumSubscriptionCost ;
    public Operator setPremiumSubscriptionCost(double premiumSubscription) {
        this.premiumSubscriptionCost = premiumSubscription ;
        return this ;
    }
    public double getPremiumSubscriptionCost() {
        return premiumSubscriptionCost ;
    }

    private Map <Integer,Double> foreignCall ;
    public Operator setCallCost(int operatorID, double cost) {
        foreignCall.put(operatorID, cost) ;
        return this ;
    }
    public Double getCallCost(int operatorID) {
        return foreignCall.get(operatorID) ;
    }

    private Map <Integer,Double> foreignSms ;
    public Operator setSmsCost(int operatorID, double cost) {
        foreignSms.put(operatorID, cost) ;
        return this ;
    }
    public Double getSmsCost(int operatorID) {
        return foreignSms.get(operatorID) ;
    }

    public Operator(String name, int ID) {
        this.name = name ;
        this.ID = ID ;
        foreignCall = new TreeMap<Integer, Double>() ;
        foreignSms = new TreeMap<Integer, Double>() ;
    }
}
