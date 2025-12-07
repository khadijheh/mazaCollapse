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
            double currentCost = currentState.getAccumulatedCost();

            if (LogicME.isGoal(currentState)) {
                List<State> path = LogicME.getPath(currentState);
                return new UCSResult(path, nodesExpanded, generatedStates);
            }

            Set<Move> possibleMoves = LogicME.getPossibleMoves(currentState);
            for (Move move : possibleMoves) {
                State nextState = LogicME.applyMove(currentState, move);
                generatedStates++;

                double newCost = nextState.getAccumulatedCost();

                if (!visitedWithMinCost.containsKey(nextState) || newCost < visitedWithMinCost.get(nextState)) {

                    visitedWithMinCost.put(nextState, newCost);
                    frontier.add(nextState);
                }
            }
        }

        return new UCSResult(null, nodesExpanded, generatedStates);
    }
}