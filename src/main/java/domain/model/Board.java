package domain.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int size;
    private final char[][] grid;
    private final int[][] shipIdGrid;
    private final List<Ship> fleet;

    public Board(int size) {
        this.size = size;
        this.grid = new char[size][size];
        this.shipIdGrid = new int[size][size];
        this.fleet = new ArrayList<>();

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                grid[y][x] = '.';
                shipIdGrid[y][x] = -1;
            }
        }
    }

    public int getSize() { return size; }
    public char[][] getGrid() { return grid; }
    public List<Ship> getFleet() { return fleet; }

    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public void placeShipDirectly(Ship ship, int startX, int startY, boolean horizontal) {
        fleet.add(ship);
        for (int i = 0; i < ship.getSize(); i++) {
            int curX = startX + (horizontal ? i : 0);
            int curY = startY + (horizontal ? 0 : i);
            grid[curY][curX] = 'S';
            shipIdGrid[curY][curX] = ship.getId();
            ship.addCoordinate(new Coordinate(curX, curY));
        }
    }

    public ShotResult receiveShot(Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();

        if (grid[y][x] == 'S') {
            grid[y][x] = 'X';
            int sid = shipIdGrid[y][x];
            Ship ship = fleet.stream().filter(s -> s.getId() == sid).findFirst().orElse(null);
            
            if (ship != null) {
                boolean sunk = ship.takeHit();
                if (sunk) return new ShotResult(ShotResult.Type.SUNK, coord, ship.getName());
                return new ShotResult(ShotResult.Type.HIT, coord, ship.getName());
            }
        } else if (grid[y][x] == '.') {
            grid[y][x] = 'o';
        }
        return new ShotResult(ShotResult.Type.WATER, coord, null);
    }

    public int getAliveShipsCount() {
        return (int) fleet.stream().filter(s -> !s.isSunk()).count();
    }
}