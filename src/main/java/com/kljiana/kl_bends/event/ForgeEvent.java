package com.kljiana.kl_bends.event;

import com.kljiana.kl_bends.BendsMod;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.kljiana.kl_bends.util.AnimUtil.*;


@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = BendsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
    @SubscribeEvent
    public static void walkAnim(TickEvent.ClientTickEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        PlayerAnimationAccess.PlayerAssociatedAnimationData animationData = PlayerAnimationAccess.getPlayerAssociatedData(player);
        ModifierLayer<IAnimation> animation = (ModifierLayer<IAnimation>) animationData.get(animationLocation("player_animation"));
        if (animation == null) return;

        if (player.getDeltaMovement().horizontalDistanceSqr() > 0.02) {
            replaceAnimation(animation,"run");
        } else if (!player.onGround() && player.getDeltaMovement().y > 0) {
            replaceAnimation(animation,"jump");
        } else if (player.getDeltaMovement().horizontalDistanceSqr() > 0.01) {
            replaceAnimation(animation,"walking");
        } else {
            if (replaceAnimation(animation,"rest")) return;
            animation.setAnimation(frameLocation("rest"));
        }
    }
}
