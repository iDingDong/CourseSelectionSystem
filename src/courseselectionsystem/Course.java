/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionsystem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 堃
 */
public class Course {
	private int m_id;
	
	public Course(int id) {
		m_id = id;
	}
	
	public String get_name() {
		String result = "[Unknown]";
		String sql =
			"SELECT course_name FROM courses WHERE course_id = " + get_id()
		;
		try {
			java.sql.ResultSet sql_result =
				CourseSelectionSystem.get_statement().executeQuery(sql)
			;
			if (sql_result.next()) {
				result = sql_result.getString(1);
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		return result;
	}
	
	public int get_id() {
		return m_id;
	}
	
	public List<Student> get_students() {
		List<Student> result = new ArrayList<Student>();
		String sql =
			"SELECT student_id FROM selections WHERE course_id = " + get_id()
		;
		try {
			java.sql.ResultSet sql_result =
				CourseSelectionSystem.get_statement().executeQuery(sql)
			;
			while (sql_result.next()) {
				result.add(new Student(sql_result.getInt(1)));
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		return result;
	}
	
}
