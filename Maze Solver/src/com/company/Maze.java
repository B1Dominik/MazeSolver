package com.company;

import java.util.ArrayList;


class Maze {
    private int[][] maze2D;
    private Node start;

    private final int emptyCell = 0;
    private final int endCell = 9;
    private final int visitedCell = -1;
    private final int startCell = 8;

    Maze(ArrayList<String> mazeString) {

        // Assign maze size from the file.
        String[] dimensionData = mazeString.get(0).split(" ");
        int width = Integer.valueOf(dimensionData[0]);
        int height = Integer.valueOf(dimensionData[1]);
        this.maze2D = new int[height][width];
        this.start = new Node(Integer.valueOf(mazeString.get(1).split(" ")[1]), Integer.valueOf(mazeString.get(1).split(" ")[0]), null);
        Node end = new Node(Integer.valueOf(mazeString.get(2).split(" ")[1]), Integer.valueOf(mazeString.get(2).split(" ")[0]), null);

        for (int i = 0; i < this.maze2D.length; i++) {
            String[] lineData = mazeString.get(i + 3).split(" "); // i + 3 to offset first 3 lines of text file.
            for (int j = 0; j < this.maze2D[i].length; j++) {
                this.maze2D[i][j] = Integer.valueOf(lineData[j]);
            }
        }
        this.maze2D[start.getX()][start.getY()] = startCell; // Assign maze start from the file.
        this.maze2D[end.getX()][end.getY()] = endCell; // Assign maze end from the file.
    }

    void printMazeResult(String[][] results, Node p, Node start) {
        boolean mazeIsSolved=false;

        for (int i = 0; i < getMaze2D().length; i++) {
            for (int j = 0; j < getMaze2D()[i].length; j++) {
                switch (maze2D[i][j]) {
                    case 0:
                        results[i][j] = " ";
                        break;
                    case 1:
                        results[i][j] = "#";
                        break;
                    case -1:
                        results[i][j] = " ";
                        break;
                    case 9:
                        results[i][j] = "E";
                        break;
                }
            }
        }

        try {
            while (p.getParent() != null) {
                p = p.getParent();
                if (maze2D[p.getX()][p.getY()] == endCell) {
                    continue;
                }
                results[p.getX()][p.getY()] = "X";
            }
            results[start.getY()][start.getX()] = "S";
            mazeIsSolved = true;
        } catch (java.lang.NullPointerException ignored) {

        }
        //results is array of strings
        for (String[] result : results) {
            for (String s : result) {
                System.out.print(s);
            }
            System.out.println();
        }
        if (mazeIsSolved) {
            System.out.println("--- Maze has been solved ---");
        } else {
            System.out.println("--- Maze is unsolvable ---");
        }
    }

    Node getStart() {
        return start;
    }

    int[][] getMaze2D() {
        return maze2D;
    }

    int getEndCell() {
        return endCell;
    }

    int getEmptyCell() {
        return emptyCell;
    }

    int getVisitedCell() {
        return visitedCell;
    }
}