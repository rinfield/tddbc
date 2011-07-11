package tddbc.tokyo.project1;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VendingMachineTest {

    @Test(expected = Exception.class)
    public void test千円札と硬貨以外が投入された場合Exceptionをスローする() throws Exception {
        VendingMachine machine = new VendingMachine();
        machine.insertMoney(Money.MONEY_10000);
    }

    @Test
    public void test千円札が投入された場合Exceptionがスローされない() throws Exception {
        VendingMachine machine = new VendingMachine();
        machine.insertMoney(Money.MONEY_01000);
    }

    @Test
    public void test初期状態の合計金額は0円である() {
        VendingMachine machine = new VendingMachine();
        assertThat(machine.getDeposit(), is(0));
    }

    @Test
    public void test千円を投入した後の合計金額は1000円() throws Exception {
        VendingMachine machine = new VendingMachine();
        machine.insertMoney(Money.MONEY_01000);
        assertThat(machine.getDeposit(), is(1000));
    }

    @Test
    public void test千円500円100円50円10円を投入した後の合計金額は1660円() throws Exception {
        VendingMachine machine = new VendingMachine();
        machine.insertMoney(Money.MONEY_01000);
        machine.insertMoney(Money.MONEY_00500);
        machine.insertMoney(Money.MONEY_00100);
        machine.insertMoney(Money.MONEY_00050);
        machine.insertMoney(Money.MONEY_00010);
        assertThat(machine.getDeposit(), is(1660));
    }

    @Test
    public void testID1のスロットにはコーラ120円が設定されている() throws Exception {
        VendingMachine machine = new VendingMachine();
        machine.setSlotInfo(1, 120, "コーラ");
        // machine.addDrink(1, 5);

        assertThat(machine.getSlot(1), is(new Slot(120, "コーラ")));
        assertThat(machine.getSlotStockCount(1), is(0));
    }

    @Test
    public void testID1のスロットには120円のコーラが5本在庫されている() throws Exception {
        VendingMachine machine = new VendingMachine();
        machine.setSlotInfo(1, 120, "コーラ");
        machine.addDrink(1, 5);

        assertThat(machine.getSlot(1), is(new Slot(120, "コーラ")));
        assertThat(machine.getSlotStockCount(1), is(5));
    }

    @Test(expected = Exception.class)
    public void testID1のスロットのコーラを在庫が無い状態で120円を投入して1本購入すると例外がスローされる()
            throws Exception {
        VendingMachine machine = new VendingMachine();
        machine.setSlotInfo(1, 120, "コーラ");
        machine.insertMoney(Money.MONEY_00100);
        machine.insertMoney(Money.MONEY_00010);
        machine.insertMoney(Money.MONEY_00010);

        machine.sellDrink(1);
    }

    @Test(expected = Exception.class)
    public void testID1のスロットのコーラを在庫が5本ある状態で110円を投入して1本購入すると例外がスローされる()
            throws Exception {
        VendingMachine machine = new VendingMachine();
        machine.setSlotInfo(1, 120, "コーラ");
        machine.addDrink(1, 5);
        machine.insertMoney(Money.MONEY_00100);
        machine.insertMoney(Money.MONEY_00010);

        machine.sellDrink(1);
    }

    @Test
    public void 何も販売していない場合は売上金額は0円() throws Exception {
        VendingMachine machine = new VendingMachine();

        assertThat(machine.getTotalSales(), is(0));
    }

    @Test
    public void test初期化後は買えるスロットが一つもない() throws Exception {
        VendingMachine machine = new VendingMachine();

        List<Integer> expected = new ArrayList<Integer>();
        assertThat(machine.getAvailableSlotIDs(), is(expected));
    }

    @Test
    public void testID1にコーラ5本の在庫があって120円を投入している状況でID1が購入可能対象として得られる()
            throws Exception {
        VendingMachine machine = new VendingMachine();
        machine.setSlotInfo(1, 120, "コーラ");
        machine.addDrink(1, 5);
        machine.insertMoney(Money.MONEY_00100);
        machine.insertMoney(Money.MONEY_00010);
        machine.insertMoney(Money.MONEY_00010);

        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        assertThat(machine.getAvailableSlotIDs(), is(expected));
    }

    @Test
    public void testID1にコーラ5本の在庫があって110円を投入している状況で購入可能対象は何も得られない()
            throws Exception {
        VendingMachine machine = new VendingMachine();
        machine.setSlotInfo(1, 120, "コーラ");
        machine.addDrink(1, 5);
        machine.insertMoney(Money.MONEY_00100);
        machine.insertMoney(Money.MONEY_00010);

        List<Integer> expected = new ArrayList<Integer>();
        assertThat(machine.getAvailableSlotIDs(), is(expected));
    }

    @Test
    public void testID1コーラの在庫がない状態で120円を投入している状況で購入可能対象は何も得られない()
            throws Exception {
        VendingMachine machine = new VendingMachine();
        machine.setSlotInfo(1, 120, "コーラ");
        machine.insertMoney(Money.MONEY_00100);
        machine.insertMoney(Money.MONEY_00010);
        machine.insertMoney(Money.MONEY_00010);

        List<Integer> expected = new ArrayList<Integer>();
        assertThat(machine.getAvailableSlotIDs(), is(expected));
    }

    @Test
    public void testID1コーラの在庫が2本ある状態で120円を投入している状況でコーラを購入したらあたりが出て2本のコーラが結果として購入できること()
            throws Exception {
        LotSelector mockSelector = createMock(LotSelector.class);
        expect(mockSelector.canOfferOneMoreDrink()).andReturn(true);
        replay(mockSelector);

        VendingMachine machine = new VendingMachine(mockSelector);
        machine.setSlotInfo(1, 120, "コーラ");
        machine.addDrink(1, 2);
        machine.insertMoney(Money.MONEY_00100);
        machine.insertMoney(Money.MONEY_00010);
        machine.insertMoney(Money.MONEY_00010);

        machine.sellDrink(1);

        assertThat(machine.getSlotStockCount(1), is(0));
        assertThat(machine.getTotalSales(), is(120));
        verify(mockSelector);
    }

    @Test
    public void testID1コーラの在庫が2本ある状態で120円を投入している状況でコーラを購入したらハズレが出て1本のコーラが結果として購入できること()
            throws Exception {
        LotSelector mockSelector = createMock(LotSelector.class);
        expect(mockSelector.canOfferOneMoreDrink()).andReturn(false);
        replay(mockSelector);

        VendingMachine machine = new VendingMachine(mockSelector);
        machine.setSlotInfo(1, 120, "コーラ");
        machine.addDrink(1, 2);
        machine.insertMoney(Money.MONEY_00100);
        machine.insertMoney(Money.MONEY_00010);
        machine.insertMoney(Money.MONEY_00010);

        machine.sellDrink(1);

        assertThat(machine.getSlotStockCount(1), is(1));
        assertThat(machine.getTotalSales(), is(120));
        verify(mockSelector);
    }

    @Test
    public void testID1コーラの在庫が1本ある状態で120円を投入している状況でコーラを購入したら抽選せず1本のコーラが結果として購入できること()
            throws Exception {
        LotSelector mockSelector = createMock(LotSelector.class);
        replay(mockSelector);

        VendingMachine machine = new VendingMachine(mockSelector);
        machine.setSlotInfo(1, 120, "コーラ");
        machine.addDrink(1, 1);
        machine.insertMoney(Money.MONEY_00100);
        machine.insertMoney(Money.MONEY_00010);
        machine.insertMoney(Money.MONEY_00010);

        machine.sellDrink(1);

        assertThat(machine.getSlotStockCount(1), is(0));
        assertThat(machine.getTotalSales(), is(120));
        verify(mockSelector);
    }

    @Test
    public void test初期化直後はお釣り用に千円札5枚硬貨それぞれ10枚を持つ() throws Exception {
        VendingMachine machine = new VendingMachine();

        assertThat(machine.getChangeCount(Money.MONEY_01000), is(5));
        assertThat(machine.getChangeCount(Money.MONEY_00500), is(10));
        assertThat(machine.getChangeCount(Money.MONEY_00100), is(10));
        assertThat(machine.getChangeCount(Money.MONEY_00050), is(10));
        assertThat(machine.getChangeCount(Money.MONEY_00010), is(10));
    }
}
