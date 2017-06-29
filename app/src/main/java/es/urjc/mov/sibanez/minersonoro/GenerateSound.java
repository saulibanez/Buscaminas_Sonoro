package es.urjc.mov.sibanez.minersonoro;

import android.os.Handler;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class GenerateSound {

    private final int duration = 3; // seconds
    private final int sampleRate = 8000;
    private final int numSamples = duration * sampleRate;
    private final double sample[] = new double[numSamples];

    private final byte generatedSnd[] = new byte[2 * numSamples];

    Handler handler = new Handler();

    public void play(final int freqOfTone) {
        new Thread(new Runnable() {
            public void run() {
                //freqOfTone in Hz
                genTone(freqOfTone);
                handler.post(new Runnable() {

                    public void run() {
                        playSound();
                    }
                });
            }
        }).start();
    }

    private void genTone(int freqOfTone) {
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (double dVal : sample) {
            short val = (short) (dVal * 32767);
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }

    private void playSound() {
        AudioTrack audioTrack = null;

        try {

            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT, numSamples,
                    AudioTrack.MODE_STATIC);                   // Get audio track
            audioTrack.write(generatedSnd, 0, numSamples);   // Load the track
            audioTrack.play();                               // Play the track
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

