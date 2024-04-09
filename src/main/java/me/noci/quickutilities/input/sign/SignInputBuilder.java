package me.noci.quickutilities.input.sign;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XTag;
import com.google.common.collect.Lists;
import me.noci.quickutilities.input.Input;
import me.noci.quickutilities.input.functions.CanceledInput;
import me.noci.quickutilities.input.functions.InputExecutor;
import me.noci.quickutilities.input.sign.packets.SignPacketHandler;
import me.noci.quickutilities.packethandler.PacketHandlerFactory;
import me.noci.quickutilities.utils.Legacy;
import me.noci.quickutilities.utils.MathUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SignInputBuilder {

    private final SignPacketHandler packetHandler;

    private InputExecutor inputExecutor = null;
    private CanceledInput canceledInput = null;
    private List<String> signLines = Lists.newArrayList();
    private SignColor signColor = SignColor.BLACK;
    private Material signType = XMaterial.OAK_WALL_SIGN.parseMaterial();
    private byte glowingText = 0;
    private int inputLine = 1;

    public SignInputBuilder() {
        packetHandler = PacketHandlerFactory.getPacketHandler(SignPacketHandler.class);
    }

    public SignInputBuilder input(InputExecutor input) {
        this.inputExecutor = input;
        return this;
    }

    public SignInputBuilder canceledInput(CanceledInput canceledInput) {
        this.canceledInput = canceledInput;
        return this;
    }

    public SignInputBuilder signeLines(Component... signLines) {
        return signLines(Arrays.asList(signLines));
    }

    public SignInputBuilder signLines(List<Component> signLines) {
        this.signLines = signLines.stream().map(Legacy::serialize).collect(Collectors.toList());
        return this;
    }

    public SignInputBuilder setSignLines(String... signLines) {
        return this.setSignLines(List.of(signLines));
    }

    public SignInputBuilder setSignLines(List<String> signLines) {
        this.signLines = signLines;
        return this;
    }

    public SignInputBuilder addLines(Component... lines) {
        return addLines(Arrays.stream(lines).map(Legacy::serialize).toArray(String[]::new));
    }

    public SignInputBuilder addLines(String... line) {
        Collections.addAll(this.signLines, line);
        return this;
    }


    public SignInputBuilder color(SignColor signColor) {
        this.signColor = signColor;
        return this;
    }

    public SignInputBuilder material(Material signType) {
        return material(XMaterial.matchXMaterial(signType));
    }

    private SignInputBuilder material(XMaterial signType) {
        if (!XTag.SIGNS.isTagged(signType) && !XTag.HANGING_SIGNS.isTagged(signType))
            throw new IllegalArgumentException(String.format("%s is not a sign.", signType.name()));
        this.signType = signType.parseMaterial();
        return this;
    }

    public SignInputBuilder glowingText(boolean glowingText) {
        this.glowingText = (byte) (glowingText ? 1 : 0);
        return this;
    }

    public SignInputBuilder inputLine(int inputLine) {
        this.inputLine = MathUtils.clamp(inputLine, 1, 4) - 1;
        return this;
    }

    public Input build(Player player) {
        var input = new PlayerSignInput(packetHandler, player, signType, signColor.getColor(), glowingText, inputLine, signLines, inputExecutor);
        input.onCancel(canceledInput);
        return input;
    }

}
