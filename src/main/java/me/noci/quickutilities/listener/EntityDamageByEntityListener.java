package me.noci.quickutilities.listener;

import me.noci.quickutilities.events.Events;
import me.noci.quickutilities.events.PlayerDamagedPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener {

    private static boolean initialised = false;

    public static void initialise() {
        if (initialised) return;

        synchronized (EntityDamageByEntityListener.class) {
            if (initialised) return;
            initialised = true;

            Events.subscribe(EntityDamageByEntityEvent.class, EventPriority.HIGHEST)
                    .attribute(EventAttacker.class, EntityDamageByEntityListener::getEventAttacker)
                    .filter(e -> e.getEntity() instanceof Player)
                    .filterAttribute(EventAttacker.class, (e, a) -> a.get().attacker() != null)
                    .handle((e, attribute) -> {
                        EventAttacker eventAttacker = attribute.get(EventAttacker.class).get();
                        Player player = (Player) e.getEntity();
                        var damageEvent = Events.call(new PlayerDamagedPlayerEvent(
                                player,
                                eventAttacker.attacker(),
                                e.getCause(),
                                eventAttacker.projectile(),
                                e.getDamage(),
                                e.isCancelled()
                        ));

                        e.setDamage(damageEvent.getDamage());
                        e.setCancelled(damageEvent.isCancelled());
                    });
        }
    }

    private static EventAttacker getEventAttacker(EntityDamageByEntityEvent event) {
        return switch (event.getDamager()) {
            case Player player -> new EventAttacker(player);
            case Projectile projectile when projectile.getShooter() instanceof Player player ->
                    new EventAttacker(player, projectile);
            default -> new EventAttacker();
        };
    }

    private record EventAttacker(Player attacker, Projectile projectile) {

        public EventAttacker() {
            this(null, null);
        }

        public EventAttacker(Player attacker) {
            this(attacker, null);
        }

    }
}
