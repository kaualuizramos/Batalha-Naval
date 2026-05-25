package ui;

import config.GameConfig;
import database.MatchRepository;
import domain.model.Board;
import domain.model.Coordinate;
import domain.model.Ship;
import domain.model.ShotResult;
import domain.service.GameEngine;
import domain.service.FleetValidator;
import java.sql.ResultSet;
import java.util.List;
import java.util.Scanner;

public class TerminalInterface {
    private final GameConfig config;
    private final MatchRepository matchRepo;
    private final Scanner sc;

    public TerminalInterface(GameConfig config, MatchRepository matchRepo) {
        this.config = config;
        this.matchRepo = matchRepo;
        this.sc = new Scanner(System.in);
    }

    public void run() {
        String mode = config.getGameMode().toUpperCase();
        if (mode.equals("LIST")) showHistory();
        else if (mode.equals("REPLAY")) runReplayMode();
        else startNewGame();
    }

    private void startNewGame() {
        System.out.println("=== " + config.getGroupName() + " - BATALHA NAVAL ===");
        GameEngine engine = new GameEngine(config, matchRepo);
        engine.initMatch();

        System.out.print("Deseja posicionar os navios manualmente? (s/N): ");
        String ans = sc.nextLine().trim();
        if (ans.equalsIgnoreCase("s")) manualPlacement(engine.getPlayerBoard());
        else {
            engine.autoPlacePlayerFleet();
            System.out.println("Frota alocada dinamicamente com sucesso!");
        }

        while (engine.isActive()) {
            renderScreen(engine.getPlayerBoard(), engine.getCpuBoard());
            if (engine.isPlayerTurn()) {
                System.out.println("\n--- SEU TURNO ---");
                Coordinate target = null;
                while (target == null) {
                    System.out.print("Informe a coordenada do disparo (Ex: B4): ");
                    target = Coordinate.parse(sc.nextLine(), config.getBoardSize());
                    if (target == null) System.out.println("Coordenada Invalida!");
                }
                ShotResult res = engine.executePlayerShot(target);
                System.out.println("Resultado do disparo: " + res.getType());
            } else {
                System.out.println("\n--- TURNO DA CPU ---");
                ShotResult res = engine.executeCpuShot();
                System.out.println("A CPU atirou em " + res.getCoordinate().toDisplayString() + " -> " + res.getType());
            }
        }
        System.out.println("\n=== FIM DE JOGO ===");
    }

    private void manualPlacement(Board b) {
        int[] sizes = config.getFleetSizes();
        String[] names = config.getFleetNames();
        for (int i = 0; i < sizes.length; i++) {
            boolean ok = false;
            while (!ok) {
                System.out.println("Posicione seu " + names[i] + " (Tamanho " + sizes[i] + ")");
                System.out.print("Coordenada Inicial (Ex: A1): ");
                Coordinate coord = Coordinate.parse(sc.nextLine(), config.getBoardSize());
                if (coord == null) continue;
                System.out.print("Orientacao (H/V): ");
                boolean horiz = sc.nextLine().trim().equalsIgnoreCase("H");
                if (FleetValidator.canPlaceStatic(b, coord.getX(), coord.getY(), sizes[i], horiz)) {
                    Ship s = new Ship(i, names[i], sizes[i]);
                    b.placeShipDirectly(s, coord.getX(), coord.getY(), horiz);
                    ok = true;
                }
            }
        }
    }

    private void renderScreen(Board p, Board c) {
        int N = config.getBoardSize();
        System.out.println(String.format("\n%-30s | %s", "SEU TABULEIRO", "TIROS NO INIMIGO"));
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) System.out.print(p.getGrid()[y][x] + " ");
            System.out.print(" | ");
            for (int x = 0; x < N; x++) System.out.print(c.getGrid()[y][x] + " ");
            System.out.println();
        }
    }

    private void showHistory() {
        List<String> matches = matchRepo.listMatches();
        matches.forEach(m -> System.out.println(m));
    }

    private void runReplayMode() {
        System.out.print("ID da partida: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        try (ResultSet rs = matchRepo.getReplayShots(id)) {
            while (rs != null && rs.next()) {
                System.out.println("Turno " + rs.getInt("turno") + " | " + rs.getString("jogador") + " -> " + rs.getString("coordenada"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}