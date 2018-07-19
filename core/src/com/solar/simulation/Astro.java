package com.solar.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Astro {

    private Vector2 position;
    private Vector2 velocity;
    private Vector2 acc;

    private float mass;
    private float diameter;

    private Texture texture;
    private ParticleEffect particleEffect;

    private float time;
    private final float MIN_TIME_SPEED = 1.0f;
    private final float MAX_TIME_SPEED = 12000.0f;

    private float line_time = 0.05f;

    private ShapeRenderer sp;

    private Array<Vector2> orbit;

    private float angle = 0;
    private float t = 0;
    private boolean createOrbit = true;
    private boolean debug = true;
    private Array<Astro> astros;

    public Astro(Vector2 position, Vector2 velocity,
                 float mass, float diameter, Array<Astro> astros){
        this.position = position;
        this.velocity = velocity;
        this.mass = mass;
        this.diameter = diameter;
        this.astros = astros;

        sp = new ShapeRenderer();
        orbit = new Array<Vector2>();
        time = 100;
        acc = new Vector2();
    }

    private void update(float dt){

        if(Gdx.input.isKeyJustPressed(Input.Keys.K) && time < MAX_TIME_SPEED)
            time *= 2;

        if(Gdx.input.isKeyJustPressed(Input.Keys.L) && time > MIN_TIME_SPEED)
            time /= 2;

        acc = new Vector2();

        if(astros != null){
            for(Astro a: astros){
                if(a == this)
                    continue;
                acc = acc.add(View.gravity(this, a));
            }
        }

        acc = acc.scl(1 / mass);

        velocity = velocity.add(acc.scl(dt * time));

        position = position.add(new Vector2(velocity).scl(dt * time));

        t += dt * time;

        if(createOrbit) {
            if(t > line_time){
                t -= line_time;
                orbit.add(new Vector2(position));
            }
            angle += velocity.len() / position.len() * dt * time;
        }

        if(angle > 4 * Math.PI) {
            createOrbit = false;
            angle = 0;
            //System.out.println("Orbit Completed");
        }

    }

    public void render(SpriteBatch batch){

        update(Gdx.graphics.getDeltaTime());

        if(Gdx.input.isKeyJustPressed(Input.Keys.D))
            debug = !debug;

        if(debug)
            debug(batch);

        batch.begin();

        if(texture != null){
            batch.draw(texture,
                    position.x - diameter * 2,
                    position.y - diameter * 1.5f,
                    diameter * 4, diameter * 3
                    );
        }

        if(particleEffect != null){
            particleEffect.setPosition(position.x, position.y);
            particleEffect.draw(batch, Gdx.graphics.getDeltaTime());

            float ang = velocity.angle();

            rotateBy(ang + 180);

        }

        batch.end();

    }

    private float SCALE = 100f;

    private void debug(SpriteBatch batch){

        sp.setProjectionMatrix(batch.getProjectionMatrix());

        sp.setColor(Color.WHITE);
        sp.begin(ShapeRenderer.ShapeType.Line);

        for(int i = 0; i < orbit.size - 1; i++){
            sp.line(orbit.get(i), orbit.get(i + 1));
        }

        // draw velocity vector

        sp.setColor(Color.GREEN);

        Vector2 pos = new Vector2(position);
        Vector2 vel = new Vector2(velocity).nor().scl(SCALE);
        Vector2 arrow = new Vector2(pos).add(vel);

        sp.line(pos, arrow);

        // draw gravity vector

        sp.setColor(Color.RED);

        pos = new Vector2(position);
        Vector2 gravity = new Vector2(acc).nor().scl(SCALE / 2);
        arrow = new Vector2(pos).add(gravity);

        sp.line(pos, arrow);

        sp.end();
    }

    public void setTexture(Texture texture){
        this.texture = texture;
    }

    public void setParticleEffect(ParticleEffect effect){
        this.particleEffect = effect;
    }

    private void rotateBy(float amountInDegrees) {
        Array<ParticleEmitter> emitters = particleEffect.getEmitters();
        for (int i = 0; i < emitters.size; i++) {
            ParticleEmitter.ScaledNumericValue val = emitters.get(i).getAngle();
            float amplitude = (val.getHighMax() - val.getHighMin()) / 2f;
            float h1 = amountInDegrees + amplitude;
            float h2 = amountInDegrees - amplitude;
            val.setHigh(h1, h2);
            val.setLow(amountInDegrees);
        }
    }

    public Vector2 getPosition(){
        return new Vector2(position);
    }

    public Vector2 getVelocity(){
        return new Vector2(velocity);
    }

    public float getMass() {
        return mass;
    }
}
