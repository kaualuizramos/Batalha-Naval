cat << 'EOF' > src/main/java/domain/model/Coordinate.java
package domain.model;

import java.util.Objects;

public class Coordinate {
    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public static Coordinate parse(String s, int boardSize) {
        if (s == null) return null;
        String t = s.trim().toUpperCase().replace(" ", "");
        if (t.length() < 2 || t.length() > 3) return null;

        char col = t.charAt(0);
        if (col < 'A' || col >= ('A' + boardSize)) return null;
        int x = col - 'A';

        try {
            int row = Integer.parseInt(t.substring(1));
            if (row < 1 || row > boardSize) return null;
            return new Coordinate(x, row - 1);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String toDisplayString() {
        char c = (char) ('A' + x);
        return "" + c + (y + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
EOF