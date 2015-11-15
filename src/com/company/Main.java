/******************************************************************************\
 * Copyright (C) 2012-2013 Leap Motion, Inc. All rights reserved.               *
 * Leap Motion proprietary and confidential. Not for distribution.              *
 * Use subject to the terms of the Leap Motion SDK Agreement available at       *
 * https://developer.leapmotion.com/sdk_agreement, or another agreement         *
 * between Leap Motion and you, your company or other organization.             *
 \******************************************************************************/
package com.company;
import java.io.IOException;
import java.lang.Math;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

class SampleListener extends Listener {
    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
        controller.config().setFloat("Gesture.KeyTap.MinDownVelocity", 40.0f);
        controller.config().setFloat("Gesture.KeyTap.HistorySeconds", .2f);
        controller.config().setFloat("Gesture.KeyTap.MinDistance", 3.0f);
        controller.config().save();
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
        Frame frame = controller.frame();
//        System.out.println("Frame id: " + frame.id()
//                + ", timestamp: " + frame.timestamp()
//                + ", hands: " + frame.hands().count()
//                + ", fingers: " + frame.fingers().count()
//                + ", tools: " + frame.tools().count()
//                + ", gestures " + frame.gestures().count());


        GestureList gestures = frame.gestures();
        for (int i = 0; i < gestures.count(); i++) {
            Gesture gesture = gestures.get(i);

            switch (gesture.type()) {
                case TYPE_KEY_TAP:
                    KeyTapGesture keyTap = new KeyTapGesture(gesture);
                    for(Hand hand : frame.hands()) {
                        String handType = hand.isLeft() ? "Left hand" : "Right hand";
                        System.out.println(handType);
                        // Get fingers
                        for (Finger finger : hand.fingers()) {
                            System.out.println("    " + finger.type()
                                    +" x:"+finger.tipPosition().getX()+ " y:"+finger.tipPosition().getY()+ " z:"+finger.tipPosition().getZ());

                            int finNum = fingerNum(hand, finger, keyTap);
                            if(finNum!=0){
                                System.out.println(finNum);
                            }

//                for(Bone.Type boneType : Bone.Type.values()) {
//                    Bone bone = finger.bone(boneType);
//                    System.out.println("      " + bone.type()
//                            + " bone, start: " + bone.prevJoint()
//                            + ", end: " + bone.nextJoint()
//                            + ", direction: " + bone.direction());
//                }
                        }
                    }

                    System.out.println("  Key Tap id: " + keyTap.id()
                            + ", " + keyTap.state()
                            + ", position: " + keyTap.position()
                            + ", direction: " + keyTap.direction());
                    break;
                default:
                    //System.out.println("Unknown gesture type.");
                    break;
            }
        }

        //Get hands



//        if (!frame.hands().isEmpty() || !gestures.isEmpty()) {
//            System.out.println();
//        }
    }

    private int fingerNum(Hand hand, Finger finger, KeyTapGesture keyTap) {
        int num = 0;
        float fingerX = finger.tipPosition().getX();
        float fingerY = finger.tipPosition().getY();
        float fingerZ = finger.tipPosition().getZ();


        float tapX = keyTap.position().getX();
        float tapY = keyTap.position().getY();
        float tapZ = keyTap.position().getZ();
        if(hand.isLeft()){
            if(Math.abs(fingerX-tapX)<8&&Math.abs(fingerY-tapY)>5&&Math.abs(fingerZ-tapZ)<8){
                switch (finger.type()){
                    case TYPE_PINKY:
                        num = 1;
                        break;
                    case TYPE_RING:
                        num = 2;
                        break;
                    case TYPE_MIDDLE:
                        num = 3;
                        break;
                    case TYPE_INDEX:
                        num = 4;
                        break;
                    case TYPE_THUMB:
                        num = 5;
                        break;
                    default:
                        num = 0;
                }
            }
        }else{
            if(Math.abs(fingerX-tapX)<8&&Math.abs(fingerY-tapY)>5&&Math.abs(fingerZ-tapZ)<8){
                switch (finger.type()){
                    case TYPE_PINKY:
                        num = 10;
                        break;
                    case TYPE_RING:
                        num = 9;
                        break;
                    case TYPE_MIDDLE:
                        num = 8;
                        break;
                    case TYPE_INDEX:
                        num = 7;
                        break;
                    case TYPE_THUMB:
                        num = 6;
                        break;
                    default:
                        num = 0;
                }
            }

        }
        return num;
    }
}

class Main {
    public static void main(String[] args) {
        // Create a sample listener and controller
        SampleListener listener = new SampleListener();
        Controller controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener);

        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
    }
}
