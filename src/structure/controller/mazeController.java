package structure.controller;

import structure.models.Move;
import structure.models.State;
import logic.LogicME;
public class mazeController {
    private final MoveCheckController moveCheckController;
    private final MoveOnController moveOnController;

    public mazeController() {
        this.moveCheckController = new MoveCheckController();
        this.moveOnController = new MoveOnController();
    }

    public boolean checkMove(State state, Move move) {
        return moveCheckController.isMoveValid(state, move);
    }

    public State applyMove(State currentState, Move move) {
        return moveOnController.move(currentState, move);
    }

    public boolean isGameWon(State state) {
        return LogicME.isGoal(state);
    }

    public boolean isGameOver(State state) {
        return LogicME.getPossibleMoves(state).isEmpty() && !isGameWon(state);
    }
}