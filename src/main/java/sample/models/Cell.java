package sample.models;

import com.google.gson.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static sample.view.print;

public class Cell extends StackPane {

    private Rectangle rectangle;
    public Circle coin;
    public Circle bomb;

    public int model;

    public Cell(int model) {
        this.model = model;
        rectangle = new Rectangle(30, 30);
        this.getChildren().add(rectangle);
        setModel(model);
    }

    public void setCoin(){
        coin = new Circle(2, Color.YELLOW);
        this.getChildren().add(coin);
    }

    public void setBomb(){
        removeCoin();
        bomb = new Circle(4, Color.GOLD);
        this.getChildren().add(bomb);
    }

    public void setModel(int i) {
        model = i;

        if (model == 1) {
            rectangle.setFill(Color.BLACK);
        }
        if (model == 3) {
            rectangle.setFill(Color.WHITE);
        }
        if (model == 2) {
            rectangle.setFill(Color.BLUE);
        }
    }

    public String getData(){

        JsonObject data = new JsonObject();

        data.addProperty("coin", coin != null);
        data.addProperty("bomb", bomb != null);
        data.addProperty("model" , model);

        return data.toString();
    }

    public static Cell deserialize(String data){

        Cell cell;
        JsonObject object = JsonParser.parseString(data).getAsJsonObject();
        cell = new Cell(object.get("model").getAsInt());
        if(object.get("coin").getAsBoolean()){
            cell.setCoin();
        } else if(object.get("bomb").getAsBoolean()){
            cell.setBomb();
        }
        return cell;
    }

    public void removeCoin(){
        getChildren().remove(coin);
        coin = null;
    }

    public void removeBomb(){
        getChildren().remove(bomb);
        bomb = null;
    }
}