package com.example.idony.youthsongs;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by idony on 01.01.2018.
 */

public final class Controller {

    private static final Controller CONTROLLER = new Controller();
    private static final Pattern PATTERN = Pattern.compile("^\\d$");
    private Map<Integer, Song> songs;

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
        songs = initSong(context);
    }

    public Map<Integer, Song> getListSong() {
        return songs;
    }

    public String getSongText(final Integer number) {
        return songs.get(number).getText();
    }

    public Map<Integer, Song> find(String searchString) {
        if (PATTERN.matcher(searchString).matches()) {
            Song song = songs.get(Integer.parseInt(searchString));
            if (song == null)
                return new HashMap<>();
            return Collections.singletonMap(song.getNumber(), song);
        }
        Map<Integer, Song> searchMap = new HashMap<>();
        for (Song song : Controller.getInstance().getListSong().values()) {
            if (containsIgnoreCase(song.getDescription(), searchString)||containsIgnoreCase(song.getText(), searchString)) {
                searchMap.put(song.getNumber(), song);
            }
        }
        return searchMap;
    }

    private Map<Integer, Song> initSong(Context context) {
        XmlPullParserFactory pullParserFactory;

        try (InputStream in_s = context.getAssets().open("textSong.xml")) {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            return parseXML(parser);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private Map<Integer, Song> parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        Map<Integer, Song> songMap = null;
        int eventType = parser.getEventType();
        Song song = null;
        Integer id = 1;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    songMap = new HashMap<>();
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
                        songMap.put(song.getNumber(), song);
                    }
            }
            eventType = parser.next();
        }
        return songMap;
    }
}
