package fr.leroideskiwis.ktp;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Move;
import fr.leroideskiwis.mapgame.managers.TextureManager;
import fr.leroideskiwis.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main extends ApplicationAdapter {
	private SpriteBatch batch;
	private Game game;
	private float multiplicatorY;
	private float multiplicatorX;
	private long started;
	private Texture emptyCase;
	private final TextureManager<Entity> textureManager = new TextureManager<>();
	private BitmapFont font;
	private final List<Move> moves = new ArrayList<>();

	@Override
	public void resize(int width, int height) {
		multiplicatorY = (1050f/1.75f)/game.getMap().getHeight();
		multiplicatorX = (1050f/1.75f)/game.getMap().getWidth();
    }

	private void initGame(){
		Gdx.app.log("INFO", "starting game...");

		try {
			this.game = new Game(textureManager);
			resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Gdx.app.log("INFO", "game is started !");

	}

	@Override
	public void create () {

		Locale locale = new Locale("en");
		FileHandle fileHandle = Gdx.files.internal("bundles/bundle");
		Utils.resourceBundle = I18NBundle.createBundle(fileHandle, locale);

		moves.add(new Move(Input.Keys.UP, 0, 1));
		moves.add(new Move(Input.Keys.DOWN, 0, -1));
		moves.add(new Move(Input.Keys.RIGHT, 1, 0));
		moves.add(new Move(Input.Keys.LEFT, -1, 0));

		initGame();
		emptyCase = textureManager.toTexture("emptycase.png");

		this.started = System.currentTimeMillis();
		this.font = new BitmapFont();
		this.font.setColor(new Color(1, 1, 1, 1));

		this.batch = new SpriteBatch();

		//DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> System.out.println(user.username+"#"+user.discriminator+" played the version "+game.version)).build();

		DiscordRPC discord = DiscordRPC.INSTANCE;
		discord.Discord_Initialize("562996306646138911", new DiscordEventHandlers(), true, "");
		game.updatePresence(started);

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

	private void update(boolean hasFail){
		if(hasFail) return;
		game.updatePresence(started);
		try {
			game.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor( 0, 0, 0, 0 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
		updateGame();
		batch.begin();

		game.drawBuffer(batch, font);

		game.drawMap(textureManager, batch, multiplicatorX, multiplicatorY, emptyCase);

		batch.flush();
		batch.end();

	}

	private void updateGame(){

		moves.stream().filter(Move::canMove).limit(1).forEach(move -> update(!game.movePlayer(move.x, move.y)));

		if(game.hasLose() && Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
			try {
				initGame();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	/*public static void playSound(String key){
		playSound(key, 0.5f);
	}

	public static void playSound(String key, float volume){
		Sound sound = sounds.get(key);
		if(sound == null) return;
		sound.play(volume);
	}*/

	@Override
	public void dispose () {
	    Gdx.app.log("INFO", "Stopping game...");
		batch.dispose();
		Gdx.app.log("INFO", "game stopped.");
		font.dispose();
		textureManager.dispose();
		emptyCase.dispose();
	}
}
