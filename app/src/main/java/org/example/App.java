package org.example;

import java.util.Random;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Random random = new Random();
        LemonadeStand stand = new LemonadeStand();

        System.out.println("Welcome to My Lemonade Stand!");

        while (true) {
            if (stand.isGameOver()) {
                System.out.println();
                System.out.println("You have no lemons and not enough money to buy one.");
                System.out.println("Your lemonade stand has gone out of business. Game over!");
                break;
            }
            runPreparationPhase(stand, input);
            runSellingPhase(stand, random);
        }
        input.close();
    }
    private static void runPreparationPhase(LemonadeStand stand, Scanner input) {
        System.out.println();
        System.out.println("Day " + stand.getDay() + ": Preparation Phase");
        System.out.println("Let's prepare for the day!");

        boolean preparing = true;
        while (preparing) {
            printStatus(stand);
            System.out.println("1. Purchase Ingredients");
            System.out.println("2. Tweak the Recipe");
            System.out.println("3. Set the Price");
            System.out.println("4. Make Pitchers of Lemonade");
            System.out.println("5. Move Onto the Selling Phase");
            System.out.println();
            System.out.print("What would you like to do? ");
            String choice = input.nextLine().trim();
            System.out.println();

            if (choice.equals("1")) {
                purchaseIngredients(stand, input);
            } else if (choice.equals("2")) {
                tweakRecipe(stand, input);
            } else if (choice.equals("3")) {
                setPrice(stand, input);
            } else if (choice.equals("4")) {
                makePitchers(stand, input);
            } else if (choice.equals("5")) {
                preparing = false;
            } else {
                System.out.println("Invalid choice, try again.");
            }
        }
    }

    private static void purchaseIngredients(LemonadeStand stand, Scanner input) {
        System.out.println("Lemons are " + formatMoney(LemonadeStand.LEMON_COST) + " each");
        System.out.println("Sugar is " + formatMoney(LemonadeStand.SUGAR_COST) + " per cup");
        System.out.println();

        int lemons = readInt(input, "How many lemons do you want to buy? ", 0);
        int sugar = readInt(input, "How many cups of sugar do you want to buy? ", 0);
        System.out.println();

        if (lemons == 0 && sugar == 0) {
            System.out.println("You didn't buy anything.");
        } else if (stand.buyIngredients(lemons, sugar)) {
            System.out.println("Great! Transaction complete!");
        } else {
            System.out.println("Sorry, that would cost " + formatMoney(stand.getCost(lemons, sugar))
                    + " but you only have " + formatMoney(stand.getCash()) + ".");
        }
    }

    private static void tweakRecipe(LemonadeStand stand, Scanner input) {
        int lemons = readInt(input, "How many lemons do you want per pitcher? ", 1);
        int sugar = readInt(input, "How many cups of sugar do you want per pitcher? ", 0);
        int water = readInt(input, "How many cups of water do you want per pitcher? ", 1);
        System.out.println();

        if (stand.setRecipe(lemons, sugar, water)) {
            System.out.println("Great! Your recipe is set!");
        } else {
            System.out.println("That recipe doesn't work. Your recipe was not changed.");
        }
    }
    private static void setPrice(LemonadeStand stand, Scanner input) {
        int cents = readPriceInCents(input,
                "How much will you sell one cup of lemonade for? Remember, one pitcher is "
                        + LemonadeStand.CUPS_PER_PITCHER + " cups: $");
        System.out.println();

        if (stand.setPrice(cents)) {
            int pitcherPrice = cents * LemonadeStand.CUPS_PER_PITCHER;
            System.out.println("Awesome! Each cup of lemonade is " + formatMoney(cents)
                    + " and each pitcher is " + LemonadeStand.CUPS_PER_PITCHER + " x "
                    + formatMoney(cents) + " = " + formatMoney(pitcherPrice) + " total.");
        } else {
            System.out.println("That price doesn't work. Your price was not changed.");
        }
    }

    private static void makePitchers(LemonadeStand stand, Scanner input) {
        int max = stand.getMaxPitchers();
        if (max == 0) {
            System.out.println("You don't have enough ingredients to make a pitcher.");
            System.out.println("Your recipe needs " + stand.getLemonsPerPitcher() + " lemons and "
                    + stand.getSugarPerPitcher() + " cups of sugar per pitcher.");
            return;
        }

        System.out.println("With your ingredients, you can make up to " + max + " pitchers.");
        int count = readInt(input, "How many pitchers of lemonade do you want to make? ", 0);
        System.out.println();

        if (count == 0) {
            System.out.println("You didn't make any pitchers.");
        } else if (stand.makePitchers(count)) {
            System.out.println("Fantastic! You used " + ((long) count * stand.getLemonsPerPitcher())
                    + " lemons, " + ((long) count * stand.getSugarPerPitcher())
                    + " cups of sugar, and " + ((long) count * stand.getWaterPerPitcher())
                    + " cups of water to make " + count + " pitchers of lemonade.");
        } else {
            System.out.println("You can't make that many pitchers. The most you can make is " + max + ".");
        }
    }

    private static void runSellingPhase(LemonadeStand stand, Random random) {
        System.out.println();
        System.out.println("Day " + stand.getDay() + ": Selling Phase");
        System.out.println();
        System.out.println("The customers are coming!");
        System.out.println();

        int customers = random.nextInt(LemonadeStand.MAX_CUSTOMERS) + 1;
        boolean hadLemonade = stand.getPitchers() > 0;
        SaleResult result = stand.sellLemonade(customers);

        System.out.println(result.customers + " customers visited your stand today! You sold "
                + result.cupsSold + " cups of lemonade for " + formatMoney(result.revenue) + ".");

        if (!hadLemonade) {
            System.out.println("You didn't make any lemonade, so everyone left disappointed.");
        } else if (result.customers > result.cupsSold) {
            System.out.println("You sold out! " + (result.customers - result.cupsSold)
                    + " customers left without lemonade.");
        }

        if (result.wastedPitchers > 0 || result.wastedCups > 0) {
            System.out.println();
            System.out.println("Unfortunately, you did not sell the remaining " + result.wastedPitchers
                    + " pitchers + " + result.wastedCups + " cups of lemonade. "
                    + "That lemonade got thrown onto a potted plant. The potted plant died.");
        }

        System.out.println();
        System.out.println("Let's get a good night sleep and try again tomorrow!");
    }

    private static void printStatus(LemonadeStand stand) {
        System.out.println();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
        System.out.println("Lemons: " + stand.getLemons());
        System.out.println("Sugar (Cups): " + stand.getSugar());
        System.out.println("Water (Cups): Infinity");
        System.out.println("Current Pitchers: " + stand.getPitchers());
        System.out.println("Cash: " + formatMoney(stand.getCash()));
        System.out.println();
    }

    private static int readInt(Scanner input, String prompt, int min) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().trim();
            try {
                int value = Integer.parseInt(text);
                if (value >= min) {
                    return value;
                }
                System.out.println("Please enter a number that is at least " + min + ".");
            } catch (NumberFormatException e) {
                System.out.println("That's not a valid whole number. Please try again.");
            }
        }
    }

    private static int readPriceInCents(Scanner input, String prompt) {
        double min = LemonadeStand.MIN_PRICE / 100.0;
        double max = LemonadeStand.MAX_PRICE / 100.0;
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().trim().replace("$", "");
            try {
                double dollars = Double.parseDouble(text);
                if (!(dollars >= min && dollars <= max)) {
                    System.out.println("Please enter a price from " + formatMoney(LemonadeStand.MIN_PRICE)
                            + " to " + formatMoney(LemonadeStand.MAX_PRICE) + ".");
                } else {
                    double cents = dollars * 100;
                    long roundedCents = Math.round(cents);
                    if (Math.abs(cents - roundedCents) > 0.0001) {
                        System.out.println("You can't charge a fraction of a penny. Please try again.");
                    } else {
                        return (int) roundedCents;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("That's not a valid price. Please try again.");
            }
        }
    }

    private static String formatMoney(long cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }
}