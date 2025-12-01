package logic;

import structure.models.State;
import structure.models.Move;

import java.util.*;

public class A_Star {
    public static List<State> findShortestPath(State initialState) {
        PriorityQueue<State> frontier = new PriorityQueue<>();
        Map<State, Double> visitedWithMinCost = new HashMap<>();
        frontier.add(initialState);
        visitedWithMinCost.put(initialState, initialState.getAccumulatedCost());

        int nodesExpanded = 0;

        while (!frontier.isEmpty()) {
            State currentState = frontier.poll();
            nodesExpanded++;
            double currentCost = currentState.getAccumulatedCost();
            if (LogicME.isGoal(currentState)) {
                return LogicME.getPath(currentState);
            }

            Set<Move> possibleMoves = LogicME.getPossibleMoves(currentState);
            for (Move move : possibleMoves) {
                State nextState = LogicME.applyMove(currentState, move);
                double newCost = nextState.getAccumulatedCost(); // هذه هي قيمة g(n) الجديدة
                if (!visitedWithMinCost.containsKey(nextState) || newCost < visitedWithMinCost.get(nextState)) {

                    visitedWithMinCost.put(nextState, newCost);
                    frontier.add(nextState);
                }
            }
        }

        return null;
    }
}