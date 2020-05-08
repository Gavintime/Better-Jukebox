package net.keckle.betterjukebox;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.keckle.betterjukebox.blocks.BetterJukeboxBlock;
import net.keckle.betterjukebox.blocks.BetterJukeboxEntity;
import net.keckle.betterjukebox.gui.BetterJukeboxController;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.BlockContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

public class MainInit implements ModInitializer {

    public static final BetterJukeboxBlock BETTER_JUKEBOX_BLOCK = new BetterJukeboxBlock(FabricBlockSettings.of(Material.WOOD).build());
    public static final BlockItem BETTER_JUKEBOX_ITEM = new BlockItem(BETTER_JUKEBOX_BLOCK, new Item.Settings().group(ItemGroup.DECORATIONS));
    public static final String modId = "betterjukebox";
    public static final Identifier BETTER_JUKEBOX = new Identifier(modId, "better_jukebox");
    //public static final String BETTER_JUKEBOX_TRANSLATION_KEY = Util.createTranslationKey("container", BETTER_JUKEBOX);
    public static BlockEntityType<BetterJukeboxEntity> BETTER_JUKEBOX_ENTITY;


    @Override
    public void onInitialize() {

        //register jukebox block and blockitem
        Registry.register(Registry.BLOCK, BETTER_JUKEBOX, BETTER_JUKEBOX_BLOCK);
        Registry.register(Registry.ITEM, BETTER_JUKEBOX, BETTER_JUKEBOX_ITEM);

        //register better jukebox entity (a block entity type connects a block to a block entity)
        BETTER_JUKEBOX_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, BETTER_JUKEBOX,
                BlockEntityType.Builder.create(BetterJukeboxEntity::new, BETTER_JUKEBOX_BLOCK).build(null));

        ContainerProviderRegistry.INSTANCE.registerFactory(BETTER_JUKEBOX, (syncId, id, player, buf) ->
                new BetterJukeboxController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
    }
}
