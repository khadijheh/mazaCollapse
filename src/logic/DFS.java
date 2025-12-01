package logic;

import structure.models.Move;
import structure.models.State;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class DFS {
    static int generated_count = 0;
    static int visit_count = 0;

    public static List<State> Dfs(State initialState) {
        Stack<State> stack = new Stack<>();
         int generated_count = 0;
         int visit_count = 0;
        Set<State> visited = new HashSet<>();
        stack.push(initialState);
        visited.add(initialState);
        while (!stack.empty()) {
            State current = stack.pop();
            generated_count += 1;
            if (LogicME.isGoal(current)) {
                System.out.printf("count visit///// " + visit_count + "---count generated//// " +generated_count+"\n");
                return LogicME.getPath(current);
            }
            for (Move move : LogicME.getPossibleMoves(current)) {
                State next = LogicME.applyMove(current, move);
                if (!visited.contains(next)) {
                    visited.add(next);
                    stack.push(next);
                }if (visited.contains(next)){ visit_count += 1;

                }
            }
        }
        return null;
    }
}

