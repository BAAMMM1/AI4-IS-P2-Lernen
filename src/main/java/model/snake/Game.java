package model.snake;


import javafx.scene.input.KeyCode;
import model.snake.clock.Clock;
import model.snake.entity.Field;
import model.snake.entity.Score;
import model.snake.entity.Snake;
import model.snake.entity.SnakeDirection;
import model.snake.event.BodyCollision;
import model.snake.event.Collision;
import model.snake.event.PickUpCollision;
import model.snake.event.WallColision;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Game extends Observable {


    private Clock clock;
    private Field  field;
    private Snake snake;
    private SnakeDirection inputDirection;
    private boolean run;
    private List<Collision> collisions;
    private Score score;

    public Game(int fieldColumns) {
        this.clock = new Clock();
        this.field = new Field(fieldColumns);
        this.snake = new Snake(field.getColumns());
        this.inputDirection = SnakeDirection.LEFT;
        this.score = new Score();
        this.collisions = new ArrayList<>();
        collisions.add(new PickUpCollision(field, snake, score));
        collisions.add(new WallColision(field, snake, score));
        collisions.add(new BodyCollision(field, snake, score));

        this.run = true;
    }

    public void run(){

        while(run){
            clock.tick();
            snake.move(inputDirection);

            for(Collision collision: collisions){
                if(collision.isCollision()){
                    collision.action();
                }
            }


            setChanged();
            notifyObservers();
        }

    }

    public void input(KeyCode code){

        if(code.equals(KeyCode.UP)) inputDirection = SnakeDirection.UP;
        else if(code.equals(KeyCode.DOWN)) inputDirection = SnakeDirection.DOWN;
        else if(code.equals(KeyCode.LEFT)) inputDirection = SnakeDirection.LEFT;
        else if(code.equals(KeyCode.RIGHT)) inputDirection = SnakeDirection.RIGHT;

    }

    public String visualization(){ // TODO in den Controller

        int[][] array = new int[field.getColumns()][field.getColumns()];

        array[snake.getHead().getY()][snake.getHead().getX()] = 1;

        for(int i = 1; i < snake.getTails().size(); i++){
            array[snake.getTails().get(i).getY()][snake.getTails().get(i).getX()] = 2;
        }

        array[field.getPickUp().getY()][field.getPickUp().getX()] = 3;

        String result = "";

        for(int y = 0; y < field.getColumns(); y++){

            for(int x = 0; x < field.getColumns(); x++){
                result = result + array[y][x] + "\t";
            }
            result = result + "\n";

        }

        return result;
    }

    public Field getField() {
        return field;
    }

    public Snake getSnake() {
        return snake;
    }

    public Score getScore() {
        return score;
    }

    private boolean isCollision() {
        return false;
    } // TODO Im Field?


}