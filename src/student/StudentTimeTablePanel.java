package student;

import server.Lecture;
import server.ServerConnection;

import javax.swing.*;
import java.awt.*;
import java.util.*;

class StudentTimeTablePanel extends JPanel {

    ServerConnection server = new ServerConnection();
    int[][] timeTable;

    public StudentTimeTablePanel() {
        setLayout(new GridLayout(9,6));
    }

    public void updateTimeTable(int studentId) {
        removeAll();
        revalidate();
        repaint();

        timeTable = new int[9][6];

        // 서버에서 강의 목록을 가져와서,
        // 그 강의에 해당 학생이 신청되어 있다면 시간표에 표시
        HashMap<Integer, String> appliedLectures = new HashMap<>();  // 강의번호, 시간
        try {
            ArrayList<Lecture> lectures = server.getLecture();
            for (Lecture lecture : lectures) {
                ArrayList<Integer> appliedStudents = server.getRegisteredStudentId(lecture.id);
                for (int appliedStudent : appliedStudents) {
                    if (studentId == appliedStudent) {
                        appliedLectures.put(lecture.id, lecture.time);
                    }
                }
            }
        } catch (Exception e) {
            return;
        }

        Set<Integer> keys = appliedLectures.keySet();
        for (int key : keys) {
            String a = appliedLectures.get(key);
            int code = key;
            StringTokenizer ab = new StringTokenizer(a,", ");
            int n = ab.countTokens();

            if (n >= 2) {
                String token = ab.nextToken();
                String token2 = ab.nextToken();
                String F_day = token.substring(0,1);//x=��
                String F_time = token.substring(1);//xx="1-3"
                String S_day = token2.substring(0,1);//y=��
                String S_time = token2.substring(1);//yy=5
                StringTokenizer time = new StringTokenizer(F_time,"-");
                StringTokenizer time2 = new	StringTokenizer(S_time,"-");

                int F_time1 = Integer.valueOf(time.nextToken()); //xxx=1
                int F_time2;
                if (time.hasMoreElements())
                    F_time2 = Integer.valueOf(time.nextToken()); //yyy=3
                else
                    F_time2 = F_time1;

                int S_time1 = Integer.valueOf(time2.nextToken());
                int S_time2;
                if (time2.hasMoreElements())
                    S_time2 = Integer.valueOf(time2.nextToken());
                else
                    S_time2 = S_time1;

                if (F_day.equals("월")) {
                    for(int i = F_time1; i <= F_time2; i++) {
                        timeTable[i][1] = code;//lecture[k].id
                    }
                }else if(F_day.equals("화")) {
                    for(int i = F_time1; i <= F_time2; i++) {
                        timeTable[i][2] = code;
                    }
                }else if(F_day.equals("수")) {
                    for(int i = F_time1; i <= F_time2; i++) {
                        timeTable[i][3] = code;
                    }
                }else if(F_day.equals("목")) {
                    for(int i = F_time1; i <= F_time2; i++) {
                        timeTable[i][4] = code;
                    }
                }else if(F_day.equals("금")) {
                    for(int i = F_time1; i <= F_time2; i++) {
                        timeTable[i][5] = code;
                    }
                }

                if (S_day.equals("월")) {
                    for(int i = S_time1; i <= S_time2; i++) {
                        timeTable[i][1] = code;
                    }
                }else if(S_day.equals("화")) {
                    for(int i = S_time1; i <= S_time2; i++) {
                        timeTable[i][2] = code;
                    }
                }else if(S_day.equals("수")) {
                    for(int i = S_time1; i <= S_time2; i++) {
                        timeTable[i][3] = code;
                    }
                }else if(S_day.equals("목")) {
                    for(int i = S_time1; i <= S_time2; i++) {
                        timeTable[i][4] = code;
                    }
                }else if(S_day.equals("금")) {
                    for(int i = S_time1; i <= S_time2; i++) {
                        timeTable[i][5] = code;
                    }
                }
            } else {
                String token = ab.nextToken();
                String F_day = token.substring(0, 1);//x=��
                String F_time = token.substring(1);//xx="1-3"
                StringTokenizer time = new StringTokenizer(F_time, "-");

                int F_time1 = Integer.valueOf(time.nextToken()); //xxx=1
                int F_time2;
                if (time.hasMoreElements())
                    F_time2 = Integer.valueOf(time.nextToken()); //yyy=3
                else
                    F_time2 = F_time1;
                if (F_day.equals("월")) {
                    for (int i = F_time1; i <= F_time2; i++) {
                        timeTable[i][1] = code;
                    }
                } else if (F_day.equals("화")) {
                    for (int i = F_time1; i <= F_time2; i++) {
                        timeTable[i][2] = code;
                    }
                } else if (F_day.equals("수")) {
                    for (int i = F_time1; i <= F_time2; i++) {
                        timeTable[i][3] = code;
                    }
                } else if (F_day.equals("목")) {
                    for (int i = F_time1; i <= F_time2; i++) {
                        timeTable[i][4] = code;
                    }
                } else if (F_day.equals("금")) {
                    for (int i = F_time1; i <= F_time2; i++) {
                        timeTable[i][5] = code;
                    }
                }
            }
        }

        Random rand = new Random();
        HashMap<Integer, Color> colorTemplate = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 6; j++) {
                JPanel pn1 = new JPanel();
                JLabel lb1 = new JLabel();
                lb1.setHorizontalAlignment(SwingConstants.CENTER);
                pn1.setBackground(Color.WHITE);
                pn1.setBorder(BorderFactory.createTitledBorder(""));
                pn1.setLayout(new BorderLayout());
                pn1.add(lb1, BorderLayout.CENTER);
                add(pn1);

                if(timeTable[i][j] != 0) {
                    lb1.setText(Integer.toString(timeTable[i][j]));

                    Color tmpColor;
                    if (!colorTemplate.containsKey(timeTable[i][j])) {
                        int r = rand.nextInt(10) * 10 + 150;
                        int g = rand.nextInt(10) * 10 + 150;
                        int b = rand.nextInt(10) * 10 + 150;
                        tmpColor = new Color(r, g, b);
                        colorTemplate.put(timeTable[i][j], tmpColor);
                    }
                    else {
                        tmpColor = colorTemplate.get(timeTable[i][j]);
                    }

                    pn1.setBackground(tmpColor);
                }

                if (i ==0 && j == 1){
                    lb1.setText("  월요일  ");
                } else if(i ==0 && j==2){
                    lb1.setText("  화요일  ");
                } else if(i ==0 && j==3){
                    lb1.setText("  수요일  ");
                } else if(i ==0 && j==4){
                    lb1.setText("  목요일  ");
                } else if(i ==0 && j==5){
                    lb1.setText("  금요일  ");
                }

                if (j == 0 && i >= 1) {
                    lb1.setText(i+"교시");
                }

            }
        }
    }

    // 중복 검사를 할 때 사용하는 메소드
    public boolean[][] getAppliedTimeTable() {
        boolean[][] appliedTimeTable = new boolean[9][6];

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 6; j++) {
                if (timeTable[i][j] != 0) {
                    appliedTimeTable[i][j] = true;
                }
            }
        }
        return appliedTimeTable;
    }

    public static void main(String[] args) {
        new StudentTimeTablePanel();
    }
}
