package com.kljiana.kl_bends.util;

import com.kljiana.kl_bends.BendsMod;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.resources.ResourceLocation;

public class AnimUtil {
    public static boolean replaceAnimation(ModifierLayer<IAnimation> animation, String name, int length, Ease ease){
        if (animation.getAnimation() != null && animation.getAnimation() instanceof KeyframeAnimationPlayer keyframeAnimation) {
            if (!keyframeAnimation.getData().extraData.containsValue(String.format("\"%s\"", name))) {
                AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeIn(length, ease);
                animation.replaceAnimationWithFade(fadeModifier, frameLocation(name));
            }
            return true;
        }
        return false;
    }

    public static boolean replaceAnimation(ModifierLayer<IAnimation> animation, String name){
        return replaceAnimation(animation, name, 10, Ease.OUTEXPO);
    }

    public static boolean replaceAnimation(ModifierLayer<IAnimation> animation, String name, int length){
        return replaceAnimation(animation, name, length, Ease.OUTEXPO);
    }


    public static ResourceLocation animationLocation(String path) {
        return new ResourceLocation(BendsMod.MODID, path);
    }

    public static KeyframeAnimationPlayer frameLocation(String path) {
        KeyframeAnimation keyframeAnimation = PlayerAnimationRegistry.getAnimation(animationLocation(path));
        if (keyframeAnimation != null) {
            return new KeyframeAnimationPlayer(keyframeAnimation);
        }
        return null;
    }
}
