package com.example.idony.youthsongs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

public class TextSong extends AppCompatActivity {

    public final static String NUMBER_SONG = "NUMBER_SONG";
    private static Integer MIN_VALUE_SIZE_BAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MIN_VALUE_SIZE_BAR = getResources().getInteger(R.integer.min_value_seek_bar);
        setContentView(R.layout.activity_text_song);
        ((TextView) findViewById(R.id.song_text)).setText(Controller.getInstance().getSongText(getIntent().getIntExtra(NUMBER_SONG, 0)));
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ((TextView) findViewById(R.id.size_bar)).setText(i + MIN_VALUE_SIZE_BAR + "");
                ((TextView) findViewById(R.id.song_text)).setTextSize(i + MIN_VALUE_SIZE_BAR);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        ((TextView) findViewById(R.id.size_bar)).setText(MIN_VALUE_SIZE_BAR + "");
    }
}
