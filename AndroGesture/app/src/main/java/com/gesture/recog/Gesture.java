package com.gesture.recog;

import java.io.Serializable;

public class Gesture implements Serializable {
    String name;
    String command;

    public Gesture(String name, String command) {
        this.name = name;
        this.command = command;
    }
}
