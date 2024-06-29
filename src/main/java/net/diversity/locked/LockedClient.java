package net.diversity.locked;

import net.diversity.locked.client.MinimapOverlay;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class LockedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient()
    {
        HudRenderCallback.EVENT.register(new MinimapOverlay());
    }
}
