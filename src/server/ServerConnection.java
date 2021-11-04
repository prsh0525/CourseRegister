package server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.ArrayList;

public class ServerConnection {
    String hostUrl = "http://52.78.151.64";

    // Server Connection - GET
    private String getMethod(String routeUrl) throws Exception {
        URL url = new URL(hostUrl + routeUrl);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        http.setDefaultUseCaches(false);
        http.setDoInput(true);
        http.setDoOutput(true);
        http.setRequestMethod("GET");
        http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

        InputStreamReader isr = new InputStreamReader(http.getInputStream(), "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String str;

        while ((str = br.readLine()) != null) {
            str = URLDecoder.decode(str, "UTF-8");
            sb.append(str + "\n");
        }

        http.disconnect();
        return sb.toString();
    }

    // Server Connection - POST
    private void postMethod(String routeUrl, JSONObject jsonObject) throws Exception {
        URL url = new URL(hostUrl + routeUrl);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        http.setDefaultUseCaches(false);
        http.setDoInput(true);
        http.setDoOutput(true);
        http.setRequestMethod("POST");
        http.setRequestProperty("content-type", "application/json; utf-8");

        byte input[] = jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8);
        OutputStream os = http.getOutputStream();
        os.write(input, 0, input.length);

        int responseCode = http.getResponseCode();
        if (responseCode != 200) throw new Exception();

        http.disconnect();
    }

    // Check Server Status
    public String serverStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>서버: 52.78.151.64<br>");
        sb.append(System.getProperty("line.separator"));
        sb.append("서버 상태: ");
        try {
            postMethod("/status", new JSONObject());
            sb.append("정상");
        }
        catch (Exception e) {
            sb.append("오류");
        }
        finally {
            sb.append("</html>");
        }

        return sb.toString();
    }

    // Basic Operations
    // Student
    public ArrayList<Student> getStudent() throws Exception {
        String output = getMethod("/student");
        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(output);

        ArrayList<Student> studentList = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = (JSONObject) array.get(i);
            int id = Integer.parseInt(object.get("id").toString());
            String password = object.get("password").toString();
            String name = object.get("name").toString();
            int grade = Integer.parseInt(object.get("grade").toString());
            String major = object.get("major").toString();

            Student student = new Student(id, password, name, grade, major);
            studentList.add(student);
        }

        return studentList;
    }

    public Student getStudentById(int id) throws Exception {
        String output = getMethod("/student/" + id);
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(output);

        if (object == null)
            return null;

        String password = object.get("password").toString();
        String name = object.get("name").toString();
        int grade = Integer.parseInt(object.get("grade").toString());
        String major = object.get("major").toString();

        Student student = new Student(id, password, name, grade, major);
        return student;
    }

    public void registerStudent(Student student) throws Exception {
        JSONObject object = student.getJsonObject();
        postMethod("/student/register", object);
    }

    public void deleteStudent(int id) throws Exception {
        getMethod("/student/delete/" + id);
    }

    // Professor
    public ArrayList<Professor> getProfessor() throws Exception {
        String output = getMethod("/professor");
        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(output);

        ArrayList<Professor> professorList = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = (JSONObject) array.get(i);
            int id = Integer.parseInt(object.get("id").toString());
            String password = object.get("password").toString();
            String name = object.get("name").toString();
            String position = object.get("position").toString();
            String major = object.get("major").toString();

            Professor professor = new Professor(id, password, name, position, major);
            professorList.add(professor);
        }

        return professorList;
    }

    public Professor getProfessorById(int id) throws Exception {
        String output = getMethod("/professor/" + id);
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(output);

        if (object == null)
            return null;

        String password = object.get("password").toString();
        String name = object.get("name").toString();
        String position = object.get("position").toString();
        String major = object.get("major").toString();

        Professor professor = new Professor(id, password, name, position, major);
        return professor;
    }

    public void registerProfessor(Professor professor) throws Exception {
        JSONObject object = professor.getJsonObject();
        postMethod("/professor/register", object);
    }

    public void deleteProfessor(int id) throws Exception {
        getMethod("/professor/delete/" + id);
    }

    // Lecture
    public ArrayList<Lecture> getLecture() throws Exception {
        String output = getMethod("/lecture");
        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(output);

        ArrayList<Lecture> lectureList = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = (JSONObject) array.get(i);
            int id = Integer.parseInt(object.get("id").toString());
            String name = object.get("name").toString();
            String section = object.get("section").toString();
            int credit = Integer.parseInt(object.get("credit").toString());
            String time = object.get("time").toString();
            String room = object.get("room").toString();
            int capacity = Integer.parseInt(object.get("capacity").toString());
            int professorId = Integer.parseInt(object.get("professor_id").toString());

            Lecture lecture = new Lecture(id, name, section, credit, time, room, capacity, professorId);
            lectureList.add(lecture);
        }

        return lectureList;
    }

    public Lecture getLectureById(int id) throws Exception {
        String output = getMethod("/lecture/" + id);
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(output);

        if (object == null)
            return null;

        String name = object.get("name").toString();
        String section = object.get("section").toString();
        int credit = Integer.parseInt(object.get("credit").toString());
        String time = object.get("time").toString();
        String room = object.get("room").toString();
        int capacity = Integer.parseInt(object.get("capacity").toString());
        int professorId = Integer.parseInt(object.get("professor_id").toString());

        Lecture lecture = new Lecture(id, name, section, credit, time, room, capacity, professorId);
        return lecture;
    }

    public void registerLecture(Lecture lecture) throws Exception {
        JSONObject object = lecture.getJsonObject();
        postMethod("/lecture/register", object);
    }

    public void deleteLecture(int id) throws Exception {
        getMethod("/lecture/delete/" + id);
    }

    // Lecture Registration
    // 등록하기 전에 반드시 isRegistrable 메소드를 호출하여 신청 시점에 빈 자리가 있는지 확인하고
    // 불가능할 경우 이 함수를 호출하지 않고 에러 메시지를 보여준다.
    // 동시에 여러 번 호출하여 중복해서 등록하게 되는 상황은 서버에서 처리함.
    public void applyLecture(int lectureId, int studentId) throws Exception {
        JSONObject object = new JSONObject();
        object.put("lecture_id", lectureId);
        object.put("student_id", studentId);
        postMethod("/registration/register", object);
    }

    public void cancelLecture(int lectureId, int studentId) throws Exception {
        JSONObject object = new JSONObject();
        object.put("lecture_id", lectureId);
        object.put("student_id", studentId);
        postMethod("/registration/cancel", object);
    }

    // lectureId에 신청한 학생의 id 값 반환
    public ArrayList<Integer> getRegisteredStudentId(int lectureId) throws Exception {
        String output = getMethod("/registration/" + lectureId);
        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(output);

        ArrayList<Integer> studentIdList = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = (JSONObject) array.get(i);
            studentIdList.add(Integer.parseInt(object.get("student_id").toString()));
        }

        return studentIdList;
    }

    // lectureId에 신청한 학생 인원 수 조회
    public int getRegisteredNumber(int lectureId) throws Exception {
        return getRegisteredStudentId(lectureId).size();
    }

    // 수강 신청 가능 여부 반환
    public boolean isRegistrable(int lectureId) throws Exception {
        int current = getRegisteredStudentId(lectureId).size();
        int capacity = getLectureById(lectureId).capacity;

        if (current < capacity) return true;
        else return false;
    }
}