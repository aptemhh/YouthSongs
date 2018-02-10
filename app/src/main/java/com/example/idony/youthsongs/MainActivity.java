package com.example.idony.youthsongs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    public final static String NUMBER_SONG = "NUMBER_SONG";
    Intent intent;
    ArrayAdapter<Song> adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Controller.getInstance().loadResource(getApplicationContext());

        intent = new Intent(this, TextSong.class);

        listView = findViewById(R.id.list_description);
        // установка списка всех песен
        adapter = new ArrayAdapter<Song>(this,
                R.layout.list_name_song,
                new ArrayList(Controller.getInstance().getListSong()));

        listView.setAdapter(adapter);

        //переход по номеру песни из списка к песне
        listView.setOnItemClickListener((parent, view, position, id) -> {
            intent.putExtra(NUMBER_SONG, ((Song)((ListView) parent).getAdapter().getItem((int)id)).getNumber());
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
                setListNameSong(Controller.getInstance().find(s));
                return true;
            }

            /**
             * Ввод текста
             * @param s текст поиска
             * @return
             */
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        /**
         * После закрытия поисковика востановить список
         */
        searchView.setOnCloseListener(() -> {
            setListNameSong(Controller.getInstance().getListSong());
            return false;
        });
    }

    public void setListNameSong(List<Song> listNameSong)
    {
       adapter.clear();
       adapter.addAll(listNameSong);
       adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Controller.getInstance().updateSong(getApplicationContext(), this);
        return true;
    }
}
