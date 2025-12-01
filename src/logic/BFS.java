package logic;

import structure.models.Move;
import structure.models.State;

import java.util.*;

public class BFS {
    public static List<State> Bfs(State initialState) {
        Queue<State> frontier = new LinkedList<>();
        Set<State> visited = new HashSet<>();
        int generated_count = 0;
        int visit_count = 0;
        frontier.offer(initialState);
        visited.add(initialState);
        while (!frontier.isEmpty()) {
            State currentState = frontier.poll();
            generated_count += 1;
            if (LogicME.isGoal(currentState)) {
                System.out.printf("count visit///// " + visit_count + "---count generated//// " + generated_count + "\n");
                return LogicME.getPath(currentState);
            }
            for (Move move : LogicME.getPossibleMoves(currentState)) {
                State nextState = LogicME.applyMove(currentState, move);
                if (!visited.contains(nextState)) {
                    visited.add(nextState);
                    visit_count += 1;
                    frontier.offer(nextState);
                }
            }
        }
        return null;
    }

}
