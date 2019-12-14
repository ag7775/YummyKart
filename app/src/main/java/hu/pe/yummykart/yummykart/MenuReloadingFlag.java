package hu.pe.yummykart.yummykart;

/**
 * Created by 1576202 on 5/19/2017.
 */

public class MenuReloadingFlag
{
    public static int menu_reloading_flag = 0;

    public static void setMenu_reloading_flag(int menu_reloading_flag)
    {
        MenuReloadingFlag.menu_reloading_flag = menu_reloading_flag;
    }

    public static int getMenu_reloading_flag()
    {
        return menu_reloading_flag;
    }
}
