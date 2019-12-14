package hu.pe.yummykart.yummykart;

public class CurrentOrdersDataModel
{
    int order_no,Flag,FlagD;
    String Restaurant_Name,Restaurant_Mobile,Order_Amount,Date,Time;

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public void setFlag(int flag) {
        Flag = flag;
    }

    public void setFlagD(int flagD) {
        FlagD = flagD;
    }

    public void setRestaurant_Name(String restaurant_Name) {
        Restaurant_Name = restaurant_Name;
    }

    public String getRestaurant_Name() {
        return Restaurant_Name;
    }

    public void setRestaurant_Mobile(String restaurant_Mobile) {
        Restaurant_Mobile = restaurant_Mobile;
    }

    public void setOrder_Amount(String order_Amount) {
        Order_Amount = order_Amount;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getOrder_no() {
        return order_no;
    }

    public int getFlag() {
        return Flag;
    }

    public int getFlagD() {
        return FlagD;
    }

    public String getRestaurant_Mobile() {
        return Restaurant_Mobile;
    }

    public String getOrder_Amount() {
        return Order_Amount;
    }

    public String getDate() {
        return Date;
    }
    public String getTime() {
        return Time;
    }
}
