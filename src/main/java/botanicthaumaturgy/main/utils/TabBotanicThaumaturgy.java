package botanicthaumaturgy.main.utils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import thaumcraft.common.Thaumcraft;

public class TabBotanicThaumaturgy extends CreativeTabs {

    public static TabBotanicThaumaturgy tabBotanicThaumaturgy = new TabBotanicThaumaturgy();

    public TabBotanicThaumaturgy() {
        super(getNextID(), VersionInfo.ModName);
    }

    public Item getTabIconItem() {
        return Thaumcraft.tabTC.getTabIconItem();
    }
}
