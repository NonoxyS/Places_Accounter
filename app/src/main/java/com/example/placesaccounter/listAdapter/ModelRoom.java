package com.example.placesaccounter.listAdapter;

import java.util.ArrayList;
import java.util.List;

public class ModelRoom {
    private int floor_number;
    private int room_number;
    private int beds_number;
    private List<ModelLearner> learners_in_room = new ArrayList<>();

    public ModelRoom(int floor_number, int room_number, int beds_number, List<ModelLearner> learners_in_room) {
        this.floor_number = floor_number;
        this.room_number = room_number;
        this.beds_number = beds_number;
        this.learners_in_room = learners_in_room;
    }

    public int getFloor_number() {
        return floor_number;
    }

    public int getRoom_number() {
        return room_number;
    }

    public int getBeds_number() {
        return beds_number;
    }

    public List<ModelLearner> getLearners_in_room() {
        return learners_in_room;
    }

    public void setFloor_number(int floor_number) {
        this.floor_number = floor_number;
    }

    public void setRoom_number(int room_number) {
        this.room_number = room_number;
    }

    public void setBeds_number(int beds_number) {
        this.beds_number = beds_number;
    }

    public void setLearners_in_room(List<ModelLearner> learners_in_room) {
        this.learners_in_room = learners_in_room;
    }
}
