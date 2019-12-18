package com.automata;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class has the scope to retain multiple states, who are unique for every text-field available in the SingUpInterface;
 * With this class, I am verifying the current states(if it is final or not), if the current character get me to
 * the another state or it is corresponding to my actual one;
 */
class Transition {
    private ArrayList<State> states;
    private int index;

    Transition(State...states){
        this.states=new ArrayList<>();
        this.states.addAll(Arrays.asList(states)); //convert states into a list to be added with the addAll method;
        index=0;
    }

    boolean nextState(char c){
        if((c+"").matches(this.states.get(index).getTransition())) {
            ++index;
            return true;
        }
        return false;
    }

    boolean verifyCurrentState(char check) {
        return states.get(index).checkPattern(check);
    }

    boolean currentState(){
        return this.states.get(index).isFinalState();
    }
}
