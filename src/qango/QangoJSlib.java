package qango;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.function.Consumer;

import static generic.CommandLine.*;
import static qango.Player.PLAYER1;
import static qango.Player.PLAYER2;


public class QangoJSlib {
    private final Qango6Board board;
    private boolean zoomed = false;
    private final Consumer<Boolean> showBoard;
    private final ArrayList<String> options;

    private final EnumMap<Player, String> playerNames;

    public QangoJSlib(){
        board = new Qango6Board();
        showBoard = zoomed -> System.out.println(zoomed ? board.toBigString() : board.toString());
        playerNames = new EnumMap<>(Player.class);
        playerNames.put(PLAYER1, PLAYER1.getDefaultPlayerName());
        playerNames.put(PLAYER2, PLAYER2.getDefaultPlayerName());
        options = new ArrayList<>();
        options.add("input playernames");
        options.add("start game");
    }

    public String[] getStartMenu(){
        return options.toArray(new String[0]);
    }

    public void chooseFromStartMenu(int choice){
        switch (choice) {
            case 1: {
                setPlayerNames();
                break;
            }
            case 2: {
                play();
                break;
            }
        }
    }

    private void play() {
        board.emptyBoard();
        Player currentPlayer = null;
        Coordinate lastMove;

        do {
            showBoard.accept(zoomed);
            currentPlayer = (currentPlayer == PLAYER1) ? PLAYER2 : PLAYER1;
            lastMove = askForMove(currentPlayer);
            board.placePlayer(currentPlayer, lastMove);

        } while (!(board.playerWon(currentPlayer, lastMove) || board.freeLocations().isEmpty()));

        showBoard.accept(zoomed);
        if (board.playerWon(currentPlayer, lastMove)) {
            System.out.printf("Congratulation %s, you won!\n", playerNames.get(currentPlayer));
        } else {
            System.out.println("It's a draw, well played!");
        }
    }

    private Coordinate askForMove(Player player) {
        do {
            String moveChosen = askForString(String.format("%s, what is your next move? ", playerNames.get(player))).trim().toLowerCase();
            if (moveChosen.length() != 2) {
                System.out.println("Please enter the location in a notation like: a1");
            } else {
                int row = moveChosen.charAt(0) - 'a';
                int column = moveChosen.charAt(1) - '0';
                if (board.locationOnBoard(row, column)) {
                    Coordinate chosenCoordinate = new Coordinate(row, column);
                    if (board.locationTaken(chosenCoordinate)) {
                        System.out.println("Please enter an empty location");
                    } else {
                        return chosenCoordinate;
                    }
                } else {
                    System.out.println("This is not a valid location");
                }
            }
        } while (true);
    }

    private void setPlayerNames() {
        for (Player player : Player.values()) {
            String name;
            do {
                name = askForString(String.format("input name for %s(%s): ", player.getDefaultPlayerName(), player == PLAYER1 ? "white" : "black"));
                playerNames.put(player, name);
                if (name.trim().isEmpty())
                    System.out.println(red.apply("Need more then only spaces"));//feels ugly, but best I can think of
            } while (name.trim().isEmpty());
        }
    }
}
