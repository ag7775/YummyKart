package hu.pe.yummykart.yummykart;

import java.util.ArrayList;

public class CartInitialisation
{
    public static ArrayList<MenuInitialisation> cartInitialisationArrayList = new ArrayList<>();

    public void addCartItem(MenuInitialisation cartItem)
    {
        cartInitialisationArrayList.add(cartItem);
    }

    public static ArrayList<MenuInitialisation> getCartInitialisationArrayList()
    {
        return cartInitialisationArrayList;
    }

    public void delete_cartInitialisationArrayList()
    {
        cartInitialisationArrayList.clear();
    }

}
