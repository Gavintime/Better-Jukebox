package net.keckle.betterjukebox;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.keckle.betterjukebox.gui.BetterJukeboxController;
import net.keckle.betterjukebox.gui.BetterJukeboxScreen;
import net.minecraft.container.BlockContext;

public class ClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ScreenProviderRegistry.INSTANCE.registerFactory(MainInit.BETTER_JUKEBOX, (syncId, identifier, player, buf) ->
                new BetterJukeboxScreen(new BetterJukeboxController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
    }
}
