package com.kljiana.kl_bends.event;

import com.kljiana.kl_bends.BendsMod;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = BendsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
    @SubscribeEvent
    public static void walkAnim(TickEvent.ClientTickEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        PlayerAnimationAccess.PlayerAssociatedAnimationData animationData = PlayerAnimationAccess.getPlayerAssociatedData(player);
        ModifierLayer<IAnimation> animation = (ModifierLayer<IAnimation>) animationData.get(animLocation("player_animation"));
        if (animation == null) return;

        BendsMod.LOGGER.info(String.valueOf(player.getDeltaMovement()));
        if (!player.onGround() && player.getDeltaMovement().y > 0) {
            if (animation.getAnimation() != null && animation.getAnimation() instanceof KeyframeAnimationPlayer keyframeAnimation) {
                if (!keyframeAnimation.getData().extraData.containsValue("\"jump\"")) {
                    AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeIn(3, Ease.INOUTSINE);
                    animation.replaceAnimationWithFade(fadeModifier, frameLocation("jump"));
                }
                return;
            }
            animation.setAnimation(frameLocation("jump"));
        } else if (player.getDeltaMovement().horizontalDistanceSqr() > 0.02) {
            if (animation.getAnimation() != null && animation.getAnimation() instanceof KeyframeAnimationPlayer keyframeAnimation) {
                if (!keyframeAnimation.getData().extraData.containsValue("\"run\"")) {
                    AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeIn(10, Ease.OUTEXPO);
                    animation.replaceAnimationWithFade(fadeModifier, frameLocation("run"));
                }
                return;
            }
            animation.setAnimation(frameLocation("run"));
        } else if (player.getDeltaMovement().horizontalDistanceSqr() > 0.01) {
            if (animation.getAnimation() != null && animation.getAnimation() instanceof KeyframeAnimationPlayer keyframeAnimation) {
                if (!keyframeAnimation.getData().extraData.containsValue("\"walking\"")) {
                    AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeIn(10, Ease.OUTEXPO);
                    animation.replaceAnimationWithFade(fadeModifier, frameLocation("walking"));
                }
                return;
            }
            animation.setAnimation(frameLocation("walking"));
        } else {
            if (animation.getAnimation() != null && animation.getAnimation() instanceof KeyframeAnimationPlayer keyframeAnimation) {
                if (!keyframeAnimation.getData().extraData.containsValue("\"rest\"")) {
                    AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeIn(10, Ease.INOUTSINE);
                    animation.replaceAnimationWithFade(fadeModifier, frameLocation("rest"));
                }
                return;
            }
            animation.setAnimation(frameLocation("rest"));
        }
    }

    private static ResourceLocation animLocation(String path) {
        return new ResourceLocation(BendsMod.MODID, path);
    }

    private static KeyframeAnimationPlayer frameLocation(String path) {
        KeyframeAnimation keyframeAnimation = PlayerAnimationRegistry.getAnimation(animLocation(path));
        if (keyframeAnimation != null) {
            return new KeyframeAnimationPlayer(keyframeAnimation);
        }
        return null;
    }
}
