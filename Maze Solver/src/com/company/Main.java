package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
/*
*@ Author Dominik Banialis
*
* Maze solver program using Depth First Search Algorithm.
* Compatible with wrap and sparse maze.
* After running user will be prompted to enter file patch and name (example below in printout line 32)
* When finished with maze program will loop and ask for another file or -1 to terminate program.
*
*/

public class Main {

    private Maze testMaze;

    public static void main(String[] args) {
        Main main = new Main();
        main.startMaze();
    }

    private void startMaze() {
        Scanner reader = new Scanner(System.in);
        String fileLocation;
        System.out.print("Enter exact maze file location (/i.e. E:\\Users\\Matthew\\Desktop\\files\\Samples.txt): ");
        while (!(fileLocation = reader.nextLine()).equals("-1")) {

            ArrayList<String> mazeRaw = new ArrayList<>();
            File file = new File(fileLocation);
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String st;
                while ((st = br.readLine()) != null) {
                    mazeRaw.add(st);
                }
                testMaze = new Maze(mazeRaw); //2D Int array maze constructor
                String[][] results = new String[testMaze.getMaze2D().length][testMaze.getMaze2D()[0].length];
                Node p = solveMaze(testMaze, testMaze.getStart()); //Node p contains p.parent that hold the path through the maze.
                testMaze.printMazeResult(results, p, testMaze.getStart());
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
            System.out.println("Select another file or type -1 to close the program");
        }
        System.out.println("Bye :)");
        reader.close();
    }

    private Node solveMaze(Maze maze, Node start) {
        Stack<Node> mPath = new Stack<>(); // Stack is used for Depth First Search Algorithm
        mPath.push(new Node(start.getX(), start.getY(), null)); // Adds the starting coordinates to the stack.
        while (!mPath.isEmpty()) { // Until the stack is empty continue loop.

            Node p = mPath.pop(); // Get the top element from the Stack and assign it to p

            // Valid States //-------------------------------------------------------------------------------// 

            //If Exit found at coordinates p.x,p.y in 2Dint Array return the p Node.
            if (maze.getMaze2D()[p.getX()][p.getY()] == testMaze.getEndCell()) {
                return p;
            }

            //If x,y location cannot be wrapped on either ends then check if its sparse for formatting purposes. 
            if (!isWrappable(maze, p) && isSparse(maze, p)) {
                maze.getMaze2D()[p.getX()][p.getY()] = testMaze.getVisitedCell();
                Node nextP = new Node(p.getX() + 1, p.getY(), p);
                mPath.push(nextP);
                continue;
            }

            //Check if p node is at the border and if the border wraps around the array in a valid way and make sure that the node parent is not at border to avoid loops
            if (isWrappingBorder(maze, p.getX(), p.getY(), p) != null && isWrappingBorder(maze, p.getParent().getX(), p.getParent().getY(), p.getParent()) == null) {
                mPath.push(isWrappingBorder(maze, p.getX(), p.getY(), p));
            }

            // 4 Directional Movements //-------------------------------------------------------------------------------//

            // South
            if (isFree(maze, p.getX() + 1, p.getY())) {
                maze.getMaze2D()[p.getX()][p.getY()] = testMaze.getVisitedCell();
                Node nextP = new Node(p.getX() + 1, p.getY(), p);
                mPath.push(nextP);
            }

            // North
            if (isFree(maze, p.getX() - 1, p.getY())) {
                maze.getMaze2D()[p.getX()][p.getY()] = testMaze.getVisitedCell();
                Node nextP = new Node(p.getX() - 1, p.getY(), p);
                mPath.push(nextP);
            }

            // West
            if (isFree(maze, p.getX(), p.getY() + 1)) {
                maze.getMaze2D()[p.getX()][p.getY()] = testMaze.getVisitedCell();
                Node nextP = new Node(p.getX(), p.getY() + 1, p);
                mPath.push(nextP);
            }

            // East
            if (isFree(maze, p.getX(), p.getY() - 1)) {
                maze.getMaze2D()[p.getX()][p.getY()] = testMaze.getVisitedCell();
                Node nextP = new Node(p.getX(), p.getY() - 1, p);
                mPath.push(nextP);
            }
        }
        return null; // If Maze cannot be solved value null is returned;
    }


    //This method checks if the coordinates adjacent to the Node p are empty to detect sparse input for formatting purposes
    private boolean isSparse(Maze maze, Node p) {
        return maze.getMaze2D()[p.getX()][p.getY() - 1] == maze.getEmptyCell() && maze.getMaze2D()[p.getX() + 1][p.getY()] == maze.getEmptyCell()
                && maze.getMaze2D()[p.getX() + 1][p.getY() - 1] == maze.getEmptyCell() || maze.getMaze2D()[p.getX() + 1][p.getY()] == maze.getEndCell();
    }

    //Checks if the array can be wrapped around at either ends and direction from the current Node p.
    private boolean isWrappable(Maze maze, Node p) {
        return maze.getMaze2D()[0][p.getY()] == maze.getEmptyCell()
                || maze.getMaze2D()[p.getX()][0] == maze.getEmptyCell()
                || maze.getMaze2D()[maze.getMaze2D().length - 1][p.getY()] == maze.getEmptyCell()
                || maze.getMaze2D()[p.getX()][maze.getMaze2D()[0].length - 1] == maze.getEmptyCell();
    }


    //Detect the point where Array wraps around itself and return a Node that comes out on the other side of the wrapped array. If border is not wraping returns null
    private Node isWrappingBorder(Maze maze, int x, int y, Node parent) {

        if (x == maze.getEmptyCell() && maze.getMaze2D()[x][y] != 1) {
            if (maze.getMaze2D()[maze.getMaze2D().length - 1][y] != 1) {
                return new Node(maze.getMaze2D().length - 1, y, parent);
            }
        }

        if (x == maze.getMaze2D().length - 1 && maze.getMaze2D()[x][y] != 1) {
            if (maze.getMaze2D()[0][y] != 1) {
                return new Node(maze.getEmptyCell(), y, parent);
            }
        }

        if (y == maze.getEmptyCell() && maze.getMaze2D()[x][y] != 1) {
            if (maze.getMaze2D()[x][maze.getMaze2D().length - 1] != 1) {
                return new Node(x, maze.getMaze2D().length - 1, parent);
            }
        }

        if (y == maze.getMaze2D()[0].length - 1 && maze.getMaze2D()[x][y] != 1) {
            if (maze.getMaze2D()[x][0] != 1) {
                return new Node(x, maze.getEmptyCell(), parent);
            }
        }
        return null;
    }

    //Checks if int x, and int y which are passed from Node.x and Node.y values are valid coordinates
    private boolean isFree(Maze maze, int x, int y) {
        return (x >= maze.getEmptyCell() && x < maze.getMaze2D().length) &&
                (y >= maze.getEmptyCell() && y < maze.getMaze2D()[x].length) &&
                (maze.getMaze2D()[x][y] == maze.getEmptyCell() || maze.getMaze2D()[x][y] == maze.getEndCell());
    }
}