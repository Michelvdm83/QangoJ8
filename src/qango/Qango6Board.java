package qango;

import qango.fielddata.Field;
import qango.fielddata.Qango6ColorZone;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Qango6Board {
    private final TreeMap<Coordinate, Field> board = new TreeMap<>();
    private final int lowestCoordinateNumber = Qango6ColorZone.MINIMUM_COORDINATE_VALUE;
    private final int highestCoordinateNumber = Qango6ColorZone.MAXIMUM_COORDINATE_VALUE;

    public Qango6Board(){
        for(Qango6ColorZone cz: Qango6ColorZone.values()){
            for(Coordinate c: cz.getLocations()){
                board.put(c, new Field(cz.getColor()));
            }
        }
    }

    public void emptyBoard(){
        board.values().forEach(field -> field.setPlayer(null));
    }

    public void placePlayer(Player player, Coordinate coordinate){
        if(locationTaken(coordinate)) throw new IllegalArgumentException("Location should first be checked with locationTaken!");
        board.get(coordinate).setPlayer(player);
    }

    private boolean wonByColorZone(Player player, Coordinate lastMove){
        return Arrays.stream(Qango6ColorZone.getZoneOfCoordinate(lastMove).getLocations()).allMatch(c ->
                board.get(c).hasPlayer() && board.get(c).getPlayer().equals(player));
    }

    public boolean playerWon(Player player, Coordinate lastMove){
        int row = lastMove.row();
        int column = lastMove.column();

        Predicate<Coordinate> rowFilter = coordinate -> coordinate.row() == row;
        Predicate<Coordinate> columnFilter = coordinate -> coordinate.column() == column;
        Predicate<Coordinate> diagonalSlashFilter = coordinate -> coordinate.column() + coordinate.row() == column + row;
        Predicate<Coordinate> diagonalBackSlashFilter = coordinate -> coordinate.column() - coordinate.row() == column - row;

        boolean straightLineWin = wonByLine(player, rowFilter) || wonByLine(player, columnFilter);
        boolean diagonalLineWin = wonByLine(player, diagonalSlashFilter) || wonByLine(player, diagonalBackSlashFilter);

        return straightLineWin || diagonalLineWin || wonBySquare(player, lastMove) || wonByColorZone(player, lastMove);
    }

    private boolean wonBySquare(Player player, Coordinate lastMove){
        int row = lastMove.row();
        int column = lastMove.column();
        List<Coordinate> startingCoordinates = board.keySet().stream().filter(c -> (c.row() == row || c.row() == row-1) && (c.column() == column || c.column() == column-1)).collect(Collectors.toList());

        for(Coordinate coordinate: startingCoordinates){
            Predicate<Coordinate> squareRowsPart = coord -> (coord.row() == coordinate.row() || coord.row() == coordinate.row()+1);
            Predicate<Coordinate> squareColumnsPart = coord -> (coord.column() == coordinate.column() || coord.column() == coordinate.column()+1);
            Predicate<Coordinate> playerPart = coord -> board.get(coord).hasPlayer() && board.get(coord).getPlayer().equals(player);

            if(board.keySet().stream().filter(squareRowsPart.and(squareColumnsPart).and(playerPart)).count() == 4) return true;
        }
        return false;
    }

    private boolean wonByLine(Player player, Predicate<Coordinate> filter){
        List<Coordinate> coordList = board.keySet().stream().filter(filter).collect(Collectors.toList());

        int concurrentFieldsTaken = 0;
        for(Coordinate c: coordList){
            if(board.get(c).hasPlayer() && board.get(c).getPlayer().equals(player)){
                concurrentFieldsTaken++;
                if(concurrentFieldsTaken >= 5) return true;
            } else{
                concurrentFieldsTaken = 0;
            }
        }
        return false;
    }

    public boolean locationOnBoard(int row, int column){
        return lowestCoordinateNumber <= Math.min(row, column) && Math.max(row, column) <= highestCoordinateNumber;
    }

    public boolean locationTaken(Coordinate location){
        if(!board.containsKey(location))throw new IllegalArgumentException("Coordinate " + location + " is not on the board!");
        return board.get(location).hasPlayer();
    }

    public ArrayList<Coordinate> freeLocations(){
        return board.keySet().stream().filter(coordinate -> !board.get(coordinate).hasPlayer()).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String toString(){
        String rowLegendFormat = "%c ";
        StringBuilder boardAsString = new StringBuilder(String.format(rowLegendFormat, ' '));

        IntStream.rangeClosed(lowestCoordinateNumber, highestCoordinateNumber).forEachOrdered(i -> boardAsString.append(String.format(" %d ", i)));
        boardAsString.append("\n");

        IntStream.rangeClosed(lowestCoordinateNumber, highestCoordinateNumber).forEachOrdered(i -> {
            boardAsString.append(String.format("%c ", 'a'+i));
            board.keySet().stream().filter(c -> c.row() == i).forEach(coord -> boardAsString.append(board.get(coord)));
            boardAsString.append("\n");
        });

        return boardAsString.toString();
    }

    public String toBigString(){
        String rowLegendFormat = "%c ";
        StringBuilder boardAsString = new StringBuilder(String.format(rowLegendFormat, ' '));

        //printing the numbers for the coordinates
        IntStream.rangeClosed(lowestCoordinateNumber, highestCoordinateNumber).forEachOrdered(i -> boardAsString.append(String.format("    %d    ", i)));
        boardAsString.append("\n");
        //everything above is the same in toString(), except the format for coordinate numbers

        StringBuilder[] linesPerRow = new StringBuilder[3];

        IntStream.rangeClosed(lowestCoordinateNumber, highestCoordinateNumber).forEachOrdered(i -> {
            linesPerRow[0] = new StringBuilder(String.format(rowLegendFormat, ' '));
            linesPerRow[1] = new StringBuilder(String.format(rowLegendFormat, 'a'+i));
            linesPerRow[2] = new StringBuilder(String.format(rowLegendFormat, ' '));

            board.keySet().stream().filter(c -> c.row() == i).forEach(coord -> {
                String[] fieldLines = board.get(coord).toBigString().split("\n");
                IntStream.range(0, linesPerRow.length).forEachOrdered(iterator -> linesPerRow[iterator].append(fieldLines[iterator]));
            });

            Arrays.stream(linesPerRow).forEach(sb -> boardAsString.append(sb).append("\n"));
        });

        return boardAsString.toString();
    }
}

/*
comparing toBigString and toString:
StringBuilder[] linesPerRow = new StringBuilder[3];

IntStream.rangeClosed(lowestCoordinateNumber, highestCoordinateNumber).forEachOrdered(i -> {
    linesPerRow[0] = new StringBuilder(String.format(rowLegendFormat, ' '));
    linesPerRow[1] = new StringBuilder(String.format(rowLegendFormat, 'a'+i));
    linesPerRow[2] = new StringBuilder(String.format(rowLegendFormat, ' '));

    board.keySet().stream().filter(c -> c.row() == i).forEach(coord -> {
        String[] fieldLines = board.get(coord).toBigString().split("\n");
        IntStream.range(0, linesPerRow.length).forEachOrdered(iterator -> linesPerRow[iterator].append(fieldLines[iterator]));
    });

    Arrays.stream(linesPerRow).forEach(sb -> boardAsString.append(sb).append("\n"));
});

IntStream.rangeClosed(lowestCoordinateNumber, highestCoordinateNumber).forEachOrdered(i -> {
    boardAsString.append(String.format("%c ", 'a'+i));
    board.keySet().stream().filter(c -> c.row() == i).forEach(coord -> boardAsString.append(board.get(coord)));
    boardAsString.append("\n");
});

>>>using the same streams
>>>format coordinate numbers > needs tenary if to be put in 1 method
>>>toBigString needs 3 lines, toString needs 1 line > can this be solved by calling split() on the toString like:
>>>board.firstEntry().getValue().toString().split("\n");?
*/