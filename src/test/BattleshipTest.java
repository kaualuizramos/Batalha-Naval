import config.GameConfig;
import domain.model.Board;
import domain.model.Coordinate;
import domain.model.ShotResult;
import domain.service.FleetValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BattleShipTest {
    private GameConfig config;

    @BeforeEach
    public void setup() {
        config = new GameConfig();
        config.load();
    }

    @Test
    public void testCoordinateParsing() {
        Coordinate valid = Coordinate.parse("A1", 10);
        assertNotNull(valid);
        assertEquals(0, valid.getX());
        assertEquals(0, valid.getY());

        Coordinate invalid = Coordinate.parse("Z20", 10);
        assertNull(invalid);
    }

    @Test
    public void testShipPlacementAndCollision() {
        Board board = new Board(10);
        assertTrue(FleetValidator.canPlaceStatic(board, 0, 0, 5, true));
        
        // Simular colisao no mesmo espaco
        board.getGrid()[0][2] = 'S'; 
        assertFalse(FleetValidator.canPlaceStatic(board, 0, 0, 5, true));
    }

    @Test
    public void testShotApplication() {
        Board board = new Board(10);
        Coordinate coord = new Coordinate(2, 2);
        
        ShotResult waterRes = board.receiveShot(coord);
        assertEquals(ShotResult.Type.WATER, waterRes.getType());

        board.getGrid()[5][5] = 'S';
        ShotResult hitRes = board.receiveShot(new Coordinate(5, 5));
        assertEquals(ShotResult.Type.HIT, hitRes.getType());
    }
}