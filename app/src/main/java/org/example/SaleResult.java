package org.example;

public class SaleResult {
    public final int customers;
    public final int cupsSold;
    public final int revenue;        // in cents
    public final int wastedPitchers; // whole pitchers thrown out
    public final int wastedCups;     // leftover cups thrown out

    public SaleResult(int customers, int cupsSold, int revenue,
                      int wastedPitchers, int wastedCups) {
        this.customers = customers;
        this.cupsSold = cupsSold;
        this.revenue = revenue;
        this.wastedPitchers = wastedPitchers;
        this.wastedCups = wastedCups;
    }
}

