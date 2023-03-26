package model;

import java.time.LocalTime;

//class to store data about pickers
public class Picker {
    private LocalTime pickingStartTime;
    private String id;
    private LocalTime pickingEndTime;

    public Picker(String id, LocalTime pickingStartTime, LocalTime pickingEndTime) {
        this.id = id;
        this.pickingStartTime = pickingStartTime;
        this.pickingEndTime = pickingEndTime;
    }
    public LocalTime getPickingStartTime() {
        return pickingStartTime;
    }

    public void setPickingStartTime(LocalTime pickingStartTime) {
        this.pickingStartTime = pickingStartTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalTime getPickingEndTime() {
        return pickingEndTime;
    }

    public void setPickingEndTime(LocalTime pickingEndTime) {
        this.pickingEndTime = pickingEndTime;
    }

}
