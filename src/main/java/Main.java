import config.GameConfig;
import database.DatabaseManager;
import database.MatchRepository;
import ui.TerminalInterface;

public class Main {
    public static void main(String[] args) {
        GameConfig config = new GameConfig();
        config.load();
        DatabaseManager dbManager = new DatabaseManager(config);
        dbManager.autoMigrate();
        MatchRepository matchRepo = new MatchRepository(dbManager);
        TerminalInterface ui = new TerminalInterface(config, matchRepo);
        ui.run();
    }
}