package net.keckle.betterjukebox.gui;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.*;
import net.keckle.betterjukebox.blocks.BetterJukeboxEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;


public class BetterJukeboxController extends CottonCraftingController {

    public BetterJukeboxController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        //create root panel and set size
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);

        //ui title
        //TODO: replace with translatable key
        WLabel title = new WLabel("Better Jukebox");
        root.add(title, 0, -2);

        //add 6x3 inventory slots
        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0, 6, 3);
        root.add(itemSlot, 0, 10);

        //play button
        WButton playButton = new WButton(new TranslatableText("P"));
        //this defines what the button should do when clicked by passing a lamda function
        //this lamda function runs the context.run command, which takes another lamda function
        //the contexts lamda function defines what to run for an arbitrary context object
        playButton.setOnClick(() -> context.run((world, pos) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BetterJukeboxEntity) {
                ((BetterJukeboxEntity) blockEntity).play();
            }
        }));
        root.add(playButton, 6 * 18 + 2, 9);

        //shuffle button
        WButton shuffleButton = new WButton(new TranslatableText("Sh"));
        shuffleButton.setOnClick(() -> context.run((world, pos) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BetterJukeboxEntity) {
                ((BetterJukeboxEntity) blockEntity).shuffle();
            }
        }));
        root.add(shuffleButton, 6 * 18 + 30, 9);

        //loop button
        WToggleButton loopButton = new WToggleButton(new TranslatableText("Loop"));
        //define loop button toggle behavior
        loopButton.setOnToggle((enabled) -> context.run((world, pos) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BetterJukeboxEntity) {
                ((BetterJukeboxEntity) blockEntity).setLoop(enabled);
            }
        }));
        //retrieve current loop state
        loopButton.setToggle(context.run((world, pos) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            return (blockEntity instanceof BetterJukeboxEntity && ((BetterJukeboxEntity) blockEntity).getLoop());
        }).get());
        root.add(loopButton, 6 * 18 + 2, 30);

        //playlist button
        WToggleButton playlistButton = new WToggleButton(new TranslatableText("Playlist"));
        //define playlist button toggle behavior
        playlistButton.setOnToggle((enabled) -> context.run((world, pos) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BetterJukeboxEntity) {
                ((BetterJukeboxEntity) blockEntity).setPlaylistEnabled(enabled);
            }
        }));
        //retrieve current playlist enabled state
        playlistButton.setToggle(context.run((world, pos) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            return (blockEntity instanceof BetterJukeboxEntity && ((BetterJukeboxEntity) blockEntity).getPlaylistEnabled());
        }).get());
        root.add(playlistButton, 6 * 18 + 2, 50);

        //ui title
        WLabel invText = new WLabel("Inventory");
        root.add(invText, 0, 12 + 3 * 18);

        //adds the player inventory panel
        root.add(this.createPlayerInventoryPanel(), 0, 23 + 3 * 18);

        //validating the root panel should always happen last
        root.validate(this);
    }
}
