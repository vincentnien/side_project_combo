package com.a30corner.combomaster.playground.effect;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.sprite.Sprite;

public class Shake {

    public static final float DEFAULT_DURATION = 0.6f;
    
    float duration;
    float frequency;
    float amplitude;
    List<Double> samples;
    
    private Shake(float duration, float frequency, float amplitude) {
        this.duration = duration;
        this.frequency = frequency;
        this.amplitude = amplitude;
    }
    
    public float[] generate() {
        float sampleCount = (duration / 1000.0f) * frequency;
        
        samples = new ArrayList<Double>();
        for(int i=0; i<sampleCount; ++i) {
            samples.add(Math.random()*2-1);
        }
        
        float[] data = new float[(int)sampleCount+1];
        for(int i=0; i<sampleCount; ++i) {
            int t = (int)(i * 1000 / frequency);
            
            float x = (float)(amplitude(t) * this.amplitude);
            data[i] = x;
        }
        
        return data;
    }
    
    public float[] generate(float basis) {
        float[] data = generate();
        float[] dataWithBasis = new float[data.length];
        for(int i=0; i<data.length; ++i) {
            dataWithBasis[i] = data[i] + basis;
        }
        return dataWithBasis;
    }
    
    private double amplitude(long t) {
        double s = t * frequency / 1000;
        int s0 = (int)Math.floor(s);
        int s1 = s0 + 1;
        
        double k = decay(t);
        
        return (noise(s0) + (s-s0)*(noise(s1) - noise(s0))) * k;
    }
    
    private double decay(long t) {
        if ( t >= duration ) {
            return 0.0;
        } else {
            return (duration - t) / duration;
        }
        
    }
    
    private double noise(int s) {
        if (s>=samples.size()) {
            return 0;
        }
        return samples.get(s);
    }
    
    // I think this is not a good function
    public static void apply(Sprite s, float duration, float frequency, float amplitude) {
        float animDuration = DEFAULT_DURATION;
        final float x = s.getX();
        final float y = s.getY();
        float[] xcoord = create(duration, frequency, amplitude).generate(x);
        float[] ycoord = create(duration, frequency, amplitude).generate(y);
        Path path = new Path(xcoord, ycoord);
        PathModifier modifier = new PathModifier(animDuration, path){
            @Override
            protected void onModifierFinished(IEntity pItem) {
                super.onModifierFinished(pItem);
                pItem.setPosition(x, y);
            }
        };
        modifier.setAutoUnregisterWhenFinished(true);
        s.registerEntityModifier(modifier);
    }
    
    public static Shake create(float duration, float frequency, float amplitude) {
        return new Shake(duration, frequency, amplitude);
    }
}
