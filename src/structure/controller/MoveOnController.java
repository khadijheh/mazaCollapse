package structure.controller;

import structure.models.Move;
import structure.models.State;
import logic.LogicME;

public class MoveOnController {
    public State move(State currentState, Move move) {
        if (new MoveCheckController().isMoveValid(currentState, move)) {
            return LogicME.applyMove(currentState, move);
        }
        return currentState;
    }
}