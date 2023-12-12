package org.bingo.rpc;

import java.util.HashSet;
import java.util.Random;

public class Game {
    private static final Integer MaxRound = 10;
    private Integer round;
    private Integer gameScore;
    private HashSet<Integer> urn;
    public Game() {
        urn = new HashSet<Integer>();
        for(int i = 0;i < 9;i++)
            urn.add(i);
        gameScore = 0;
        round = 0;
    }
    public int GetGameScore() {
        return gameScore;
    }
    public boolean isEnded() { return round >= Game.MaxRound;}

    public Integer Guess(Integer guessedNumber) {
        if(isEnded()) return gameScore;

        Random random = new Random();
        int pickedNumber;
        do {
            pickedNumber = random.nextInt(0,10);
        } while(!urn.contains(pickedNumber));
        round++;
        if (pickedNumber != guessedNumber) return gameScore;
        gameScore++;
        urn.remove(pickedNumber);
        return gameScore;
    }
}
