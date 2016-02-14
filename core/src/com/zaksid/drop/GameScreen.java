package com.zaksid.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    private final Drop game;

    private final int BUCKET_WIDTH = 64;
    private final int BUCKET_HEIGHT = 64;
    private final int SPAWN_INTERVAL = 1000000000;

    private OrthographicCamera camera;
    private Texture dropImage;
    private Texture bucketImage;
    private Sound dropSound;
    private Music rainMusic;
    private Rectangle bucket;
    private Vector3 touchPosition;
    private Array<Rectangle> raindrops;

    private long lastDropTime;
    private int caughtDrops;
    private int spawnedDrops;

    public GameScreen(final Drop game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Drop.SCREEN_WIDTH, Drop.SCREEN_HEIGHT);

        touchPosition = new Vector3();

        dropImage = new Texture("droplet.png");
        bucketImage = new Texture("bucket.png");

        dropSound = Gdx.audio.newSound(Gdx.files.internal("waterdrop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("undertreeinrain.mp3"));
        rainMusic.setLooping(true);
        rainMusic.play();

        bucket = new Rectangle();
        bucket.x = Drop.SCREEN_WIDTH / 2 - BUCKET_WIDTH / 2;
        bucket.y = 20;
        bucket.width = BUCKET_WIDTH;
        bucket.height = BUCKET_HEIGHT;

        raindrops = new Array<Rectangle>();
        spawnRaindrop();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.549f, 0.62f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        String caughtDropsInfo = String.format("Drops caught: %d of %d ", caughtDrops, spawnedDrops);
        game.font.draw(game.batch, caughtDropsInfo, 0, 480);
        game.batch.draw(bucketImage, bucket.x, bucket.y);

        for (Rectangle raindrop : raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y);
        }

        game.batch.end();

        if (Gdx.input.isTouched()) {
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            bucket.x = touchPosition.x - BUCKET_WIDTH / 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            bucket.x -= 200 * Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            bucket.x += 200 * Gdx.graphics.getDeltaTime();

        if (bucket.x < 0)
            bucket.x = 0;

        if (bucket.x > Drop.SCREEN_WIDTH - BUCKET_WIDTH)
            bucket.x = Drop.SCREEN_WIDTH - BUCKET_WIDTH;

        if (TimeUtils.nanoTime() - lastDropTime > SPAWN_INTERVAL)
            spawnRaindrop();

        Iterator<Rectangle> iterator = raindrops.iterator();
        while (iterator.hasNext()) {
            Rectangle raindrop = iterator.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();

            if (raindrop.y + 64 < 0) {
                iterator.remove();
            }

            if (raindrop.overlaps(bucket)) {
                caughtDrops++;
                dropSound.play();
                iterator.remove();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
    }

    @Override
    public void show() {
        rainMusic.play();
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, Drop.SCREEN_WIDTH - BUCKET_WIDTH);
        raindrop.y = Drop.SCREEN_HEIGHT;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
        spawnedDrops++;
    }
}
