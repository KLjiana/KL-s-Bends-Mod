package com.kaleblangley.kl_bends.util;

import net.minecraft.client.player.AbstractClientPlayer;

public class PlayerUtil {
    public static boolean isPlayerWalk(AbstractClientPlayer player){
        return player.getDeltaMovement().horizontalDistanceSqr() > 0D;
    }

    public static boolean isPlayerRun(AbstractClientPlayer player){
        return player.getDeltaMovement().horizontalDistanceSqr() > 0.02D;
    }

    public static boolean isPlayerJump(AbstractClientPlayer player){
        return !player.onGround() && player.getDeltaMovement().y > 0D;
    }
}
