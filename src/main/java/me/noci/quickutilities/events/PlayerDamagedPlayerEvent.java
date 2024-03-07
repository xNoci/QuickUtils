package me.noci.quickutilities.events;

import lombok.Getter;
import lombok.Setter;
import me.noci.quickutilities.events.core.CorePlayerCancellableEvent;
import me.noci.quickutilities.listener.EntityDamageByEntityListener;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Optional;

/**
 * To use this event you first have to invoke {@link EntityDamageByEntityListener#initialise()}.
 * This will register the listener which will trigger the {@link PlayerDamagedPlayerEvent}.
 */
@Getter
public class PlayerDamagedPlayerEvent extends CorePlayerCancellableEvent {

    private final Player attacker;
    private final EntityDamageEvent.DamageCause cause;
    private final Projectile projectile;
    @Setter private double damage;

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
