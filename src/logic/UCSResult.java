package logic;

import structure.models.State;

import java.util.List;

 public class UCSResult {
    public List<State> path;
    public int expandedStatesCount;
    public int generatedStatesCount;

    public UCSResult(List<State> path, int expandedStatesCount, int generatedStatesCount) {
        this.path = path;
        this.expandedStatesCount = expandedStatesCount;
        this.generatedStatesCount = generatedStatesCount;
    }
}
