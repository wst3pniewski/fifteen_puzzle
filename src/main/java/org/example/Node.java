package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {

    private int columns;
    private int rows;
    private int depth;
    private char directionLetter;
    private int emptyTileIndex;

    private ArrayList<Integer> gameBoard;
    private List<Node> children;
    private Node parent;
    public Step lastStep;

    // Constructors
    public Node(ArrayList<Integer> gameBoard, int columns, int rows) {
        this.gameBoard = gameBoard;
        this.columns = columns;
        this.rows = rows;
        this.depth = 0;
        this.children = new ArrayList<>();
        this.emptyTileIndex = this.gameBoard.indexOf(0);
    }

    // Tiles movement
    public void moveTile(int oldPosition, int newPosition, char directionLetter) {
        ArrayList<Integer> newGameBoard = this.arrayListCopy(this.gameBoard);
        newGameBoard.set(newPosition, this.gameBoard.get(oldPosition));
        newGameBoard.set(oldPosition, this.gameBoard.get(newPosition));
        Node childNode = new Node(newGameBoard, this.columns, this.rows);
        childNode.setParent(this);
        childNode.setDepth(this.depth + 1);
        childNode.setDirectionLetter(directionLetter);
        this.children.add(childNode);
    }

    public void moveTileLeft(int movedElementIndex) {
        if (movedElementIndex % this.columns > 0) {
            this.moveTile(movedElementIndex, movedElementIndex - 1, 'L');
        }
    }

    public void moveTileRight(int movedElementIndex) {
        if (movedElementIndex % this.columns < this.columns - 1) {
            this.moveTile(movedElementIndex, movedElementIndex + 1, 'R');
        }
    }

    public void moveTileUp(int movedElementIndex) {
        if (movedElementIndex - this.columns >= 0) {
            this.moveTile(movedElementIndex, movedElementIndex - this.columns, 'U');
        }
    }

    public void moveTileDown(int movedElementIndex) {
        if (movedElementIndex + this.rows < this.gameBoard.size()) {
            this.moveTile(movedElementIndex, movedElementIndex + this.columns, 'D');
        }
    }

    public void createChildren() {
        this.emptyTileIndex = this.gameBoard.indexOf(0);
        moveTileLeft(emptyTileIndex);
        moveTileRight(emptyTileIndex);
        moveTileUp(emptyTileIndex);
        moveTileDown(emptyTileIndex);
    }

    // Creates children of node in order given by creationOrder
    public void createChildrenInOrder(char[] creationOrder) throws IllegalArgumentException {
        this.emptyTileIndex = this.gameBoard.indexOf(0);
        for (int i = 0; i < creationOrder.length; i++) {
            switch (creationOrder[i]) {
                case 'L':
                    moveTileLeft(emptyTileIndex);
                    break;
                case 'R':
                    moveTileRight(emptyTileIndex);
                    break;
                case 'U':
                    moveTileUp(emptyTileIndex);
                    break;
                case 'D':
                    moveTileDown(emptyTileIndex);
                    break;
                default:
                    throw new IllegalArgumentException("Podano nieprawidlowy kierunek!");
            }
        }
    }

    public boolean isBoardSolved() {
        for (int i = 0; i < this.gameBoard.size() - 1; i++) {
            if (this.gameBoard.get(i) != i + 1) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Integer> arrayListCopy(ArrayList<Integer> originalList) {
        ArrayList<Integer> arrCopy = new ArrayList<>(originalList);
        return arrCopy;
    }

    public boolean makeStep(char directionLetter) {
        switch (directionLetter) {
            case 'L':
                if (emptyTileIndex % this.columns > 0) {
                    int tmp = this.gameBoard.get(emptyTileIndex - 1);
                    this.gameBoard.set(emptyTileIndex - 1, 0);
                    this.gameBoard.set(emptyTileIndex, tmp);
                    this.emptyTileIndex = emptyTileIndex - 1;
                } else {
                    return false;
                }
                break;
            case 'R':
                if (emptyTileIndex % this.columns < this.columns - 1) {
                    int tmp = this.gameBoard.get(emptyTileIndex + 1);
                    this.gameBoard.set(emptyTileIndex + 1, 0);
                    this.gameBoard.set(emptyTileIndex, tmp);
                    this.emptyTileIndex = emptyTileIndex + 1;
                } else {
                    return false;
                }
                break;
            case 'U':
                if (emptyTileIndex - this.columns > 0) {
                    int tmp = this.gameBoard.get(emptyTileIndex - this.columns);
                    this.gameBoard.set(emptyTileIndex - this.columns, 0);
                    this.gameBoard.set(emptyTileIndex, tmp);
                    this.emptyTileIndex = emptyTileIndex - this.columns;
                } else {
                    return false;
                }
                break;
            case 'D':
                if (emptyTileIndex + this.columns < this.gameBoard.size()) {
                    int tmp = this.gameBoard.get(emptyTileIndex + this.columns);
                    this.gameBoard.set(emptyTileIndex + this.columns, 0);
                    this.gameBoard.set(emptyTileIndex, tmp);
                    this.emptyTileIndex = emptyTileIndex + this.columns;
                } else {
                    return false;
                }
                break;
        }
        this.depth++;
        Step newStep = new Step();
        newStep.stepBefore = this.lastStep;
        newStep.direction = directionLetter;
        this.lastStep = newStep;
        return true;
    }

    public void undoStep() {
        if (this.lastStep == null) {
            return;
        }
        int tmp;
        switch (this.lastStep.direction) {
            case 'R':
                tmp = this.gameBoard.get(emptyTileIndex - 1);
                this.gameBoard.set(emptyTileIndex - 1, 0);
                this.gameBoard.set(emptyTileIndex, tmp);
                this.emptyTileIndex = emptyTileIndex - 1;
                break;
            case 'L':
                tmp = this.gameBoard.get(emptyTileIndex + 1);
                this.gameBoard.set(emptyTileIndex + 1, 0);
                this.gameBoard.set(emptyTileIndex, tmp);
                this.emptyTileIndex = emptyTileIndex + 1;
                break;
            case 'U':
                tmp = this.gameBoard.get(emptyTileIndex + this.columns);
                this.gameBoard.set(emptyTileIndex + this.columns, 0);
                this.gameBoard.set(emptyTileIndex, tmp);
                this.emptyTileIndex = emptyTileIndex + this.columns;
                break;
            case 'D':
                tmp = this.gameBoard.get(emptyTileIndex - this.columns);
                this.gameBoard.set(emptyTileIndex - this.columns, 0);
                this.gameBoard.set(emptyTileIndex, tmp);
                this.emptyTileIndex = emptyTileIndex - this.columns;
                break;
        }
        this.depth--;
        this.lastStep = this.lastStep.stepBefore;
    }

    public ArrayList<Character> getSteps() {
        ArrayList<Character> steps = new ArrayList<>();
        Step currentStep = this.lastStep;
        while (currentStep != null) {
            steps.add(currentStep.direction);
            currentStep = currentStep.stepBefore;
        }
        return steps;
    }

    // Heurestics
    public int getManhattanDistanceCost() {
        int cost = 0;
        for (int i = 0; i < this.gameBoard.size(); i++) {
            if (this.gameBoard.get(i) != 0) {
                int row = i / this.columns;
                int column = i % this.columns;
                int targetRow = (this.gameBoard.get(i) - 1) / this.columns;
                int targetColumn = (this.gameBoard.get(i) - 1) % this.columns;
                cost += Math.abs(row - targetRow) + Math.abs(column - targetColumn);
            }
        }
        return cost + this.depth;
    }

    public int getHammingDistanceCost() {
        int cost = 0;
        for (int i = 0; i < this.gameBoard.size(); i++) {
            if (this.gameBoard.get(i) != 0 && this.gameBoard.get(i) != i + 1) {
                cost++;
            }
        }
        return cost + this.depth;
    }

    // Getters and Setters
    public int getChildrenCount() {
        return children.size();
    }

    public Node getChildrenByIndex(int index) {
        if (index < children.size()) {
            return children.get(index);
        }
        return null;
    }

    public void setDirectionLetter(char directionLetter) {
        this.directionLetter = directionLetter;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ArrayList<Integer> getGameBoard() {
        return gameBoard;
    }

    public char getDirectionLetter() {
        return directionLetter;
    }

    public List<Node> getChildren() {
        ArrayList<Node> children = new ArrayList<>(this.children);
        this.children = null;
        return children;
    }
}
