package me.noci.quickutilities.hooks;

public class ProtocolLibHook extends PluginHook {

    protected ProtocolLibHook() {
        super("ProtocolLib");
    }

    public static boolean isEnabled() {
        return Singleton.HOOK.enabled();
    }

    private static class Singleton {
        private static final ProtocolLibHook HOOK = new ProtocolLibHook();
    }

}
