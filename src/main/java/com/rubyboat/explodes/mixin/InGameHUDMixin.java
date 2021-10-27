package com.rubyboat.explodes.mixin;

import com.rubyboat.explodes.PlayerExt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public abstract class InGameHUDMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    //renderStatusBars
    @Inject(at = @At("TAIL"), method = "renderStatusBars", cancellable = true)
    public void use(MatrixStack matrices, CallbackInfo ci) {
        PlayerEntity PlayerEnt = (PlayerEntity) this.client.player;
        PlayerExt PlayerExt = (PlayerExt) PlayerEnt;
        if(PlayerExt == null) {
            System.out.println("null");
        }
        System.out.println(PlayerExt.getWaterTime());
        if(PlayerExt.getWaterTime() > 0)
        {
            String airTime = String.valueOf(this.client.player.getAir());
            int w = (this.scaledWidth - this.getTextRenderer().getWidth(airTime)) / 2;
            int h = this.scaledHeight - 4 - 100;
            this.getTextRenderer().draw(matrices, airTime, w, h,16777215);
        }
    }
}
