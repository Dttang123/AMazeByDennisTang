package com.example.amazebydennistang.gui;
import com.example.amazebydennistang.generation.Maze;

public class DataHolder {
    private static Maze maze;
    private static Maze getMaze(){
        return maze;
    }
    public static void setMaze(Maze maze){
        DataHolder.maze = maze;
    }
}
