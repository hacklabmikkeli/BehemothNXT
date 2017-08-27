package fi.hacklabmikkeli.behemoth.core;

import java.io.DataInputStream;
import java.io.IOException;

import fi.hacklabmikkeli.behemoth.io.CommunicationsThread;
import fi.hacklabmikkeli.behemoth.movement.MovementController;
import fi.hacklabmikkeli.behemoth.movement.MovementDirection;
import fi.hacklabmikkeli.behemoth.movement.MovementMode;
import fi.hacklabmikkeli.behemoth.snatcher.SnatcherController;
import fi.hacklabmikkeli.behemoth.utils.BehemothUtils;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class Behemoth {

  public static void main(String[] args) {
    Button.ESCAPE.addButtonListener(new ButtonListener() {
      
      @Override
      public void buttonReleased(Button b) {
      }
      
      @Override
      public void buttonPressed(Button b) {
        System.exit(0);
      }
    });
    
    MovementController movementController = new MovementController(BehemothConsts.LEFT_MOTOR, BehemothConsts.RIGHT_MOTOR, BehemothConsts.LIGHT_SENSOR_PORT);
    SnatcherController snatcherController = new SnatcherController(BehemothConsts.SNATCHER_MOTOR);
    
    System.out.println("Waiting for connection");
    
    BTConnection connection = Bluetooth.waitForConnection();
    DataInputStream controlInputStream = connection.openDataInputStream();
    
    System.out.println("Connected");
    
    CommunicationsThread communicationsThread = new CommunicationsThread(movementController, snatcherController, controlInputStream);
    communicationsThread.start();

    while (!Button.ESCAPE.isPressed()) {
      if (movementController.getMode().equals(MovementMode.LINE_FOLLOW)) {
        movementController.lineFollowProgress();
      }
    }
  }
}
