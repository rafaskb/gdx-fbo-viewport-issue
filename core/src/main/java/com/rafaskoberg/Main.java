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

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;


    private Viewport viewport;
    private OrthographicCamera viewportCamera;
    private OrthographicCamera simpleCamera;

    private FrameBuffer fbo;

    private SpriteBatch batch;
    private Texture screenshot;
    private Texture logo;

    @Override
    public void create() {
        viewportCamera = new OrthographicCamera();
        simpleCamera = new OrthographicCamera();
        viewport = new FitViewport(WIDTH, HEIGHT, viewportCamera);
        viewport.apply();

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, WIDTH, HEIGHT, false);

        batch = new SpriteBatch();
        screenshot = new Texture("screenshot.png");
        logo = new Texture("libgdx.png");
    }

    @Override
    public void render() {
        // Clear screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f); // Dark blue
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw game to FBO
        fbo.begin();
        Gdx.gl.glClearColor(0.15f, 0.815f, 0.2f, 1f); // Green
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawGameScreenshot(1f);
        fbo.end();

        // Draw FBO to screen
        drawFboToScreen();

        // Draw expected result
        drawGameScreenshot(0.1f);
    }

    private void drawGameScreenshot(float alpha) {
        // Apply viewport
        viewport.apply(true);

        // Set batch matrices
        batch.setProjectionMatrix(viewportCamera.projection);
        batch.setTransformMatrix(viewportCamera.view);

        // Draw textures
        batch.begin();
        batch.setColor(1, 1, 1, alpha);
        batch.draw(screenshot, 0, 0, WIDTH, HEIGHT);
        batch.draw(logo, 140, 210);
        batch.end();
    }

    private void drawFboToScreen() {
        // Reset batch matrices
        batch.setProjectionMatrix(simpleCamera.projection);
        batch.setTransformMatrix(simpleCamera.view);

        // End FBO and draw it to the screen
        batch.begin();
        batch.draw(fbo.getColorBufferTexture(), viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(), 0, 0, 1, 1);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        simpleCamera.setToOrtho(false, width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        screenshot.dispose();
        logo.dispose();
    }
}
