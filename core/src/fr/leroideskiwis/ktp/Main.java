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
import fr.leroideskiwis.ktp.textbox.IpTextBoxListener;
import fr.leroideskiwis.ktp.window.Button;
import fr.leroideskiwis.mapgame.*;
import fr.leroideskiwis.mapgame.multiplayer.Multiplayer;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Map;
import java.util.stream.Stream;

public class Main extends ApplicationAdapter {
	private SpriteBatch batch;
	private Game game;
	private ShapeRenderer renderer;
	private List<String> tmpBuffer = new ArrayList<>();
	private float multiplicatorY;
	private float multiplicatorX;
	private long started;
	private static Map<String, Sound> sounds = new HashMap<>();
	private Texture emptyCase;
	private Menu menu = Menu.MAINMENU;
	private List<Button> buttons = new ArrayList<>();
	private Animator animator = new Animator();
	private Texture background;
	private Multiplayer multi;
	private IpTextBoxListener ipListener;

	@Override
	public void resize(int width, int height) {
    }

    private void startAnimator(int updatePerSeconds){
		animator.start(updatePerSeconds);
	}

	private void initGame(Menu menu){
		this.menu = menu;
		Gdx.app.log("INFO", "starting game...");
		try {
			this.game = new Game();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Gdx.app.log("INFO", "game is started !");

		multiplicatorY = (1050f/1.75f)/game.getMap().getSize();
		multiplicatorX = (1050f/1.75f)/game.getMap().getSize();

	}

	@Override
	public void create () {
		Gdx.graphics.setContinuousRendering(false);
		emptyCase = getTexture("emptycase.png");
		background = getTexture("background.png");

		this.started = System.currentTimeMillis();

		batch = new SpriteBatch();
		renderer = new ShapeRenderer();

		//DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> System.out.println(user.username+"#"+user.discriminator+" played the version "+game.version)).build();

		buttons.add(new Button(() -> game == null, () -> initGame(Menu.SOLO), getTexture("soloButton.png"), "solo",150, 250));
		buttons.add(new Button(() -> {
				if(true) return;
				this.multi = new Multiplayer(game);
				ipListener = new IpTextBoxListener(multi);
				Gdx.input.getTextInput(ipListener, "ip du serveur ?", "", "tapez l'ip");
				initGame(Menu.MULTI);
		}, getTexture("MultiButton.png"), "multi", 550, 250));
		buttons.add(new Button(() -> game != null, () -> menu = Menu.SOLO, getTexture("continue.png"), "continue", 150, 250));
		buttons.add(new Button(() -> game != null, () -> initGame(Menu.SOLO), getTexture("reset.png"), "reset", 150, 50));
		buttons.add(new Button(() -> Gdx.app.exit(), getTexture("exit.png"), "exit", 550, 50));
		buttons.add(new Button(() -> multi != null, () -> multi = null, getTexture("quit.png"), "quit", 550, 250));

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
	public void render() {
		batch.begin();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch(menu){
			case SOLO:
				processSoloGame();
				break;

			case MAINMENU:
				processMainMenu();
				break;
			case MULTI:
				try {
					processMulti();
				} catch (IOException | JSONException | IllegalAccessException | InstantiationException | InvocationTargetException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				break;
		}

		batch.end();

	}

	private Stream<Button> getButtonsWithCondition(){
		return buttons.stream().filter(Button::condition);
	}

	private void reset() throws IOException {
		this.game = null;
		if(multi != null){
			multi.close();
			multi = null;
		}
		menu = Menu.MAINMENU;
	}

	private void processMainMenu(){
		startAnimator(5);
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		getButtonsWithCondition().forEach(button -> button.draw(batch));
		getButtonsWithCondition().forEach(Button::runClicked);

	}

	private void processMulti() throws IOException, JSONException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
		if(multi == null || (multi.isClosed() && multi.isConnected())) reset();
		else {

			if(multi.isConnected()) {
				updateGame();
				multi.reloadMaps();

				drawMap(game.getMap(), 1, 1);
				drawMap(multi.getOtherMap(), 1, game.getMap().getSize() * multiplicatorY + 20);
			}

		}

	}

	private boolean updateGame(){

		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) menu = Menu.MAINMENU;

		if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) return update(-1, 0);
		else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) return update(1, 0);
		else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) return update(0, 1);
		else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) return update(0, -1);
		if(Gdx.input.isKeyPressed(Input.Keys.ENTER) && (game.getPlayer().hasLose() || game.getMap().getEmptyCases().isEmpty())) {
			try {
				game.getPluginManager().unloadPlugins();
				menu = Menu.MAINMENU;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;

	}

	private void processSoloGame() {
		animator.stop();

		boolean hasLose = updateGame();

		if(!hasLose) {
			tmpBuffer.clear();
			tmpBuffer.add("You are dead. Please press enter.");
		}

		drawText("Your score is "+game.getScore(), 750, 500, null);

		for (String s : game.getBuffer()) {
			if(!tmpBuffer.contains(s)) tmpBuffer = new ArrayList<>(game.getBuffer());
		}

		game.getBuffer().clear();

		for(int i = 0; i < tmpBuffer.size(); i++) {
			String s = tmpBuffer.get(i);
			drawText(s, 650, 460-i*20, null);
		}

		drawMap(game.getMap(), 1, 1);

		batch.flush();

		renderer.end();

	}

	private void drawMap(fr.leroideskiwis.mapgame.Map map, float xStart, float yStart){
		for(int x = 0; x < map.getContent().length; x++) {

			for(int y = 0; y < map.getContent()[x].length; y++){

				Entity entity = map.getObject(x, y);

				Rectangle rectangle = new Rectangle(x*multiplicatorX+xStart, y*multiplicatorY+yStart, multiplicatorX, multiplicatorY);

				if(entity != null) drawTexture(rectangle, entity.texture());
				else drawTexture(rectangle, emptyCase);
			}
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

	private Sound getSound(String path){
		FileHandle handle = getAsset(path);
		if(handle == null) return null;
		return Gdx.audio.newSound(handle);
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

	private void drawText(String s, int x, int y, Color color){
		if(true) return;
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
		if(game != null && game.getPluginManager() != null) game.getPluginManager().unloadPlugins();
		Gdx.app.log("INFO", "game stopped.");
	}

	private void drawTexture(Rectangle rectangle, Texture texture){

		batch.draw(texture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);

	}

	private enum Menu {
		MAINMENU, SOLO, MULTI;

	}
}
