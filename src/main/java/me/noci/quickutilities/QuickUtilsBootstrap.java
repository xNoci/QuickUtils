package me.noci.quickutilities;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.noci.quickutilities.commands.QuickUtilsCommand;

public class QuickUtilsBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar()
                    .register(
                            QuickUtilsCommand.createCommand(context.getPluginMeta()),
                            "Main command for QuickUtilities",
                            java.util.List.of("qu")
                    );
        });
    }
}
