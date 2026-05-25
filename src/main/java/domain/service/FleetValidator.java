cat << 'EOF' > src/main/java/domain/service/FleetValidator.java
package domain.service;

import domain.model.Board;
import config.GameConfig;
import java.util.ArrayList;
import java.util.List;

public class FleetValidator {
    
    public static class ValidationResult {
        private final boolean ok;
        private final List<String> errors;

        public ValidationResult(boolean ok, List<String> errors) {
            this.ok = ok;
            this.errors = errors;
        }
        public boolean isOk() { return ok; }
        public List<String> getErrors() { return errors; }
    }

    public static ValidationResult validate(Board board, GameConfig config) {
        List<String> errors = new ArrayList<>();
        if (board.getFleet().size() != config.getFleetSizes().length) {
            errors.add("Quantidade invalida de navios no tabuleiro.");
        }
        return new ValidationResult(errors.isEmpty(), errors);
    }

    public static boolean canPlaceStatic(Board board, int x, int y, int len, boolean horiz) {
        int N = board.getSize();
        char[][] grid = board.getGrid();

        if (horiz) {
            if (x + len > N) return false;
            for (int i = 0; i < len; i++) {
                if (grid[y][x + i] != '.') return false;
            }
        } else {
            if (y + len > N) return false;
            for (int i = 0; i < len; i++) {
                if (grid[y + i][x] != '.') return false;
            }
        }
        return true;
    }
}
EOF