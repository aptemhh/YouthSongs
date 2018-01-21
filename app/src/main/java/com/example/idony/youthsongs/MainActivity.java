package com.example.idony.youthsongs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    public final static String NUMBER_SONG = "NUMBER_SONG";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Controller.getInstance().loadResource(getApplicationContext());

        intent = new Intent(this, TextSong.class);

        ListView listView = findViewById(R.id.list_description);
        // установка списка всех песен
        setListNameSong(Controller.getInstance().getListSong().values(), listView);

        //переход по номеру песни из списка к песне
        listView.setOnItemClickListener((parent, view, position, id) -> {
            intent.putExtra(NUMBER_SONG,Integer.parseInt(((String)((TextView)view).getText()).split(" ")[0]));
            startActivity(intent);
        });
        SearchView searchView = findViewById(R.id.searcher);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            /**
             * Поиск текста
             * @param s текст поиска
             */
            @Override
            public boolean onQueryTextSubmit(String s) {
                setListNameSong(Controller.getInstance().find(s).values(), listView);
                return true;
            }

            /**
             * Ввод текста
             * @param s текст поиска
             * @return
             */
            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });

        /**
         * После закрытия поисковика востановить список
         */
        searchView.setOnCloseListener(() -> {
            setListNameSong(Controller.getInstance().getListSong().values(), listView);
            return true;
        });
    }

    private void setListNameSong(Collection collection, ListView listView)
    {
        ArrayAdapter<Song> adapter = new ArrayAdapter<>(this,
                R.layout.list_name_song,
                (Song[]) collection.toArray(new Song[]{}));
        listView.setAdapter(adapter);
    }
}
