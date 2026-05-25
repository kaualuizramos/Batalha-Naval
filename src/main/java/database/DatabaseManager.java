cat << 'EOF' > src/main/java/database/DatabaseManager.java
package database;

import config.GameConfig;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager {
    private final GameConfig config;

    public DatabaseManager(GameConfig config) {
        this.config = config;
    }

    public Connection getConnection() throws Exception {
        if (!config.isDbEnabled()) return null;
        String dbPath = config.getDbSqliteFile();
        File dbFile = new File(dbPath);
        if (dbFile.getParentFile() != null && !dbFile.getParentFile().exists()) {
            dbFile.getParentFile().mkdirs();
        }
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    public void autoMigrate() {
        if (!config.isDbEnabled() || !config.isDbAutoMigrate()) return;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS partida (id INTEGER PRIMARY KEY AUTOINCREMENT, inicio TEXT, fim TEXT, vencedor TEXT, seed TEXT);");
            stmt.execute("CREATE TABLE IF NOT EXISTS jogadores (id INTEGER PRIMARY KEY AUTOINCREMENT, id_partida INTEGER, nome TEXT, tipo TEXT);");
            stmt.execute("CREATE TABLE IF NOT EXISTS jogadas (id INTEGER PRIMARY KEY AUTOINCREMENT, id_partida INTEGER, turno INTEGER, jogador TEXT, coordenada TEXT, resultado TEXT);");
        } catch (Exception e) {
            System.err.println("[DB Error] Erro na migracao: " + e.getMessage());
        }
    }
}
EOF