package com.rubyboat.explodes.mixin;

import com.rubyboat.explodes.Main;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    public void updatePostDeath(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult> ci) {
        if(world.getGameRules().getBoolean(Main.IS_VEGAN_GAMEMODE))
        {
            user.sendMessage(Text.of("test"), false);
            ItemStack thisitem = user.getStackInHand(hand);
            if(thisitem.isFood())
            {
                user.sendMessage(Text.of("isfood"), false);
                if(Main.MEAT_LIST.contains(thisitem)) {
                    user.sendMessage(Text.of("ismeat"), false);
                    ci.setReturnValue(TypedActionResult.pass(thisitem));
                }
            }
        }
    }
}
