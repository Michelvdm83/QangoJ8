package qango;

import java.util.function.UnaryOperator;

import static generic.CommandLine.*;

public enum Player{
    PLAYER1(255, 255, 255, "Player1"),
    PLAYER2(0, 0, 0, "Player2");

    Player(int r, int g, int b, String defaultPlayerName){
        this.playerStringColor = getNewRGBColorStringOperator(r, g, b);
        this.playerBackgroundColor = getNewRGBColorBackgroundOperator(r, g, b);
        this.defaultPlayerName = defaultPlayerName;
    }

    private final UnaryOperator<String> playerBackgroundColor;
    private final UnaryOperator<String> playerStringColor;
    private final String defaultPlayerName;

    public String getDefaultPlayerName() {
        return defaultPlayerName;
    }

    public UnaryOperator<String> asBackgroundColor(){
        return playerBackgroundColor;
    }

    public UnaryOperator<String> asStringColor(){
        return playerStringColor;
    }
}
