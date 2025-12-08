package logic;

import structure.models.State;
import structure.models.Move;

import java.util.*;

public class A_Star {

    public static UCSResult findShortestPath(State initialState) {
        PriorityQueue<State> frontier = new PriorityQueue<>();
        Map<State, Double> visitedWithMinCost = new HashMap<>();

        frontier.add(initialState);
        visitedWithMinCost.put(initialState, initialState.getAccumulatedCost());

        int nodesExpanded = 0;
        int generatedStates = 0;

        while (!frontier.isEmpty()) {
            State currentState = frontier.poll();
            nodesExpanded++;
            double h_current = LogicME.calculateHeuristic(currentState);
            double f_current = currentState.getAccumulatedCost() + h_current;
            System.out.println("\n--- EXPANDING STATE #" + nodesExpanded + " ---");
            System.out.printf("CURRENT (g=%.1f, h=%.1f, f=%.1f) via %s\n",
                    currentState.getAccumulatedCost(), h_current, f_current, currentState.getMove() != null ? currentState.getMove().name() : "START");

            if (LogicME.isGoal(currentState)) {
                List<State> path = LogicME.getPath(currentState);
                return new UCSResult(path, nodesExpanded, generatedStates);
            }

            if (currentState.getAccumulatedCost() > visitedWithMinCost.getOrDefault(currentState, Double.MAX_VALUE)) {
                System.out.printf("  > SKIPPED: Current path (g=%.1f) is worse than recorded minimum (g=%.1f)\n",
                        currentState.getAccumulatedCost(), visitedWithMinCost.get(currentState));
                continue;
            }

            Set<Move> possibleMoves = LogicME.getPossibleMoves(currentState);
            System.out.println("  > Generating " + possibleMoves.size() + " neighbors...");

            for (Move move : possibleMoves) {
                State nextState = LogicME.applyMove(currentState, move);
                generatedStates++;

                double newCost = nextState.getAccumulatedCost(); // g(n)
                double h_neighbor = LogicME.calculateHeuristic(nextState); // h(n)
                double f_neighbor = newCost + h_neighbor; // f(n)

                if (!visitedWithMinCost.containsKey(nextState) || newCost < visitedWithMinCost.get(nextState)) {

                    visitedWithMinCost.put(nextState, newCost);
                    frontier.add(nextState);
                    System.out.printf("    | Neighbor %s (g=%.1f, h=%.1f, f=%.1f). -> ADDED/UPDATED (Path improved/new)\n",
                            move.name(), newCost, h_neighbor, f_neighbor);

                } else {
                    System.out.printf("    | Neighbor %s (g=%.1f, h=%.1f, f=%.1f). -> IGNORED (g=%.1f not better than recorded minimum)\n",
                            move.name(), newCost, h_neighbor, f_neighbor, visitedWithMinCost.get(nextState));
                }
            }
        }

        return new UCSResult(null, nodesExpanded, generatedStates);
    }
}