package me.noci.quickutilities.listener;

import lombok.Getter;
import me.noci.quickutilities.events.PlayerDamagedPlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        EventAttacker eventAttacker = getEventAttacker(event);
        if (eventAttacker.getAttacker() == null) return;

        PlayerDamagedPlayerEvent playerDamageByPlayerEvent = new PlayerDamagedPlayerEvent(player, eventAttacker.getAttacker(), event.getCause(), eventAttacker.getProjectile(), event.getDamage(), event.isCancelled());
        Bukkit.getPluginManager().callEvent(playerDamageByPlayerEvent);

        event.setDamage(playerDamageByPlayerEvent.getDamage());
        event.setCancelled(playerDamageByPlayerEvent.isCancelled());
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
