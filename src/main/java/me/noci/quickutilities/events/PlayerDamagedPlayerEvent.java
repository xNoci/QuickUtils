package me.noci.quickutilities.events;

import lombok.Getter;
import lombok.Setter;
import me.noci.quickutilities.events.core.CorePlayerCancellableEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Optional;

public class PlayerDamagedPlayerEvent extends CorePlayerCancellableEvent {

    @Getter private final Player attacker;
    @Getter private final EntityDamageEvent.DamageCause cause;
    @Getter private final Projectile projectile;
    @Getter @Setter private double damage;

    public PlayerDamagedPlayerEvent(Player player, Player attacker, EntityDamageEvent.DamageCause cause, Projectile projectile, double damage, boolean cancelled) {
        super(player, cancelled);
        this.attacker = attacker;
        this.cause = cause;
        this.damage = damage;
        this.projectile = projectile;
    }

    public boolean isProjectileUsed() {
        return this.projectile != null;
    }

    public Optional<Projectile> projectile() {
        return Optional.ofNullable(projectile);
    }

}
