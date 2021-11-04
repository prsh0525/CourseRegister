package server;

import org.json.simple.JSONObject;

public class Lecture {
    public int id, credit, capacity, professorId;
    public String name, section, time, room;

    public Lecture(int id, String name, String section, int credit, String time, String room, int capacity, int professorId) {
        this.id = id;
        this.name = name;
        this.section = section;
        this.credit = credit;
        this.time = time;
        this.room = room;
        this.capacity = capacity;
        this.professorId = professorId;
    }

    public JSONObject getJsonObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        object.put("section", section);
        object.put("credit", credit);
        object.put("time", time);
        object.put("room", room);
        object.put("capacity", capacity);
        object.put("professor_id", professorId);

        return object;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(id + ", ");
        sb.append(name + ", ");
        sb.append(section + ", ");
        sb.append(credit + ", ");
        sb.append(time + ", ");
        sb.append(room + ", ");
        sb.append(capacity + ", ");
        sb.append(professorId);

        return sb.toString();
    }

//    public String timeToArray() {
//
//    }
}