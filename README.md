# QuickUtils

QuickUtils is a handy library which adds some utilities that improves the way of writing your Spigot plugins.

Some of the current utilities are:

- QuickTab (Supports: 1.8)
- QuickGUI [in dev] (Supports: 1.8 - 1.19)
- QuickCommand [coming soon]

## QuickTab - Usage

```java
public class QuickTabExample extends JavaPlugin {
    public void quickTabExample() {
        QuickTabBuilder builder = QuickTab.builder(); //Create a new QuickTabBuilder

        builder.sortID((player, target) -> 0); //Set the sortId. Default: Integer.MAX_VALUE
        builder.entries((player, target) -> new String[]{target.getName()}); //Set the entries. Default: new String[] {target.getName()}
        builder.prefix((player, target) -> "Custom Prefix"); //Set the prefix. Default: ""
        //or 
        builder.prefix((player, target) -> "Custom Perm-Prefix", (player, target) -> target.hasPermission("perm")); //Set the prefix under a condition. Default: "" and true
        builder.suffix((player, target) -> "Custom Suffix"); //Set the suffix. Default: ""
        //or
        builder.suffix((player, target) -> "Custom Perm-Suffix", (player, target) -> target.hasPermission("perm")); //Set the suffix under a condition. Default: "" and true
        builder.color((player, target) -> ChatColor.AQUA); //Set the color for target. Default ChatColor.WHITE
        builder.allowFriendlyFire((player, target) -> false); //Set if friendly fire is allowed. Default: true
        builder.seeFriendlyInvisible((player, target) -> false); //Set if see friendly invisible is allowed. Default: true
        builder.nameTagVisibility((player, target) -> NameTagVisibility.NEVER); //Sets the name tag visibility. Default: NameTagVisibility.ALWAYS
        builder.collisionRule((player, target) -> CollisionRule.NEVER); //Sets the collision rule. Default: NameTagVisibility.ALWAYS

        QuickTab.setUpdatingTabList(JavaPlugin, builder); //Set a UpdatingTabList. This will auto-update on player join.
        QuickTab.update(Player); //Update tab list for player UpdatingTabList list.
        QuickTab.updateAll(); //Update tab list for every player UpdatingTabList list.
        QuickTab.removeUpdatingTabList(); //Remove the current UpdatingTabList.

        QuickTab.update(Player, builder); //Update tab list of player using a builder. Only available if no UpdatingTabList is set.
        QuickTab.updateAll(builder); //Update tab list of every player using a builder. Only available if no UpdatingTabList is set.
    }
}
```