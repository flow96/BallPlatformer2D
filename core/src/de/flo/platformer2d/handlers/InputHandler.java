package de.flo.platformer2d.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;


import de.flo.platformer2d.actors.Player;

public class InputHandler implements InputProcessor{

    private Player player;



    public InputHandler(Player player){
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
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
        if(screenX < Gdx.graphics.getWidth() / 2)
            player.touches.put(pointer, -1);       // Left
        else
            player.touches.put(pointer, 1);        // Right
        // if(player.touches.size() == 2)
        //     player.jump();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        player.touches.remove(pointer);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        try {
            if (screenX < Gdx.graphics.getWidth() / 2 && player.touches.get(pointer) != -1) {
                player.touches.put(pointer, -1);
            } else if (screenX > Gdx.graphics.getWidth() / 2 && player.touches.get(pointer) != 1)
                player.touches.put(pointer, 1);
        }catch (Exception e){}
        return true;
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
