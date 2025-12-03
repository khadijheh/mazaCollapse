package logic;

import structure.models.State;
import structure.models.Move;

import java.util.*;

public class UCS {

    public static List<State> findShortestPath(State initialState) {
        PriorityQueue<State> frontier = new PriorityQueue<>();
        Map<State, Double> visitedWithMinCost = new HashMap<>();
        frontier.add(initialState);
        visitedWithMinCost.put(initialState, initialState.getAccumulatedCost());
        while (!frontier.isEmpty()) {
            State currentState = frontier.poll();
            double currentCost = currentState.getAccumulatedCost();

//            System.out.println("-> POLLED (Cost " + String.format("%.1f", currentCost) + "): " + currentState.getPlayerPosition() + " | Keys: " + currentState.getCollectedKeys().size());
            if (LogicME.isGoal(currentState)) {
                return LogicME.getPath(currentState);
            }

            Set<Move> possibleMoves = LogicME.getPossibleMoves(currentState);
            for (Move move : possibleMoves) {
                State nextState = LogicME.applyMove(currentState, move);
                double newCost = nextState.getAccumulatedCost();
                if (!visitedWithMinCost.containsKey(nextState) || newCost < visitedWithMinCost.get(nextState)) {

                    visitedWithMinCost.put(nextState, newCost);
                    frontier.add(nextState);
                }
            }
        }

        return null;
    }
}