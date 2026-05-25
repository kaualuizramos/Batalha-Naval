package domain.service;

import config.GameConfig;
import database.MatchRepository;
import domain.model.Board;
import domain.model.Coordinate;
import domain.model.Ship;
import domain.model.ShotResult;
import domain.service.ai.CpuStrategy;
import domain.service.ai.HuntStrategy;
import java.util.Random;

public class GameEngine {
    private final GameConfig config;
    private final MatchRepository matchRepo;
    private final Board playerBoard;
    private final Board cpuBoard;
    private final Random rng;
    private final CpuStrategy cpuEngine;
    
    private int matchId = -1;
    private int currentTurn = 1;
    private boolean playerTurn = true;
    private boolean active = true;

    public GameEngine(GameConfig config, MatchRepository matchRepo) {
        this.config = config;
        this.matchRepo = matchRepo;
        this.playerBoard = new Board(config.getBoardSize());
        this.cpuBoard = new Board(config.getBoardSize());
        this.cpuEngine = new HuntStrategy();
        this.rng = (config.getGameSeed() != null && !config.getGameSeed().isEmpty()) ? 
                   new Random(Long.parseLong(config.getGameSeed())) : new Random();
    }

    public void initMatch() {
        if (config.isDbEnabled() && matchRepo != null) {
            this.matchId = matchRepo.createMatch(config.getGameSeed());
            matchRepo.registerPlayer(matchId, "Jogador Humano", "HUMAN");
            matchRepo.registerPlayer(matchId, "CPU Tracker", "CPU");
        }
        autoPlaceCpuFleet();
    }

    public Board getPlayerBoard() { return playerBoard; }
    public Board getCpuBoard() { return cpuBoard; }
    public boolean isPlayerTurn() { return playerTurn; }
    public boolean isActive() { return active; }

    public void autoPlacePlayerFleet() { autoPlaceFleetOnBoard(playerBoard); }
    private void autoPlaceCpuFleet() { autoPlaceFleetOnBoard(cpuBoard); }

    private void autoPlaceFleetOnBoard(Board b) {
        int[] sizes = config.getFleetSizes();
        String[] names = config.getFleetNames();
        for (int i = 0; i < sizes.length; i++) {
            boolean placed = false;
            int tries = 0;
            while (!placed && tries < 5000) {
                tries++;
                boolean horiz = rng.nextInt(2) == 0;
                int x = rng.nextInt(b.getSize());
                int y = rng.nextInt(b.getSize());
                if (FleetValidator.canPlaceStatic(b, x, y, sizes[i], horiz)) {
                    Ship ship = new Ship(i, names[i], sizes[i]);
                    b.placeShipDirectly(ship, x, y, horiz);
                    placed = true;
                }
            }
        }
    }

    public ShotResult executePlayerShot(Coordinate coord) {
        ShotResult res = cpuBoard.receiveShot(coord);
        if (matchRepo != null && matchId != -1) {
            matchRepo.saveShot(matchId, currentTurn++, "HUMAN", coord, res.getType().name());
        }
        if (cpuBoard.getAliveShipsCount() == 0) {
            active = false;
            if (matchRepo != null) matchRepo.endMatch(matchId, "HUMAN");
        } else {
            playerTurn = false;
        }
        return res;
    }

    public ShotResult executeCpuShot() {
        Coordinate target = cpuEngine.chooseTarget(playerBoard.getGrid(), rng);
        ShotResult res = playerBoard.receiveShot(target);
        if (matchRepo != null && matchId != -1) {
            matchRepo.saveShot(matchId, currentTurn++, "CPU", target, res.getType().name());
        }
        if (playerBoard.getAliveShipsCount() == 0) {
            active = false;
            if (matchRepo != null) matchRepo.endMatch(matchId, "CPU");
        } else {
            playerTurn = true;
        }
        return res;
    }
}