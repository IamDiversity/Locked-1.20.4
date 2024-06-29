package net.diversity.locked.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.diversity.locked.Locked;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

public class MinimapOverlay implements HudRenderCallback {

    private static final Identifier MINIMAP_HUD = new Identifier(Locked.MOD_ID, "textures/gui/minimap_hud.png");

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        int x = 0;
        int y = 0;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            x = width / 2;
            y = height;

            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderTexture(0, MINIMAP_HUD);

            drawContext.drawTexture(MINIMAP_HUD, width - 115, 15, 0, 0,100,100,100,100);
        }


    }
}
