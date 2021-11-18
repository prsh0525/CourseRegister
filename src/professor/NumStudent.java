package professor;

import server.Student;
import server.Lecture;
import server.ServerConnection;
import student.StudentMain;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import static javax.swing.JOptionPane.WARNING_MESSAGE;

class NumStudent extends JDialog {
    private JTable table;
    private ServerConnection server = new ServerConnection();
    /**
     * Create the dialog.
     */
    public NumStudent(int id, String titleName) {
        setBounds(100, 100, 300, 200);
        setLayout(new BorderLayout());
        setTitle(titleName + "(" + id +") 수강인원");
        setResizable(false);

        //수강인원 정보 호출
        ArrayList<Integer> stList = null;
        try {
            stList = server.getRegisteredStudentId(id);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(getRootPane(), "불러오는 도중 오류가 발생했습니다.", "", WARNING_MESSAGE);
        }

        // 테이블 데이터 setting(수강인원 정보)
        Object data[][] = new Object[stList.size()][];
        try {
            int j = 0;
            for (int i=0; i<stList.size(); i++) {
                Student s = server.getStudentById(stList.get(i));
                data[i] = new Object[]{s.id, s.name, s.major, s.grade};
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(getRootPane(), "불러오는 도중 오류가 발생했습니다.", "", WARNING_MESSAGE);
        }

        // 테이블 set
        table = new JTable();
        table.setModel(new DefaultTableModel(data, new String[]{"학번", "이름", "학과", "학년"}));
        table.setEnabled(false);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.getTableHeader().setPreferredSize(new Dimension(10, 30));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
