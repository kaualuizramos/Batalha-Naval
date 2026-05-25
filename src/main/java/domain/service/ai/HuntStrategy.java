package domain.service.ai;

import domain.model.Coordinate;
import java.util.ArrayDeque;
import java.util.Random;

public class HuntStrategy implements CpuStrategy {
    private final ArrayDeque<Coordinate> targets = new ArrayDeque<>();

    @Override
    public Coordinate chooseTarget(char[][] trackingGrid, Random rng) {
        int N = trackingGrid.length;
        
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                if (trackingGrid[y][x] == 'X' && hasUnexploredNeighbors(trackingGrid, x, y)) {
                    enqueueNeighbors(x, y, N, trackingGrid);
                }
            }
        }

        while (!targets.isEmpty()) {
            Coordinate head = targets.removeFirst();
            if (trackingGrid[head.getY()][head.getX()] == '.') {
                return head;
            }
        }

        while (true) {
            int x = rng.nextInt(N);
            int y = rng.nextInt(N);
            if (trackingGrid[y][x] == '.') {
                if ((x + y) % 2 == 0 || rng.nextInt(100) < 25) {
                    return new Coordinate(x, y);
                }
            }
        }
    }

    private boolean hasUnexploredNeighbors(char[][] grid, int x, int y) {
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx >= 0 && nx < grid.length && ny >= 0 && ny < grid.length && grid[ny][nx] == '.') {
                return true;
            }
        }
        return false;
    }

    private void enqueueNeighbors(int x, int y, int N, char[][] grid) {
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx >= 0 && nx < N && ny >= 0 && ny < N && grid[ny][nx] == '.') {
                targets.addLast(new Coordinate(nx, ny));
            }
        }
    }
}