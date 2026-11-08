package org.example;

import java.util.Random;

public class LemonadeStand {
    public static final int STARTING_CASH = 10000;
    public static final int STARTING_LEMON_PRICE = 100;
    public static final int STARTING_SUGAR_PRICE = 50;
    public static final int CUPS_PER_PITCHER = 8;
    public static final int MAX_CUSTOMERS = 500;
    public static final int MIN_PRICE = 0;
    public static final int MAX_PRICE = 10000;
    public static final int DAILY_RENT = 3000;
    public static final int MAX_LEMONADE_PRICE = 500;
    public static final double MAX_PRICE_SWING = 0.05;


    private int cash;
    private int lemons;
    private int sugar;
    private int pitchers;
    private int day;

    private double lemonMarketPrice;
    private double sugarMarketPrice;
    private int previousLemonPrice;
    private int previousSugarPrice;

    private int lemonsPerPitcher;
    private int sugarPerPitcher;
    private int waterPerPitcher;
    private int pricePerCup;

    public LemonadeStand() {
        cash = STARTING_CASH;
        lemons = 0;
        sugar = 0;
        pitchers = 0;
        day = 1;
        lemonMarketPrice = STARTING_LEMON_PRICE;
        sugarMarketPrice = STARTING_SUGAR_PRICE;
        previousLemonPrice = STARTING_LEMON_PRICE;
        previousSugarPrice = STARTING_SUGAR_PRICE;
        lemonsPerPitcher = 4;
        sugarPerPitcher = 2;
        waterPerPitcher = 6;
        pricePerCup = 100;
    }

    public long getCost(int lemonsToBuy, int sugarToBuy) {
        return (long) lemonsToBuy * getLemonPrice() + (long) sugarToBuy * getSugarPrice();
    }
    public int getLemonPrice() {
        return (int) Math.round(lemonMarketPrice);
    }

    public int getSugarPrice() {
        return (int) Math.round(sugarMarketPrice);
    }

    public int getPreviousLemonPrice() {
        return previousLemonPrice;
    }

    public int getPreviousSugarPrice() {
        return previousSugarPrice;
    }
    public void updatePrices(Random random) {
        previousLemonPrice = getLemonPrice();
        previousSugarPrice = getSugarPrice();
        lemonMarketPrice = fluctuate(lemonMarketPrice, random);
        sugarMarketPrice = fluctuate(sugarMarketPrice, random);
    }

    private double fluctuate(double price, Random random) {
        double change = (random.nextDouble() * 2 - 1) * MAX_PRICE_SWING;
        return Math.max(1.0, price * (1 + change));
    }
    public void payRent() {
        cash -= DAILY_RENT;
    }
    public long getNetWorth() {
        return (long) cash + (long) lemons * getLemonPrice() + (long) sugar * getSugarPrice();
    }
    public boolean isBankrupt() {
        return getNetWorth() < 0;
    }
    public int adjustCustomers(int rawCustomers) {
        if (pricePerCup <= 0) {
            return pitchers * CUPS_PER_PITCHER;
        }
        if (pricePerCup >= MAX_LEMONADE_PRICE) {
            return 0;
        }
        double multiplier = 1.0 - (double) pricePerCup / MAX_LEMONADE_PRICE;
        return (int) Math.round(rawCustomers * multiplier);
    }

    public boolean buyIngredients(int lemonsToBuy, int sugarToBuy) {
        if (lemonsToBuy < 0 || sugarToBuy < 0) {
            return false;
        }
        if (getCost(lemonsToBuy, sugarToBuy) > cash) {
            return false;
        }
        cash -= (int) getCost(lemonsToBuy, sugarToBuy);
        lemons += lemonsToBuy;
        sugar += sugarToBuy;
        return true;
    }

    public boolean setRecipe(int lemonsPer, int sugarPer, int waterPer) {
        if (lemonsPer < 1 || sugarPer < 0 || waterPer < 1) {
            return false;
        }
        lemonsPerPitcher = lemonsPer;
        sugarPerPitcher = sugarPer;
        waterPerPitcher = waterPer;
        return true;
    }

    public boolean setPrice(int cents) {
        if (cents < MIN_PRICE || cents > MAX_PRICE) {
            return false;
        }
        pricePerCup = cents;
        return true;
    }

    public int getMaxPitchers() {
        int max = lemons / lemonsPerPitcher;
        if (sugarPerPitcher > 0) {
            max = Math.min(max, sugar / sugarPerPitcher);
        }
        return max;
    }

    public boolean makePitchers(int count) {
        if (count < 1 || count > getMaxPitchers()) {
            return false;
        }
        lemons -= count * lemonsPerPitcher;
        sugar -= count * sugarPerPitcher;
        pitchers += count;
        return true;
    }


    public SaleResult sellLemonade(int customers) {
        if (customers < 0) {
            throw new IllegalArgumentException("customers cannot be negative");
        }
        int cupsAvailable = pitchers * CUPS_PER_PITCHER;
        int cupsSold = Math.min(customers, cupsAvailable);
        int revenue = cupsSold * pricePerCup;
        int cupsLeft = cupsAvailable - cupsSold;

        cash += revenue;
        SaleResult result = new SaleResult(customers, cupsSold, revenue,
                cupsLeft / CUPS_PER_PITCHER, cupsLeft % CUPS_PER_PITCHER);

        pitchers = 0;
        day++;
        return result;
    }

    public int getCash() {
        return cash;
    }

    public int getLemons() {
        return lemons;
    }

    public int getSugar() {
        return sugar;
    }

    public int getPitchers() {
        return pitchers;
    }

    public int getDay() {
        return day;
    }

    public int getLemonsPerPitcher() {
        return lemonsPerPitcher;
    }

    public int getSugarPerPitcher() {
        return sugarPerPitcher;
    }

    public int getWaterPerPitcher() {
        return waterPerPitcher;
    }

    public int getPricePerCup() {
        return pricePerCup;
    }
}

