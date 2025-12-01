package logic;
// Dijkstra.java

import structure.models.Move;
import structure.models.State;

import java.util.*;

public class Dijkstra {
    public static List<State> findShortestPath(State initialState) {
        PriorityQueue<State> frontier = new PriorityQueue<>(Comparator.comparingDouble(State::getAccumulatedCost));
        Set<State> visited = new HashSet<>();
        frontier.offer(initialState);
        visited.add(initialState);
        while (!frontier.isEmpty()) {
            State currentState = frontier.poll();
            if (LogicME.isGoal(currentState)) {
                return LogicME.getPath(currentState);
            }
            for (Move move : LogicME.getPossibleMoves(currentState)) {
                State nextState = LogicME.applyMove(currentState, move);
                if (!visited.contains(nextState)) {
                    visited.add(nextState);
                    frontier.offer(nextState);
                }
            }
        }
        return null;
    }
}
/*
*    PriorityQueue<State> queue = new PriorityQueue<>(
            (s1, s2) -> Double.compare(s1.getCost(), s2.getCost())
        );

        Set<State> visited = new HashSet<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            State current = queue.poll();

            // إذا وصلنا للهدف
            if (current.isGoal()) {
                return buildPath(current);
            }

            // جرب كل الاحتمالات
            for (Move move : current.getPossibleMoves()) {
                State next = current.applyMove(move);

                if (!visited.contains(next)) {
                    visited.add(next);
                    queue.offer(next);
                }
            }
        }

        return null; // ما في حل
    }*/