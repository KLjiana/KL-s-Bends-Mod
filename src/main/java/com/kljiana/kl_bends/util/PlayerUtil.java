package com.kljiana.kl_bends.util;

import net.minecraft.client.player.AbstractClientPlayer;

public class PlayerUtil {
    public static boolean isPlayerWalk(AbstractClientPlayer player){
        return player.getDeltaMovement().horizontalDistanceSqr() > 0.01;
    }

    public static boolean isPlayerRun(AbstractClientPlayer player){
        return player.getDeltaMovement().horizontalDistanceSqr() > 0.02;
    }

    public static boolean isPlayerJump(AbstractClientPlayer player){
        return !player.onGround() && player.getDeltaMovement().y > 0;
    }
}
