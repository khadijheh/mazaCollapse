package logic;

import structure.models.State;
import structure.models.Move;

import java.util.*;

public class UCS {
    public static UCSResult findShortestPath(State initialState) {
        int expandedStatesCount = 0;
        int generatedStatesCount = 0;
        PriorityQueue<State> frontier = new PriorityQueue<>();
        Map<State, Double> visitedWithMinCost = new HashMap<>();

        frontier.add(initialState);
        visitedWithMinCost.put(initialState, initialState.getAccumulatedCost());
        while (!frontier.isEmpty()) {
            State currentState = frontier.poll();
            if (currentState.getAccumulatedCost() > visitedWithMinCost.get(currentState)) {
                continue;
            }
            expandedStatesCount++;
            if (LogicME.isGoal(currentState)) {
                List<State> path = LogicME.getPath(currentState);
                return new UCSResult(path, expandedStatesCount, generatedStatesCount);
            }
            Set<Move> possibleMoves = LogicME.getPossibleMoves(currentState);

            for (Move move : possibleMoves) {
                State nextState = LogicME.applyMove(currentState, move);

                generatedStatesCount++;
                double newCost = nextState.getAccumulatedCost();
                if (!visitedWithMinCost.containsKey(nextState) || newCost < visitedWithMinCost.get(nextState)) {
                    visitedWithMinCost.put(nextState, newCost);
                    frontier.add(nextState);

                }
            }

        }
        return new UCSResult(null, expandedStatesCount, generatedStatesCount);
    }
}
