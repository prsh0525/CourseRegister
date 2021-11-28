package student;

import server.Lecture;
import server.ServerConnection;
import server.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static javax.swing.JOptionPane.WARNING_MESSAGE;

public class StudentMain extends JFrame {

    public static int id;   // 학번 값이 전달되어 저장되는 변수
    ServerConnection server = new ServerConnection();

    JTable table;
    StudentTimeTablePanel timeTable = new StudentTimeTablePanel();
    ArrayList<Lecture> lectures = new ArrayList<>();

    JLabel creditLabel;
    JTextField idTextField;
    JLabel infoLabel;
    JButton registerButton;

    public StudentMain() {
        setTitle("학생용 수강신청 프로그램");
        setBounds(0, 0, 1250, 350);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 루트 패널
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
        add(rootPanel, BorderLayout.CENTER);

        /**
         * 학생 정보 패널
         */
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(new TitledBorder(new EtchedBorder(), " 학생 정보 "));
        infoPanel.setLayout(new BorderLayout());
        gbAdd(rootPanel, infoPanel, 0, 0, 1, 1, 1, 1);

        JPanel infoInnerPanel = new JPanel();
        infoInnerPanel.setLayout(new GridLayout(2, 2));
        infoInnerPanel.setBorder(new EmptyBorder(5, 10, 8, 10));
        infoPanel.add(infoInnerPanel, BorderLayout.CENTER);

        // 서버에서 학생 정보 가져오기
        Student student = null;
        try {
            student = server.getStudentById(id);
        } catch (Exception e) {
            dispose();
        }

        // 학번 라벨
        JLabel idLabel = new JLabel("학번: " + student.id);
        infoInnerPanel.add(idLabel);

        // 이름 라벨
        JLabel nameLabel = new JLabel("이름: " + student.name);
        infoInnerPanel.add(nameLabel);

        // 학과 라벨
        JLabel departmentLabel = new JLabel("학과: " + student.major);
        infoInnerPanel.add(departmentLabel);

        // 학년 라벨
        JLabel gradeLabel = new JLabel("학년: " + student.grade + "학년");
        infoInnerPanel.add(gradeLabel);

        /**
         * 신청 현황 패널
         */
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new TitledBorder(new EtchedBorder(), " 신청 현황 "));
        statusPanel.setLayout(new BorderLayout());
        gbAdd(rootPanel, statusPanel, 0, 1, 1, 1, 1, 1);

        JPanel statusInnerPanel = new JPanel();
        statusInnerPanel.setLayout(new GridLayout(1, 2));
        statusInnerPanel.setBorder(new EmptyBorder(5, 10, 8, 10));
        statusPanel.add(statusInnerPanel, BorderLayout.CENTER);

        // 모든 강의 정보를 가져오고 해당 학생이 수강신청 했는지 확인하기
        try {
            lectures = server.getLecture();
        } catch (Exception e) {
            dispose();
        }

        // 수강신청한 과목이 있다면 그 학점만큼 더해서 현재 신청 학점을 보여준다
        int credit = 0;
        for (Lecture lecture : lectures) {
            ArrayList<Integer> appliedStudents;
            try {
                appliedStudents = server.getRegisteredStudentId(lecture.id);
                for (int appliedStudent : appliedStudents) {
                    if (student.id == appliedStudent) {
                        credit += lecture.credit;
                    }
                }
            } catch (Exception e) {
                dispose();
            }
        }

        // 신청 학점 라벨
        creditLabel = new JLabel("신청: " + credit + "학점");
        statusInnerPanel.add(creditLabel);

        // 최대 학점 라벨
        JLabel maxCreditLabel = new JLabel("최대: 20학점");
        statusInnerPanel.add(maxCreditLabel);

        /**
         * 수강 신청 패널
         */
        JPanel applyPanel = new JPanel();
        applyPanel.setBorder(new TitledBorder(new EtchedBorder(), " 수강 신청 "));
        applyPanel.setLayout(new BorderLayout());
        gbAdd(rootPanel, applyPanel, 0, 2, 1, 2, 1, 1);

        JPanel applyInnerPanel = new JPanel();
        applyInnerPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
        applyInnerPanel.setLayout(new GridBagLayout());
        applyPanel.add(applyInnerPanel, BorderLayout.CENTER);

        // 강의 번호 라벨
        JLabel lIdLabel = new JLabel("강의번호");
        gbAdd(applyInnerPanel, lIdLabel, 0, 0, 1, 1, 1, 1);

        // 강의 번호 입력 필드
        idTextField = new JTextField();
        idTextField.setColumns(20);
        gbAdd(applyInnerPanel, idTextField, 1, 0, 4, 1, 10, 1);

        // 입력 시마다 검사
        idTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkAvailable(Integer.parseInt(idTextField.getText()));
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    checkAvailable(Integer.parseInt(idTextField.getText()));
                } catch (Exception ex) {}
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                checkAvailable(Integer.parseInt(idTextField.getText()));
            }
        });

        // 동작 라벨
        infoLabel = new JLabel("강의번호를 입력하거나 선택하세요.", SwingConstants.CENTER);
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbAdd(applyInnerPanel, infoLabel, 0, 1, 5, 1, 1, 1);

        registerButton = new JButton("신청");
        registerButton.setEnabled(false);
        gbAdd(applyInnerPanel, registerButton, 0, 2, 5, 1, 1, 1);

        // 모든 경우를 검사하였으므로, 현재 버튼 클릭 시 수강 신청 또는 취소가 가능한 상태
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 수강 신청
                    if (registerButton.getText().equals("신청")) {
                        int lectureId = Integer.parseInt(idTextField.getText());

                        // 혹시나 신청 시 정원이 다 찼을 수 있으므로 한번 더 확인
                        if (!server.isRegistrable(lectureId)) {
                            JOptionPane.showMessageDialog(getRootPane(), "정원 초과되어 수강 신청할 수 없습니다.", "", WARNING_MESSAGE);
                            return;
                        }

                        server.applyLecture(lectureId, id);

                        // UI 초기화
                        updateTable();
                        idTextField.setText("");
                        infoLabel.setText("강의번호를 입력하거나 목록에서 선택하세요.");

                        // 안내 문구
                        JOptionPane.showMessageDialog(getRootPane(), "신청되었습니다.", "", JOptionPane.INFORMATION_MESSAGE);
                    }
                    // 수강 취소
                    else {
                        server.cancelLecture(Integer.parseInt(idTextField.getText()), id);

                        // UI 초기화
                        updateTable();
                        idTextField.setText("");
                        infoLabel.setText("강의번호를 입력하거나 목록에서 선택하세요.");

                        // 안내 문구
                        JOptionPane.showMessageDialog(getRootPane(), "취소되었습니다.", "", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(getRootPane(), "서버와 통신 중 오류가 발생했습니다.", "", WARNING_MESSAGE);
                }
            }
        });

        /**
         * 강의 목록 패널과 표
         */
        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(new TitledBorder(new EtchedBorder(), " 강의 목록 "));
        tablePanel.setLayout(new BorderLayout());
        gbAdd(rootPanel, tablePanel, 1, 0, 5, 4, 10, 1);

        table = new JTable();
        table.setRowHeight(25);
        table.getTableHeader().setPreferredSize(new Dimension(10, 30));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // 열 클릭 시 왼쪽 강의 번호에 입력
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int lectureId = (int) table.getValueAt(((JTable) e.getSource()).getSelectedRow(), 1);
                checkAvailable(lectureId);
                idTextField.setText(lectureId + "");
            }
        });

        /**
         * 시간표
         */
        JPanel timeTablePanel = new JPanel();
        timeTablePanel.setBorder(new TitledBorder(new EtchedBorder(), " 시간표 "));
        timeTablePanel.setLayout(new BorderLayout());
        gbAdd(rootPanel, timeTablePanel, 6, 0, 1, 4, 1, 1);

        timeTablePanel.add(timeTable, BorderLayout.CENTER);
        updateTable();

        setVisible(true);
    }

    private void updateTable() {
        String[] columnTitles = {"이수구분", "강의번호", "강의명", "담당교수", "학점", "강의시간", "강의실", "인원"};

        // 테이블에 데이터를 담기 위한 준비
        Object[][] data = new Object[lectures.size()][];
        for (int i = 0; i < lectures.size(); i++) {
            Lecture lecture = lectures.get(i);

            // 교수 이름, 인원 정보 가져오기
            String professorName = null;
            int registeredNumber = 0;
            try {
                professorName = server.getProfessorById(lecture.professorId).name;
                registeredNumber = server.getRegisteredNumber(lecture.id);
            } catch (Exception e) {
                dispose();
            }

            data[i] = new Object[]{lecture.section, lecture.id, lecture.name, professorName,
                    lecture.credit, lecture.time, lecture.room, registeredNumber + "/" + lecture.capacity};
        }

        // 현재 신청 학점 갱신
        // 수강신청한 과목이 있다면 그 학점만큼 더해서 현재 신청 학점을 보여준다
        int credit = 0;
        for (Lecture lecture : lectures) {
            ArrayList<Integer> appliedStudents;
            try {
                appliedStudents = server.getRegisteredStudentId(lecture.id);
                for (int appliedStudent : appliedStudents) {
                    if (id == appliedStudent) {
                        credit += lecture.credit;
                    }
                }
            } catch (Exception e) {
                dispose();
            }
        }
        creditLabel.setText("신청: " + credit + "학점");

        // 시간표도 새로고침
        timeTable.updateTimeTable(id);

        DefaultTableModel model = new DefaultTableModel(data, columnTitles) {
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) { return false; }
        };
        table.setModel(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);

        // 행 크기 지정
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setMaxWidth(100);
        table.getColumnModel().getColumn(3).setMaxWidth(50);
        table.getColumnModel().getColumn(4).setMaxWidth(50);
        table.getColumnModel().getColumn(6).setMaxWidth(100);
        table.getColumnModel().getColumn(7).setMaxWidth(50);
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

    private void checkAvailable(int lectureId) {
        try {
            // 기본 값 : 신청 가능
            infoLabel.setText("수강 신청 가능한 강의입니다.");
            registerButton.setText("신청");
            registerButton.setEnabled(true);

            // 없는 강의를 입력한 경우 안내 출력
            if (!lectureExists(lectureId)) {
                infoLabel.setText("올바른 강의 번호를 입력해주세요.");
                registerButton.setEnabled(false);
                return;
            }

            // 이미 수강신청한 과목이라면 버튼 비활성화
            ArrayList<Integer> registeredStudents = server.getRegisteredStudentId(lectureId);
            for (int registeredStudent : registeredStudents) {
                if (registeredStudent == id) {
                    infoLabel.setText("수강 취소 가능한 강의입니다.");
                    registerButton.setText("취소");
                    registerButton.setEnabled(true);
                    return;
                }
            }

            // 정원이 다 찬 강의를 수강 신청하려고 하는 경우
            if (!server.isRegistrable(lectureId)) {
                infoLabel.setText("수강 가능 인원이 초과한 강의입니다.");
                registerButton.setEnabled(false);
                return;
            }

            // 시간이 중복되는 강의를 수강 신청하려고 하면 버튼을 비활성화
            if (isTimeDuplicated(lectureId)) {
                infoLabel.setText("기존 강의와 시간이 중복됩니다.");
                registerButton.setEnabled(false);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(getRootPane(), "불러오는 도중 오류가 발생했습니다.", "", WARNING_MESSAGE);
        }
    }

    private boolean lectureExists(int lectureId) {
        for (int i = 0; i < lectures.size(); i++) {
            if (lectureId == lectures.get(i).id) return true;
        }
        return false;
    }

    private boolean isTimeDuplicated(int lectureId) {
        // 선택한 강의의 시간표 배열 구하기
        boolean[][] selectedTimeTable = null;
        for (Lecture lecture : lectures) {
            if (lectureId == lecture.id) {
                selectedTimeTable = getTimeTable(lecture.time);
            }
        }
        if (selectedTimeTable == null)
            return false;

        // 시간표 클래스에서 현재 시간표를 가져오고 이를 선택한 강의의 시간표와 비교
        boolean[][] appliedTimeTable = timeTable.getAppliedTimeTable();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 6; j++) {
                if (appliedTimeTable[i][j] && selectedTimeTable[i][j]) {
                    return true;
                }
            }
        }

        return false;
    }

    // 시간표 클래스의 시간 토큰화(문자열 분리) 부분 가져왔음
    // 선택된 강의만 제외하고 나머지 강의의 시간을 부울 값으로 시간표에 넣기
    private boolean[][] getTimeTable(String timeStr) {
        boolean[][] timeTable = new boolean[9][6];

        StringTokenizer ab = new StringTokenizer(timeStr,", ");
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
                    timeTable[i][1] = true;//lecture[k].id
                }
            }else if(F_day.equals("화")) {
                for(int i = F_time1; i <= F_time2; i++) {
                    timeTable[i][2] = true;
                }
            }else if(F_day.equals("수")) {
                for(int i = F_time1; i <= F_time2; i++) {
                    timeTable[i][3] = true;
                }
            }else if(F_day.equals("목")) {
                for(int i = F_time1; i <= F_time2; i++) {
                    timeTable[i][4] = true;
                }
            }else if(F_day.equals("금")) {
                for(int i = F_time1; i <= F_time2; i++) {
                    timeTable[i][5] = true;
                }
            }

            if (S_day.equals("월")) {
                for(int i = S_time1; i <= S_time2; i++) {
                    timeTable[i][1] = true;
                }
            }else if(S_day.equals("화")) {
                for(int i = S_time1; i <= S_time2; i++) {
                    timeTable[i][2] = true;
                }
            }else if(S_day.equals("수")) {
                for(int i = S_time1; i <= S_time2; i++) {
                    timeTable[i][3] = true;
                }
            }else if(S_day.equals("목")) {
                for(int i = S_time1; i <= S_time2; i++) {
                    timeTable[i][4] = true;
                }
            }else if(S_day.equals("금")) {
                for(int i = S_time1; i <= S_time2; i++) {
                    timeTable[i][5] = true;
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
                    timeTable[i][1] = true;
                }
            } else if (F_day.equals("화")) {
                for (int i = F_time1; i <= F_time2; i++) {
                    timeTable[i][2] = true;
                }
            } else if (F_day.equals("수")) {
                for (int i = F_time1; i <= F_time2; i++) {
                    timeTable[i][3] = true;
                }
            } else if (F_day.equals("목")) {
                for (int i = F_time1; i <= F_time2; i++) {
                    timeTable[i][4] = true;
                }
            } else if (F_day.equals("금")) {
                for (int i = F_time1; i <= F_time2; i++) {
                    timeTable[i][5] = true;
                }
            }
        }

        return timeTable;
    }

    public static void main(String[] args) {
        new StudentMain();
    }
}
