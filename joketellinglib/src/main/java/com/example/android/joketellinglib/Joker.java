package com.example.android.joketellinglib;

import java.util.Random;

public class Joker {

    private static String[] jokesArray = {
            "Why do we tell actors to break a leg? Because every play has a cast.",
            "Did you hear about the mathematician who is afraid of negative numbers? He will stop at nothing to avoid them. ",
            "I put my root beer in a square glass. Now it's just beer.",
            "Did you hear about the actor who fell through the floorboards? He was just going through a stage.",
            "Did you hear about the claustrophobic astronaut? He just needed a little space."};

    public String newJoke() {
        int randomNumber = new Random().nextInt(jokesArray.length);
        return jokesArray[randomNumber];
    }
}
