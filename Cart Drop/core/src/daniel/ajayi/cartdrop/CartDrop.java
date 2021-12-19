package daniel.ajayi.cartdrop;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

public class CartDrop extends ApplicationAdapter {

	SpriteBatch batch;

	int screenHeight;

	int screenWidth;

	OrthographicCamera camera;

	Viewport viewport;

	private Stage stage;

	FreeTypeFontGenerator generator;

	FreeTypeFontGenerator.FreeTypeFontParameter parameter;

	BitmapFont gameFont;

	Texture bg;

	Texture grass;

	Texture cart;

	boolean[] progenityCreated;

	Texture[] clouds;

	Texture[] fruit;

	Rectangle fruitRect;

	Rectangle cart1Rect;

	Rectangle cart2Rect;

	Texture fruitBox;

	Texture black;

	Sprite blackSprite;

	float blackOpacity;

	Texture okTexture;

	Texture pauseTexture;

	Texture startTexture;

	ImageButton okBtn;

	ImageButton pauseBtn;

	ImageButton startBtn;

	int gameState;

	boolean vanish1;

	boolean vanish2;

	final int PLAY_SCREEN = 0;

	final int GAME_SCREEN = 1;

	final int GAME_OVER = 2;

	int randomFruit;

	Random rand;

	int cart1XPos;

	int cart2XPos;

	double cartSpeed;

	int cloudSpeed;

	int[] cloudXPos;

	Music buttonTap;

	Music scoreSfx;

	Music scoreSfx2;

	Music gameMusic;

	int cartsOnScreen;

	double fruitYPosition;

	double fruitYVelocity;

	boolean initialTouch;

	double fruitYAcceleration;

	boolean fruitDescending;

	int[] randomCloud;

	int randomFruit1;

	int randomFruit2;

	Preferences prefs;

	int score;

	int highScore;

	boolean paused;

	GlyphLayout textLayout;

	int oldRandomFruit;




	@Override

	public void create () {

		batch = new SpriteBatch();

		FreeTypeFontGenerator.setMaxTextureSize(3000);

		prefs = Gdx.app.getPreferences("My Preferences");

		highScore = prefs.getInteger("high score", 0);

		generator = new FreeTypeFontGenerator(Gdx.files.internal("arcadeclasic.ttf"));

		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.size = 500;

		gameFont = generator.generateFont(parameter);

		gameFont.getData().setScale(.08f);

		gameFont.setColor(Color.WHITE);

		paused = false;

		screenHeight = 1794;

		screenWidth = 1080;

		rand = new Random();

		randomCloud = new int[5];

		cloudXPos = new int[5];

		for (int i = 0; i < 5; i++) {

			randomCloud[i] = rand.nextInt(7);

			cloudXPos[i] = 200 * i;

		}

		randomFruit = rand.nextInt(6);

		randomFruit1 = -1;

		randomFruit2 = -1;

		camera = new OrthographicCamera();

		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

		viewport = new StretchViewport(1080, 1794, camera);

		stage = new Stage(viewport, batch); //Set up a stage for the ui

		stage.setViewport(viewport);

		stage.getViewport().update(screenWidth, screenHeight);

		bg = new Texture("sky.png");

		grass = new Texture("grass2.png");

		black = new Texture("black.png");

		blackSprite = new Sprite(black);

		blackOpacity = 0f;

		initialTouch = true;

		fruit = new Texture[6];

		fruit[0] = new Texture("apple.png");

		fruit[1] = new Texture("banana.png");

		fruit[2] = new Texture("strawberry.png");

		fruit[3] = new Texture("watermelon.png");

		fruit[4] = new Texture("grapes.png");

		fruit[5] = new Texture("orange.png");

		cart = new Texture("cart.png");

		cart1XPos = -(int)(cart.getWidth() * .7);

		cart2XPos = -(int)(cart.getWidth() * .7 * 1.5) - screenWidth / 2;

		clouds = new Texture[7];

		clouds[0] = new Texture("cloud1.png");

		clouds[1] = new Texture("cloud2.png");

		clouds[2] = new Texture("cloud3.png");

		clouds[3] = new Texture("cloud4.png");

		clouds[4] = new Texture("cloud5.png");

		clouds[5] = new Texture("cloud6.png");

		clouds[6] = new Texture("cloud7.png");

		pauseTexture = new Texture("pause.png");

		okTexture = new Texture("ok.png");

		fruitBox = new Texture("fruitbox.png");

		startTexture = new Texture("start.png");

		gameState = PLAY_SCREEN;

		vanish1 = false;

		cloudSpeed = 2;

		vanish2 = false;

		Drawable playDrawable = new TextureRegionDrawable(new TextureRegion(startTexture));

		startBtn = new ImageButton(playDrawable);

		startBtn.setSize((int)(startTexture.getWidth() * .8), (int)(startTexture.getHeight() * .8));

		startBtn.setPosition((int)(screenWidth / 2 - startTexture.getWidth() * .8 / 2), (int)(screenHeight - startTexture.getHeight() * .8) / 2);

		Drawable okDrawable = new TextureRegionDrawable(new TextureRegion(okTexture));

		okBtn = new ImageButton(okDrawable);

		okBtn.setSize((int)(okTexture.getWidth() * .8), (int)(okTexture.getHeight() * .8));

		okBtn.setPosition((int)(screenWidth / 2 - okTexture.getWidth() * .8 / 2), (int)(screenHeight - startTexture.getHeight() * .8) / 2 - 200);

		Drawable pauseDrawable = new TextureRegionDrawable(new TextureRegion(pauseTexture));

		pauseBtn = new ImageButton(pauseDrawable);

		pauseBtn.setSize((int)(pauseTexture.getWidth() * .8), (int)(pauseTexture.getHeight() * .8));

		pauseBtn.setPosition((screenWidth - 55 - (int)(pauseTexture.getWidth() * .8)), (int) (screenHeight - 55 - (int)(pauseTexture.getHeight() * .8)));

		stage.addActor(startBtn);

		stage.addActor(okBtn);

		stage.addActor(pauseBtn);

		startBtn.setVisible(false);

		okBtn.setVisible(false);

		pauseBtn.setVisible(false);

		Gdx.input.setInputProcessor(stage);

		viewport.apply();

		stage.getViewport().apply();

		stage.act();

		cartSpeed = 6.25;

		fruitYPosition = 0;

		fruitYVelocity = 0;

		fruitYAcceleration = .75;

		buttonTap = Gdx.audio.newMusic(Gdx.files.internal("button_click.ogg"));

		scoreSfx = Gdx.audio.newMusic(Gdx.files.internal("to_the_point.ogg"));

		scoreSfx2 = Gdx.audio.newMusic(Gdx.files.internal("to_the_point.ogg"));

		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("kill_it.ogg"));

		gameMusic.setLooping(true);

		gameMusic.play();

		fruitDescending = false;

		textLayout = new GlyphLayout();

		oldRandomFruit = randomFruit;



	}

	@Override

	public void render () {

		camera.update();

		stage.getCamera().update();

		Gdx.gl.glClearColor(1, 0, 0, 1);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);

		stage.getBatch().setProjectionMatrix(camera.combined);

		batch.begin();

		stage.act(Gdx.graphics.getDeltaTime());

		batch.draw(bg, 0, 0, screenWidth, screenHeight);

		// Bottommost row of clouds

		batch.draw(clouds[randomCloud[0]], cloudXPos[0], 360, (int)(clouds[randomCloud[0]].getWidth() * .7), (int)(clouds[randomCloud[0]].getHeight() * .7));

		// one up from the bottommost row

		batch.draw(clouds[randomCloud[1]], screenWidth - (int)(clouds[randomCloud[1]].getWidth() * .7) - cloudXPos[1], 720, (int)(clouds[randomCloud[1]].getWidth() * .7), (int)(clouds[randomCloud[1]].getHeight() * .7));

		batch.draw(clouds[randomCloud[2]], cloudXPos[2], 1080, (int)(clouds[randomCloud[2]].getWidth() * .7), (int)(clouds[randomCloud[2]].getHeight() * .7));

		batch.draw(clouds[randomCloud[3]], screenWidth - (int)(clouds[randomCloud[3]].getWidth() * .7) - cloudXPos[3], 1440,(int)(clouds[randomCloud[3]].getWidth() * .7), (int)(clouds[randomCloud[3]].getHeight() * .7));

		batch.draw(clouds[randomCloud[4]], cloudXPos[4], 1800, (int)(clouds[randomCloud[4]].getWidth() * .7), (int)(clouds[randomCloud[4]].getHeight() * .7));


		for (int i = 0; i < 5; i++) {

			if (!paused) {

				cloudXPos[i] += cloudSpeed;

			}

			if (cloudXPos[i] >= screenWidth) {

				randomCloud[i] = rand.nextInt(7);

				cloudXPos[i] = -(int)(clouds[randomCloud[i]].getWidth() * .7);

			}

		}


		batch.draw(grass,0,0,screenWidth,grass.getHeight());

		fruitRect = new Rectangle((int)(screenWidth - fruit[0].getWidth() * .38) / 2, (int)(screenHeight - 30 - fruit[0].getHeight() * .38 - fruitYPosition), (int)(fruit[0].getWidth() * .38),(int)(fruit[0].getWidth() * .38));

		batch.draw(fruit[randomFruit],(int)(screenWidth - fruit[0].getWidth() * .38) / 2, (int)(screenHeight - 30 - fruit[0].getHeight() * .38 - fruitYPosition), (int)(fruit[0].getWidth() * .38),(int)(fruit[0].getWidth() * .38));
//
//		if(!vanish1)

		batch.draw(cart, cart1XPos, 200, (int)(cart.getWidth() * .7), (int)(cart.getHeight() * .7));
//
//		if(!vanish2)

		batch.draw(cart, cart2XPos, 200, (int)(cart.getWidth() * .7), (int)(cart.getHeight() * .7));

		cart1Rect = new Rectangle(cart1XPos, 200, (int)(cart.getWidth() * .7), (int)(cart.getHeight() * .7));

		cart2Rect = new Rectangle(cart2XPos, 200, (int)(cart.getWidth() * .7), (int)(cart.getHeight() * .7));

		if (!paused && gameState != GAME_OVER) {

			cart1XPos += cartSpeed;

			cart2XPos += cartSpeed;

		}

		//if the cart goes off screen, change the fruit on the carts before they come back on

		if (cart1XPos > screenWidth + cartSpeed * 3) {

			if (randomFruit1 == oldRandomFruit && gameState == GAME_SCREEN && !vanish1) {

				gameState = GAME_OVER;

			}

			vanish1 = false;

			cart1XPos = -(int)(cart.getWidth() * .7);

			if (rand.nextDouble() < .65) {

				randomFruit1 = randomFruit;

			} else {

				randomFruit1 = rand.nextInt(6);

			}

			oldRandomFruit = randomFruit;

			if (gameState == PLAY_SCREEN) {

				randomFruit1 = -1;

			}


		}

		if (cart2XPos > screenWidth + cartSpeed * 3) {

			if (randomFruit2 == oldRandomFruit && gameState == GAME_SCREEN && !vanish2) {

				gameState = GAME_OVER;

			}

			vanish2 = false;

			cart2XPos = -(int)(cart.getWidth() * .7);

			if (rand.nextDouble() < .65) {

				randomFruit2 = randomFruit;

			} else {

				randomFruit2 = rand.nextInt(6);

			}

			oldRandomFruit = randomFruit;

			if (gameState == PLAY_SCREEN) {

				randomFruit2 = -1;

			}

		}

		if (gameState == PLAY_SCREEN) {

			gameFont.getData().setScale(.15f);

			gameFont.setColor(255,248,0,1);

			textLayout.setText(gameFont, "High Score  " + highScore);

			gameFont.draw(batch, textLayout, (screenWidth / 2) - (textLayout.width / 2)+5, (int)(screenHeight / 2 + 125));

			gameFont.setColor(Color.WHITE);

			textLayout.setText(gameFont, "High Score  " + highScore);

			gameFont.draw(batch, textLayout, (screenWidth / 2) - (textLayout.width / 2), (int)(screenHeight / 2 + 130));

			startBtn.setVisible(true);

			startBtn.addListener(new InputListener() {

				@Override

				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

					buttonTap.play();

					gameState = GAME_SCREEN;

					startBtn.setVisible(false);

					pauseBtn.setVisible(true);

					randomFruit1 = rand.nextInt(6);

					randomFruit2 = rand.nextInt(6);

					while(randomFruit1 == randomFruit || randomFruit2 == randomFruit) {

						randomFruit1 = rand.nextInt(6);

						randomFruit2 = rand.nextInt(6);

					}

					return true;

				}

				@Override

				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

				}

			});

		} else if (gameState == GAME_SCREEN) {

			pauseBtn.addListener(new InputListener() {

				@Override

				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

					buttonTap.play();

					if(paused) {

						pauseBtn.setSize((int)(pauseTexture.getWidth() * .8), (int)(pauseTexture.getHeight() * .8));

						pauseBtn.setPosition((screenWidth - 55 - (int)(pauseTexture.getWidth() * .8)), (int) (screenHeight - 55 - (int)(pauseTexture.getHeight() * .8)));

						paused = false;

						blackOpacity = 0f;

					} else {

						paused = true;

						initialTouch = true;

					}

					return true;

				}

				@Override

				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

				}

			});

			//Display score

			if (score < 100) {

				gameFont.getData().setScale(.5f);

			} else if (score < 1000) {

				gameFont.getData().setScale(.4f);

			} else if (score < 1000) {

				gameFont.getData().setScale(.3f);

			} else if (score < 10000) {

				gameFont.getData().setScale(.2f);

			}

			gameFont.setColor(255,248,0,1);

			gameFont.draw(batch,score + "",(int) (55 + 20), (int) (screenHeight - 55 - 20));

			gameFont.setColor(Color.WHITE);

			gameFont.draw(batch,score + "",(int) (55), (int) (screenHeight - 55));

			//Draw the little white fruit boxes

			batch.draw(fruitBox, (int)(cart1XPos + cart.getWidth() * .7 / 2 - fruitBox.getWidth() * .7 /2 + 28), 280, (int)(fruitBox.getWidth() * .7), (int)(fruitBox.getHeight() * .7));

			batch.draw(fruitBox, (int)(cart2XPos + cart.getWidth() * .7 / 2 - fruitBox.getWidth() * .7 /2 + 28), 280, (int)(fruitBox.getWidth() * .7), (int)(fruitBox.getHeight() * .7));

			//Draw the little fruit on each of the white boxes

			batch.draw(fruit[randomFruit1], (int)(cart1XPos + cart.getWidth() * .7 / 2 - fruitBox.getWidth() * .7 /2 + 28 + fruitBox.getWidth() * .7 / 2 - (fruitBox.getWidth() * .3)), (int)(280 + fruitBox.getWidth() * .7 / 2 - (fruitBox.getWidth() * .3)), (int)(fruitBox.getWidth() * .6), (int)(fruitBox.getHeight() * .6));

			batch.draw(fruit[randomFruit2], (int)(cart2XPos + cart.getWidth() * .7 / 2 - fruitBox.getWidth() * .7 /2 + 28 + fruitBox.getWidth() * .7 / 2 - (fruitBox.getWidth() * .3)), (int)(280 + fruitBox.getWidth() * .7 / 2 - (fruitBox.getWidth() * .3)), (int)(fruitBox.getWidth() * .6), (int)(fruitBox.getHeight() * .6));

			if(Gdx.input.justTouched() && !initialTouch && !paused) {

				fruitDescending = true;

			} else if (Gdx.input.justTouched()) {

				initialTouch = false;

			}

			if (fruitDescending && !paused) {

				fruitYVelocity += fruitYAcceleration;

				fruitYPosition += fruitYVelocity;

			}

			if (fruitYPosition >= 1400) {

				gameState = GAME_OVER;

				fruitDescending = false;

				fruitYPosition = 0;

				fruitYVelocity = 0;

				randomFruit = rand.nextInt(6);

			}

			if (Intersector.overlaps(fruitRect,cart1Rect)) {

				if (randomFruit == randomFruit1) {

					if (scoreSfx.isPlaying()) {

						scoreSfx2.play();

					} else {

						scoreSfx.play();

					}

					score++;

					vanish1 = true;

					cartSpeed += .35;

					fruitDescending = false;

					fruitYPosition = 0;

					fruitYVelocity = 0;

					randomFruit = rand.nextInt(6);

				} else {

					gameState = GAME_OVER;

					pauseBtn.setVisible(false);

				}

			} else if (Intersector.overlaps(fruitRect,cart2Rect)) {

				if (randomFruit == randomFruit2) {

					if (scoreSfx.isPlaying()) {

						scoreSfx2.play();

					} else {

						scoreSfx.play();

					}

					score++;

					vanish2 = true;

					cartSpeed += .35;

					fruitDescending = false;

					fruitYPosition = 0;

					fruitYVelocity = 0;

					randomFruit = rand.nextInt(6);

				} else {

					gameState = GAME_OVER;

					pauseBtn.setVisible(false);

				}

			}

			if (paused) {

				blackOpacity = .5f;

				blackSprite.draw(batch, blackOpacity);

				pauseBtn.setSize((int)(pauseTexture.getWidth() * 5), (int)(pauseTexture.getHeight() * 5));

				pauseBtn.setPosition((screenWidth - pauseTexture.getWidth() * 5) / 2, (screenHeight - pauseTexture.getHeight() * 5) / 2);

				pauseBtn.draw(batch,1);

			}



		} else if (gameState == GAME_OVER) {

			if (score > highScore) {

				prefs.putInteger("high score", score);

				prefs.flush();

				highScore = prefs.getInteger("high score", 0);

			}

			//Draw the little white fruit boxes

			batch.draw(fruitBox, (int)(cart1XPos + cart.getWidth() * .7 / 2 - fruitBox.getWidth() * .7 /2 + 28), 280, (int)(fruitBox.getWidth() * .7), (int)(fruitBox.getHeight() * .7));

			batch.draw(fruitBox, (int)(cart2XPos + cart.getWidth() * .7 / 2 - fruitBox.getWidth() * .7 /2 + 28), 280, (int)(fruitBox.getWidth() * .7), (int)(fruitBox.getHeight() * .7));

			//Draw the little fruit on each of the white boxes

			batch.draw(fruit[randomFruit1], (int)(cart1XPos + cart.getWidth() * .7 / 2 - fruitBox.getWidth() * .7 /2 + 28 + fruitBox.getWidth() * .7 / 2 - (fruitBox.getWidth() * .3)), (int)(280 + fruitBox.getWidth() * .7 / 2 - (fruitBox.getWidth() * .3)), (int)(fruitBox.getWidth() * .6), (int)(fruitBox.getHeight() * .6));

			batch.draw(fruit[randomFruit2], (int)(cart2XPos + cart.getWidth() * .7 / 2 - fruitBox.getWidth() * .7 /2 + 28 + fruitBox.getWidth() * .7 / 2 - (fruitBox.getWidth() * .3)), (int)(280 + fruitBox.getWidth() * .7 / 2 - (fruitBox.getWidth() * .3)), (int)(fruitBox.getWidth() * .6), (int)(fruitBox.getHeight() * .6));


			blackSprite.draw(batch, blackOpacity);

			if (blackOpacity < .5) {

				blackOpacity += .01;

			} else {

				//Display "you scored"

				gameFont.getData().setScale(.3f);

				gameFont.setColor(Color.WHITE);

				textLayout.setText(gameFont, "You  scored");

				gameFont.draw(batch, textLayout, (screenWidth / 2) - (textLayout.width / 2), (int)(screenHeight / 2 + 400));

				//Display score golden yellow

				gameFont.getData().setScale(1f);

				gameFont.setColor(255,248,0,1);

				textLayout.setText(gameFont, score + "");

				gameFont.draw(batch, textLayout, (screenWidth / 2) - (textLayout.width / 2) + 15, (int)(screenHeight / 2 - textLayout.height  / 2) - 15 + 300);

				//Display score white

				gameFont.setColor(Color.WHITE);

				textLayout.setText(gameFont, score + "");

				gameFont.draw(batch, textLayout, (int)((screenWidth / 2) - (textLayout.width / 2)), (int)(screenHeight / 2 - textLayout.height / 2 + 300));

				okBtn.setVisible(true);





			}

			okBtn.addListener(new InputListener() {

				@Override

				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

					buttonTap.play();

					gameState = PLAY_SCREEN;

					okBtn.setVisible(false);

					startBtn.setVisible(false);

					pauseBtn.setVisible(false);

					for (int i = 0; i < 5; i++) {

						randomCloud[i] = rand.nextInt(7);

						cloudXPos[i] = 200 * i;

					}

					randomFruit1 = rand.nextInt(6);

					randomFruit2 = rand.nextInt(6);

					blackOpacity = 0f;

					initialTouch = true;

					cart1XPos = -(int)(cart.getWidth() * .7);

					cart2XPos = -(int)(cart.getWidth() * .7 * 1.5) - screenWidth / 2;

					randomFruit = rand.nextInt(6);

					cartSpeed = 6.25;

					fruitYPosition = 0;

					fruitYVelocity = 0;

					fruitYAcceleration = .75;

					score = 0;

					fruitDescending = false;

					return false;

				}

				@Override

				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

					gameState = PLAY_SCREEN;

					okBtn.setVisible(false);

					startBtn.setVisible(false);

					pauseBtn.setVisible(false);

					for (int i = 0; i < 5; i++) {

						randomCloud[i] = rand.nextInt(7);

						cloudXPos[i] = 200 * i;

					}

					randomFruit1 = rand.nextInt(6);

					randomFruit2 = rand.nextInt(6);

					blackOpacity = 0f;

					initialTouch = true;

					cart1XPos = -(int)(cart.getWidth() * .7);

					cart2XPos = -(int)(cart.getWidth() * .7 * 1.5) - screenWidth / 2;

					randomFruit = rand.nextInt(6);

					cartSpeed = 6.25;

					fruitYPosition = 0;

					fruitYVelocity = 0;

					fruitYAcceleration = .75;

					fruitDescending = false;

					oldRandomFruit = randomFruit;

				}

			});

		}

		batch.end();

		stage.draw();

	}
	
	@Override

	public void dispose () {

		batch.dispose();

		stage.dispose();

	}

	public void resize(int width, int height) {

		viewport.update(width, height);

		stage.getViewport().update(width, height, true);

		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

		stage.getCamera().position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

	}

}
