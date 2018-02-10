package com.example.idony.youthsongs;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by idony on 01.01.2018.
 */

public final class Controller {

    private static final Controller CONTROLLER = new Controller();
    private static final Pattern PATTERN = Pattern.compile("^\\d$");
    private List<Song> songs;

    private Controller() {
    }

    public static Controller getInstance() {
        return CONTROLLER;
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }

    public void loadResource(Context context) {

        try (InputStream in_s = new FileInputStream(context.getFilesDir().getAbsolutePath()
                + "/fileDownload.xml")) {
            songs = initSong(in_s);
        } catch (IOException e) {
            try (InputStream in_s = context.getAssets().open("textSong.xml")) {
                songs = initSong(in_s);
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }
    }

    public List<Song> getListSong() {
        return songs;
    }

    public String getSongText(final Integer number) {
        return songs.get(number - 1).getText();
    }

    public List<Song> find(String searchString) {
        if (PATTERN.matcher(searchString).matches()) {
            Song song = songs.get(Integer.parseInt(searchString));
            if (song == null)
                return new ArrayList<>();
            return Collections.singletonList(song);
        }
        List <Song> searchMap = new ArrayList<>();
        for (Song song : getListSong()) {
            if (containsIgnoreCase(song.getDescription(), searchString) || containsIgnoreCase(song.getText(), searchString)) {
                searchMap.add(song);
            }
        }
        return searchMap;
    }

    private List initSong(InputStream inputStream) {
        XmlPullParserFactory pullParserFactory;
        try (InputStream in_s = inputStream) {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            return parseXML(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }

    private List parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Song> songList = null;
        int eventType = parser.getEventType();
        Song song = null;
        Integer id = 1;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    songList = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("song")) {
                        song = new Song(id++);
                    } else if (name.equals("description")) {
                        song.setDescription(parser.nextText().trim());
                    } else if (name.equals("text")) {
                        song.setText(parser.nextText().trim());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("song") && song != null) {
                        songList.add(song);
                    }
            }
            eventType = parser.next();
        }
        return songList;
    }

    public void updateSong(Context context, IMainActivity mainActivity) {
        new DownloadFileFromURL().execute(context, mainActivity);
    }

    class DownloadFileFromURL extends AsyncTask<Object, IMainActivity, IMainActivity> {

        @Override
        protected IMainActivity doInBackground(Object... context) {
            int count;
            try {
                URL url = new URL(((Context)context[0]).getResources().getText(R.string.url).toString());

                url.openConnection().connect();

                InputStream input = url.openStream();
                // Output stream
                OutputStream output = new FileOutputStream(((Context)context[0]).getFilesDir().getAbsolutePath()
                        + "/fileDownload.xml");
                byte data[] = new byte[1024];
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("<>Error: ", e.getMessage());
            }

            try (InputStream inputStream = new FileInputStream(((Context)context[0]).getFilesDir().getAbsolutePath()
                    + "/fileDownload.xml")) {
                songs = initSong(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return ((IMainActivity)context[1]);
        }

        @Override
        protected void onPostExecute(IMainActivity s) {
            s.setListNameSong(songs);
        }
    }
}
