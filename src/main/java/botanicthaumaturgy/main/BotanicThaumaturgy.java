package botanicthaumaturgy.main;

import botanicthaumaturgy.main.utils.LogHelper;
import botanicthaumaturgy.main.utils.VersionInfo;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@SuppressWarnings("unused")
@Mod(
        modid = VersionInfo.ModID,
        name = VersionInfo.ModName,
        version = VersionInfo.Version,
        dependencies = VersionInfo.Depends)
public class BotanicThaumaturgy {

    @Mod.Instance(VersionInfo.ModID)
    public static botanicthaumaturgy.main.BotanicThaumaturgy instance;

    @SidedProxy(serverSide = "botanicthaumaturgy.main.CommonProxy", clientSide = "botanicthaumaturgy.main.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void handleMissingMapping(FMLMissingMappingsEvent event) {
        for (MissingMapping mapping : event.get()) {
            // TODO: ... maybe not this.
            LogHelper.info(String.format("Missing mapping: %s - ignoring.", mapping.name));
            mapping.ignore();
        }
    }
}
