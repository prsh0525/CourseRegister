package server;

import org.json.simple.JSONObject;

public class Professor {
    public int id;
    public String password, name, position, major;

    public Professor(int id, String password, String name, String position, String major) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.position = position;
        this.major = major;
    }

    public JSONObject getJsonObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("password", password);
        object.put("name", name);
        object.put("position", position);
        object.put("major", major);

        return object;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(id + ", ");
        sb.append(password + ", ");
        sb.append(name + ", ");
        sb.append(position + ", ");
        sb.append(major);

        return sb.toString();
    }
}