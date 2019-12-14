package hu.pe.yummykart.yummykart;

public class OrderDetailsDataModel
{
  String item,quantity,amount;

    public String getItem()
    {
        return item;
    }
    public String getQuantity() {
        return quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
