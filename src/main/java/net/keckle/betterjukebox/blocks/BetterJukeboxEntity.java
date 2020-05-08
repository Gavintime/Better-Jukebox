package net.keckle.betterjukebox.blocks;

import net.keckle.betterjukebox.MainInit;
import net.keckle.betterjukebox.gui.BetterJukeboxController;
import net.keckle.betterjukebox.helper.MusicDiscInfo;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.container.BlockContext;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

//TODO: call makeDirty() any time a custom var is changed in order to tell mc to call toTag thus saving the data
//TODO: implement BlockEntityClientSerializable if needing to sync block entity data to the client

public class BetterJukeboxEntity extends LockableContainerBlockEntity implements BetterJukeboxInventory {

    //6 by 3 inventory of music discs
    private final DefaultedList<ItemStack> items;
    Timer songScheduler;
    //keeps track if currently playing any music
    private boolean playing;
    //index of the current slot selected to play
    private int index;
    //should play next song once current one finishes?
    private Boolean playlistEnabled;
    private boolean loop;


    public BetterJukeboxEntity() {
        super(MainInit.BETTER_JUKEBOX_ENTITY);
        items = DefaultedList.ofSize(18, ItemStack.EMPTY);
        playing = false;
        index = 0;
        playlistEnabled = true;
        loop = false;

        //create timer to handle actions on song playback end
        //TODO: pause scheduler if in a single player game and in pause menu?
        songScheduler = new Timer();
    }


    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }


    //returns the playing status
    public boolean isPlaying() {
        return playing;
    }


    //attempts to stop the playing status, also tells the game to stop playing music disc audio
    public void stop() {
        playing = false;
        world.playLevelEvent(1010, pos, 0);
        songScheduler.cancel();
        songScheduler = new Timer();
    }


    //attempts to start playing the given item, assumes the given item is a valid music disc
    private void play(int index) {
        //ensure everything is cleaned up
        if (playing) stop();

        //play disc at index if not empty
        if (!items.get(index).isEmpty()) {
            playing = true;
            world.playLevelEvent(1010, pos, Item.getRawId(items.get(index).getItem()));

            //schedule java to tell me when song ends
            songScheduler.schedule(new TimerTask() {
                @Override
                public void run() {
                    onSongEnd();
                }
            }, 1000 * MusicDiscInfo.getSongLength(items.get(index)));
        }
    }


    //this is what the ui actually calls
    public void play() {
        play(index);
    }


    //this receives a saved tag and sets/updates the block entity data with it
    @Override
    public void fromTag(CompoundTag tag) {
        //retrieves the pos and id of the block entity?
        super.fromTag(tag);
        //sets the items to the items stored in the tag
        //items = DefaultedList.ofSize(getInvSize(), ItemStack.EMPTY);
        Inventories.fromTag(tag, items);

        index = tag.getInt("index");
        loop = tag.getBoolean("loop");
        playlistEnabled = tag.getBoolean("playlistEnabled");

    }


    //this takes all of the data from the block entity and serializes it as a tag
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        //adds the pos and id of the block entity
        super.toTag(tag);
        //adds the inventory to the tag
        Inventories.toTag(tag, items);

        tag.putInt("index", index);
        tag.putBoolean("loop", loop);
        tag.putBoolean("playlistEnabled", playlistEnabled);

        return tag;
    }


    @Override
    protected Text getContainerName() {
        //TODO: support custom text?
        return new TranslatableText("container.better_jukebox");
    }


    @Override
    protected Container createContainer(int syncId, PlayerInventory playerInventory) {
        return new BetterJukeboxController(syncId, playerInventory, BlockContext.create(playerInventory.player.world, pos));
    }


    @Override
    public boolean canPlayerUseInv(PlayerEntity player) {
        return pos.isWithinDistance(player.getBlockPos(), 6.5);
    }


    //shuffles the order of the inventory leaving empty slots at the end
    //TODO: MAKE SURE SHUFFLE STICKS!!! ( makeDirty()? )
    public void shuffle() {
        Collections.shuffle(items);

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isEmpty()) {
                for (int j = i + 1; j < items.size(); j++) {
                    if (!items.get(j).isEmpty()) {
                        Collections.swap(items, i, j);
                        break;
                    }
                }
            }
        }
        //TODO: ensure first item is not the same as the previous first item
        //TODO: implement a proper algorithm to do this in one go
    }


    public boolean getLoop() {
        return loop;
    }


    public void setLoop(boolean val) {
        loop = val;
    }


    public boolean getPlaylistEnabled() {
        return playlistEnabled;
    }


    public void setPlaylistEnabled(boolean val) {
        playlistEnabled = val;
    }


    //handles what to do when a song ends, called by a scheduler
    private void onSongEnd() {

        if (loop) {
            play(index);

        } else if (playlistEnabled) {
            index = (index + 1) % 18;
            play(index);

        } else stop();

    }
}
