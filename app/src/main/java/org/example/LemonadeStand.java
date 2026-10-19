package org.example;

public class LemonadeStand {
    public static final int STARTING_CASH = 10000;
    public static final int LEMON_COST = 100;
    public static final int SUGAR_COST = 50;
    public static final int CUPS_PER_PITCHER = 8;
    public static final int MAX_CUSTOMERS = 500;
    public static final int MIN_PRICE = 1;
    public static final int MAX_PRICE = 10000;

    private int cash;
    private int lemons;
    private int sugar;
    private int pitchers;
    private int day;

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
        lemonsPerPitcher = 4;
        sugarPerPitcher = 2;
        waterPerPitcher = 6;
        pricePerCup = 100;
    }

    public long getCost(int lemonsToBuy, int sugarToBuy) {
        return (long) lemonsToBuy * LEMON_COST + (long) sugarToBuy * SUGAR_COST;
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

    public boolean isGameOver() {
        return pitchers == 0 && lemons == 0 && cash < LEMON_COST;
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

