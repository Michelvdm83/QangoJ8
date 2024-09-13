package qango.fielddata;

import java.util.function.UnaryOperator;

import static generic.AnsiColors.RESET;

public enum FieldColor implements UnaryOperator<String> {
    PINK(255,192,203),
    ORANGE(255, 165, 0),
    BLUE(0, 0, 255),
    YELLOW(255,235,0),
    GREEN(0,255,0);

    FieldColor(int r, int g, int b){
        backgroundColorString = String.format("\u001B[48;2;%d;%d;%dm", r, g, b);
    }

    private final String backgroundColorString;

    @Override
    public String apply(String original){
        return backgroundColorString + original + RESET.getColor();
    }
}