package qango.fielddata;

import qango.Player;

public class Field {
    private static final String FIELD_OCCUPIED = " O ";
    private static final String FIELD_EMPTY    = "   ";
    private static final String ZOOMED_FIELD_PART = "   ";
    private static final String ZOOMED_FORMAT = "%1$s%1$s%1$s\n%1$s%2$s%1$s\n%1$s%1$s%1$s";

    private final FieldColor color;

    private Player player;

    public Field(FieldColor color){
        this.color = color;
        player = null;
    }

    public boolean hasPlayer(){
        return player != null;
    }
    //1 functie van te maken door Optional te gebruiken?
    public Player getPlayer() {
        return player;
    }

    public FieldColor getColor() {
        return color;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String toString(){
        return color.apply(player==null? FIELD_EMPTY : player.asStringColor().apply(FIELD_OCCUPIED));
    }

    public String toBigString(){
        String bigFieldPart = color.apply(ZOOMED_FIELD_PART);
        String bigFieldPlayerPart = player == null? color.apply(ZOOMED_FIELD_PART) : player.asBackgroundColor().apply(ZOOMED_FIELD_PART);
        return String.format(ZOOMED_FORMAT, bigFieldPart, bigFieldPlayerPart);
    }
}
