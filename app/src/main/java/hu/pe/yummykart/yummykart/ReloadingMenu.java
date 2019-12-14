package hu.pe.yummykart.yummykart;

public class ReloadingMenu
{
    public static int reloagingMenuFlag=0;

    public static void setReloagingMenuFlag(int reloagingMenuFlag)
    {
        ReloadingMenu.reloagingMenuFlag = reloagingMenuFlag;
    }

    public static int getReloagingMenuFlag()
    {
        return reloagingMenuFlag;
    }
}
