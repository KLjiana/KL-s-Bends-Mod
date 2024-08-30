package com.kljiana.kl_bends.event;

import com.kljiana.kl_bends.BendsMod;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.kljiana.kl_bends.util.AnimUtil.*;
import static com.kljiana.kl_bends.util.PlayerUtil.*;


@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = BendsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
    @SubscribeEvent
    public static void walkAnim(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer()) return;
        AbstractClientPlayer player = (LocalPlayer) event.player;
        CompoundTag compoundTag = player.getPersistentData();

        PlayerAnimationAccess.PlayerAssociatedAnimationData animationData = PlayerAnimationAccess.getPlayerAssociatedData(player);
        ModifierLayer<IAnimation> animation = (ModifierLayer<IAnimation>) animationData.get(animationLocation("player_animation"));
        if (animation == null) return;

        setUtilAnimation(animation);
        BendsMod.LOGGER.info(String.valueOf(player.getDeltaMovement().horizontalDistanceSqr()));
        if (isPlayerRun(player)) {
            if (!player.onGround() && !compoundTag.getBoolean("jump")){
                animation.addModifierBefore(new SpeedModifier(0.49f));
                compoundTag.putBoolean("jump", true);
            } else if (player.onGround() && compoundTag.getBoolean("jump")) {
                animation.removeModifier(0);
                compoundTag.putBoolean("jump", false);
            }
            replaceAnimation("run");
        } else if (isPlayerJump(player)) {
            replaceAnimation("jump", 2);
        } else if (isPlayerWalk(player)) {
            replaceAnimation("walking");
        } else {
            if (replaceAnimation("rest")) return;
            animation.setAnimation(frameLocation("rest"));
        }
    }
}
