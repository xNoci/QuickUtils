package me.noci.quickutilities.input.sign;

public enum SignColor {

    BLACK,
    WHITE,
    ORANGE,
    MAGENTA,
    LIGHT_BLUE,
    YELLOW,
    LIME,
    PINK,
    GRAY,
    LIGHT_GRAY,
    CYAN,
    PURPLE,
    BLUE,
    BROWN,
    GREEN,
    RED;


    public String getColor() {
        return name().toLowerCase();
    }


}
