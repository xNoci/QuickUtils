package me.noci.quickutilities.input.sign;

import org.bukkit.Material;

import java.util.List;

public record SignData(String textColor, Material signType, List<String> signLines, int inputLine, byte glowingText) {
}