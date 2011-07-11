package tddbc.tokyo.project1;

public enum Money {
    MONEY_10000(10000), //
    MONEY_05000(5000), //
    MONEY_01000(1000), //
    MONEY_00500(500), //
    MONEY_00100(100), //
    MONEY_00050(50), //
    MONEY_00010(10), //
    MONEY_00005(5), //
    MONEY_00001(1);

    private int value;

    Money(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
