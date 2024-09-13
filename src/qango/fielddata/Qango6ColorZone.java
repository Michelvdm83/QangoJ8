package qango.fielddata;

import qango.Coordinate;

import static qango.fielddata.FieldColor.*;

public enum Qango6ColorZone {
    ZONE_PINK1(PINK),
    ZONE_PINK2(PINK),
    ZONE_PINK3(PINK),
    ZONE_PINK4(PINK),
    ZONE_YELLOW1(YELLOW),
    ZONE_YELLOW2(YELLOW),
    ZONE_GREEN1(GREEN),
    ZONE_GREEN2(GREEN),
    ZONE_ORANGE1(ORANGE),
    ZONE_ORANGE2(ORANGE),
    ZONE_BLUE1(BLUE),
    ZONE_BLUE2(BLUE);


    public static final int MINIMUM_COORDINATE_VALUE = 0;
    public static final int MAXIMUM_COORDINATE_VALUE = 5;

//    static{
//        ArrayList<Integer> lowRange = new ArrayList<>();
//        lowRange.add(0);
//        lowRange.add(1);
//        lowRange.add(2);
//        ArrayList<Integer> highRange = new ArrayList<>();
//        highRange.add(3);
//        highRange.add(4);
//        highRange.add(5);
//        for(int row = MINIMUM_COORDINATE_VALUE; row <= MAXIMUM_COORDINATE_VALUE; row++){
//            for(int column = MINIMUM_COORDINATE_VALUE; column <= MAXIMUM_COORDINATE_VALUE; column++){
//                if(lowRange.containsAll(List.of(row, column)) || highRange.containsAll(List.of(row, column))){
//
//                }else{
//
//                }
//            }
//        }
//    }
    // will this work? and will it be better than what it is now? result is the same. or no Locations in the zones and use above to getLocations?

    Qango6ColorZone(FieldColor color){
        COLOR = color;
        //trying to find a way to make Coordinates start at 1 and making zones less static as they are now

        switch(this){
            //block1: {0, 1, 2} {0, 1, 2}
            case ZONE_PINK1: {
                LOCATIONS = new Coordinate[]{new Coordinate(0,0), new Coordinate(0,1), new Coordinate(1,0)};
                break;
            }
            case ZONE_YELLOW1: {
                LOCATIONS = new Coordinate[]{new Coordinate(0,2), new Coordinate(1,1), new Coordinate(2,0)};
                break;
            }
            case ZONE_GREEN1: {
                LOCATIONS = new Coordinate[]{new Coordinate(1,2), new Coordinate(2,2), new Coordinate(2,1)};
                break;
            }
            //block2: {0, 1, 2} {3, 4, 5}
            case ZONE_PINK2: {
                LOCATIONS = new Coordinate[]{new Coordinate(0,4), new Coordinate(0,5), new Coordinate(1,5)};
                break;
            }
            case ZONE_ORANGE1: {
                LOCATIONS = new Coordinate[]{new Coordinate(0,3), new Coordinate(1,4), new Coordinate(2,5)};
                break;
            }
            case ZONE_BLUE1: {
                LOCATIONS = new Coordinate[]{new Coordinate(1,3), new Coordinate(2,3), new Coordinate(2,4)};
                break;
            }
            //block3: {3, 4, 5} {0, 1, 2}
            case ZONE_PINK3: {
                LOCATIONS = new Coordinate[]{new Coordinate(4,0), new Coordinate(5,1), new Coordinate(5,0)};
                break;
            }
            case ZONE_ORANGE2: {
                LOCATIONS = new Coordinate[]{new Coordinate(3,0), new Coordinate(4,1), new Coordinate(5,2)};
                break;
            }
            case ZONE_BLUE2: {
                LOCATIONS = new Coordinate[]{new Coordinate(3,1), new Coordinate(3,2), new Coordinate(4,2)};
                break;
            }
            //block4: {3, 4, 5} {3, 4, 5}
            case ZONE_PINK4: {
                LOCATIONS = new Coordinate[]{new Coordinate(5,4), new Coordinate(5,5), new Coordinate(4,5)};
                break;
            }
            case ZONE_YELLOW2: {
                LOCATIONS = new Coordinate[]{new Coordinate(5,3), new Coordinate(4,4), new Coordinate(3,5)};
                break;
            }
            case ZONE_GREEN2: {
                LOCATIONS = new Coordinate[]{new Coordinate(3,3), new Coordinate(3,4), new Coordinate(4,3)};
                break;
            }
            default: LOCATIONS = new Coordinate[3];
        }
    }

    private final FieldColor COLOR;
    private final Coordinate[] LOCATIONS;
    //private final ArrayList<Coordinate> newLocations = new ArrayList<>();

    public FieldColor getColor() {
        return COLOR;
    }

    public Coordinate[] getLocations() {
        return LOCATIONS;
    }

    public static Qango6ColorZone getZoneOfCoordinate(Coordinate coordinate){
        for(Qango6ColorZone zone: Qango6ColorZone.values()){
            for(Coordinate c: zone.LOCATIONS){
                if(c.compareTo(coordinate) == 0) return zone;
            }
        }
        throw new IllegalArgumentException("There is no zone with Coordinate " + coordinate);
    }
}
