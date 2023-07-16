package me.noci.quickutilities.input.sign;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XTag;
import com.google.common.collect.Lists;
import me.noci.quickutilities.input.Input;
import me.noci.quickutilities.input.functions.CanceledInput;
import me.noci.quickutilities.input.functions.InputExecutor;
import me.noci.quickutilities.input.sign.packets.SignPacketHandler;
import me.noci.quickutilities.packethandler.PacketHandlerFactory;
import me.noci.quickutilities.utils.MathUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

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

    public SignInputBuilder signLines(String... signLines) {
        return signLines(List.of(signLines));
    }

    public SignInputBuilder signLines(List<String> signLines) {
        this.signLines = signLines;
        return this;
    }

    public SignInputBuilder addLine(String... line) {
        Collections.addAll(this.signLines, line);
        return this;
    }

    public SignInputBuilder addLine(String line) {
        this.signLines.add(line);
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
        this.inputLine = MathUtils.clamp(1, 4, inputLine) - 1;
        return this;
    }

    public Input build(Player player) {
        var input = new PlayerSignInput(packetHandler, player, signType, signColor.getColor(), glowingText, inputLine, signLines, inputExecutor);
        input.onCancel(canceledInput);
        return input;
    }

}
