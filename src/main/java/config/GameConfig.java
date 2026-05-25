cat << 'EOF' > src/main/java/config/GameConfig.java
package config;

import java.io.InputStream;
import java.util.Properties;

public class GameConfig {
    private String groupName;
    private int boardSize;
    private int[] fleetSizes;
    private String[] fleetNames;
    private String fleetAdjacencyRule;
    private boolean uiShowLegend;
    private int uiReplayDelayMs;
    private boolean dbEnabled;
    private String dbSqliteFile;
    private boolean dbAutoMigrate;
    private String gameSeed;
    private String gameMode;

    public void load() {
        Properties prop = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("game.properties")) {
            if (is == null) {
                prop.setProperty("board.size", "10");
                prop.setProperty("fleet.sizes", "5,4,3,3,2");
                prop.setProperty("fleet.names", "Porta-avioes,Encouracado,Cruzador,Submarino,Destroyer");
                prop.setProperty("game.mode", "PLAY");
                prop.setProperty("db.enabled", "false");
            } else {
                prop.load(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.groupName = prop.getProperty("group.name", "Grupo Desconhecido");
        this.boardSize = Integer.parseInt(prop.getProperty("board.size", "10"));
        
        String[] sizesStr = prop.getProperty("fleet.sizes", "5,4,3,3,2").split(",");
        this.fleetSizes = new int[sizesStr.length];
        for (int i = 0; i < sizesStr.length; i++) this.fleetSizes[i] = Integer.parseInt(sizesStr[i].trim());

        this.fleetNames = prop.getProperty("fleet.names", "Porta-avioes,Encouracado,Cruzador,Submarino,Destroyer").split(",");
        this.fleetAdjacencyRule = prop.getProperty("fleet.adjacency_rule", "ORTHO_DIAG");
        this.uiShowLegend = Boolean.parseBoolean(prop.getProperty("ui.show_legend", "true"));
        this.uiReplayDelayMs = Integer.parseInt(prop.getProperty("ui.replay_delay_ms", "250"));
        
        this.dbEnabled = Boolean.parseBoolean(prop.getProperty("db.enabled", "true"));
        this.dbSqliteFile = prop.getProperty("db.sqlite.file", "data/batalha_naval.db");
        this.dbAutoMigrate = Boolean.parseBoolean(prop.getProperty("db.auto_migrate", "true"));
        this.gameSeed = prop.getProperty("game.seed", "");
        this.gameMode = prop.getProperty("game.mode", "PLAY");
    }

    public String getGroupName() { return groupName; }
    public int getBoardSize() { return boardSize; }
    public int[] getFleetSizes() { return fleetSizes; }
    public String[] getFleetNames() { return fleetNames; }
    public String getFleetAdjacencyRule() { return fleetAdjacencyRule; }
    public boolean isUiShowLegend() { return uiShowLegend; }
    public int getUiReplayDelayMs() { return uiReplayDelayMs; }
    public boolean isDbEnabled() { return dbEnabled; }
    public String getDbSqliteFile() { return dbSqliteFile; }
    public boolean isDbAutoMigrate() { return dbAutoMigrate; }
    public String getGameSeed() { return gameSeed; }
    public String getGameMode() { return gameMode; }
}
EOF