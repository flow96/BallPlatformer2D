package de.flo.platformer2d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.File;
import java.io.FilenameFilter;

import de.flo.platformer2d.handlers.InputHandler;
import de.flo.platformer2d.screens.PlayScreen;

public class PlatformerGame extends Game {
	SpriteBatch batch;
	private int levelIndex;
	public int maxLevel;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		maxLevel = Gdx.files.internal("Maps").list(new FilenameFilter() {
			@Override
			public boolean accept(File file, String s) {
				return s.endsWith(".tmx");
			}
		}).length;

		loadLevel(1);
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
		if(levelIndex <= maxLevel)
			loadLevel(levelIndex);
		else
			loadLevel(1);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
