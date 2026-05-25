package domain.model;

public class ShotResult {
    public enum Type { WATER, HIT, SUNK }

    private final Type type;
    private final String shipName;
    private final Coordinate coordinate;

    public ShotResult(Type type, Coordinate coordinate, String shipName) {
        this.type = type;
        this.coordinate = coordinate;
        this.shipName = shipName;
    }

    public Type getType() { return type; }
    public Coordinate getCoordinate() { return coordinate; }
    public String getShipName() { return shipName; }
}