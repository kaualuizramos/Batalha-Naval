package domain.model;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private final int id;
    private final String name;
    private final int size;
    private final List<Coordinate> coordinates;
    private int hp;

    public Ship(int id, String name, int size) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.hp = size;
        this.coordinates = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getSize() { return size; }
    public int getHp() { return hp; }
    public List<Coordinate> getCoordinates() { return coordinates; }

    public void addCoordinate(Coordinate coord) {
        this.coordinates.add(coord);
    }

    public boolean takeHit() {
        if (hp > 0) hp--;
        return isSunk();
    }

    public boolean isSunk() {
        return hp == 0;
    }
}