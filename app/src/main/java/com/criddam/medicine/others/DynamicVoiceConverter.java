package com.criddam.medicine.others;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.criddam.medicine.R;

import java.util.HashMap;
import java.util.Locale;

public class DynamicVoiceConverter implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private Context context;
    private String mainTxt = "";

    public void init(String text, Context context){
        this.context = context;
        this.mainTxt = text;
        tts = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
                Toast.makeText(context, "This device is not supported voice", Toast.LENGTH_LONG).show();
            } else {
                speakOut();
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut() {
        Bundle bundle = new Bundle();
        bundle.putString(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1");
        tts.speak(mainTxt, TextToSpeech.QUEUE_FLUSH, bundle,"");
    }

    public void shutDown(){
        if (tts != null){
            tts.stop();
            tts.shutdown();
        }
    }
}
