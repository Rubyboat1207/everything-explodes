package com.rubyboat.explodes.mixin;

import com.rubyboat.explodes.Main;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin
{
    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult> ci) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(world.getGameRules().getBoolean(Main.IS_VEGAN_GAMEMODE))
        {
            if(itemStack.isFood())
            {
                if(Main.MEAT_LIST.contains(itemStack.getItem())) {
                    ci.setReturnValue(TypedActionResult.fail(itemStack));
                    ci.cancel();
                }
            }
        }
        if(world.getGameRules().getBoolean(Main.Unfair))
        {
            if(itemStack.isFood())
            {
                if(itemStack.getItem() == Items.BREAD)
                {
                    user.sendMessage(Text.of("You are allergic to Wheat"), false);
                    Main.killPlayerIfNotCreative(user);
                }
                if(Main.MEAT_LIST.contains(itemStack.getItem())) {
                    user.sendMessage(Text.of("You are vegetarian"), false);

                }
            }
        }
    }
}
