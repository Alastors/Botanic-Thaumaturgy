package botanicthaumaturgy.main;

import java.io.File;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

import botanicthaumaturgy.block.*;
import botanicthaumaturgy.item.tools.ItemAlastorsWand;
import botanicthaumaturgy.item.tools.ItemThaumicInterfacer;
import botanicthaumaturgy.main.utils.VersionInfo;
import botanicthaumaturgy.tileentity.TileBigPool;
import botanicthaumaturgy.tileentity.TileEntityInfusionMatrixAlpha;
import botanicthaumaturgy.tileentity.TileEntityInfusionPillarAlpha;
import botanicthaumaturgy.tileentity.TileEntityPedestalAlpha;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * A class to hold some data related to mod state & functions.
 *
 * @author Alastor's_Game
 */
public class Config {

    public static final String CATEGORY_MODULES = "modules";

    public static boolean thaumcraftActive;
    public static int blockStoneDeviceRI;
    public static int blockStoneDeviceTwoRI;
    public static int blockStoneDeviceThreeRI;

    public static int blockStoneDeviceFourRI;

    public static Item thaumicInterfacer;
    public static Item alastorsWand;

    public static BlockInfusionMatrixAlpha matrixAlpha;
    public static BlockInfusionPillarAlpha pillarAlpha;
    public static BlockArcaneMarble arcaneMarble;
    public static BlockArcaneMarbleBrick arcaneMarbleBrick;
    public static BlockPedestalAlpha marblePedestal;

    public static BlockBigPool bigPool;

    // ----- Config State info ----------------------------------
    public static Configuration configuration;
    private static Config instance = null;

    public static void Init(File configFile) {
        if (instance != null) return;
        instance = new Config();
        FMLCommonHandler.instance().bus().register(instance);
        configuration = new Configuration(configFile);
        configuration.load();
        processConfigFile();

        configuration.save();
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(VersionInfo.ModName)) {
            if (configuration.hasChanged()) {
                configuration.save();
            }
        }
    }

    public static void saveConfigs() {
        configuration.save();
    }

    public static void setupBlocks() {
        arcaneMarble = new BlockArcaneMarble();
        GameRegistry.registerBlock(arcaneMarble, arcaneMarble.getUnlocalizedName());
        arcaneMarbleBrick = new BlockArcaneMarbleBrick();
        GameRegistry.registerBlock(arcaneMarbleBrick, arcaneMarbleBrick.getUnlocalizedName());

        setupInfusionFucker();
    }

    public static void setupItems() {
        thaumicInterfacer = new ItemThaumicInterfacer();
        GameRegistry.registerItem(thaumicInterfacer, thaumicInterfacer.getUnlocalizedName());

        alastorsWand = new ItemAlastorsWand();
        GameRegistry.registerItem(alastorsWand, alastorsWand.getUnlocalizedName());

    }

    private static void processConfigFile() {
        syncConfigs();
    }

    private static void syncConfigs() {
        doModuleConfigs();
    }

    private static void doModuleConfigs() {
        thaumcraftActive = configuration.get(CATEGORY_MODULES, "Thaumcraft", true).getBoolean();
    }

    public static void setupInfusionFucker() {
        matrixAlpha = new BlockInfusionMatrixAlpha();
        GameRegistry.registerBlock(matrixAlpha, "matrixAlpha");
        GameRegistry
                .registerTileEntity(TileEntityInfusionPillarAlpha.class, TileEntityInfusionPillarAlpha.tileEntityName);

        pillarAlpha = new BlockInfusionPillarAlpha();
        GameRegistry.registerBlock(pillarAlpha, "pillarAlpha");
        GameRegistry
                .registerTileEntity(TileEntityInfusionMatrixAlpha.class, TileEntityInfusionMatrixAlpha.tileEntityName);

        marblePedestal = new BlockPedestalAlpha();
        GameRegistry.registerBlock(marblePedestal, "marblePedestal");
        GameRegistry.registerTileEntity(TileEntityPedestalAlpha.class, TileEntityPedestalAlpha.tileEntityName);

        bigPool = new BlockBigPool();
        GameRegistry.registerBlock(bigPool, "bigPool");
        GameRegistry.registerTileEntity(TileBigPool.class, TileBigPool.tileEntityName);
    }

    static {
        blockStoneDeviceRI = -1;
        blockStoneDeviceTwoRI = -2;
        blockStoneDeviceThreeRI = -3;
        blockStoneDeviceFourRI = -4;
    }
}
