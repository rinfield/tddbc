package tddbc.tokyo.project1;

public class Slot {

    private int price;
    private String label;

    public Slot(int price, String label) {
        this.price = price;
        this.label = label;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + price;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Slot other = (Slot) obj;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        if (price != other.price)
            return false;
        return true;
    }

    public int getPrice() {
        return price;
    }

    public String getLabel() {
        return label;
    }

}
