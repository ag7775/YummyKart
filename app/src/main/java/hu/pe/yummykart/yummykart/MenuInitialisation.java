package hu.pe.yummykart.yummykart;

public class MenuInitialisation
{
    String item;
    int price;
    String category;
    int quantity;
    int amount;
    String type;
    int detail;

    public void setItem(String item)
    {
        this.item = item;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDetail(int detail) {
        this.detail = detail;
    }

    public String getItem()
    {
        return item;
    }

    public int getPrice()
    {
        return price;
    }

    public String getCategory()
    {
        return category;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public int getAmount()
    {
        return amount;
    }

    public String getType() {
        return type;
    }

    public int getDetail() {
        return detail;
    }
}
