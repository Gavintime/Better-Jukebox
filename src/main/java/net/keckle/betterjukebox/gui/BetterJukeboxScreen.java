package net.keckle.betterjukebox.gui;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;

//the screen class is completely client sided
@Environment(EnvType.CLIENT)
public class BetterJukeboxScreen extends CottonInventoryScreen<BetterJukeboxController> {

    public BetterJukeboxScreen(BetterJukeboxController container, PlayerEntity player) {
        super(container, player);
    }
}
