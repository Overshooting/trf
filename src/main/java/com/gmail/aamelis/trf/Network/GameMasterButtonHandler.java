package com.gmail.aamelis.trf.Network;

import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameMasterBlockEntity;
import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameType;
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
                } catch (Exception e) {
                    gameMasterBlockEntity.setMessage(e.getMessage());
                }
            }
        });
    }

    public static void handleSetMessagePacket(SetMessagePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer)context.player();

            BlockEntity blockEntity = player.level().getBlockEntity(packet.pos());

            if (blockEntity instanceof GameMasterBlockEntity gameMasterBlockEntity) {
                gameMasterBlockEntity.setMessage(packet.message());
            }
        });
    }

    public static void handleOpenLightsOutMenu(OpenLightsOutMenuPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer)context.player();

            BlockEntity blockEntity = player.level().getBlockEntity(packet.pos());

            if (blockEntity instanceof GameMasterBlockEntity gameMasterBlockEntity) {
                gameMasterBlockEntity.setGame(GameType.LIGHTS_OUT);
            }
        });
    }

    public static void handleBackButtonPacket(BackButtonPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer)context.player();

            BlockEntity blockEntity = player.level().getBlockEntity(packet.pos());

            if (blockEntity instanceof GameMasterBlockEntity gameMasterBlockEntity) {
                gameMasterBlockEntity.setGame(GameType.NONE);
            }
        });
    }



}
