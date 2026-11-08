package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LemonadeStandTest {
    private LemonadeStand stand;

    @BeforeEach
    void setUp() {
        stand = new LemonadeStand();
    }


    @Test
    void newStandStartsWith100Dollars() {
        assertEquals(10000, stand.getCash());
    }

    @Test
    void newStandStartsWithNoIngredientsOrPitchers() {
        assertEquals(0, stand.getLemons());
        assertEquals(0, stand.getSugar());
        assertEquals(0, stand.getPitchers());
    }

    @Test
    void newStandStartsOnDayOne() {
        assertEquals(1, stand.getDay());
    }

    @Test
    void newStandHasDefaultRecipeAndPrice() {
        assertEquals(4, stand.getLemonsPerPitcher());
        assertEquals(2, stand.getSugarPerPitcher());
        assertEquals(6, stand.getWaterPerPitcher());
        assertEquals(100, stand.getPricePerCup());
    }


    @Test
    void getCostAddsLemonAndSugarCosts() {
        assertEquals(1200L, stand.getCost(10, 4));
    }

    @Test
    void getCostOfNothingIsZero() {
        assertEquals(0L, stand.getCost(0, 0));
    }


    @Test
    void buyIngredientsUpdatesCashAndInventory() {
        boolean result = stand.buyIngredients(10, 4);

        assertTrue(result);
        assertEquals(10, stand.getLemons());
        assertEquals(4, stand.getSugar());
        assertEquals(8800, stand.getCash());
    }

    @Test
    void buyIngredientsFailsWhenNotEnoughMoney() {
        boolean result = stand.buyIngredients(101, 0);

        assertFalse(result);
        assertEquals(10000, stand.getCash());
        assertEquals(0, stand.getLemons());
    }

    @Test
    void buyIngredientsWorksWhenSpendingAllMoney() {
        boolean result = stand.buyIngredients(100, 0);

        assertTrue(result);
        assertEquals(0, stand.getCash());
    }

    @Test
    void buyIngredientsRejectsNegativeLemons() {
        assertFalse(stand.buyIngredients(-1, 0));
        assertEquals(10000, stand.getCash());
    }

    @Test
    void buyIngredientsRejectsNegativeSugar() {
        assertFalse(stand.buyIngredients(0, -1));
        assertEquals(10000, stand.getCash());
    }

    @Test
    void buyIngredientsRejectsHugeNumbers() {
        assertFalse(stand.buyIngredients(Integer.MAX_VALUE, Integer.MAX_VALUE));
        assertEquals(10000, stand.getCash());
    }


    @Test
    void setRecipeAcceptsValidRecipe() {
        assertTrue(stand.setRecipe(5, 2, 6));

        assertEquals(5, stand.getLemonsPerPitcher());
        assertEquals(2, stand.getSugarPerPitcher());
        assertEquals(6, stand.getWaterPerPitcher());
    }

    @Test
    void setRecipeAllowsZeroSugar() {
        assertTrue(stand.setRecipe(5, 0, 6));
        assertEquals(0, stand.getSugarPerPitcher());
    }

    @Test
    void setRecipeRejectsZeroLemons() {
        assertFalse(stand.setRecipe(0, 2, 6));
    }

    @Test
    void setRecipeRejectsZeroWater() {
        assertFalse(stand.setRecipe(5, 2, 0));
    }

    @Test
    void setRecipeRejectsNegativeSugar() {
        assertFalse(stand.setRecipe(5, -1, 6));
    }

    @Test
    void invalidRecipeKeepsTheOldRecipe() {
        stand.setRecipe(5, 2, 6);
        stand.setRecipe(0, 0, 0);

        assertEquals(5, stand.getLemonsPerPitcher());
        assertEquals(2, stand.getSugarPerPitcher());
        assertEquals(6, stand.getWaterPerPitcher());
    }

    @Test
    void settingRecipeTwiceUsesTheLatestRecipe() {
        stand.setRecipe(5, 2, 6);
        stand.setRecipe(3, 1, 4);

        assertEquals(3, stand.getLemonsPerPitcher());
        assertEquals(1, stand.getSugarPerPitcher());
        assertEquals(4, stand.getWaterPerPitcher());
    }

    @Test
    void setPriceAcceptsValidPrice() {
        assertTrue(stand.setPrice(150));
        assertEquals(150, stand.getPricePerCup());
    }

    @Test
    void setPriceAcceptsLowestAndHighestPrice() {
        assertTrue(stand.setPrice(LemonadeStand.MIN_PRICE));
        assertTrue(stand.setPrice(LemonadeStand.MAX_PRICE));
    }

    @Test
    void setPriceAcceptsZero() {
        assertTrue(stand.setPrice(0));
        assertEquals(0, stand.getPricePerCup());
    }

    @Test
    void setPriceRejectsNegativePrice() {
        assertFalse(stand.setPrice(-50));
        assertEquals(100, stand.getPricePerCup());
    }

    @Test
    void setPriceRejectsPriceAboveMaximum() {
        assertFalse(stand.setPrice(LemonadeStand.MAX_PRICE + 1));
        assertEquals(100, stand.getPricePerCup());
    }


    @Test
    void maxPitchersIsZeroWithNoIngredients() {
        assertEquals(0, stand.getMaxPitchers());
    }

    @Test
    void maxPitchersIsLimitedByLemons() {
        stand.setRecipe(5, 2, 6);
        stand.buyIngredients(10, 40);

        assertEquals(2, stand.getMaxPitchers());
    }

    @Test
    void maxPitchersIsLimitedBySugar() {
        stand.setRecipe(5, 2, 6);
        stand.buyIngredients(50, 6);

        assertEquals(3, stand.getMaxPitchers());
    }

    @Test
    void maxPitchersIgnoresSugarWhenRecipeUsesNone() {
        stand.setRecipe(5, 0, 6);
        stand.buyIngredients(10, 0);

        assertEquals(2, stand.getMaxPitchers());
    }


    @Test
    void makePitchersUsesIngredientsAndAddsPitchers() {
        stand.setRecipe(5, 2, 6);
        stand.buyIngredients(50, 40);

        boolean result = stand.makePitchers(8);

        assertTrue(result);
        assertEquals(8, stand.getPitchers());
        assertEquals(10, stand.getLemons());
        assertEquals(24, stand.getSugar());
    }

    @Test
    void makePitchersFailsWhenNotEnoughIngredients() {
        stand.setRecipe(5, 2, 6);
        stand.buyIngredients(4, 10);

        assertFalse(stand.makePitchers(1));
        assertEquals(0, stand.getPitchers());
        assertEquals(4, stand.getLemons());
    }

    @Test
    void makePitchersFailsForZeroOrNegativeCount() {
        stand.buyIngredients(20, 20);

        assertFalse(stand.makePitchers(0));
        assertFalse(stand.makePitchers(-1));
        assertEquals(0, stand.getPitchers());
    }

    @Test
    void makePitchersTwiceAddsUpPitchers() {
        stand.setRecipe(5, 2, 6);
        stand.buyIngredients(50, 40);

        stand.makePitchers(2);
        stand.makePitchers(3);

        assertEquals(5, stand.getPitchers());
    }

    @Test
    void sellLemonadeSellsOutWhenMoreCustomersThanCups() {
        stand.setRecipe(5, 2, 6);
        stand.buyIngredients(10, 4);
        stand.makePitchers(2);
        int cashBefore = stand.getCash();

        SaleResult result = stand.sellLemonade(100);

        assertEquals(100, result.customers);
        assertEquals(16, result.cupsSold);
        assertEquals(1600, result.revenue);
        assertEquals(0, result.wastedPitchers);
        assertEquals(0, result.wastedCups);
        assertEquals(cashBefore + 1600, stand.getCash());
    }

    @Test
    void sellLemonadeWastesUnsoldLemonade() {
        stand.setRecipe(5, 2, 6);
        stand.buyIngredients(40, 16);
        stand.makePitchers(8);
        int cashBefore = stand.getCash();

        SaleResult result = stand.sellLemonade(30);

        assertEquals(30, result.cupsSold);
        assertEquals(3000, result.revenue);
        assertEquals(4, result.wastedPitchers);
        assertEquals(2, result.wastedCups);
        assertEquals(cashBefore + 3000, stand.getCash());
    }

    @Test
    void sellLemonadeUsesTheCurrentPrice() {
        stand.setRecipe(5, 2, 6);
        stand.buyIngredients(5, 2);
        stand.makePitchers(1);
        stand.setPrice(50);

        SaleResult result = stand.sellLemonade(8);

        assertEquals(400, result.revenue);
    }

    @Test
    void sellLemonadeWithNoPitchersSellsNothing() {
        SaleResult result = stand.sellLemonade(50);

        assertEquals(0, result.cupsSold);
        assertEquals(0, result.revenue);
        assertEquals(10000, stand.getCash());
    }

    @Test
    void sellLemonadeWithZeroCustomersWastesEverything() {
        stand.setRecipe(5, 2, 6);
        stand.buyIngredients(5, 2);
        stand.makePitchers(1);

        SaleResult result = stand.sellLemonade(0);

        assertEquals(0, result.cupsSold);
        assertEquals(1, result.wastedPitchers);
        assertEquals(0, result.wastedCups);
    }

    @Test
    void sellLemonadeEmptiesPitchersAndStartsNextDay() {
        stand.setRecipe(5, 2, 6);
        stand.buyIngredients(10, 4);
        stand.makePitchers(2);

        stand.sellLemonade(1);

        assertEquals(0, stand.getPitchers());
        assertEquals(2, stand.getDay());
    }

    @Test
    void sellLemonadeThrowsExceptionForNegativeCustomers() {
        assertThrows(IllegalArgumentException.class, () -> stand.sellLemonade(-1));
    }


    // ----- Rent and game end -----

    @Test
    void payRentSubtractsFixedRent() {
        stand.payRent();
        assertEquals(10000 - LemonadeStand.DAILY_RENT, stand.getCash());
    }

    @Test
    void notBankruptAtTheStart() {
        assertFalse(stand.isBankrupt());
    }

    @Test
    void netWorthIncludesIngredientsAtMarketPrice() {
        stand.buyIngredients(10, 4);
        assertEquals(10000, stand.getNetWorth());
    }

    @Test
    void notBankruptWhileNetWorthIsPositive() {
        stand.payRent();
        stand.payRent();
        stand.payRent();
        assertEquals(1000, stand.getNetWorth());
        assertFalse(stand.isBankrupt());
    }

    @Test
    void bankruptWhenNetWorthBelowZero() {
        for (int i = 0; i < 4; i++) {
            stand.payRent();
        }
        assertEquals(-2000, stand.getNetWorth());
        assertTrue(stand.isBankrupt());
    }

    @Test
    void ingredientsCountTowardNetWorthSoRentAloneDoesNotBankruptYou() {
        stand.buyIngredients(100, 0); // cash 0, lemons worth $100
        stand.payRent();              // cash -$30, net worth $70
        assertEquals(7000, stand.getNetWorth());
        assertFalse(stand.isBankrupt());
    }

    // ----- Fluctuating prices -----

    @Test
    void pricesStartAtStartingValues() {
        assertEquals(100, stand.getLemonPrice());
        assertEquals(50, stand.getSugarPrice());
    }

    @Test
    void pricesStayWithinFivePercentEachDay() {
        java.util.Random random = new java.util.Random(42);
        for (int i = 0; i < 1000; i++) {
            double lemonBefore = stand.getLemonPrice();
            double sugarBefore = stand.getSugarPrice();
            stand.updatePrices(random);
            // allow 0.5 cent of rounding slack
            assertTrue(Math.abs(stand.getLemonPrice() - lemonBefore) <= lemonBefore * 0.05 + 1);
            assertTrue(Math.abs(stand.getSugarPrice() - sugarBefore) <= sugarBefore * 0.05 + 1);
            assertTrue(stand.getLemonPrice() >= 1);
            assertTrue(stand.getSugarPrice() >= 1);
        }
    }

    @Test
    void updatePricesRemembersPreviousPrices() {
        stand.updatePrices(new java.util.Random(1));
        assertEquals(100, stand.getPreviousLemonPrice());
        assertEquals(50, stand.getPreviousSugarPrice());
    }

    @Test
    void buyingUsesCurrentMarketPrice() {
        stand.updatePrices(new java.util.Random(7));
        assertEquals(10L * stand.getLemonPrice() + 4L * stand.getSugarPrice(), stand.getCost(10, 4));
    }

    // ----- Supply and demand -----

    @Test
    void zeroPriceSellsEntireStock() {
        stand.setRecipe(5, 2, 6);
        stand.buyIngredients(10, 4);
        stand.makePitchers(2);
        stand.setPrice(0);
        assertEquals(16, stand.adjustCustomers(3));
    }

    @Test
    void maxPriceSellsNothing() {
        stand.setPrice(LemonadeStand.MAX_LEMONADE_PRICE);
        assertEquals(0, stand.adjustCustomers(100));
    }

    @Test
    void priceAboveMaxSellsNothing() {
        stand.setPrice(LemonadeStand.MAX_LEMONADE_PRICE + 100);
        assertEquals(0, stand.adjustCustomers(100));
    }

    @Test
    void middlePriceScalesCustomers() {
        stand.setPrice(100); // 1 - 100/500 = 0.8
        assertEquals(80, stand.adjustCustomers(100));
        stand.setPrice(250); // 0.5
        assertEquals(50, stand.adjustCustomers(100));
    }

    @Test
    void demandMultiplierRoundsToNearestWholeNumber() {
        stand.setPrice(100);
        assertEquals(4, stand.adjustCustomers(5)); // 4.0
        assertEquals(3, stand.adjustCustomers(4)); // 3.2
        assertEquals(2, stand.adjustCustomers(2)); // 1.6
    }
}
