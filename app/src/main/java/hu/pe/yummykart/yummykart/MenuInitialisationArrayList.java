package hu.pe.yummykart.yummykart;


import java.util.ArrayList;

public class MenuInitialisationArrayList
{
   public static ArrayList<MenuInitialisation> menuInitialisationArrayList = new ArrayList<>();

    public static void setMenuInitialisationArrayList(ArrayList<MenuInitialisation> menuInitialisationArrayList)
    {
        MenuInitialisationArrayList.menuInitialisationArrayList = menuInitialisationArrayList;
    }

    public static ArrayList<MenuInitialisation> getMenuInitialisationArrayList()
    {
        return menuInitialisationArrayList;
    }

    public void delete_menuInitialisationArrayList()
    {
        menuInitialisationArrayList.clear();
    }
}
