package logic;

import structure.models.State;
import structure.models.Move;
import structure.models.Position;
import java.util.*;

public class HillClimbing {
    private static final Position GOAL_POSITION = new Position(1, 6);
    public static UCSResult findSolution(State initialState) {
        State currentState = initialState;
        int maxIterations = 1000;
        int iterations = 0;

        int nodesExpanded = 0;
        int nodesGenerated = 1;

        while (iterations < maxIterations) {
            iterations++;

            if (LogicME.isGoal(currentState)) {
                System.out.println("Goal found in Hill Climbing.");
                List<State> path = LogicME.getPath(currentState);
                return new UCSResult(path, nodesExpanded, nodesGenerated);
            }

            Set<Move> possibleMoves = LogicME.getPossibleMoves(currentState);
            if (possibleMoves.isEmpty()) {
                break;
            }

            State bestNeighbor = null;
            double bestHeuristic = Double.MAX_VALUE;

            for (Move move : possibleMoves) {
                State neighbor = LogicME.applyMove(currentState, move);
                nodesGenerated++;
                double neighborHeuristic = LogicME.calculateHeuristic(neighbor.getPlayerPosition(), GOAL_POSITION);
                if (neighborHeuristic < bestHeuristic) {
                    bestHeuristic = neighborHeuristic;
                    bestNeighbor = neighbor;
                }
                else if (neighborHeuristic == bestHeuristic) {
                    if (bestNeighbor == null || Math.random() < 0.5) {
                        bestNeighbor = neighbor;
                    }
                }
            }

            double currentHeuristic = LogicME.calculateHeuristic(currentState.getPlayerPosition(), GOAL_POSITION);
            if (bestNeighbor != null) {
                if (bestHeuristic <= currentHeuristic) {
                    currentState = bestNeighbor;
                    nodesExpanded++;
                }
                else {
                    System.out.println("Reached local peak/plateau. Hill Climbing stops.");
                    break;
                }
            } else {
                break;
            }
        }

        List<State> path = LogicME.getPath(currentState);
        return new UCSResult(path, nodesExpanded, nodesGenerated);
    }
}