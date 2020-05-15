package food.user.demand.FCViews;

import android.view.animation.Interpolator;

public class MyBounceInterpolator implements Interpolator {

    private double aAmplitude = 1;
    private double mFrequency = 10;

    public MyBounceInterpolator(double aAmplitude, double mFrequency) {
        this.aAmplitude = aAmplitude;
        this.mFrequency = mFrequency;
    }

    @Override
    public float getInterpolation(float input) {
        return (float) (-1 * Math.pow(Math.E, -input / aAmplitude) * Math.cos(mFrequency * input) + 1);
    }
}