package fr.leroideskiwis.ktp.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.leroideskiwis.utils.CustomPredicate;

public class Button {

    private final Runnable ifClick;
    private final CustomPredicate predicate;
    private int x, y, height, width;
    private Texture texture;
    private String id;

    public Button(CustomPredicate predicate, Runnable ifClicked, Texture texture, String id, int x, int y) {
        this.x = x;
        this.y = y;
        this.height = 75;
        this.width = 300;
        this.texture = texture;
        this.id = id;
        this.ifClick = ifClicked;
        this.predicate = predicate;
    }

    public Button(Runnable ifClicked, Texture texture, String id, int x, int y) {
        this.ifClick = ifClicked;
        this.x = x;
        this.y = y;
        this.height = 75;
        this.width = 300;
        this.texture = texture;
        this.id = id;
        this.predicate = () -> true;
    }

    public void draw(SpriteBatch batch){
        float tmpHeight = height;
        float tmpWidth = width;
        float tmpX = x;
        float tmpY = y;

        float size = 1.15f;

        float xSize = tmpWidth*(size-1f);
        float ySize = tmpHeight*(size-1f);

        if(hasTouch()){
            tmpWidth+=xSize;
            tmpHeight+=ySize;
            tmpX-=xSize/2f;
            tmpY-=ySize/2f;
        }

        batch.draw(texture, tmpX, tmpY, tmpWidth, tmpHeight);
    }

    public void runClicked(){

        if(checkClick())
            ifClick.run();

    }

    public boolean hasTouch(){

        return (Gdx.input.getX() > x && Gdx.input.getX() < x+width) && (getBottomLeftY() > y && getBottomLeftY() < y+height);

    }

    public boolean condition(){
        return predicate.call();
    }

    public int getBottomLeftY(){
       return Gdx.graphics.getHeight()-Gdx.input.getY();
    }

    public boolean checkClick(){
        return Gdx.input.isButtonPressed(Input.Keys.LEFT) && hasTouch();
    }

    public String getId() {
        return id;
    }
}
