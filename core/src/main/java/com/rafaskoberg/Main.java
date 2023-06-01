package com.rafaskoberg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.ChainVfxEffect;
import com.crashinvaders.vfx.effects.ChromaticAberrationEffect;
import com.crashinvaders.vfx.effects.FilmGrainEffect;
import com.crashinvaders.vfx.effects.GaussianBlurEffect;

import java.util.ArrayList;
import java.util.List;

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

    // Rendering
    private SpriteBatch batch;
    private Texture screenshot;

    // FBO & VFX
    private FrameBuffer fbo;
    private VfxManager vfxManager;
    private List<ChainVfxEffect> vfxEffects = new ArrayList<>();

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

        // Create VFX
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888, WIDTH, HEIGHT);
        {
            ChainVfxEffect vfxEffect = new GaussianBlurEffect();
            vfxManager.addEffect(vfxEffect);
            vfxEffects.add(vfxEffect);
        }
        {
            ChainVfxEffect vfxEffect = new ChromaticAberrationEffect(5);
            vfxManager.addEffect(vfxEffect);
            vfxEffects.add(vfxEffect);
        }
        {
            ChainVfxEffect vfxEffect = new FilmGrainEffect();
            vfxManager.addEffect(vfxEffect);
            vfxEffects.add(vfxEffect);
        }

    }

    @Override
    public void render() {
        // Clear screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f); // Dark blue
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Apply viewport
        viewport.apply(false);
        batch.setProjectionMatrix(camera.combined);

        // Begin FBO
        /*
        fbo.begin();
        Gdx.gl.glClearColor(0.15f, 0.815f, 0.2f, 1f); // Green
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        */

        // Begin VFX
        vfxManager.update(Gdx.graphics.getDeltaTime());
        vfxManager.cleanUpBuffers(); // Clean up internal buffers, as we don't need any information from the last render.
        vfxManager.beginInputCapture(); // Begin render to an off-screen buffer.

        // Draw game
        batch.begin();
        batch.setColor(1, 1, 1, 1f);
        batch.draw(screenshot, 0, 0, WIDTH, HEIGHT);
        batch.end();

        // End FBO
        /*
        fbo.end();
         */

        // End VFX
        vfxManager.endInputCapture(); // End render to an off-screen buffer.
        vfxManager.applyEffects(); // Apply the effects chain to the captured frame.

        // Apply viewport
        viewport.apply(true);
        batch.setProjectionMatrix(camera.combined);

        // Draw FBO to screen
        /*
        batch.begin();
        batch.draw(fbo.getColorBufferTexture(), 0, 0, WIDTH, HEIGHT, 0, 0, 1, 1);
        batch.end();
         */

        // Draw VFX to the screen
        vfxManager.renderToScreen(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        vfxManager.resize(width, height);
    }

    @Override
    public void dispose() {
        fbo.dispose();
        batch.dispose();
        screenshot.dispose();
        vfxManager.dispose();
        vfxEffects.forEach(Disposable::dispose);
    }
}
