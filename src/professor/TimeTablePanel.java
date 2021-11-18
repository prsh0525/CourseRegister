package professor;

import server.Lecture;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

class TimeTablePanel extends JPanel {

    public TimeTablePanel() {
        setLayout(new GridLayout(9,6));
    }

    public void updateTimeTable(ArrayList<Lecture> lecture) {
        removeAll();
        revalidate();
        repaint();

        int[][] timeTable = new int[9][6];

        for(int k =0; k<lecture.size(); k++){
            String a = lecture.get(k).time;
            int code = lecture.get(k).id;
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
                        int r = rand.nextInt(50) + 200;
                        int g = rand.nextInt(50) + 200;
                        int b = rand.nextInt(50) + 200;
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

    public static void main(String[] args) {
        new TimeTablePanel();
    }
}
