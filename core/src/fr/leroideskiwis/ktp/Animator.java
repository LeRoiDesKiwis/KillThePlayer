package fr.leroideskiwis.ktp;

import com.badlogic.gdx.Gdx;

public class Animator {

    private Thread thread;

    public void start(int updatePerSeconds){
        if(thread != null) return;
        this.thread = new Thread(() -> {

            while(!Thread.currentThread().isInterrupted()){
                Gdx.graphics.requestRendering();
                try {
                    Thread.sleep(1000/updatePerSeconds);
                } catch (InterruptedException e) {
                    break;
                }
            }

        }, "animator");
        thread.start();
    }
    
    public void stop(){
        if(thread != null) thread.interrupt();
    }

}
