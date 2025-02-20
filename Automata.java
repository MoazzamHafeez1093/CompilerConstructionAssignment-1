package CompConstructionPackage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
public class Automata {
    private Map<String, Map<Character, String>> transitions;
    private String initialState;
    private Set<String> finalStates;//stores set of valid final states

    public Automata() {
        transitions = new HashMap<>();
        finalStates = new HashSet<>();
    }
    //Adding transition from fromState to toState when input is read
    public void addTransition(String fromState, char input, String toState) {
        transitions.putIfAbsent(fromState, new HashMap<>());//Ensuring if state exists if not then adding it
        transitions.get(fromState).put(input, toState);
    }

    public void setInitialState(String state) {
        this.initialState = state;
    }

    public void addFinalState(String state) {
        finalStates.add(state);
    }
    //Ftn to check if a given state has a transition for the input character
    public boolean hasTransition(String state, char input) {
        return transitions.containsKey(state) && transitions.get(state).containsKey(input);
    }
    //ftn to get next state based on curr state and input
    public String getNextState(String state, char input) {
        return transitions.get(state).get(input);
    }

    public boolean isFinalState(String state) {
        return finalStates.contains(state);
    }

    public String getInitialState() {
        return initialState;
    }

    public void displayTransitionTable() {
        System.out.println("State Transition Table:");
        for (Map.Entry<String, Map<Character, String>> entry : transitions.entrySet()) {
            System.out.print(entry.getKey() + " -> {");
            for (Map.Entry<Character, String> transition : entry.getValue().entrySet()) {
                System.out.print(transition.getKey() + "=" + transition.getValue() + ", ");
            }
            System.out.println("}");
        }
    }
}

