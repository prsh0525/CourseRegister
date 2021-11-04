package home;

import admin.AdminMain;
import professor.ProfessorMain;
import server.Professor;
import server.ServerConnection;
import server.Student;
import student.StudentMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Home extends JFrame {

    public Home() {
        ServerConnection server = new ServerConnection();

        setTitle("수강신청 프로그램");
        setSize(840, 460);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        // 배경 패널
        JPanel backgroundPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(new ImageIcon("src/home/background.png").getImage(), 0, 0, null);
            }
        };
        backgroundPanel.setSize(840, 460);
        backgroundPanel.setLocation(0, 0);
        backgroundPanel.setLayout(null);
        add(backgroundPanel);

        // 메인 패널
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setSize(640, 160);
        mainPanel.setLocation(100, 140);
        mainPanel.setBackground(new Color(255, 255, 255, 200));
        backgroundPanel.add(mainPanel);

        // 메인 패널 왼쪽 문구
        JPanel textPanel = new JPanel();
        textPanel.setSize(300, 80);
        textPanel.setLocation(0, 40);
        textPanel.setLayout(new GridLayout(2, 1));
        textPanel.setBackground(new Color(0, 0, 0, 0));
        mainPanel.add(textPanel);

        JLabel titleLabel = new JLabel("수강신청 프로그램");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        textPanel.add(titleLabel);

        JLabel infoLabel = new JLabel(server.serverStatus());
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        textPanel.add(infoLabel);

        // 흰 배경 패널
        JPanel whitePanel = new JPanel();
        whitePanel.setSize(340, 160);
        whitePanel.setLocation(300, 0);
        whitePanel.setLayout(null);
        whitePanel.setBackground(new Color(250,250,250));
        mainPanel.add(whitePanel);

        // 로그인 패널
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());
        loginPanel.setSize(300, 120);
        loginPanel.setLocation(20, 20);
        loginPanel.setBackground(new Color(250, 250, 250));
        whitePanel.add(loginPanel);

        // 위쪽(North)에 라디오 버튼 그룹 추가
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1, 3));
        northPanel.setBackground(new Color(250, 250, 250));
        loginPanel.add(northPanel, BorderLayout.NORTH);

        ButtonGroup radioGroup = new ButtonGroup();
        JRadioButton radio[] = new JRadioButton[3];
        String radioName[] = {"학생용", "교수용", "관리자용"};
        for (int i = 0; i < 3; i++) {
            radio[i] = new JRadioButton(radioName[i]);
            radio[i].setPreferredSize(new Dimension(100, 30));
            radio[i].setHorizontalAlignment(JRadioButton.CENTER);
            radioGroup.add(radio[i]);
            northPanel.add(radio[i]);
        }
        radio[0].setSelected(true);

        // 중간(Center)에 로그인 폼 구현
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 1));
        centerPanel.setBackground(new Color(250, 250, 250));
        loginPanel.add(centerPanel, BorderLayout.CENTER);

        // Placeholder 구현
        JTextField idField = new JTextField(" 아이디", 20);
        idField.setForeground(Color.GRAY);
        idField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (idField.getText().equals(" 아이디")) {
                    idField.setText("");
                    idField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (idField.getText().isEmpty()) {
                    idField.setText(" 아이디");
                    idField.setForeground(Color.GRAY);
                }
            }
        });
        centerPanel.add(idField);

        JPasswordField pwField = new JPasswordField(" 비밀번호", 20);
        pwField.setForeground(Color.GRAY);
        pwField.setEchoChar((char) 0);
        pwField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String pw = String.valueOf(pwField.getPassword());
                if (pw.equals(" 비밀번호")) {
                    pwField.setText("");
                    pwField.setEchoChar('*');
                    pwField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                String pw = String.valueOf(pwField.getPassword());
                if (pw.isEmpty()) {
                    pwField.setText(" 비밀번호");
                    pwField.setEchoChar((char) 0);
                    pwField.setForeground(Color.GRAY);
                }
            }
        });
        centerPanel.add(pwField);

        // 오른쪽(East)에 로그인 버튼 구현
        JButton loginButton = new JButton("로그인");
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(250,250,250));
        loginButton.setOpaque(true);
        loginPanel.add(loginButton, BorderLayout.EAST);
        getRootPane().setDefaultButton(loginButton);    // 엔터 키 입력 시 로그인 버튼의 이벤트로 바인딩

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRadio = 0;
                for (int i = 0; i < 3; i++) {
                    if (radio[i].isSelected())
                        selectedRadio = i;
                }

                try {
                    switch (selectedRadio) {
                        case 0:
                            int studentId = Integer.parseInt(idField.getText());
                            String studentPw = String.valueOf(pwField.getPassword());
                            Student student = server.getStudentById(studentId);
                            if (student != null && studentId == student.id && studentPw.equals(student.password)) {
                                StudentMain.id = studentId;
                                new StudentMain();
                                dispose();
                            } else {
                                errorDialog();
                            }
                            break;
                        case 1:
                            int professorId = Integer.parseInt(idField.getText());
                            String professorPw = String.valueOf(pwField.getPassword());
                            Professor professor = server.getProfessorById(professorId);
                            if (professor != null && professorId == professor.id && professorPw.equals(professor.password)) {
                                ProfessorMain.id = professorId;
                                new ProfessorMain();
                                dispose();
                            } else {
                                errorDialog();
                            }
                            break;
                        case 2:
                            String adminId = idField.getText();
                            String adminPw = String.valueOf(pwField.getPassword());
                            if (adminId.equals("admin") && adminPw.equals("admin")) {
                                new AdminMain();
                                dispose();
                            } else {
                                errorDialog();
                            }
                            break;
                        default:
                            errorDialog();
                            break;
                    }
                }
                catch (Exception ex) {
                    errorDialog();
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    private void errorDialog() {
        JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 확인하세요.", "", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        new Home();
    }
}
