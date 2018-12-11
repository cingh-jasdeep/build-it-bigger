package com.example.android.jokeprovider;

public class JokeProvider {
    private String[] awesomeJokes =
            {"An Android app walks into a bar. Bartender asks, \"Can I get you a drink?\" The app looks disappointed and says, \"That wasn't my intent.\"",
             "A pregnant fragment walks into a bar. The bartender says, \"Whoa! Whoa! We don't support nested fragments here!\"",
             "A fragment walks into a bar, and the bartender asks for an ID. Fragment says, \"I don't have an ID.\" So the bartender says, \"Okay, I'll make a NullPointerException.\"",
            "\"There are 10 kinds of people in the world: Those that know binary & those that don't\""};

    //https://stackoverflow.com/questions/5887709/getting-random-numbers-in-java
    public String getJoke(){
        return awesomeJokes[(int)(Math.random() * 3)];
    }
}
