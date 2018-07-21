package com.solar.simulation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class View extends ApplicationAdapter implements InputProcessor{

	private SpriteBatch batch;

	private Astro sun, earth, moon, mercury, mars, venus,
				  jupiter, saturn, uranus, neptune,
				  halley;

	// constants

	public static final float WIDTH = 1524, HEIGHT = 864;

	private Viewport viewport;
	private OrthographicCamera camera;

	private Stage stage;

	private Array<Astro> astros;

	private static final float mE = 1; // earth mass
	private static final float vE = 1; // earth velocity
	private static final float au = 50000; // astronomic unit (distance from earth to sun)
	private static final float dE = 30; // earth diameter

	private static final float mS = 333000 * mE; // sun mass
	private static final float G = 0.15f; // universal gravitation constant

	private static final float CAMERA_SPEED = 5000;
	private static final float CAMERA_ZOOM_SPEED = 14;
	private static final float CAMERA_MIN_ZOOM = 0.5f;
	private static final float CAMERA_MAX_ZOOM = 1000.0f;
	private final float MIN_TIME_SPEED = 1.0f;
	private final float MAX_TIME_SPEED = 2000;

	public static float time = 1;

	private Texture sunTexture, mercuryTexture, venusTexture,
					earthTexture, moonTexture, marsTexture, jupiterTexture,
					saturnTexture, uranusTexture, neptuneTexture;

	private ParticleEffect halleyEffect;

	private boolean interpolating;
	private float t;
	private Astro follow;

	private Label zoomLabel, timeLabel, cameraLabel, planetShorcut, credits;

	private Music backgroundMusic;

	@Override
	public void create () {

		sunTexture = new Texture(Gdx.files.internal("sun.png"));
		mercuryTexture = new Texture(Gdx.files.internal("mercury.png"));
		venusTexture = new Texture(Gdx.files.internal("venus.png"));
		earthTexture = new Texture(Gdx.files.internal("earth.png"));
		moonTexture = new Texture(Gdx.files.internal("moon.png"));
		marsTexture = new Texture(Gdx.files.internal("mars.png"));
		jupiterTexture = new Texture(Gdx.files.internal("jupiter.png"));
		saturnTexture = new Texture(Gdx.files.internal("saturn.png"));
		uranusTexture = new Texture(Gdx.files.internal("uranus.png"));
		neptuneTexture = new Texture(Gdx.files.internal("neptune.png"));

		halleyEffect = new ParticleEffect();
		halleyEffect.load(Gdx.files.internal("halley.particle"),
				Gdx.files.internal(""));


		sunTexture.setFilter(Texture.TextureFilter.Nearest,
				Texture.TextureFilter.Nearest);
		mercuryTexture.setFilter(Texture.TextureFilter.Nearest,
				Texture.TextureFilter.Nearest);
		venusTexture.setFilter(Texture.TextureFilter.Nearest,
				Texture.TextureFilter.Nearest);
		earthTexture.setFilter(Texture.TextureFilter.Nearest,
				Texture.TextureFilter.Nearest);
		moonTexture.setFilter(Texture.TextureFilter.Nearest,
				Texture.TextureFilter.Nearest);
		marsTexture.setFilter(Texture.TextureFilter.Nearest,
				Texture.TextureFilter.Nearest);
		jupiterTexture.setFilter(Texture.TextureFilter.Nearest,
				Texture.TextureFilter.Nearest);
		saturnTexture.setFilter(Texture.TextureFilter.Nearest,
				Texture.TextureFilter.Nearest);
		uranusTexture.setFilter(Texture.TextureFilter.Nearest,
				Texture.TextureFilter.Nearest);
		neptuneTexture.setFilter(Texture.TextureFilter.Nearest,
				Texture.TextureFilter.Nearest);

		astros = new Array<Astro>();

		sun = new Astro(
				new Vector2(0,0),
				new Vector2(0, 0),
				mS, 109 * dE, astros, // sun's diameter is 119 times the earth
				"Sun"
		);

		mercury = new Astro(
				new Vector2(0.387f * au, 0),
				new Vector2(0,1.59f * vE),
				0.0553f * mE, 0.383f * dE, astros, "Mercury");

		venus = new Astro(
				new Vector2(0.723f * au, 0),
				new Vector2(0,1.18f * vE),
				0.815f * mE, 0.949f * dE, astros, "Venus");

		earth = new Astro(
				new Vector2(au, 0),
				new Vector2(0, vE), mE, dE, astros, "Earth");

		moon = new Astro(
				new Vector2(au + 0.0025695f * au, 0),
				new Vector2(0, 0.0343f * vE + vE),
				0.0123f * mE, 0.2724f * dE, astros, "Moon"
		);

		mars = new Astro(
				new Vector2(1.52f * au, 0),
				new Vector2(0,0.808f * vE),
				0.107f * mE, 0.532f * dE, astros, "Mars");

		jupiter = new Astro(
				new Vector2(5.20f * au, 0),
				new Vector2(0, 0.439f * vE),
				317.8f * mE, 11.21f * dE, astros, "Jupiter");

		saturn = new Astro(
				new Vector2(9.58f * au, 0),
				new Vector2(0, 0.325f * vE),
				95.2f * mE, 9.45f * dE, astros, "Saturn");

		uranus = new Astro(
				new Vector2(19.20f * au, 0),
				new Vector2(0,0.228f * vE),
				14.5f * mE, 4.01f * dE, astros, "Uranus");

		neptune = new Astro(
				new Vector2(30.05f * au, 0),
				new Vector2(0,0.182f * vE),
				17.1f * mE, 3.88f * dE, astros, "Neptune");

		halley = new Astro(
				new Vector2(35.1f * au, 0),
				new Vector2(0, -0.029496644f * vE),
				0.00001f * mE, dE, astros, "1P / Halley (Comet)");

		sun.setTexture(sunTexture);
		mercury.setTexture(mercuryTexture);
		venus.setTexture(venusTexture);
		earth.setTexture(earthTexture);
		moon.setTexture(moonTexture);
		mars.setTexture(marsTexture);
		jupiter.setTexture(jupiterTexture);
		saturn.setTexture(saturnTexture);
		uranus.setTexture(uranusTexture);
		neptune.setTexture(neptuneTexture);

		halley.setParticleEffect(halleyEffect);

		astros.add(sun);
		astros.add(mercury);
        astros.add(venus);
		astros.add(earth);
		astros.add(moon);
		astros.add(mars);
		astros.add(jupiter);
		astros.add(saturn);
		astros.add(uranus);
		astros.add(neptune);
		astros.add(halley);

		follow = null;
		t = 0;
		interpolating = false;
		camera = new OrthographicCamera();
		camera.zoom = 14;
		viewport = new FitViewport(WIDTH, HEIGHT, camera);
		batch = new SpriteBatch();
		Gdx.input.setInputProcessor(this);

		stage = new Stage();

		for(Astro a: astros)
			stage.addActor(a.getName());

		zoomLabel = new Label("Zoom (Z / X) : " + camera.zoom,
				new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		zoomLabel.setPosition(15,
				Gdx.graphics.getHeight() - 25);

		timeLabel = new Label("Time (K / L) : " + time,
				new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		timeLabel.setPosition(15,
				Gdx.graphics.getHeight() - 50);

		cameraLabel = new Label("Coordinates (Arrow Keys): "
				+ "X : " + camera.position.x + " Y : " + camera.position.y,
				new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		cameraLabel.setPosition(15,
				Gdx.graphics.getHeight() - 75);

		planetShorcut = new Label("Go to: Sun (0), Mercury (1), Venus (2), " +
				"Earth (3), Moon (4), Mars (5), Jupiter (6), Saturn (7), Uranus (8), " +
				"Neptune (9), 1 / P Halley (H)",
				new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		planetShorcut.setPosition(15,
				15);

		credits = new Label("Developed By Joshua Hernandez",
				new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		credits.setPosition(Gdx.graphics.getWidth() - 250,
				Gdx.graphics.getHeight() - 25);

		stage.addActor(zoomLabel);
		stage.addActor(timeLabel);
		stage.addActor(cameraLabel);
		stage.addActor(planetShorcut);
		stage.addActor(credits);

		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("backgroundMusic.mp3"));
		backgroundMusic.setLooping(true);
		backgroundMusic.play();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.draw();

		handleInput(Gdx.graphics.getDeltaTime());

		if(follow != null && !interpolating){
			followAstro();
		}

		batch.setProjectionMatrix(camera.combined);

		for(int i = 0; i < astros.size; i++)
			astros.get(i).render(batch, camera);

	}

	private void followAstro(){
		camera.position.x = follow.getPosition().x;
		camera.position.y = follow.getPosition().y;
		camera.update();

		cameraLabel.setText("Coordinates (Arrow Keys): "
				+ "X : " + camera.position.x + " Y : " + camera.position.y);
	}

	private void handleInput(float dt){

		if(!interpolating) {
			cameraInput(dt);
		}else{
			t += dt;
			interpolate(t);
			if(t > 1){
				interpolating = false;
				t = 0;
			}
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.K) && View.time < MAX_TIME_SPEED) {
			time *= 2;
			timeLabel.setText("Time (K / L) : " + time);
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.L) && View.time > MIN_TIME_SPEED) {
			time /= 2;
			timeLabel.setText("Time (K / L) : " + time);
		}

		camera.update();
	}

	private void interpolate(float time){
		assert follow != null: "Error: Follow is null";

		Vector2 pos = follow.getPosition();

		camera.position.slerp(new Vector3(pos.x, pos.y, 0), time);

		cameraLabel.setText("Coordinates (Arrow Keys): "
				+ "X : " + camera.position.x + " Y : " + camera.position.y);
	}

	private void cameraInput(float dt){
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			camera.position.x += CAMERA_SPEED * dt;
			follow = null;
			cameraLabel.setText("Coordinates (Arrow Keys): "
					+ "X : " + camera.position.x + " Y : " + camera.position.y);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			camera.position.x -= CAMERA_SPEED * dt;
			follow = null;
			cameraLabel.setText("Coordinates (Arrow Keys): "
					+ "X : " + camera.position.x + " Y : " + camera.position.y);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			camera.position.y += CAMERA_SPEED * dt;
			follow = null;
			cameraLabel.setText("Coordinates (Arrow Keys): "
					+ "X : " + camera.position.x + " Y : " + camera.position.y);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			camera.position.y -= CAMERA_SPEED * dt;
			follow = null;
			cameraLabel.setText("Coordinates (Arrow Keys): "
					+ "X : " + camera.position.x + " Y : " + camera.position.y);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.Z) && camera.zoom < CAMERA_MAX_ZOOM){
			camera.zoom += CAMERA_ZOOM_SPEED * dt;
			zoomLabel.setText("Zoom (Z / X): " + camera.zoom);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.X) && camera.zoom > CAMERA_MIN_ZOOM){
			camera.zoom -= CAMERA_ZOOM_SPEED * dt;
			zoomLabel.setText("Zoom (Z / X): " + camera.zoom);
		}
	}

	private void interpolateToPlanet(Astro astro){
		interpolating = true;
		follow = astro;
	}

	public static Vector2 gravity(Astro a, Astro b){
		Vector2 r = b.getPosition().sub(a.getPosition());
		r = r.scl(G * a.getMass() * b.getMass() / (r.len2() * r.len()));
		return r;
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
	}

	@Override
	public void dispose () {
		batch.dispose();
		sunTexture.dispose();
		mercuryTexture.dispose();
		venusTexture.dispose();
		earthTexture.dispose();
		marsTexture.dispose();
		jupiterTexture.dispose();
		saturnTexture.dispose();
		uranusTexture.dispose();
		neptuneTexture.dispose();
		halleyEffect.dispose();
		backgroundMusic.dispose();
	}


	@Override
	public boolean keyDown(int keycode) {

		for(int i = 0; i < astros.size - 1; i++){

			if(keycode == 7 + i){ // numbers from 0 to 9
				interpolateToPlanet(astros.get(i));
				return false;
			}
		}

		if(keycode == Input.Keys.H){
			interpolateToPlanet(astros.get(astros.size - 1));
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
