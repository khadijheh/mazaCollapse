package structure.controller;

import logic.*;
import structure.levels.MazeLevels;
import structure.models.Move;
import structure.models.State;

import java.io.IOException;
import java.util.*;

public class UserPlay {
    private final mazeController gameController;
    private State currentState;
    private final Scanner scanner;
    private final Stack<State> historyStack;

    public UserPlay() {
        this.gameController = new mazeController();
        this.scanner = new Scanner(System.in);
        this.historyStack = new Stack<>();
    }

    public void start() throws IOException {
        System.out.println("Starting Logic Maze Collapse Game.........with ITE khadijhe");
        currentState = MazeLevels.loadLevel1();
        System.out.println("Initial State:   " + currentState);
        LogicME.printCostGrid(currentState, 10, 10);
        System.out.println("Do you want to play manually (M) or solve automatically by algorthm?");
        String mode = scanner.nextLine().toUpperCase();
        historyStack.push(currentState);
//        if (mode.equals("BFS")) {
//            selectedAlgorithm("Starting Automatic BFS...", BFS.Bfs(currentState), " No solution found for this level.");
//
//        }
//        else if (mode.equals("DFS")) {
//            selectedAlgorithm("Starting Automatic DFS ...", DFS.Dfs(currentState), " No solution found for this level.");
//        }
        if (mode.equals("DI")) {
            selectedAlgorithm("Starting Automatic  Dijkstra...", Dijkstra.findShortestPath(currentState), "No solution found for this level.");
        } else if (mode.equals("UCS")) {
            selectedAlgorithm("Starting Automatic Uniform Cost Search...", UCS.findShortestPath(currentState), "No solution found for this level.");
        }
        else if (mode.equals("AS")) {
            selectedAlgorithm("Starting Automatic A* Search...", A_Star.findShortestPath(currentState), " No solution found for this level.");
        }
        else {
            while (true) {
                if (gameController.isGameWon(currentState)) {
                    System.out.println("**********CONGRATULATIONS! You won the game!************");
                    printPath(currentState);
                    break;
                }
                if (gameController.isGameOver(currentState)) {
                    System.out.println("**********GAME OVER! No possible moves left.************");
                    break;
                }
                System.out.println("Enter move --U,D,L,R -- or 'Q' to quit:");
                String input = scanner.nextLine().toUpperCase();
                if (input.equals("Q")) {
                    break;
                }
                if (input.equals("Z"))
                    if (historyStack.size() > 1) {
                        historyStack.pop();
                        currentState = historyStack.peek();
                        System.out.println("Undo successful. Current State:   " + currentState);
                    } else {
                        System.out.println("Cannot undo: This is the initial state.");
                    }
                Move move = parseMove(input);
                if (move == null) {
                    System.out.println("Invalid input. Please use U, D, L, or R.");
                    continue;
                }
                if (gameController.checkMove(currentState, move)) {
                    State nextState = gameController.applyMove(currentState, move);
                    historyStack.push(nextState);
                    currentState = nextState;
                    System.out.println("Move executed. New State:   " + currentState);
                } else {
                    System.out.println("Invalid move! Cannot move " + move.name() + " from current position.");
                }
            }
        }
    }

    private void selectedAlgorithm(String x, List<State> currentState, String x1) {
        System.out.println(x);
        List<State> path = currentState;
        if (path != null) {
            System.out.println("-------------------------------");
            System.out.println("CONGRATULATIONS! Solution Found!");
            printPath(path.get(path.size() - 1));
        } else {
            System.out.println("-------------------------------");
            System.out.println(x1);
        }
    }

    private Move parseMove(String input) {
        return switch (input) {
            case "U" -> Move.UP;
            case "D" -> Move.DOWN;
            case "L" -> Move.LEFT;
            case "R" -> Move.RIGHT;
            default -> null;
        };
    }

    private void printPath(State goalState) {
        System.out.println("\n--- Solution Path (Sequence of Moves) ---");
        List<State> path = LogicME.getPath(goalState);
        for (int i = 0; i < path.size(); i++) {
            State state = path.get(i);
            if (i == 0) {
                System.out.println("Step 0: START");
            } else {
                System.out.println("Step " + i + ": Move " + state.getMove().name());
            }
            System.out.println(state);
        }
//        System.out.println("\n--- Solution Summary -----------------------------------------------");
        System.out.println("\n----------------");
        System.out.println("Total Visit of Cell: " + (path.size() - 1));
        System.out.println("Total Cost " + goalState.getAccumulatedCost());
    }
}