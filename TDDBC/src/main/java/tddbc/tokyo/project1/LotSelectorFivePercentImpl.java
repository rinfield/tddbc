package tddbc.tokyo.project1;

import java.util.Random;

public class LotSelectorFivePercentImpl implements LotSelector {

    private Random random = new Random();

    public boolean canOfferOneMoreDrink() {
        int value = random.nextInt(100);
        return (0 <= value && value < 5);
    }
}
