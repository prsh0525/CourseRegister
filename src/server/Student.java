package server;

import org.json.simple.JSONObject;

public class Student {
    public int id, grade;
    public String password, name, major;

    public Student(int id, String password, String name, int grade, String major) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.grade = grade;
        this.major = major;
    }

    public JSONObject getJsonObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("password", password);
        object.put("name", name);
        object.put("grade", grade);
        object.put("major", major);

        return object;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(id + ", ");
        sb.append(password + ", ");
        sb.append(name + ", ");
        sb.append(grade + ", ");
        sb.append(major);

        return sb.toString();
    }
}