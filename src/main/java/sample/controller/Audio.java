package sample.controller;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.nio.file.Paths;

public class Audio {

    public static boolean isPlayingBackground = false;
    public static boolean isPlayingSounds = false;

    public static MediaPlayer pacman_beginning;
    public static AudioClip pacman_chomp;
    public static AudioClip pacman_death;
    public static AudioClip pacman_eatGhost;

    static {
        String path = "./Music/pacman_beginning.wav";
        pacman_beginning = new MediaPlayer(getMedia(path));
        pacman_beginning.setVolume(0.5);
        pacman_beginning.setCycleCount(MediaPlayer.INDEFINITE);

        path = "./Music/pacman_chomp.wav";
        pacman_chomp = new AudioClip(getPath(path));
        pacman_chomp.setVolume(0.5);

        path = "./Music/pacman_eatGhost.wav";
        pacman_eatGhost = new AudioClip(getPath(path));
        pacman_eatGhost.setVolume(1);

        path = "./Music/pacman_death.wav";
        pacman_death = new AudioClip(getPath(path));
        pacman_death.setVolume(1);
    }

    public static void playChomp() {
        if (isPlayingSounds) {
            pacman_chomp.play();
        }
    }

    public static void playPacMacDeath() {
        if (isPlayingSounds) {
            pacman_death.play();
        }
    }

    public static void playEatGhost() {
        if (isPlayingSounds) {
            pacman_eatGhost.play();
        }
    }


    public static Media getMedia(String path) {
        File file = new File(path);
        String pathForMedia = "file:///" +
                Paths.get(file.toURI()).toString().replace("\\", "/");

        return new Media(pathForMedia);
    }

    public static String getPath(String path) {
        File file = new File(path);
        return "file:///" +
                Paths.get(file.toURI()).toString().replace("\\", "/");
    }


    public static void setAudioButton(Button button) {

        button.setMaxSize(40, 40);
        button.setMinSize(40, 40);

        Image image1 = new Image("pic/audio1.png");
        ImageView imageView1 = new ImageView(image1);
        imageView1.fitWidthProperty().bind(button.widthProperty());
        imageView1.fitHeightProperty().bind(button.heightProperty());

        Image image2 = new Image("pic/mute.png");
        ImageView imageView2 = new ImageView(image2);
        imageView2.fitWidthProperty().bind(button.widthProperty());
        imageView2.fitHeightProperty().bind(button.heightProperty());

        if(isPlayingBackground){
            button.setGraphic(imageView1);
        } else{
            button.setGraphic(imageView2);
        }

        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Page.stage.getScene().setCursor(Cursor.HAND);
            }
        });

        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Page.stage.getScene().setCursor(Cursor.DEFAULT);
            }
        });

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isPlayingBackground) {
                    pacman_beginning.pause();
                    button.setGraphic(imageView2);
                    isPlayingSounds = false;
                    isPlayingBackground = false;
                } else {
                    pacman_beginning.play();
                    button.setGraphic(imageView1);
                    isPlayingSounds = true;
                    isPlayingBackground = true;
                }
            }
        });
    }

    public static Button getAudioButton() {
        Button button = new Button();
        Audio.setAudioButton(button);
        return button;
    }

}

