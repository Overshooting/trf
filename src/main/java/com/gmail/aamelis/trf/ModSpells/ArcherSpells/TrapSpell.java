package com.gmail.aamelis.trf.ModSpells.ArcherSpells;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModEntities.Other.MonsterTrap;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.SoundsInit;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

import java.util.List;

public class TrapSpell implements ISpell {
    @Override
    public String getId() {
        return "monster_trap";
    }

    @Override
    public String getDisplayName() {
        return "Monster Trap";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.ARCHER;
    }

    @Override
    public int getRequiredMana() {
        return 100;
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
        PlayerStatData playerStatData = player.getData(AttachmentTypesInit.PLAYER_STATS);

        MonsterTrap thisTrap = new MonsterTrap(player.level(), playerStatData.getPerception());

        thisTrap.setPos(player.getX(), player.getY(), player.getZ());

        player.level().addFreshEntity(thisTrap);

        player.level().playSound(null, player.blockPosition(), SoundsInit.MONSTER_TRAP_PLACE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void repeatedCast(ServerPlayer player, int iteration) {

    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.C,
                SpellInput.B,
                SpellInput.V
        );
    }

    @Override
    public ResourceLocation getFullPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/trap_full.png");
    }

    @Override
    public ResourceLocation getEmptyPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/trap_empty.png");
    }

    @Override
    public ResourceLocation animationId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_quick_shot");
    }
}
