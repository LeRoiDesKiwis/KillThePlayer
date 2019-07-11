package fr.leroideskiwis.ktp;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Location;

import java.util.Optional;

public class Main extends ApplicationAdapter {
	private SpriteBatch batch;
	private Game game;
	private float multiplicatorY;
	private float multiplicatorX;
	private long started;
	private Texture emptyCase;

	@Override
	public void resize(int width, int height) {
    }


	private void initGame(){
		Gdx.app.log("INFO", "starting game...");
		try {
			this.game = new Game();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Gdx.app.log("INFO", "game is started !");

		multiplicatorY = (1050f/1.75f)/game.getMap().getHeight();
		multiplicatorX = (1050f/1.75f)/game.getMap().getWidth();

	}

	@Override
	public void create () {
		initGame();
		Gdx.graphics.setContinuousRendering(false);
		emptyCase = getTexture("emptycase.png");

		this.started = System.currentTimeMillis();

		batch = new SpriteBatch();

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
		if(game != null) {
			presence.state = "score : " + game.getScore();
			presence.details = "size of the map : " + game.getSize();
		}
		presence.startTimestamp = started;
        presence.largeImageKey = "new_high_score_";
		DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);
	}

	private void update(int x, int y){
		if(!game.getPlayer().move(x, y)) return;
		updatePresence();
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



		drawText("Your score is "+game.getScore(), 750, 500);

		for(int i = 0; i < game.getBuffer().size(); i++) {
			String s = game.getBuffer().get(i);
			drawText(s, 650, 460-i*20);
		}

		drawMap(game.getMap());

		batch.flush();
		batch.end();

	}

	private void updateGame(){

		if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			update(-1, 0);
			return;
		} else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			update(1, 0);
			return;
		} else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			update(0, 1);
			return;
		} else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			update(0, -1);
			return;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.ENTER) && game.getPlayer().hasLose()) {
			try {
				game.getPluginManager().unloadPlugins();
				initGame();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void drawMap(fr.leroideskiwis.mapgame.Map map){

		for(Location location : map.getLocations()){
			Optional<Entity> entity = map.getEntity(location);

			Rectangle rectangle = new Rectangle(location.getX()*multiplicatorX+1, location.getY()*multiplicatorY+1, multiplicatorX, multiplicatorY);

			if(entity.isPresent()) drawTexture(rectangle, entity.get().texture());
			else drawTexture(rectangle, emptyCase);
		}
	}

	public static Texture getTexture(String path){
		try {
			Texture texture = new Texture(getAsset(path));
			texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			return texture;
		}catch(Exception exception){
			return null;
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

	private static FileHandle getAsset(String path){
	    if(path.endsWith(".png.png")) path = path.substring(0, path.length()-4);

		path = "textures/"+path;
		FileHandle handle = Gdx.files.internal(path);
		if (handle.exists())
			return handle;
		else
			handle = Gdx.files.classpath(path);
		return handle.exists() ? handle : null;
	}

	private void drawText(String s, int x, int y){
		BitmapFont font = new BitmapFont();
		font.setColor(new Color(1, 1, 1, 1));

		font.draw(batch, s, x, y);
	}

	@Override
	public void dispose () {
	    Gdx.app.log("INFO", "Stopping game...");
		batch.dispose();
		if(game != null && game.getPluginManager() != null) game.getPluginManager().unloadPlugins();
		Gdx.app.log("INFO", "game stopped.");
	}

	private void drawTexture(Rectangle rectangle, Texture texture){

		batch.draw(texture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);

	}
}
