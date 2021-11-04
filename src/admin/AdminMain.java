package admin;

import home.Home;
import server.Professor;
import server.ServerConnection;
import server.Student;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AdminMain extends JFrame {
    ServerConnection server = new ServerConnection();
    JTable sTable, pTable;

    public AdminMain() {
        setTitle("관리자용 수강신청 프로그램");
        setSize(800, 450);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.setBackground(new Color(250,250,250));

        // 학생 관리 패널
        JPanel sPanel = new JPanel();
        sPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        sPanel.setLayout(new BorderLayout(10, 0));

        // 왼쪽의 등록과 삭제를 담을 패널
        JPanel sWestPanel = new JPanel();
        sWestPanel.setLayout(new GridBagLayout());
        sPanel.add(sWestPanel, BorderLayout.WEST);

        // 학생 등록 패널
        JPanel sRegisterPanel = new JPanel();
        sRegisterPanel.setLayout(new GridBagLayout());
        sRegisterPanel.setBorder(new TitledBorder(new EtchedBorder(), " 학생 등록 "));
        gbAdd(sWestPanel, sRegisterPanel, 0, 0, 1, 10, 1, 3);

        // 라벨 추가하기
        String[] sLabel = {" 학번", " 비밀번호", " 이름", " 학년", " 학과"};
        for (int i = 0; i < sLabel.length; i++) {
            gbAdd(sRegisterPanel, new JLabel(sLabel[i]), 0, i, 1, 1, 1, 1);
        }

        // 필드 추가하기
        JTextField sTextField[] = new JTextField[5];
        String[] gradeString = {"1학년", "2학년", "3학년", "4학년", "5학년"};
        JComboBox<String> sGradeCombo = new JComboBox<>(gradeString);;
        for (int i = 0; i < sLabel.length; i++) {
            // 학년은 ComboBox로 구현
            if (i == 3) {
                gbAdd(sRegisterPanel, sGradeCombo, 1, i, 3, 1, 1, 1);
            }
            // 그 외에는 TextField
            else {
                sTextField[i] = new JTextField(20);
                gbAdd(sRegisterPanel, sTextField[i], 1, i, 3, 1,1,1);
            }
        }

        // 등록 버튼 추가하기
        JButton sRegisterButton = new JButton("등록");
        gbAdd(sRegisterPanel, sRegisterButton, 0, sLabel.length, 4, 1, 1, 1);
        sRegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(sTextField[0].getText());
                String password = sTextField[1].getText();
                String name = sTextField[2].getText();
                int grade = sGradeCombo.getSelectedIndex() + 1;
                String major = sTextField[4].getText();

                // 서버에 등록하기 전 정보를 다 입력했는지 확인
                if (password.equals("") || name.equals("") || major.equals("")) {
                    JOptionPane.showMessageDialog(getRootPane(), "모든 항목을 입력해주세요.", "", JOptionPane.WARNING_MESSAGE);
                }
                // 서버에 학생 정보 등록
                else {
                    try {
                        Student student = new Student(id, password, name, grade, major);
                        server.registerStudent(student);
                        JOptionPane.showMessageDialog(getRootPane(), "등록되었습니다.", "", JOptionPane.INFORMATION_MESSAGE);
                        updateStudentTable();

                        // 필드 초기화
                        for (int i = 0; i < sTextField.length; i++)
                            if (i != 3) sTextField[i].setText("");
                        sGradeCombo.setSelectedIndex(0);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(getRootPane(), "오류가 발생했습니다.", "", JOptionPane.WARNING_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });

        // 학생 삭제 패널
        JPanel sDeletePanel = new JPanel();
        sDeletePanel.setLayout(new GridBagLayout());
        sDeletePanel.setBorder(new TitledBorder(new EtchedBorder(), " 학생 삭제 "));
        gbAdd(sWestPanel, sDeletePanel, 0, 10, 1, 1, 1, 1);

        // 학번
        gbAdd(sDeletePanel, new JLabel(" 학번     "), 0, 0, 1, 1, 1, 1);
        JTextField sDeleteTextField = new JTextField(20);
        gbAdd(sDeletePanel, sDeleteTextField, 1, 0, 3, 1, 1, 1);

        // 삭제 버튼
        JButton sDeleteButton = new JButton("삭제");
        gbAdd(sDeletePanel, sDeleteButton, 0, 1, 4, 1, 1, 1);
        sDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(sDeleteTextField.getText());

                // 값이 있는지 확인
                if (sDeleteTextField.getText().equals("")) {
                    JOptionPane.showMessageDialog(getRootPane(), "학번을 입력해주세요.", "", JOptionPane.WARNING_MESSAGE);
                }
                // 서버에 학생 정보 삭제
                else {
                    try {
                        server.deleteStudent(id);
                        JOptionPane.showMessageDialog(getRootPane(), "삭제되었습니다.", "", JOptionPane.INFORMATION_MESSAGE);
                        updateStudentTable();

                        // 필드 초기화
                        sDeleteTextField.setText("");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(getRootPane(), "오류가 발생했습니다.", "", JOptionPane.WARNING_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });

        // 오른쪽의 학생 정보 테이블
        JPanel sTablePanel = new JPanel();
        sTablePanel.setLayout(new BorderLayout());
        sPanel.add(sTablePanel, BorderLayout.CENTER);

        sTable = new JTable();
        updateStudentTable();     // 서버에서 학생 정보를 가져와 저장하고 출력한다.
        sTable.setRowHeight(25);
        sTable.getTableHeader().setPreferredSize(new Dimension(10, 30));
        JScrollPane sScroll = new JScrollPane(sTable);
        sTablePanel.add(sScroll, BorderLayout.CENTER);

        // ======================================
        // 교수 관리 패널
        JPanel pPanel = new JPanel();
        pPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pPanel.setLayout(new BorderLayout(10, 0));

        // 왼쪽의 등록과 삭제를 담을 패널
        JPanel pWestPanel = new JPanel();
        pWestPanel.setLayout(new GridBagLayout());
        pPanel.add(pWestPanel, BorderLayout.WEST);

        // 교수 등록 패널
        JPanel pRegisterPanel = new JPanel();
        pRegisterPanel.setLayout(new GridBagLayout());
        pRegisterPanel.setBorder(new TitledBorder(new EtchedBorder(), " 교수 등록 "));
        gbAdd(pWestPanel, pRegisterPanel, 0, 0, 1, 10, 1, 3);

        // 라벨 추가하기
        String[] pLabel = {" 아이디", " 비밀번호", " 이름", " 직책", " 학과"};
        for (int i = 0; i < pLabel.length; i++) {
            gbAdd(pRegisterPanel, new JLabel(pLabel[i]), 0, i, 1, 1, 1, 1);
        }

        // 필드 추가하기
        JTextField pTextField[] = new JTextField[5];
        String[] positionString = {"교수", "부교수", "조교수", "전임교원", "명예교수"};
        JComboBox<String> pPositionCombo = new JComboBox<>(positionString);
        for (int i = 0; i < pLabel.length; i++) {
            // 직책은 ComboBox로 구현
            if (i == 3) {
                gbAdd(pRegisterPanel, pPositionCombo, 1, i, 3, 1, 1, 1);
            }
            // 그 외에는 TextField
            else {
                pTextField[i] = new JTextField(20);
                gbAdd(pRegisterPanel, pTextField[i], 1, i, 3, 1,1,1);
            }
        }

        // 등록 버튼 추가하기
        JButton pRegisterButton = new JButton("등록");
        gbAdd(pRegisterPanel, pRegisterButton, 0, pLabel.length, 4, 1, 1, 1);
        pRegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(pTextField[0].getText());
                String password = pTextField[1].getText();
                String name = pTextField[2].getText();
                String position = positionString[pPositionCombo.getSelectedIndex()];
                String major = pTextField[4].getText();

                // 서버에 등록하기 전 정보를 다 입력했는지 확인
                if (password.equals("") || name.equals("") || major.equals("")) {
                    JOptionPane.showMessageDialog(getRootPane(), "모든 항목을 입력해주세요.", "", JOptionPane.WARNING_MESSAGE);
                }
                // 서버에 학생 정보 등록
                else {
                    try {
                        Professor professor = new Professor(id, password, name, position, major);
                        server.registerProfessor(professor);
                        JOptionPane.showMessageDialog(getRootPane(), "등록되었습니다.", "", JOptionPane.INFORMATION_MESSAGE);
                        updateProfessorTable();

                        // 필드 초기화
                        for (int i = 0; i < pTextField.length; i++)
                            if (i != 3) pTextField[i].setText("");
                        pPositionCombo.setSelectedIndex(0);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(getRootPane(), "오류가 발생했습니다.", "", JOptionPane.WARNING_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });

        // 교수 삭제 패널
        JPanel pDeletePanel = new JPanel();
        pDeletePanel.setLayout(new GridBagLayout());
        pDeletePanel.setBorder(new TitledBorder(new EtchedBorder(), " 교수 삭제 "));
        gbAdd(pWestPanel, pDeletePanel, 0, 10, 1, 1, 1, 1);

        // 아이디
        gbAdd(pDeletePanel, new JLabel(" 아이디  "), 0, 0, 1, 1, 1, 1);
        JTextField pDeleteTextField = new JTextField(20);
        gbAdd(pDeletePanel, pDeleteTextField, 1, 0, 3, 1, 1, 1);

        // 삭제 버튼
        JButton pDeleteButton = new JButton("삭제");
        gbAdd(pDeletePanel, pDeleteButton, 0, 1, 4, 1, 1, 1);
        pDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(pDeleteTextField.getText());

                // 값이 있는지 확인
                if (pDeleteTextField.getText().equals("")) {
                    JOptionPane.showMessageDialog(getRootPane(), "아이디를 입력해주세요.", "", JOptionPane.WARNING_MESSAGE);
                }
                // 서버에 교수 정보 삭제
                else {
                    try {
                        server.deleteProfessor(id);
                        JOptionPane.showMessageDialog(getRootPane(), "삭제되었습니다.", "", JOptionPane.INFORMATION_MESSAGE);
                        updateProfessorTable();

                        // 필드 초기화
                        pDeleteTextField.setText("");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(getRootPane(), "오류가 발생했습니다.", "", JOptionPane.WARNING_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });

        // 오른쪽의 교수 정보 테이블
        JPanel pTablePanel = new JPanel();
        pTablePanel.setLayout(new BorderLayout());
        pPanel.add(pTablePanel, BorderLayout.CENTER);

        pTable = new JTable();
        updateProfessorTable();     // 서버에서 교수 정보를 가져와 저장하고 출력한다.
        pTable.setRowHeight(25);
        pTable.getTableHeader().setPreferredSize(new Dimension(10, 30));
        JScrollPane pScroll = new JScrollPane(pTable);
        pTablePanel.add(pScroll, BorderLayout.CENTER);

        // 탭으로 감싸기
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("학생 관리", sPanel);
        tabbedPane.add("교수 관리", pPanel);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        container.add(tabbedPane, BorderLayout.CENTER);

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

    // 중복된 코드를 피하고자 GridBagLayout add 동작을 함수로 만들었음
    private void gbAdd(JPanel panel, JComponent c, int x, int y, int w, int h, int weightx, int weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = weightx;  gbc.weighty = weighty;
        gbc.gridx = x;          gbc.gridy = y;
        gbc.gridwidth = w;      gbc.gridheight = h;
        gbc.insets = new Insets(2, 2, 2, 2);
        panel.add(c, gbc);
    }

    // 첫 실행, 등록, 삭제 시 호출되어 표에 값을 채운다.
    private void updateStudentTable() {
        // 학생 정보를 가져온다.
        ArrayList<Student> students = null;
        try {
            students = server.getStudent();
        }
        catch (Exception e) {}

        // 학생 정보 테이블의 값
        String sHeader[] = {"학번", "비밀번호", "이름", "학년", "학과"};
        Object sContents[][] = new Object[students.size()][5];
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            sContents[i][0] = s.id;
            sContents[i][1] = s.password;
            sContents[i][2] = s.name;
            sContents[i][3] = s.grade;
            sContents[i][4] = s.major;
        }

        DefaultTableModel model = new DefaultTableModel(sContents, sHeader);
        sTable.setModel(model);
        model.fireTableDataChanged();
    }

    private void updateProfessorTable() {
        // 학생 정보를 가져온다.
        ArrayList<Professor> professors = null;
        try {
            professors = server.getProfessor();
        }
        catch (Exception e) {}

        // 학생 정보 테이블의 값
        String pHeader[] = {"아이디", "비밀번호", "이름", "직책", "학과"};
        Object pContents[][] = new Object[professors.size()][5];
        for (int i = 0; i < professors.size(); i++) {
            Professor p = professors.get(i);
            pContents[i][0] = p.id;
            pContents[i][1] = p.password;
            pContents[i][2] = p.name;
            pContents[i][3] = p.position;
            pContents[i][4] = p.major;
        }

        DefaultTableModel model = new DefaultTableModel(pContents, pHeader);
        pTable.setModel(model);
        model.fireTableDataChanged();
    }

    public static void main(String[] args) {
        new AdminMain();
    }
}