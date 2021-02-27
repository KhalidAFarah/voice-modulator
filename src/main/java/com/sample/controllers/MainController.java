package com.sample.controllers;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javax.sound.sampled.*;
import java.util.Arrays;

public class MainController {


    private boolean run = true;
    private Thread tr;
    private AnimationTimer animationTimer;

    @FXML
    private Rectangle Rect1;

    @FXML
    private Rectangle Rect2;

    @FXML
    private Rectangle Rect3;

    @FXML
    private Rectangle Rect4;

    @FXML
    private Rectangle Rect5;

    @FXML
    private Rectangle Rect6;

    @FXML
    private Rectangle Rect7;

    @FXML
    private Rectangle Rect8;


    public void setup() {
        run = true;


        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

        try {

            //Speaker
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open();
            //Microphone

            info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
            targetLine.open();


            tr = new Thread() {
                @Override
                public void run() {

                    targetLine.start();
                    sourceLine.start();

                    byte[] data = new byte[100];
                    int[] readBytes = {0};
                    byte[] biggest = new byte[8];
                    final int[] counter = {0};

                    animationTimer = new AnimationTimer() {
                        @Override
                        public void handle(long now) {
                            readBytes[0] = targetLine.read(data, 0, data.length);

                            move(data);
                            if(counter[0] == 8){
                                counter[0] = 0;
                                //System.out.println(targetLine.isActive());
                                //System.out.println(Arrays.toString(biggest));
                                //System.out.println(Arrays.toString(data));

                                upSize(biggest[0], Rect1);
                                upSize(biggest[1], Rect2);
                                upSize(biggest[2], Rect3);
                                upSize(biggest[3], Rect4);

                                upSize(biggest[4], Rect5);
                                upSize(biggest[5], Rect6);
                                upSize(biggest[6], Rect7);
                                upSize(biggest[7], Rect8);

                            }else{
                                biggest[counter[0]] = data[data.length-1];
                                counter[0]++;
                            }
                        }
                    };
                    animationTimer.start();

                }
            };
            tr.start();

            //targetLine.stop();
            //targetLine.close();
        }
        catch(LineUnavailableException lue) { lue.printStackTrace(); }
    }
    @FXML
    void stop(ActionEvent event) {
        if(animationTimer == null)
            setup();
        else if(run) {
            animationTimer.stop();
            run = false;
        }
        else {
            animationTimer.start();
            run = true;
        }



    }
    public static void quicksort(byte[] a, int fra, int til){
        if(til-fra <= 0)
            return;

        int p = til;
        int placement = fra;
        for(int i = fra; i < til; i++){
            if(a[i] < a[p]){

                byte temp = a[placement];
                a[placement] = a[i];
                a[i] = temp;

                placement++;
            }
        }

        byte temp = a[p];
        a[p] = a[placement];
        a[placement] = temp;


        quicksort(a, fra, placement-1);
        quicksort(a, placement+1, til);


    }
    public void move(byte[] b){

        quicksort(b, 0, b.length/2);
        quicksort(b, b.length/2 + 1, b.length-1);
        //System.out.println("unsorted: " + Arrays.toString(b));
        //System.out.println("sorted: " + Arrays.toString(big));




    }

    public void upSize(byte height, Rectangle Rect){


        double scale;
        if((int) height < 25) scale = 0.5;
        else if((int) height <= 35) scale = 1;
        else if((int) height <= 50) scale = 1.5;
        else if((int) height <= 65) scale = 2;
        else if((int) height <= 75) scale = 2.5;
        else if((int) height <= 85) scale = 3;
        else if((int) height <= 100) scale = 3.5;
        else if((int) height <= 115) scale = 4;
        else scale = 4.5;

        ScaleTransition t = new ScaleTransition(Duration.millis(100), Rect);

        t.setAutoReverse(false);
        t.setToY(scale);
        t.setCycleCount(1);

        t.play();
    }
}