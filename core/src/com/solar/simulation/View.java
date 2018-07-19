package com.solar.simulation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class View extends ApplicationAdapter {

	private SpriteBatch batch;

	private Astro sun, earth, moon, mercury, mars, venus,
				  jupiter, saturn, uranus, neptune,
				  halley;

	// constants

	private float WIDTH = 1524, HEIGHT = 864;

	private Viewport viewport;
	private OrthographicCamera camera;

	private Array<Astro> astros;

	private static final float mE = 1;
	private static final float vE = 1;
	private static final float au = 500;
	private static final float dE = 30;

	private static final float mS = 333000 * mE;
	private static final float G = 0.0015f;

	private static final float CAMERA_SPEED = 1000;
	private static final float CAMERA_ZOOM_SPEED = 2;
	private static final float CAMERA_MIN_ZOOM = 0.5f;
	private static final float CAMERA_MAX_ZOOM = 15.0f;

	private Texture sunTexture, mercuryTexture, venusTexture,
					earthTexture, moonTexture, marsTexture, jupiterTexture,
					saturnTexture, uranusTexture, neptuneTexture;

	private ParticleEffect halleyEffect;

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
				mS, dE, astros
		);

		mercury = new Astro(
				new Vector2(0.387f * au, 0),
				new Vector2(0,1.59f * vE),
				0.0553f * mE, 0.383f * dE, astros);

		venus = new Astro(
				new Vector2(0, 0.723f * au),
				new Vector2(-1.18f * vE,0),
				0.815f * mE, 0.949f * dE, astros);

		earth = new Astro(
				new Vector2(0, -au),
				new Vector2(vE, 0), mE, dE, astros);

		moon = new Astro(
				new Vector2(0, -au - 0.0025695f * au),
				new Vector2(0.0343f * vE + vE, 0),
				0.0123f * mE, 0.2724f * dE, astros
		);

		mars = new Astro(
				new Vector2(1.52f * au, 0),
				new Vector2(0,0.808f * vE),
				0.107f * mE, 0.532f * dE, astros);

		jupiter = new Astro(
				new Vector2(0, 5.20f * au),
				new Vector2(-0.439f * vE, 0),
				317.8f * mE, 11.21f * dE, astros);

		saturn = new Astro(
				new Vector2(-9.58f * au, 0),
				new Vector2(0, -0.325f * vE),
				95.2f * mE, 9.45f * dE, astros);

		uranus = new Astro(
				new Vector2(0, -19.20f * au),
				new Vector2(0.228f * vE,0),
				14.5f * mE, 4.01f * dE, astros);

		neptune = new Astro(
				new Vector2(30.05f * au, 0),
				new Vector2(0,0.182f * vE),
				17.1f * mE, 3.88f * dE, astros);

		halley = new Astro(
				new Vector2(35.1f * au, 0),
				new Vector2(0, -0.029496644f * mE),
				0.00001f * mE, dE, astros);

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

		camera = new OrthographicCamera();
		camera.zoom = CAMERA_MIN_ZOOM;
		viewport = new FitViewport(WIDTH, HEIGHT, camera);
		batch = new SpriteBatch();

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			camera.position.x += CAMERA_SPEED * Gdx.graphics.getDeltaTime();
			camera.update();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			camera.position.x -= CAMERA_SPEED * Gdx.graphics.getDeltaTime();
			camera.update();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			camera.position.y += CAMERA_SPEED * Gdx.graphics.getDeltaTime();
			camera.update();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			camera.position.y -= CAMERA_SPEED * Gdx.graphics.getDeltaTime();
			camera.update();
		}

		if(Gdx.input.isKeyPressed(Input.Keys.Z) && camera.zoom < CAMERA_MAX_ZOOM){
			camera.zoom += CAMERA_ZOOM_SPEED * Gdx.graphics.getDeltaTime();
			camera.update();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.X) && camera.zoom > CAMERA_MIN_ZOOM){
			camera.zoom -= CAMERA_ZOOM_SPEED * Gdx.graphics.getDeltaTime();
			camera.update();
		}

		batch.setProjectionMatrix(camera.combined);

		for(int i = 0; i < astros.size; i++)
			astros.get(i).render(batch);

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
	}
}
