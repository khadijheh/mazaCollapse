package structure.controller;

import structure.models.Move;
import structure.models.State;
import logic.LogicME;


public class MoveCheckController {

    public boolean isMoveValid(State state, Move move) {
        return LogicME.getPossibleMoves(state).contains(move);
    }
}