package com.gmail.aamelis.trf.ModSpells.ArcherSpells;

import com.gmail.aamelis.trf.ModCastingSystem.DelayedEffects.DelayedSpellEffect;
import com.gmail.aamelis.trf.ModCastingSystem.DelayedEffects.DelayedSpellEffectScheduler;
import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModItems.DataComponents.BowCastingData;
import com.gmail.aamelis.trf.ModItems.Weapons.Ranger.AbstractModBowItem;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.Network.Packets.RenderBowTimerPacket;
import com.gmail.aamelis.trf.Registries.DataComponentsInit;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class QuickShotSpell implements ISpell {

    @Override
    public String getId() {
        return "quick_shot";
    }

    @Override
    public String getDisplayName() {
        return "Quick Shot";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.ARCHER;
    }

    @Override
    public int getRequiredMana() {
        return 200;
    }

    @Override
    public long getCooldown() {
        return 10000;
    }

    @Override
    public int multiCastTicks() {
        return 0;
    }

    @Override
    public int repetitions() {
        return 0;
    }

    @Override
    public void cast(ServerPlayer player) {
        ItemStack bow = player.getMainHandItem();

        if (!(bow.getItem() instanceof AbstractModBowItem)) return;

        bow.remove(DataComponentsInit.BOW_DATA);

        bow.set(DataComponentsInit.BOW_DATA, new BowCastingData(System.currentTimeMillis(), BowCastingData.QUICK));

        PacketDistributor.sendToPlayer(player, new RenderBowTimerPacket(System.currentTimeMillis() + (100 * 50L), 0xFF00FFFF));

        player.sendSystemMessage(Component.literal("Quick Shot Active!"));

        DelayedSpellEffectScheduler.schedule(player.level(), new DelayedSpellEffect(
                100, (lvl) -> {
                    BowCastingData data = bow.get(DataComponentsInit.BOW_DATA);
                    if (data != null && data.castType() == BowCastingData.QUICK) {
                        player.sendSystemMessage(Component.literal("Quick Shot Ended!"));
                        bow.remove(DataComponentsInit.BOW_DATA);
                    }
                }
        ));
    }

    @Override
    public void repeatedCast(ServerPlayer player, int iteration) {

    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.V,
                SpellInput.C,
                SpellInput.B
        );
    }

    @Override
    public ResourceLocation getFullPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/quick_shot_full.png");
    }

    @Override
    public ResourceLocation getEmptyPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/quick_shot_full.png");
    }

    @Override
    public ResourceLocation animationId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_quick_shot_poison");
    }
}
