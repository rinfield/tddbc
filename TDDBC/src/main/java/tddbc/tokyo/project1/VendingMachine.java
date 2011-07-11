package tddbc.tokyo.project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class VendingMachine {

    private List<Money> moneys = new ArrayList<Money>();
    private Map<Integer, Slot> slots = new HashMap<Integer, Slot>();
    private Map<Integer, Integer> stocks = new HashMap<Integer, Integer>();
    private int totalSales = 0;
    private LotSelector lotSelector;

    public VendingMachine(LotSelector lotSelector) {
        for (int i = 1; i <= 8; i++) {
            stocks.put(i, 0);
        }
        this.lotSelector = lotSelector;
    }

    public VendingMachine() {
        this(new LotSelectorFivePercentImpl());
    }

    public void insertMoney(Money money) throws Exception {
        switch (money) {
        case MONEY_10000:
        case MONEY_05000:
        case MONEY_00005:
        case MONEY_00001:
            throw new Exception();
        default:
        }
        moneys.add(money);
    }

    public int getDeposit() {
        int result = 0;
        for (Money eachMoney : moneys) {
            result += eachMoney.getValue();
        }
        return result;
    }

    public void setSlotInfo(int id, int price, String label) {
        slots.put(id, new Slot(price, label));
    }

    public Slot getSlot(int id) {
        return slots.get(id);
    }

    public Integer getSlotStockCount(int id) {
        return stocks.get(id);
    }

    public void addDrink(int id, int count) {
        int currentCount = getSlotStockCount(id);
        stocks.put(id, currentCount + count);
    }

    public void sellDrink(int id) throws Exception {
        if (!getAvailableSlotIDs().contains(id)) {
            throw new Exception();
        }

        int currentCount = getSlotStockCount(id);
        stocks.put(id, --currentCount);
        Slot slot = getSlot(id);
        totalSales += slot.getPrice();

        if (currentCount > 0 && lotSelector.canOfferOneMoreDrink()) {
            currentCount = getSlotStockCount(id);
            stocks.put(id, --currentCount);
        }
    }

    public int getTotalSales() {
        return totalSales;
    }

    public List<Integer> getAvailableSlotIDs() {
        List<Integer> resultIDs = new ArrayList<Integer>();

        for (Entry<Integer, Slot> each : slots.entrySet()) {
            if (each.getValue().getPrice() <= getDeposit()
                    && getSlotStockCount(each.getKey()) > 0) {
                resultIDs.add(each.getKey());
            }
        }

        return resultIDs; // TODO
    }

    public int getChangeCount(Money money01000) {
        return 0;
    }
}
