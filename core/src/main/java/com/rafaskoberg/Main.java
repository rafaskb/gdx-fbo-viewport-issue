package com.rafaskoberg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    // Internal game size
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;


    // Cameras and viewports
    private Viewport viewport;
    private OrthographicCamera camera;

    // FBO
    private FrameBuffer fbo;

    // Rendering
    private SpriteBatch batch;
    private Texture screenshot;

    @Override
    public void create() {
        // Create viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        viewport.apply();

        // Create FBO
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, WIDTH, HEIGHT, false);

        // Create rendering stuff
        batch = new SpriteBatch();
        screenshot = new Texture("screenshot.png");
    }

    @Override
    public void render() {
        // Clear screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f); // Dark blue
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Apply viewport
        viewport.apply(false);
        batch.setProjectionMatrix(camera.combined);

        // Draw game to FBO
        fbo.begin();
        Gdx.gl.glClearColor(0.15f, 0.815f, 0.2f, 1f); // Green
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.setColor(1, 1, 1, 1f);
        batch.draw(screenshot, 0, 0, WIDTH, HEIGHT);
        batch.end();
        fbo.end();

        // Apply viewport
        viewport.apply(true);
        batch.setProjectionMatrix(camera.combined);

        // Draw FBO to screen
        batch.begin();
        batch.draw(fbo.getColorBufferTexture(), 0, 0, WIDTH, HEIGHT, 0, 0, 1, 1);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        fbo.dispose();
        batch.dispose();
        screenshot.dispose();
    }
}
