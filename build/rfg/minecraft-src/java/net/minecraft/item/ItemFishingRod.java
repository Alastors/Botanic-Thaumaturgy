package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemFishingRod extends Item
{
    @SideOnly(Side.CLIENT)
    private IIcon theIcon;
    private static final String __OBFID = "CL_00000034";

    public ItemFishingRod()
    {
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

    /**
     * Returns true if this item should be rotated by 180 degrees around the Y axis when being held in an entities
     * hands.
     */
    @SideOnly(Side.CLIENT)
    public boolean shouldRotateAroundWhenRendering()
    {
        return true;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player)
    {
        if (player.fishEntity != null)
        {
            int i = player.fishEntity.func_146034_e();
            itemStackIn.damageItem(i, player);
            player.swingItem();
        }
        else
        {
            worldIn.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!worldIn.isRemote)
            {
                worldIn.spawnEntityInWorld(new EntityFishHook(worldIn, player));
            }

            player.swingItem();
        }

        return itemStackIn;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        this.itemIcon = register.registerIcon(this.getIconString() + "_uncast");
        this.theIcon = register.registerIcon(this.getIconString() + "_cast");
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_94597_g()
    {
        return this.theIcon;
    }

    /**
     * Checks isDamagable and if it cannot be stacked
     */
    public boolean isItemTool(ItemStack p_77616_1_)
    {
        return super.isItemTool(p_77616_1_);
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return 1;
    }
}