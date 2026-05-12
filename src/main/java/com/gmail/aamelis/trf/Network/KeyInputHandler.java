    package com.gmail.aamelis.trf.Network;

    import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.CastKeybinds;
    import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
    import com.gmail.aamelis.trf.ModPlayerData.ModStats.Keybinds.StatKeybinds;
    import com.gmail.aamelis.trf.ModScreens.StatSheetScreen;
    import com.gmail.aamelis.trf.Network.Packets.SpellInputPacket;
    import net.minecraft.client.Minecraft;
    import net.minecraft.network.chat.Component;
    import net.neoforged.neoforge.client.event.ClientTickEvent;
    import net.neoforged.neoforge.client.network.ClientPacketDistributor;

    public class KeyInputHandler {

        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();

            if (mc.player == null) return;

            while (CastKeybinds.SPELL_C.consumeClick()) {
                sendSpellInput(SpellInput.C);
            }

            while (CastKeybinds.SPELL_V.consumeClick()) {
                sendSpellInput(SpellInput.V);
            }

            while (CastKeybinds.SPELL_B.consumeClick()) {
                sendSpellInput(SpellInput.B);
            }

            while (StatKeybinds.TOGGLE_STAT_SHEET.consumeClick()) {
                if (mc.screen instanceof StatSheetScreen) {
                    mc.setScreen(null);
                } else {
                    mc.setScreen(new StatSheetScreen(mc.player));
                }
            }
        }

        private static void sendSpellInput(SpellInput input) {
            ClientPacketDistributor.sendToServer(
                    new SpellInputPacket(input)
            );
        }



    }
