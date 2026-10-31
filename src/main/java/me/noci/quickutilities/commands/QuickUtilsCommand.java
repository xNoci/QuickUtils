package me.noci.quickutilities.commands;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XTag;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.configuration.PluginMeta;
import lombok.SneakyThrows;
import me.noci.quickutilities.hooks.viaversion.ViaVersionHook;
import me.noci.quickutilities.input.Inputs;
import me.noci.quickutilities.inventory.GuiItem;
import me.noci.quickutilities.inventory.InventoryContent;
import me.noci.quickutilities.inventory.InventoryPageContent;
import me.noci.quickutilities.inventory.PagedQuickGUIProvider;
import me.noci.quickutilities.quicktab.QuickTab;
import me.noci.quickutilities.quicktab.builder.QuickTabBuilder;
import me.noci.quickutilities.scoreboard.QuickBoard;
import me.noci.quickutilities.utils.EnumUtils;
import me.noci.quickutilities.utils.InventoryPattern;
import me.noci.quickutilities.utils.ItemBuilder;
import me.noci.quickutilities.utils.Legacy;
import me.noci.quickutilities.utils.time.BukkitUnit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.function.Predicate;

public class QuickUtilsCommand {

    public static LiteralCommandNode<CommandSourceStack> createCommand(PluginMeta pluginMeta) {
        return Commands.literal("quickutils")
                .then(Commands.literal("item")
                        .then(Commands.argument("item", ArgumentTypes.itemStack())
                                .then(Commands.argument("name", StringArgumentType.word()).executes(
                                        context -> {
                                            final ItemStack item = context.getArgument("item", ItemStack.class);
                                            final String name = StringArgumentType.getString(context, "name");
                                            return giveItem(context, item, name);
                                        }))
                        ).executes(QuickUtilsCommand::giveItem)

                )
                .then(Commands.literal("input").executes(QuickUtilsCommand::input))
                .then(Commands.literal("tab").executes(QuickUtilsCommand::tab))
                .then(Commands.literal("board").executes(context -> board(context, pluginMeta)))
                .then(Commands.literal("gui").executes(QuickUtilsCommand::gui))
                .build();
    }

    @SneakyThrows
    private static int giveItem(CommandContext<CommandSourceStack> context) {
        ItemStack item = ItemBuilder.of(XMaterial.DIAMOND_SWORD.get()).withUnbreakable(true).build();
        return giveItem(context, item, "Diamond Sword");
    }

    @SneakyThrows
    private static int giveItem(CommandContext<CommandSourceStack> context, ItemStack item, String name) {
        Player player = context.getSource().getPlayerOrThrow();
        ItemBuilder itemBuilder = ItemBuilder.of(item.getType(), Legacy.deserialize(name));
        itemBuilder.withItemFlags().withAmount(1).withUnbreakable(true);

        player.getInventory().addItem(itemBuilder.build());
        return Command.SINGLE_SUCCESS;
    }

    @SneakyThrows
    private static int input(CommandContext<CommandSourceStack> context) {
        Player player = context.getSource().getPlayerOrThrow();
        Inputs.sign(player, 1, System.out::println);
        return Command.SINGLE_SUCCESS;
    }

    private static int tab(CommandContext<CommandSourceStack> context) {
        QuickTabBuilder builder = QuickTab.builder().color((_, _) -> NamedTextColor.RED)
                .prefix((_, _) -> Component.text("QuickUtils").color(NamedTextColor.BLUE)
                        .append(Component.text(" | ").color(NamedTextColor.GRAY)), (player, player2) -> player.isSneaking())
                .suffix((player, _) -> Component.text(" (" + ViaVersionHook.getVersion(player).getName() + ")")
                        .color(NamedTextColor.GREEN));
        QuickTab.setUpdatingTabList(builder, 1, BukkitUnit.SECONDS);
        return Command.SINGLE_SUCCESS;
    }

    private static int board(CommandContext<CommandSourceStack> context, PluginMeta pluginMeta) {
        QuickBoard<String> quickBoard = QuickBoard.string();
        quickBoard.replaceUpdatingScoreboard(1, BukkitUnit.TICKS, (player, scoreboard) -> {
            scoreboard.updateTitle(
                    "§7%s §7(§c%s§7)".formatted(pluginMeta.getName(),
                            pluginMeta.getVersion()));
            scoreboard.updateLines("", "§cX§7: " + player.getLocation().getBlockX(),
                    "§aY§7: " + player.getLocation().getBlockY(), "§9Z§7: " + player.getLocation().getBlockZ(), "");
        });
        quickBoard.updateAll();

        return Command.SINGLE_SUCCESS;
    }

    @SneakyThrows
    private static int gui(CommandContext<CommandSourceStack> context) {
        Player player = context.getSource().getPlayerOrThrow();
        new AllItems().provide(player);
        return Command.SINGLE_SUCCESS;
    }

    private static class AllItems extends PagedQuickGUIProvider {

        private AllItems() {
            super(Component.text("All Items", NamedTextColor.GREEN), 54);
        }

        @Override
        public void init(Player player, InventoryContent content) {
            content.fillPattern(ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).asGuiItem(),
                    InventoryPattern.FULL);
        }

        @Override
        public void initPage(Player player, InventoryPageContent pageContent) {
            var items = EnumUtils.asStream(XMaterial.values())
                    .filter(Predicate.not(material -> XMaterial.BEETROOTS.equals(material)))
                    .filter(Predicate.not(material -> XMaterial.PITCHER_CROP.equals(material)))
                    .filter(Predicate.not(material -> XMaterial.POTTED_CLOSED_EYEBLOSSOM.equals(material)))
                    .filter(Predicate.not(material -> XMaterial.POTTED_OPEN_EYEBLOSSOM.equals(material)))
                    .filter(Predicate.not(material -> XMaterial.COPPER_WALL_TORCH.equals(material)))
                    .filter(Predicate.not(material -> XMaterial.POTTED_GOLDEN_DANDELION.equals(material)))
                    .filter(Predicate.not(XTag.INVENTORY_NOT_DISPLAYABLE::isTagged)).map(XMaterial::get)
                    .filter(Objects::nonNull)
                    .map(material -> ItemBuilder.of(
                                    material,
                                    Component.text(
                                            material.name(),
                                            TextColor.color((float) Math.random(), (float) Math.random(), (float) Math.random())
                                    )
                            )
                    )
                    .map(ItemBuilder::asGuiItem)
                    .toArray(GuiItem[]::new);
            pageContent.pageContent(items);
            pageContent.itemSlots(InventoryPattern.range(1, 45));
            pageContent.previousPageButton(45, new ItemStack(Material.ARROW),
                    new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            pageContent.nextPageButton(53, new ItemStack(Material.ARROW),
                    new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
    }

}
