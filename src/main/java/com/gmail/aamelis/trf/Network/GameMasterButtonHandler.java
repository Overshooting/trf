package com.gmail.aamelis.trf.Network;

import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameMasterBlockEntity;
import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameTypes;
import com.gmail.aamelis.trf.Network.Packets.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class GameMasterButtonHandler {

    public static void handleStartGamePacket(StartGamePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
           ServerPlayer player = (ServerPlayer)context.player();

           BlockEntity blockEntity = player.level().getBlockEntity(packet.pos());

           if (blockEntity instanceof GameMasterBlockEntity gameMasterBlockEntity) {
               try {
                   gameMasterBlockEntity.startGame();
                   gameMasterBlockEntity.setMessage("Game Started");
               } catch (Exception e) {
                   gameMasterBlockEntity.setMessage(e.getMessage());
               }
           }
        });
    }

    public static void handleSetCornersPacket(SetCornersPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer)context.player();

            BlockEntity blockEntity = player.level().getBlockEntity(packet.gmPos());

            if (blockEntity instanceof GameMasterBlockEntity gameMasterBlockEntity) {
                try {
                    gameMasterBlockEntity.setNewCorners(packet.c1(), packet.c2());
                    gameMasterBlockEntity.setMessage("Corners set from: " +
                            packet.c1().getX() + ", " + packet.c1().getY() + ", " + packet.c1().getZ() +
                            " to " + packet.c2().getX() + ", " + packet.c2().getY() + ", " + packet.c2().getZ());
                } catch (Exception e) {
                    gameMasterBlockEntity.setMessage(e.getMessage());
                }
            }
        });
    }

    public static void handleResetGamePacket(ResetGamePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer)context.player();

            BlockEntity blockEntity = player.level().getBlockEntity(packet.pos());

            if (blockEntity instanceof GameMasterBlockEntity gameMasterBlockEntity) {
                try {
                    gameMasterBlockEntity.resetGame();
                    gameMasterBlockEntity.setMessage("Reset game!");
                } catch (Exception e) {
                    gameMasterBlockEntity.setMessage(e.getMessage());
                }
            }
        });
    }

    public static void handleOpenLightsOutMenu(OpenLightsOutMenuPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer)context.player();

            BlockEntity blockEntity = player.level().getBlockEntity(packet.pos());

            if (blockEntity instanceof GameMasterBlockEntity gameMasterBlockEntity) {
                gameMasterBlockEntity.setGame(GameTypes.LIGHTS_OUT);
            }
        });
    }

    public static void handleBackButtonPacket(BackButtonPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer)context.player();

            BlockEntity blockEntity = player.level().getBlockEntity(packet.pos());

            if (blockEntity instanceof GameMasterBlockEntity gameMasterBlockEntity) {
                gameMasterBlockEntity.setGame(GameTypes.NONE);
                System.out.println("Back Button Packet Received!");
            }
        });
    }

}
