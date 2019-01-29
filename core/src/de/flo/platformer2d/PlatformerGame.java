package de.flo.platformer2d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.File;
import java.io.FilenameFilter;

import de.flo.platformer2d.screens.LevelSelectScreen;
import de.flo.platformer2d.screens.PlayScreen;
import de.flo.platformer2d.screens.SplashScreen;
import de.flo.platformer2d.screens.StartScreen;
import de.flo.platformer2d.utils.Assets;

public class PlatformerGame extends Game {
	SpriteBatch batch;
	private int levelIndex;
	public int maxLevel;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		maxLevel = Gdx.files.internal("Maps/New").list(new FilenameFilter() {
			@Override
			public boolean accept(File file, String s) {
				return s.endsWith(".tmx");
			}
		}).length;

		setScreen(new SplashScreen(batch, this, null));
	}

	public void loadLevel(int index){
		this.levelIndex = index;
		if(getScreen() != null){
			//getScreen().dispose();
		}
		setScreen(new PlayScreen(this, batch, index));
	}

	public void loadNextLevel(){
		levelIndex++;
		Preferences prefs = Gdx.app.getPreferences("level-data");
		if(prefs.getInteger("lastUnlocked", 1) < levelIndex) {
			prefs.putInteger("lastUnlocked", levelIndex);
			prefs.flush();
			Gdx.app.log("GameManager", "Storing LastUnlocked: " + levelIndex);
		}
		if(levelIndex <= maxLevel)
			loadLevel(levelIndex);
		else
			showLevelSelectScreen();
	}

	public void showLevelSelectScreen(){
		setScreen(new LevelSelectScreen(batch, this));
	}

	public void showStartScreen(boolean transition){
		setScreen(new StartScreen(batch, this, transition));
	}


	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		Assets.getInstance().dispose();
	}


	@Override
	public void resume() {
		super.resume();

	}
}
