package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public abstract class AbstractClientPlayer extends EntityPlayer implements SkinManager.SkinAvailableCallback
{
    public static final ResourceLocation locationStevePng = new ResourceLocation("textures/entity/steve.png");
    private ResourceLocation locationSkin;
    private ResourceLocation locationCape;
    private static final String __OBFID = "CL_00000935";

    public AbstractClientPlayer(World p_i45074_1_, GameProfile p_i45074_2_)
    {
        super(p_i45074_1_, p_i45074_2_);
        String s = this.getCommandSenderName();

        if (!s.isEmpty())
        {
            SkinManager skinmanager = Minecraft.getMinecraft().func_152342_ad();
            skinmanager.func_152790_a(p_i45074_2_, this, true);
        }
    }

    public boolean func_152122_n()
    {
        return this.locationCape != null;
    }

    public boolean func_152123_o()
    {
        return this.locationSkin != null;
    }

    public ResourceLocation getLocationSkin()
    {
        return this.locationSkin == null ? locationStevePng : this.locationSkin;
    }

    public ResourceLocation getLocationCape()
    {
        return this.locationCape;
    }

    public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username)
    {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        Object object = texturemanager.getTexture(resourceLocationIn);

        if (object == null)
        {
            object = new ThreadDownloadImageData((File)null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", new Object[] {StringUtils.stripControlCodes(username)}), locationStevePng, new ImageBufferDownload());
            texturemanager.loadTexture(resourceLocationIn, (ITextureObject)object);
        }

        return (ThreadDownloadImageData)object;
    }

    public static ResourceLocation getLocationSkin(String username)
    {
        return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
    }

    public void func_152121_a(Type skinPart, ResourceLocation skinLoc)
    {
        switch (AbstractClientPlayer.SwitchType.field_152630_a[skinPart.ordinal()])
        {
            case 1:
                this.locationSkin = skinLoc;
                break;
            case 2:
                this.locationCape = skinLoc;
        }
    }

    @SideOnly(Side.CLIENT)

    static final class SwitchType
        {
            static final int[] field_152630_a = new int[Type.values().length];
            private static final String __OBFID = "CL_00001832";

            static
            {
                try
                {
                    field_152630_a[Type.SKIN.ordinal()] = 1;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_152630_a[Type.CAPE.ordinal()] = 2;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}