package database;

import domain.model.Coordinate;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MatchRepository {
    private final DatabaseManager dbManager;

    public MatchRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public int createMatch(String seed) {
        String sql = "INSERT INTO partida(inicio, seed) VALUES(?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (conn == null) return -1;
            pstmt.setString(1, LocalDateTime.now().toString());
            pstmt.setString(2, seed);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void registerPlayer(int matchId, String name, String type) {
        String sql = "INSERT INTO jogadores(id_partida, nome, tipo) VALUES(?, ?, ?)";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return;
            pstmt.setInt(1, matchId);
            pstmt.setString(2, name);
            pstmt.setString(3, type);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveShot(int matchId, int turn, String player, Coordinate coord, String res) {
        String sql = "INSERT INTO jogadas(id_partida, turno, jogador, coordenada, resultado) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return;
            pstmt.setInt(1, matchId);
            pstmt.setInt(2, turn);
            pstmt.setString(3, player);
            pstmt.setString(4, coord.toDisplayString());
            pstmt.setString(5, res);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void endMatch(int matchId, String winner) {
        String sql = "UPDATE partida SET fim = ?, vencedor = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return;
            pstmt.setString(1, LocalDateTime.now().toString());
            pstmt.setString(2, winner);
            pstmt.setInt(3, matchId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> listMatches() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT id, inicio, vencedor FROM partida ORDER BY id DESC";
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (conn == null) return list;
            while (rs.next()) {
                list.add(String.format("Partida ID: %d | Iniciada em: %s | Vencedor: %s",
                        rs.getInt("id"), rs.getString("inicio"), rs.getString("vencedor")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ResultSet getReplayShots(int matchId) throws Exception {
        Connection conn = dbManager.getConnection();
        if (conn == null) return null;
        String sql = "SELECT turno, jogador, coordenada, resultado FROM jogadas WHERE id_partida = ? ORDER BY turno ASC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, matchId);
        return pstmt.executeQuery();
    }
}