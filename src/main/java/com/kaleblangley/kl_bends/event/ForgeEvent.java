package com.kaleblangley.kl_bends.event;

import com.kaleblangley.kl_bends.BendsMod;
import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import com.zigythebird.playeranimatorapi.data.PlayerParts;
import com.zigythebird.playeranimatorapi.mixin.AnimationStackAccessor;
import com.zigythebird.playeranimatorapi.playeranims.CustomModifierLayer;
import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Pair;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.kaleblangley.kl_bends.util.AnimUtil.*;
import static com.kaleblangley.kl_bends.util.PlayerUtil.*;


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

        if (player.isCrouching()) {
            if (isPlayerWalk(player)){
                replaceAnimation(animation, "crouch_walk");
                return;
            }
            replaceAnimation(animation, "crouch");
        } else if (isPlayerRun(player)) {
            if (!player.onGround() && !compoundTag.getBoolean("jump")) {
                animation.addModifierBefore(new SpeedModifier(0.4f));
                compoundTag.putBoolean("jump", true);
            } else if (player.onGround() && compoundTag.getBoolean("jump")) {
                animation.removeModifier(0);
                compoundTag.putBoolean("jump", false);
            }
            replaceAnimation(animation, "run");
        } else if (isPlayerJump(player)) {
            replaceAnimation(animation, "jump", 2);
        } else if (isPlayerWalk(player)) {
            replaceAnimation(animation, "walking");
        } else {
            if (replaceAnimation(animation, "rest")) return;
            animation.setAnimation(frameLocation("rest"));
        }
    }

    @SubscribeEvent
    public static void putBlock(PlayerInteractEvent.RightClickBlock event){
        if (!event.getSide().isClient()) return;

        if (event.getEntity() instanceof LocalPlayer player){
            PlayerAnimationAccess.PlayerAssociatedAnimationData animationData = PlayerAnimationAccess.getPlayerAssociatedData(player);
            ModifierLayer<IAnimation> animation = (ModifierLayer<IAnimation>) animationData.get(animationLocation("player_animation"));
            if (animation == null) return;

            PlayerParts part = new PlayerParts();
            part.head.setEnabled(false);
            part.torso.setEnabled(false);
            part.body.setEnabled(false);
            part.leftArm.setEnabled(false);
            part.leftLeg.setEnabled(false);
            part.rightLeg.setEnabled(false);

            PlayerAnimationData data = new PlayerAnimationData(player.getUUID(), animationLocation("put"),
                    part, null, -1, -1, 42, false);
            PlayerAnimations.playAnimation(player, data);
            //replaceAnimation(animation,"put", 2);
        }
    }
}
