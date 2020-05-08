package net.keckle.betterjukebox.helper;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MusicDiscItem;

import java.util.HashMap;
import java.util.Map;

public class MusicDiscInfo {

    //stores the length of a music disc as an integer in seconds rounded up
    private static final Map<Item, Integer> discInfo;

    //static constructor
    static {
        discInfo = new HashMap<>();
        discInfo.put(Items.MUSIC_DISC_13, 178);
        discInfo.put(Items.MUSIC_DISC_CAT, 185);
        discInfo.put(Items.MUSIC_DISC_BLOCKS, 345);
        discInfo.put(Items.MUSIC_DISC_CHIRP, 185);
        discInfo.put(Items.MUSIC_DISC_FAR, 174);
        discInfo.put(Items.MUSIC_DISC_MALL, 197);
        discInfo.put(Items.MUSIC_DISC_MELLOHI, 96);
        discInfo.put(Items.MUSIC_DISC_STAL, 150);
        discInfo.put(Items.MUSIC_DISC_STRAD, 188);
        discInfo.put(Items.MUSIC_DISC_WARD, 251);
        discInfo.put(Items.MUSIC_DISC_11, 71);
        discInfo.put(Items.MUSIC_DISC_WAIT, 238);
    }

    //TODO: when porting to 1.16 add pigstep
    //TODO: add support for modded music discs


    public static int getSongLength(ItemStack disc) {
        if (disc.getItem() instanceof MusicDiscItem) {
            return discInfo.get(disc.getItem());
        } else {
            System.out.println("itemstack was not a music disc");
            return 0;
        }
    }


}
