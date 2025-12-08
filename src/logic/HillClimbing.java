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

            double currentHeuristic = LogicME.calculateHeuristic(currentState);
            System.out.println("\n--- HILL CLIMBING ITERATION #" + iterations + " ---");
            System.out.printf("CURRENT State h(n) = %.1f:\n", currentHeuristic);

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

            System.out.println("  > Evaluating neighbors (Seeking lowest h(n)):");

            for (Move move : possibleMoves) {
                State neighbor = LogicME.applyMove(currentState, move);
                nodesGenerated++;

                double neighborHeuristic = LogicME.calculateHeuristic(neighbor);

                System.out.printf("    | Neighbor %s h(n) = %.1f\n", move.name(), neighborHeuristic);

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

            if (bestNeighbor != null) {
                if (bestHeuristic <= currentHeuristic) {
                    System.out.printf("  > DECISION: MOVE to %s (Best h=%.1f is <= Current h=%.1f)\n",
                            bestNeighbor.getMove().name(), bestHeuristic, currentHeuristic);
                    currentState = bestNeighbor;
                    nodesExpanded++;
                }
                else {
                    System.out.printf("  > DECISION: STOP (Best h=%.1f is > Current h=%.1f). Reached local peak.\n",
                            bestHeuristic, currentHeuristic);
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