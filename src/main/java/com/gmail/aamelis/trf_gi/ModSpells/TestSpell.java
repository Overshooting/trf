package com.gmail.aamelis.trf_gi.ModSpells;

import com.gmail.aamelis.trf_gi.ModEntities.StaffProjectile;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class TestSpell implements ISpell {
    @Override
    public String getId() {
        return "test";
    }

    @Override
    public String getRequiredClass() {
        return "default";
    }

    @Override
    public void cast(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("Test Spell Cast!"));

        Level level = player.level();

        StaffProjectile projectile = new StaffProjectile(level, player);

        projectile.setDamage(5.0f);

        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 2.0f, 0.1f);

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1.0f, 1.0f);

        level.addFreshEntity(projectile);
    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.C,
                SpellInput.C,
                SpellInput.C
        );
    }
}
