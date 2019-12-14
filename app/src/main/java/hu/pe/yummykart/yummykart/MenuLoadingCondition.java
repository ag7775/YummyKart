package hu.pe.yummykart.yummykart;


public class MenuLoadingCondition
{
    public static int menu_loading_flag=0 ;

    public void set_menu_loading_flag(int f)
    {
        menu_loading_flag=f;
    }

    public int get_menu_loading_flag()
    {
        return menu_loading_flag;
    }
}
