package com.example.idony.youthsongs;

/**
 * Created by idony on 01.01.2018.
 */

public class Song {
    private Integer number;
    private String description;
    private String text;

    public Song(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = number +" "+ description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if(description == null)
        {
            this.description = number +" "+ text.substring(0, text.indexOf('\n'));
        }
        this.text = number +"."+ text;
    }

    @Override
    public String toString() {
        return description;
    }
}
