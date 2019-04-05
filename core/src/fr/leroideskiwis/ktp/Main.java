package fr.leroideskiwis.ktp;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import fr.leroideskiwis.mapgame.*;
import fr.leroideskiwis.mapgame.specialobjects.SpecialObj;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends ApplicationAdapter {
	private SpriteBatch batch;
	private Game game;
	private ShapeRenderer renderer;
	private List<String> tmpBuffer = new ArrayList<>();
	private float MULTIPLICATOR;
	private long started;
	private Map<String, Texture> textures = new HashMap<>();
	private static Map<String, Sound> sounds = new HashMap<>();

	@Override
	public void resize(int width, int height) {
    }

	@Override
	public void create () {
		textures.put("ennemy", getTexture("ennemy.png"));
		textures.put("coin", getTexture("coin.png"));
		textures.put("obstacle", getTexture("obstacle.png"));
		textures.put("empty", getTexture("emptycase.png"));
		textures.put("player", getTexture("player.png"));
		sounds.put("coinsound", getSound("coin.mp3"));
		sounds.put("objectsound", getSound("object.mp3"));

		this.started = System.currentTimeMillis();
	    Gdx.app.log("INFO", "starting game...");
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		try {
			this.game = new Game();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Gdx.graphics.setContinuousRendering(false);

		Gdx.app.log("INFO", "game is started !");

		MULTIPLICATOR = (1005f/1.675f)/game.getMap().getSize()[0];

		//DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> System.out.println(user.username+"#"+user.discriminator+" played the version "+game.version)).build();

		DiscordRPC discord = DiscordRPC.INSTANCE;
		discord.Discord_Initialize("562996306646138911", new DiscordEventHandlers(), true, "");
		updatePresence();

		new Thread(() -> {
			while(!Thread.currentThread().isInterrupted()){
				DiscordRPC.INSTANCE.Discord_RunCallbacks();

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "RPC-Callback-Handler").start();

	}

	private void updatePresence(){
		DiscordRichPresence presence = new DiscordRichPresence();//setDetails();
		presence.state = "score : "+game.getScore();
		presence.details = "in version "+game.version;
		presence.startTimestamp = started;
        presence.largeImageKey = "new_high_score_";
		DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);
	}

	private boolean update(int x, int y){
		if(!game.getPlayer().move(x, y)) return true;
		updatePresence();
		try {
			return game.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void render () {

		playSound("coinsound", 1f);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		boolean hasLose = false;
		batch.begin();

		if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) hasLose = update(-1, 0);
		else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) hasLose = update(1, 0);
		else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) hasLose = update(0, 1);
		else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) hasLose = update(0, -1);
		if(Gdx.input.isKeyPressed(Input.Keys.ENTER) && (game.getPlayer().hasLose() || game.getMap().getEmptyCases().isEmpty())) {
			try {
				game = new Game();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(!hasLose) {
			tmpBuffer.clear();
			tmpBuffer.add("You are dead. Please press enter.");
		}

		renderer.begin(ShapeRenderer.ShapeType.Filled);

		drawText("Your score is "+game.getScore(), 750, 500, null);

		for (String s : game.getBuffer()) {
			if(!tmpBuffer.contains(s)) tmpBuffer = new ArrayList<>(game.getBuffer());
		}

		game.getBuffer().clear();

		for(int i = 0; i < tmpBuffer.size(); i++) {
			String s = tmpBuffer.get(i);
			drawText(s, 650, 460-i*20, null);
		}

		for(int x = 0; x < game.getMap().getContent().length; x++) {

				for(int y = 0; y < game.getMap().getContent()[x].length; y++){

					Object object = game.getMap().getObject(x, y);

					Rectangle rectangle = new Rectangle(x*MULTIPLICATOR+1, y*MULTIPLICATOR+1, MULTIPLICATOR, MULTIPLICATOR);

					if(object instanceof Obstacle)
						drawTexture(rectangle, getMapTexture("obstacle"));
					if(object instanceof Player) drawTexture(rectangle, getMapTexture("player"));
					if(object instanceof Ennemy) drawTexture(rectangle, getMapTexture("ennemy"));
					if(object instanceof SpecialObj) drawTexture(rectangle, ((SpecialObj)object).texture());
					if(object instanceof Coin) drawTexture(rectangle, getMapTexture("coin"));
					if(object == null) drawTexture(rectangle, getMapTexture("empty"));

				}
			}

		batch.flush();

		renderer.end();
		batch.end();

	}

	public static Texture getTexture(String path){
		return new Texture(getAsset(path));
	}

	private Sound getSound(String path){
		FileHandle handle = getAsset(path);
		if(handle == null) return null;
		return Gdx.audio.newSound(handle);
	}

	public static void playSound(String key){
		playSound(key, 0.5f);
	}

	public static void playSound(String key, float volume){
		Sound sound = sounds.get(key);
		if(sound == null) return;
		sound.play(volume);
	}

	private static FileHandle getAsset(String path){

		path = "textures/"+path;
		File textureFile = new File(path);
		FileHandle handle = Gdx.files.internal(path);
		if (handle.exists())
			return Gdx.files.internal(path);
		else
			handle = Gdx.files.classpath(path);
		return handle.exists() ? handle : null;
	}

	private Texture getMapTexture(String key){
		return textures.get(key);
	}

	private void drawText(String s, int x, int y, Color color){
		BitmapFont font = new BitmapFont();
		if(color != null) font.setColor(color);
		else font.setColor(new Color(1, 1, 1, 1));

		font.draw(batch, s, x, y);
	}

	@Override
	public void dispose () {
	    Gdx.app.log("INFO", "Stopping game...");
		batch.dispose();
		renderer.dispose();
	}

	private void drawTexture(Rectangle rectangle, Texture texture){

		batch.draw(texture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);

	}
}
