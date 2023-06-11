package botanicthaumaturgy.main.utils.compat;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import botanicthaumaturgy.main.Config;
import botanicthaumaturgy.main.utils.BlockInterface;
import botanicthaumaturgy.main.utils.ItemInterface;
import botanicthaumaturgy.main.utils.LocalizationManager;
import botanicthaumaturgy.main.utils.VersionInfo;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.config.ConfigItems;

public class ThaumcraftHelper implements IModHelper {

    @SuppressWarnings("unused")
    public enum MiscResource {
        ALUMENTUM,
        NITOR,
        THAUMIUM,
        QUICKSILVER,
        MAGIC_TALLOW,
        BRAIN_DEPRECATED,
        AMBER,
        ENCHANTED_FABRIC,
        VIS_FILTER,
        KNOWLEDGE_FRAGMENT,
        MIRRORED_GLASS,
        TAINTED_GOO,
        TAINTED_TENDRIL,
        JAR_LABEL,
        SALIS,
        CHARM,
        VOID_INGOT,
        VOID_SEED,
        COIN,;
    }

    @SuppressWarnings("unused")
    public enum NuggetType {
        IRON,
        COPPER,
        TIN,
        SILVER,
        LEAD,
        QUICKSILVER,
        THAUMIUM,
        VOID_METAL,
        _8,
        _9,
        _10,
        _11,
        _12,
        _13,
        _14,
        _15,
        NATIVE_IRON,
        NATIVE_COPPER,
        NATIVE_TIN,
        NATIVE_SILVER,
        NATIVE_LEAD,
        NATIVE_CINNABAR,
        _22,
        _23,
        _24,
        _25,
        _26,
        _27,
        _28,
        _29,
        _30,
        NATIVE_GOLD,;
    }

    @SuppressWarnings("unused")
    public enum ShardType {
        AIR,
        FIRE,
        WATER,
        EARTH,
        ORDER,
        CHAOS,
        BALANCED,;
    }

    @SuppressWarnings("unused")
    public enum MetalDeviceType {
        CRUCIBLE,
        ALEMBIC,
        VIS_CHARGE_RELAY,
        ADVANCED_ALCHEMICAL_CONSTRUCT,
        _4,
        ITEM_GRATE,
        _6,
        ARCANE_LAMP,
        LAMP_OF_GROWTH,
        ALCHEMICAL_CONSTRUCT,
        THAUMATORIUM,
        _11,
        MNEMONIC_MATRIX,
        LAMP_OF_FERTILITY,
        VIS_RELAY,;
    }

    @SuppressWarnings("unused")
    public enum WoodenDeviceType {
        BELLOWS,
        EAR,
        PRESSURE_PLATE,
        PRESSURE_PLATE_B,
        BORE_BASE,
        BORE,
        PLANKS_GREATWOOD,
        PLANKS_SILVERWOOD,
        BANNER,;
    }

    @SuppressWarnings("unused")
    public enum AiryBlockType {
        NODE,
        NITOR,
        _2,
        _3,
        WARDING_STONE_FENCE,
        ENERGIZED_NODE,;
    }

    @SuppressWarnings("unused")
    public enum Entity {

        BRAINY_ZOMBIE("entBrainyZombie", "EntityBrainyZombie"),
        GIANT_BRAINY_ZOMBIE("entGiantBrainyZombie", "EntityGiantBrainyZombie"),
        WISP("entWisp", "EntityWisp"),
        FIREBAT("entFirebat", "EntityFireBat"),;

        private static final String packageName = "thaumcraft.common.entities.monster.";

        public final String entityID;
        private final String className;

        private Entity(String id, String clazz) {
            this.entityID = id;
            this.className = clazz;
        }

        public String getClassName() {
            return packageName + this.className;
        }
    }

    @SuppressWarnings("unused")
    public enum BlockPlant {
        GREATWOOD_SAPLING,
        SILVERWOOD_SAPLING,
        SHIMMERLEAF,
        CINDERPEARL,
        PURIFYING_PLANT,
        VISHROOM,;
    }

    @SuppressWarnings("unused")
    public enum TreeType {
        GREATWOOD,
        SILVERWOOD,;
    }

    public static Block plant;
    public static Block candle;
    public static Block crystal;
    public static Block marker;
    public static Block jar;
    public static Block log;
    public static Block leaf;
    public static Block warded;
    public static Block wooden;
    public static Block metal;
    public static Block airy;

    public static Item filledJar;
    public static Item miscResource;
    public static Item shard;
    public static Item golem;
    public static Item nuggetMetal;
    public static Item nuggetChicken;
    public static Item nuggetBeef;
    public static Item nuggetPork;
    public static Item zombieBrain;

    public static final String Name = "Thaumcraft";

    public void preInit() {}

    public void init() {
        getBlocks();
        getItems();
    }

    public void postInit() {
        setupItemAspects();
        setupCrafting();
        setupResearch();
    }

    public static InfusionRecipe infusionIntercepter;
    public static InfusionRecipe thaumicInterfacer;
    public static CrucibleRecipe soapAlpha;
    public static CrucibleRecipe soapBeta;
    public static CrucibleRecipe bigShot;

    public static void getBlocks() {
        plant = BlockInterface.getBlock(Name, "blockCustomPlant");
        candle = BlockInterface.getBlock(Name, "blockCandle");
        crystal = BlockInterface.getBlock(Name, "blockCrystal");
        marker = BlockInterface.getBlock(Name, "blockMarker");
        jar = BlockInterface.getBlock(Name, "blockJar");
        log = BlockInterface.getBlock(Name, "blockMagicalLog");
        leaf = BlockInterface.getBlock(Name, "blockMagicalLeaves");
        warded = BlockInterface.getBlock(Name, "blockWarded");
        wooden = BlockInterface.getBlock(Name, "blockWoodenDevice");
        metal = BlockInterface.getBlock(Name, "blockMetalDevice");
        airy = BlockInterface.getBlock(Name, "blockAiry");
    }

    public static void getItems() {
        filledJar = ItemInterface.getItem(Name, "BlockJarFilledItem");
        miscResource = ItemInterface.getItem(Name, "ItemResource");
        shard = ItemInterface.getItem(Name, "ItemShard");
        golem = ItemInterface.getItem(Name, "ItemGolemPlacer");
        nuggetMetal = ItemInterface.getItem(Name, "ItemNugget");
        shard = ItemInterface.getItem(Name, "ItemShard");
        nuggetChicken = ItemInterface.getItem(Name, "ItemNuggetChicken");
        nuggetBeef = ItemInterface.getItem(Name, "ItemNuggetBeef");
        nuggetPork = ItemInterface.getItem(Name, "ItemNuggetPork");
        zombieBrain = ItemInterface.getItem(Name, "ItemZombieBrain");
    }

    public static void setupCrafting() {
        thaumicInterfacer = ThaumcraftApi.addInfusionCraftingRecipe(
                "TI_ThaumicInterfacer",
                new ItemStack(Config.thaumicInterfacer),
                64,
                new AspectList().add(Aspect.FIRE, 8),
                new ItemStack(ConfigItems.itemThaumometer),
                new ItemStack[] { new ItemStack(metal, 1, MetalDeviceType.ALEMBIC.ordinal()),
                        new ItemStack(metal, 1, MetalDeviceType.ALEMBIC.ordinal()) });
    }

    public static void setupResearch() {
        String category = "BOTANICTHAUMATURGY";
        ResearchCategories.registerCategory(
                category,
                new ResourceLocation(VersionInfo.ModID, "textures/items/arcanium_filter.png"),
                new ResourceLocation(VersionInfo.ModID, "textures/gui/research_bg1_b.png"));

        ResearchItem thaumicInterfacerPage;
        ResearchPage thaumicInterfacer1;
        ResearchPage thaumicInterface2;

        thaumicInterfacerPage = new ResearchItem(
                "BT_ThaumicInterfacer",
                category,
                new AspectList().add(Aspect.FIRE, 1).add(Aspect.TOOL, 1),
                -5,
                5,
                -5,
                new ItemStack(Config.thaumicInterfacer));

        thaumicInterfacer1 = new ResearchPage("ThaumicInterfacer.1");
        thaumicInterface2 = new ResearchPage(thaumicInterfacer);

        thaumicInterfacerPage.setPages(thaumicInterfacer1, thaumicInterface2);
        thaumicInterfacerPage.setParents("INFUSION");

        ResearchCategories.addResearch(thaumicInterfacerPage);

    }

    public static ResearchPage getResearchPage(String ident) {
        return new ResearchPage(LocalizationManager.getLocalizedString("tc.research_page." + ident));
    }

    public static void setupItemAspects() {}
}
