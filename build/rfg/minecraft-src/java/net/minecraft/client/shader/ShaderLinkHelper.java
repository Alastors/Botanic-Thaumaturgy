package net.minecraft.client.shader;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.util.JsonException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class ShaderLinkHelper
{
    private static final Logger logger = LogManager.getLogger();
    private static ShaderLinkHelper staticShaderLinkHelper;
    private static final String __OBFID = "CL_00001045";

    public static void setNewStaticShaderLinkHelper()
    {
        staticShaderLinkHelper = new ShaderLinkHelper();
    }

    public static ShaderLinkHelper getStaticShaderLinkHelper()
    {
        return staticShaderLinkHelper;
    }

    public void func_148077_a(ShaderManager p_148077_1_)
    {
        p_148077_1_.func_147994_f().func_148054_b(p_148077_1_);
        p_148077_1_.func_147989_e().func_148054_b(p_148077_1_);
        OpenGlHelper.func_153187_e(p_148077_1_.func_147986_h());
    }

    public int func_148078_c() throws JsonException
    {
        int i = OpenGlHelper.func_153183_d();

        if (i <= 0)
        {
            throw new JsonException("Could not create shader program (returned program ID " + i + ")");
        }
        else
        {
            return i;
        }
    }

    public void func_148075_b(ShaderManager manager)
    {
        manager.func_147994_f().func_148056_a(manager);
        manager.func_147989_e().func_148056_a(manager);
        OpenGlHelper.func_153179_f(manager.func_147986_h());
        int i = OpenGlHelper.func_153175_a(manager.func_147986_h(), OpenGlHelper.field_153207_o);

        if (i == 0)
        {
            logger.warn("Error encountered when linking program containing VS " + manager.func_147989_e().func_148055_a() + " and FS " + manager.func_147994_f().func_148055_a() + ". Log output:");
            logger.warn(OpenGlHelper.func_153166_e(manager.func_147986_h(), 32768));
        }
    }
}