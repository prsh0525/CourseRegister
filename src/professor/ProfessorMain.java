package professor;

import home.Home;
import server.Lecture;
import server.Professor;
import server.ServerConnection;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static javax.swing.JOptionPane.WARNING_MESSAGE;

public class ProfessorMain extends JFrame {
    // 아이디 값이 전달되어 저장되는 변수
    public static int id;

    // 서버
    private final ServerConnection server = new ServerConnection();

    //강의 목록
    private final String[] columnNames = {"이수구분", "강의번호", "강의명", "학점", "강의시간", "강의실", "인원", "동작"};
    private final JTable table;

    //강의 등록
    private final JTextField LectureNameField;
    private final JTextField LectureNumField;
    private final JTextField RoomField1;
    private final JButton RegistButton;
    private final JButton ClearButton;
    private final JButton addButton;
    private final JComboBox GradeComboBox;
    private final JSpinner spinner;
    private final JComboBox comboBoxDay;
    private final JComboBox PartComboBox;
    private final JComboBox comboBoxStrat;
    private final JComboBox comboBoxTime;
    private final JComboBox comboBoxDay2;
    private final JComboBox comboBoxStart2;
    private final JComboBox comboBoxTime2;

    // 시간표 패널
    TimeTablePanel timeTablePanel = new TimeTablePanel();

    public ProfessorMain() {
        setTitle("교수용 수강신청 프로그램");
        setBounds(0, 0, 1300, 550);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 루트 패널
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
        add(rootPanel, BorderLayout.CENTER);

        /**
         * 신상정보 패널
         */
        {
            JPanel PersonalInfoPanel = new JPanel();
            PersonalInfoPanel.setBorder(new TitledBorder(
                    new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
                    " 교수 정보 ", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
            gbAdd(rootPanel, PersonalInfoPanel, 0, 0, 1, 1, 1, 1);
            PersonalInfoPanel.setLayout(new BorderLayout());

            Professor professor = null;
            try {
                professor = server.getProfessorById(id);
            } catch (Exception e) {

            }

            JPanel infoInnerPanel = new JPanel();
            infoInnerPanel.setLayout(new GridLayout(2, 2));
            infoInnerPanel.setBorder(new EmptyBorder(5, 10, 8, 10));
            PersonalInfoPanel.add(infoInnerPanel, BorderLayout.CENTER);

            //id라벨
            JLabel IDLabel = new JLabel("아이디: " + professor.id);
            infoInnerPanel.add(IDLabel);

            //이름 라벨
            JLabel NameLabel = new JLabel("이름: " + professor.name);
            infoInnerPanel.add(NameLabel);

            //직책 라벨
            JLabel RoleLabel = new JLabel("직책: " + professor.position);
            RoleLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
            infoInnerPanel.add(RoleLabel);

            //학과 라벨
            JLabel DepartmentLabel = new JLabel("학과: " + professor.major);
            infoInnerPanel.add(DepartmentLabel);
        }

        /**
         * 강의등록 패널
         */
        {
            // border 적용 패널
            JPanel RegistPanel = new JPanel();
            RegistPanel.setBorder(new TitledBorder(
                    new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
                    " 강의 등록 ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
            gbAdd(rootPanel, RegistPanel, 0, 1, 1, 4, 1, 10);
            RegistPanel.setLayout(new BorderLayout());

            // 컴포넌트 패널
            JPanel LectureRegistPanel = new JPanel();
            LectureRegistPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
            LectureRegistPanel.setLayout(new GridBagLayout());
            RegistPanel.add(LectureRegistPanel, BorderLayout.CENTER);

            // 이수구분 라벨
            JLabel PartLabel = new JLabel("이수구분");
            gbAdd(LectureRegistPanel, PartLabel, 0, 0, 1, 1, 1, 1);

            // 이수구분 콤보박스
            PartComboBox = new JComboBox();
            PartComboBox.setModel(new DefaultComboBoxModel(new String[]{"공통교양", "균형교양", "자율교양", "공학소양",
                    "계열교양", "전공필수", "전공선택"}));
            PartComboBox.setSelectedIndex(6);
            gbAdd(LectureRegistPanel, PartComboBox, 1, 0, 4, 1, 10, 1);

            // 교과목 이름 라벨
            JLabel LectureNameLabel = new JLabel("강의명");
            gbAdd(LectureRegistPanel, LectureNameLabel, 0, 1, 1, 1, 1, 1);

            // 교과목 입력 필드
            LectureNameField = new JTextField();
            LectureNameField.setColumns(20);
            gbAdd(LectureRegistPanel, LectureNameField, 1, 1, 4, 1, 1, 1);

            // 교과목 번호 라벨
            JLabel lblNewLabel = new JLabel("강의번호");
            gbAdd(LectureRegistPanel, lblNewLabel, 0, 2, 1, 1, 1, 1);

            // 교과목 번호 필드
            LectureNumField = new JTextField();
            LectureNumField.setColumns(10);
            gbAdd(LectureRegistPanel, LectureNumField, 1, 2, 4, 1, 1, 1);

            // 학점 라벨
            JLabel GradeLabel = new JLabel("학점");
            gbAdd(LectureRegistPanel, GradeLabel, 0, 3, 1, 1, 1, 1);

            // 학점 콤보박스
            GradeComboBox = new JComboBox();
            GradeComboBox.setModel(
                    new DefaultComboBoxModel(new String[]{"0", "1", "2", "3", "4"}));
            GradeComboBox.setSelectedIndex(3);
            gbAdd(LectureRegistPanel, GradeComboBox, 1, 3, 4, 1, 1, 1);

            // 강의실 라벨-1
            JLabel RoomLabel_1 = new JLabel("강의실");
            gbAdd(LectureRegistPanel, RoomLabel_1, 0, 4, 1, 1, 1, 1);

            // 강의실 필드-1
            RoomField1 = new JTextField();
            RoomField1.setColumns(10);
            gbAdd(LectureRegistPanel, RoomField1, 1, 4, 4, 1, 1, 1);

            // 수강인원 라벨
            JLabel PersonNumLabel_1 = new JLabel("수강인원 ");
            gbAdd(LectureRegistPanel, PersonNumLabel_1, 0, 5, 1, 1, 1, 1);

            // 수강인원 스피너
            spinner = new JSpinner();
            gbAdd(LectureRegistPanel, spinner, 1, 5, 4, 1, 1, 1);

            // 강의시간 라벨
            JPanel lectureTimePanel = new JPanel();
            lectureTimePanel.setLayout(new BorderLayout());
            gbAdd(LectureRegistPanel, lectureTimePanel, 0, 6, 5, 1, 1, 1);

            JLabel LectureTimeLabel = new JLabel("강의시간");
            lectureTimePanel.add(LectureTimeLabel, BorderLayout.CENTER);

            // +버튼
            addButton = new JButton("+");
            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // +버튼
                    if (addButton.getText().equals("+")) {
                        comboBoxDay2.setEnabled(true);
                        comboBoxStart2.setEnabled(true);
                        comboBoxTime2.setEnabled(true);
                        addButton.setText("-");
                    } else {
                        comboBoxDay2.setEnabled(false);
                        comboBoxStart2.setEnabled(false);
                        comboBoxTime2.setEnabled(false);
                        addButton.setText("+");
                    }
                }
            });
            addButton.setPreferredSize(new Dimension(50, 20));
            lectureTimePanel.add(addButton, BorderLayout.EAST);

            // 강의시간 콤보박스 1
            JPanel timePanel1 = new JPanel();
            timePanel1.setLayout(new GridLayout(1, 3));
            gbAdd(LectureRegistPanel, timePanel1, 0, 7, 5, 1, 1, 1);

            String[] dayLabel = {"요일", "월요일", "화요일", "수요일", "목요일", "금요일"};
            String[] timeLabel = {"시작시간", "9시", "10시", "11시", "12시", "13시", "14시", "15시", "16시"};
            String[] durationLabel = {"연강시간", "1시간", "2시간", "3시간", "4시간", "5시간"};

            // 요일 콤보박스1
            comboBoxDay = new JComboBox();
            comboBoxDay.setModel(new DefaultComboBoxModel(dayLabel));
            timePanel1.add(comboBoxDay);

            // 시작시간 콤보박스1
            comboBoxStrat = new JComboBox();
            comboBoxStrat.setModel(new DefaultComboBoxModel(timeLabel));
            timePanel1.add(comboBoxStrat);

            // 연강시간 콤보박스1
            comboBoxTime = new JComboBox();
            comboBoxTime.setModel(new DefaultComboBoxModel(durationLabel));
            timePanel1.add(comboBoxTime);

            // 강의시간 콤보박스 2
            JPanel timePanel2 = new JPanel();
            timePanel2.setLayout(new GridLayout(1, 3));
            gbAdd(LectureRegistPanel, timePanel2, 0, 8, 5, 1, 1, 1);

            // 요일 콤보박스2
            comboBoxDay2 = new JComboBox();
            comboBoxDay2.setModel(new DefaultComboBoxModel(dayLabel));
            comboBoxDay2.setEnabled(false);
            timePanel2.add(comboBoxDay2);

            // 시작시간 콤보박스2
            comboBoxStart2 = new JComboBox();
            comboBoxStart2.setModel(new DefaultComboBoxModel(timeLabel));
            comboBoxStart2.setEnabled(false);
            timePanel2.add(comboBoxStart2);


            // 연강시간 콤보박스2
            // 시작시간에 따른 연강시간 비활성화 필요
            comboBoxTime2 = new JComboBox();
            comboBoxTime2.setModel(new DefaultComboBoxModel(durationLabel));
            comboBoxTime2.setEnabled(false);
            timePanel2.add(comboBoxTime2);

            // 등록버튼과 이벤트 처리기
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BorderLayout());
            gbAdd(LectureRegistPanel, buttonPanel, 0, 9, 5, 1, 1, 1);

            RegistButton = new JButton("등록");
            buttonPanel.add(RegistButton, BorderLayout.CENTER);
            RegistButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Lecture lecture = new Lecture(0, null, null, 0, null, null,
                            0, 0);
                    /// 등록
                    {
                        try {
                            lecture.id = Integer.parseInt(LectureNumField.getText());
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(getRootPane(), "강의번호를 입력하세요.", "", WARNING_MESSAGE);
                            ex.printStackTrace();
                            return;
                        }
                        // 수정
                        if (RegistButton.getText().equals("수정")) {
                            //삭제
                            try {
                                // 서버 삭제
                                server.deleteLecture(lecture.id);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(getRootPane(), "수정 중 오류가 발생했습니다.", "", WARNING_MESSAGE);
                                ex.printStackTrace();
                            }
                        }

                        // 강의명, 강의실 미입력
                        lecture.name = LectureNameField.getText();
                        lecture.room = RoomField1.getText();
                        if (lecture.name.isEmpty() || lecture.room.isEmpty()) {
                            JOptionPane.showMessageDialog(getRootPane(), "강의명 또는 강의실을 입력하세요.", "", WARNING_MESSAGE);
                            return;
                        }

                        lecture.section = (String) PartComboBox.getSelectedItem();
                        lecture.credit = Integer.parseInt((String) GradeComboBox.getSelectedItem());
                        lecture.capacity = (int) spinner.getValue();
                        lecture.professorId = id;

                        //강의시간 미 입력시
                        if (comboBoxDay.getSelectedItem().equals("요일") || comboBoxStrat.getSelectedItem().equals("시작시간") ||
                                comboBoxTime.getSelectedItem().equals("연강시간")) {
                            JOptionPane.showMessageDialog(getRootPane(), "강의시간을 입력하세요.", "", WARNING_MESSAGE);
                            return;
                        }

                        //요일시작시간 //시작시간 -> 교시 바꾸어주어도 됨
                        int start = comboBoxStrat.getSelectedIndex() + 8;
                        int time = comboBoxTime.getSelectedIndex();
                        lecture.time = ((String) comboBoxDay.getSelectedItem()).substring(0, 1) + (start - 8);

                        //9교시 이상 등록
                        if (start + time > 17) {
                            JOptionPane.showMessageDialog(getRootPane(), "8교시까지 등록 가능합니다.", "", WARNING_MESSAGE);
                            return;
                        }

                        // 시간 중복처리
                        if (isExistance((String) comboBoxDay.getSelectedItem(), start, time)) {
                            JOptionPane.showMessageDialog(getRootPane(), "시간이 중복되었습니다.", "", WARNING_MESSAGE);
                            return;
                        }
                        // 연강시간 1이 아닌 경우
                        if (time != 1)
                            lecture.time += "-" + (start + time - 9);
                        if (addButton.getText().equals("-")) {
                            //강의시간 미입력시
                            if (comboBoxDay2.getSelectedItem().equals("요일") || comboBoxStart2.getSelectedItem().equals("시작시간") ||
                                    comboBoxTime2.getSelectedItem().equals("연강시간")) {
                                JOptionPane.showMessageDialog(getRootPane(), "강의시간을 입력하세요.", "", WARNING_MESSAGE);
                                return;
                            }
                            //9교시 이상 등록
                            if (start + time > 17) {
                                JOptionPane.showMessageDialog(getRootPane(), "8교시까지 등록 가능합니다.", "", WARNING_MESSAGE);
                                return;
                            }

                            start = comboBoxStart2.getSelectedIndex() + 8;
                            time = comboBoxTime2.getSelectedIndex();
                            lecture.time += ", " + ((String) comboBoxDay2.getSelectedItem()).substring(0, 1) + (start - 8);
                            // 시간 중복처리
                            if (isExistance((String) comboBoxDay2.getSelectedItem(), start, time)) {
                                JOptionPane.showMessageDialog(getRootPane(), "시간이 중복되었습니다.", "", WARNING_MESSAGE);
                                return;
                            }
                            // 연강시간 1이 아닌 경우
                            if (time != 1)
                                lecture.time += "-" + (start + time - 9);
                        }
                    }
                    //전송
                    try {
                        ServerConnection server = new ServerConnection();
                        server.registerLecture(lecture);
                        JOptionPane.showMessageDialog(getRootPane(), "등록되었습니다.", "", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception a) {
                        JOptionPane.showMessageDialog(getRootPane(), "등록 중 오류가 발생했습니다.", "", WARNING_MESSAGE);
                        a.printStackTrace();
                    }
                    //강의목록 업데이트
                    updateTable();
                    clear();
                }// actionperformed end
            });// 등록버튼 이벤트 처리기 end

            //clear 버튼
            ClearButton = new JButton("초기화");
            ClearButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    clear();
                }
            });
            buttonPanel.add(ClearButton, BorderLayout.EAST);
        }

        /**
         *  교수 강의리스트 패널
         */
        {
            JPanel LectureListPanel = new JPanel();
            LectureListPanel.setBorder(new TitledBorder(
                    new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
                    " 강의 목록 ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
            gbAdd(rootPanel, LectureListPanel, 1, 0, 5, 5, 100, 1);

            // 테이블
            LectureListPanel.setLayout(new BorderLayout());
            table = new JTable();
            table.setRowHeight(25);
            table.getTableHeader().setPreferredSize(new Dimension(10, 30));
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String name = (String) table.getValueAt(((JTable) e.getSource()).getSelectedRow(), 2);
                    int classId = (int) table.getValueAt(((JTable) e.getSource()).getSelectedRow(), 1);
                    switch (((JTable) e.getSource()).getSelectedColumn()) {
                        // get clicked column
                        case 6:
                            // 7번째 열 - 수강인원 확인
                            new NumStudent(classId, name);// 수강인원 다이얼로그 호출
                            break;
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            // 클릭한 행 왼쪽테이블에 텍스트 추가
                            clear();
                            selectedRow(((JTable) e.getSource()).getSelectedRow());
                            RegistButton.setText("수정");
                            break;
                        case 7:
                            // 8번 삭제 열 - 클릭 시 해당강의 삭제
                            try {
                                // 서버 삭제
                                server.deleteLecture(classId);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(getRootPane(), "삭제 도중 오류가 발생했습니다.", "", WARNING_MESSAGE);
                                ex.printStackTrace();
                            }
                            updateTable(); // 삭제 후 업데이트
                            clear();
                            JOptionPane.showMessageDialog(getRootPane(), name + " 강의를 삭제하였습니다.", "", WARNING_MESSAGE);
                            break;
                        default:
                            break;
                    }
                } //마우스 클릭 이벤트 end
            }); //테이블 이벤트 추가 end

            updateTable();// 초기 업데이트
            table.setFillsViewportHeight(true);

            JScrollPane scrollPane = new JScrollPane(table);
            LectureListPanel.add(scrollPane);
        }

        /**
         * 시간표 패널
         */
        {
            JPanel TablePanel = new JPanel();
            TablePanel.setBorder(
                    new TitledBorder(null, " 시간표 ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
            TablePanel.setLayout(new BorderLayout());
            gbAdd(rootPanel, TablePanel, 6, 0, 2, 5, 1, 1);

            TablePanel.add(timeTablePanel, BorderLayout.CENTER);
        }

        setVisible(true);
    }

    /**
     * 교수 강의정보 호출
     */
    private ArrayList<Lecture> callProfLecture(){
        ArrayList<Lecture> lectureList = null;
        ArrayList<Lecture> plectList = new ArrayList<Lecture>();

        //전체 강의 목록 불러오기
        try {
            lectureList = server.getLecture();
        }
        catch (Exception e) {
            // 에러 창 출력
            JOptionPane.showMessageDialog(getRootPane(), "강의 목록을 가져오는 도중 오류가 발생했습니다.", "", WARNING_MESSAGE);
            // 콘솔에 에러 내용 출력
            e.printStackTrace();
        }

        // 해당교수 강의만 따로 저장
        for(int i=0; i< lectureList.size(); i++){
            if(lectureList.get(i).professorId == id)
                plectList.add(lectureList.get(i));
        }
        return plectList;
    }

    /**
     * 표 업데이트
     */
    private void updateTable(){
        ArrayList<Lecture> plectList = callProfLecture();
        Object[][] data = new Object[plectList.size()][];
        // 테이블 초기화 할 데이터 저장
        try {
            for (int i = 0; i < plectList.size(); i++) {
                data[i] = new Object[]{plectList.get(i).section, plectList.get(i).id, plectList.get(i).name,
                        plectList.get(i).credit, plectList.get(i).time, plectList.get(i).room,
                        server.getRegisteredNumber(plectList.get(i).id) + "/" + plectList.get(i).capacity};
            }
        }
        catch (Exception e) {
            // 에러 창 출력
            JOptionPane.showMessageDialog(getRootPane(), "정보를 불러오지 못했습니다.", "", WARNING_MESSAGE);
            // 콘솔에 에러 내용 출력
            e.printStackTrace();
        }

        // 팀장 추가 - 테이블 수정 불가 옵션
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        table.setModel(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getColumnModel().getColumn(7).setCellRenderer(new DeleteButtonCell());
        table.getColumnModel().getColumn(7).setCellEditor(new DeleteButtonCell());

        // 행의 크기를 지정한다
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setMaxWidth(100);
        table.getColumnModel().getColumn(3).setMaxWidth(50);
        table.getColumnModel().getColumn(5).setMaxWidth(100);
        table.getColumnModel().getColumn(6).setMaxWidth(50);
        table.getColumnModel().getColumn(7).setMaxWidth(100);

        timeTablePanel.updateTimeTable(plectList);
    }

    /**
     * 시간표 중복확인 함수(중복o:true, 중복x:false)
     */
    private boolean isExistance(String Day, int start, int time){
        ArrayList<Lecture> plectList = callProfLecture();

        for(int i=0;i< plectList.size();i++) {
            StringTokenizer st = new StringTokenizer(plectList.get(i).time,", ");
            while(st.hasMoreTokens()){
                String token = st.nextToken();

                //요일이 다른경우 건너뜀
                if(token.substring(0, 1).equals(Day) == false)
                    continue;

                // 월1과 같은 형태일 경우
                if(token.length() == 2) {
                    for (int j = 0; j < time; j++) {
                        if (Integer.parseInt(token.substring(1, 2)) == start + j - 8)
                            return true;
                    }
                }
                // 월1-3과 같은 형태일 경우
                else if(token.length() == 4){
                    for(int j=0; j<time; j++){
                        if(Integer.parseInt(token.substring(3, 4)) >= start+j-8
                                && Integer.parseInt(token.substring(1, 2)) <= start+j-8)
                            return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 등록필드 클리어 함수
     */
    private void clear(){
        GradeComboBox.setSelectedIndex(3);
        PartComboBox.setSelectedIndex(6);
        LectureNameField.setText("");
        LectureNumField.setText("");
        RoomField1.setText("");

        comboBoxDay.setSelectedIndex(0);
        comboBoxStrat.setSelectedIndex(0);
        comboBoxTime.setSelectedIndex(0);

        comboBoxDay2.setEnabled(false);
        comboBoxDay2.setSelectedIndex(0);
        comboBoxStart2.setEnabled(false);
        comboBoxStart2.setSelectedIndex(0);
        comboBoxTime2.setEnabled(false);
        comboBoxTime2.setSelectedIndex(0);
        addButton.setText("+");

        spinner.setValue(0);
        RegistButton.setText("등록");
    }

    /**
     * 선택 테이블 정보 -> 등록필드 세팅 함수
     */
    private void selectedRow(int row){
        String[] array = new String[]{"공통교양", "균형교양", "자율교양", "공학소양", "계열교양", "전공필수", "전공선택"};
        String s = (String) table.getValueAt(row, 0);
        for(int i=0; i< array.length; i++) {
            if (array[i].equals(s))
                PartComboBox.setSelectedIndex(i);
        }
        LectureNumField.setText(table.getValueAt(row, 1).toString());
        LectureNameField.setText((String) table.getValueAt(row, 2));

        GradeComboBox.setSelectedIndex((int) table.getValueAt(row, 3));
        StringTokenizer st = new StringTokenizer((String) table.getValueAt(row, 4),", ");
        array = new String[]{"월", "화", "수", "목", "금"};
        //토큰 1개
        if (st.hasMoreTokens()) {
            String token = st.nextToken();
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(token.substring(0, 1)))
                    comboBoxDay.setSelectedIndex(i + 1);
            }
            comboBoxStrat.setSelectedIndex(Integer.parseInt(token.substring(1, 2)));
            // 월1과 같은 형태일 경우
            if (token.length() == 2) {
                comboBoxTime.setSelectedIndex(1);
            }
            // 월1-3과 같은 형태일 경우
            if (token.length() == 4) {
                comboBoxTime.setSelectedIndex(Integer.parseInt(token.substring(3, 4))
                        - Integer.parseInt(token.substring(1, 2)) + 1);
            }

        }
        //토큰 2개
        if (st.hasMoreTokens()) {
            String token = st.nextToken();
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(token.substring(0, 1)))
                    comboBoxDay2.setSelectedIndex(i + 1);
            }
            comboBoxStart2.setSelectedIndex(Integer.parseInt(token.substring(1, 2)));
            // 월1과 같은 형태일 경우
            if (token.length() == 2) {
                comboBoxTime2.setSelectedIndex(1);
            }
            // 월1-3과 같은 형태일 경우
            if (token.length() == 4) {
                comboBoxTime2.setSelectedIndex(Integer.parseInt(token.substring(3, 4))
                        - Integer.parseInt(token.substring(1, 2)) + 1);
            }
            comboBoxDay2.setEnabled(true);
            comboBoxStart2.setEnabled(true);
            comboBoxTime2.setEnabled(true);
            addButton.setText("-");
        }

        RoomField1.setText((String) table.getValueAt(row, 5));
        spinner.setValue(Integer.parseInt(((String) table.getValueAt(row, 6)).substring(2)));
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

    // 테이블에서 삭제 버튼을 보여주기 위한 클래스
    class DeleteButtonCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
        JButton deleteButton;

        public DeleteButtonCell() {
            deleteButton = new JButton("삭제");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
        }
        @Override
        public Object getCellEditorValue() {
            return null;
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return deleteButton;
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return deleteButton;
        }
    }

    public static void main(String[] args) {
        new ProfessorMain();
    }
}