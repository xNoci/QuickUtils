package me.noci.quickutilities.listener;

import lombok.Getter;
import me.noci.quickutilities.events.Events;
import me.noci.quickutilities.events.PlayerDamagedPlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener {

    public EntityDamageByEntityListener() {
        Events.subscribe(EntityDamageByEntityEvent.class, EventPriority.HIGHEST)
                .attribute(EventAttacker.class, this::getEventAttacker)
                .filter(e -> !(e.getEntity() instanceof Player))
                .filterAttribute(EventAttacker.class, (e, a) -> a.get().getAttacker() != null)
                .handle((e, attribute) -> {
                    EventAttacker eventAttacker = attribute.get(EventAttacker.class).get();
                    Player player = (Player) e.getEntity();
                    PlayerDamagedPlayerEvent playerDamageByPlayerEvent = new PlayerDamagedPlayerEvent(player, eventAttacker.getAttacker(), e.getCause(), eventAttacker.getProjectile(), e.getDamage(), e.isCancelled());
                    Bukkit.getPluginManager().callEvent(playerDamageByPlayerEvent);

                    e.setDamage(playerDamageByPlayerEvent.getDamage());
                    e.setCancelled(playerDamageByPlayerEvent.isCancelled());
                });
    }

    private EventAttacker getEventAttacker(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if (entity instanceof Player player) {
            return new EventAttacker(player, null);
        }

        if (entity instanceof Projectile projectile && projectile.getShooter() instanceof Player shooter) {
            return new EventAttacker(shooter, projectile);
        }

        return new EventAttacker();
    }

    private static class EventAttacker {

        @Getter private final Player attacker;
        @Getter private final Projectile projectile;

        public EventAttacker() {
            this(null, null);
        }

        public EventAttacker(Player attacker, Projectile projectile) {
            this.attacker = attacker;
            this.projectile = projectile;
        }
    }
}
