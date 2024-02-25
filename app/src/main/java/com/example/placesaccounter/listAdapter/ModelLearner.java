package com.example.placesaccounter.listAdapter;

public class ModelLearner {
    private int room_number;
    private int stream_number;
    private String check_in_date;
    private String check_out_date;

    public ModelLearner(int room_number, int stream_number, String check_in_date, String check_out_date) {
        this.room_number = room_number;
        this.stream_number = stream_number;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
    }

    public int getRoom_number() {
        return room_number;
    }

    public int getStream_number() {
        return stream_number;
    }

    public String getCheck_in_date() {
        return check_in_date;
    }

    public String getCheck_out_date() {
        return check_out_date;
    }

    public void setRoom_number(int room_number) {
        this.room_number = room_number;
    }

    public void setStream_number(int stream_number) {
        this.stream_number = stream_number;
    }

    public void setCheck_in_date(String check_in_date) {
        this.check_in_date = check_in_date;
    }

    public void setCheck_out_date(String check_out_date) {
        this.check_out_date = check_out_date;
    }
}
