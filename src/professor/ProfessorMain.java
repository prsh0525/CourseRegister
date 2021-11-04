package professor;

import home.Home;
import server.Professor;
import server.ServerConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ProfessorMain extends JFrame {
    // 아이디 값이 전달되어 저장되는 변수
    public static int id;

    public ProfessorMain() {
        ServerConnection server = new ServerConnection();

        setTitle("교수용 수강신청 프로그램");
        setSize(300, 100);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container container = getContentPane();
        container.setLayout(new FlowLayout());

        // 여기부터는 예시입니다.
        // 아래를 지우고 실제 코딩 시작하세요.
        // 아이디로 교수 정보 가져오기
        Professor professor = null;
        try {
            professor = server.getProfessorById(id);
        }
        catch (Exception e) {
            // 예외 발생 시 창 닫기
            dispose();
        }

        // 서버에서 가져온 값을 출력하기
        JLabel label = new JLabel(professor.toString());
        add(label);

        // 여기는 수정하지 마세요.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                new Home();
            }
        });
        setVisible(true);
    }

    public static void main(String[] args) {
        new ProfessorMain();
    }
}