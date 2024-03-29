package com.example.placesaccounter.models;

public class ModelLearner {
    private final long _id;
    private int room_number;
    private int stream_number;
    private String check_in_date;
    private String check_out_date;

    public ModelLearner(long _id, int room_number, int stream_number, String check_in_date, String check_out_date) {
        this._id = _id;
        this.room_number = room_number;
        this.stream_number = stream_number;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
    }

    public long get_id() {
        return _id;
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
