package com.kljiana.kl_bends.mixin;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @ModifyConstant(method = "getRenderOffset(Lnet/minecraft/client/player/AbstractClientPlayer;F)Lnet/minecraft/world/phys/Vec3;",constant = @Constant(doubleValue = -0.125D, ordinal = 0))
    private double cancelOffset(double constant){
        return 0;
    }
}
