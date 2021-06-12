package sample.models;

import sample.controller.DataBaseController;
import sample.controller.game;

import java.util.ArrayList;

import static sample.view.print;

public class Data {//

    public boolean hasGameStarted;
    public boolean isContinueGame;
    public String username;
    public int life = 0;
    public String BoardData;
    public int boardID;
    public PacmanData pacmanData;
    public ArrayList<RohData> rohDatas = new ArrayList<>();


    private Data() {
    }

    public static Data getInstance(){
        Data data = new Data();

        data.hasGameStarted = game.instance.hasGameStarted;

        data.isContinueGame = game.instance.isContinueGame;

        data.boardID = Pacman.getInstance().board.id;

        data.username = game.getUsername();

        data.life = game.getCurrentInstance().life;

        data.BoardData = Pacman.getInstance().board.getData();

        data.pacmanData = Pacman.getInstance().getData();

        for (roh roh : roh.rohs) {
            data.rohDatas.add(roh.getData());
        }
        return data;
    }

    public static void saveData() {
        DataBaseController.createFile("data.json", getInstance());
    }

    public static Data getLastData() {

        Data data = null;
        try {
            data = (Data) DataBaseController.getObjectByPath("data.json", Data.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void deleteData(){
        try {
            DataBaseController.deleteFile("data.json");
        }catch (Exception e){
            print(e.getMessage());
        }
    }

}
