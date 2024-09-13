package generic;

public enum AnsiColors {
    BLACK("\u001B[38;2;0;0;0m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[38;2;255;255;255m"),
    RESET("\u001B[0m");

    AnsiColors(String colorString) {
        this.COLOR_STRING = colorString;
    }

    private final String COLOR_STRING;

    public String getColor() {
        return COLOR_STRING;
    }
}