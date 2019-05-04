package fr.leroideskiwis.ktp.textbox;

import com.badlogic.gdx.Input;
import fr.leroideskiwis.mapgame.multiplayer.Multiplayer;

import java.io.IOException;

public class IpTextBoxListener implements Input.TextInputListener {

    private Multiplayer multiplayer;

    public IpTextBoxListener(Multiplayer multi) {
        this.multiplayer = multi;
    }

    @Override
    public void input(String text) {
        try {
            multiplayer.connect(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void canceled() {
        try {
            multiplayer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
