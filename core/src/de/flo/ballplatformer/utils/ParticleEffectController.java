package de.flo.ballplatformer.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class ParticleEffectController {

    private static ParticleEffectController instance;

    private ParticleEffect effect;

    public boolean emit = false;
    private float dur;

    public static ParticleEffectController getInstance(){
        if(instance == null)
            instance = new ParticleEffectController();
        return instance;
    }

    private ParticleEffectController(){
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("Particles/RollParticle.p"), Gdx.files.internal("Particles/"));
        //effect.start();
        effect.scaleEffect(1/ de.flo.ballplatformer.constants.Constants.PPM);
        dur = effect.getEmitters().first().duration;
    }

    public void update(float delta){
        effect.update(delta);
    }

    public void draw(Batch batch){
        effect.draw(batch);
    }

    public void setPos(float x, float y){
        effect.getEmitters().first().setPosition(x, y);

    }

    public void setEmit(boolean em){
        if(emit && !em && !effect.isComplete()){
            effect.getEmitters().first().allowCompletion();
        }
        if(em && !emit){
            effect.start();
        }
        emit = em;
    }


}
