/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionsystem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 堃
 */
public class Student {
	private int m_id;
	
	public Student(int id) {
		m_id = id;
	}
	
	public int get_id() {
		return m_id;
	}
	
	public String get_name() {
		String result = "[Unknown]";
		String sql =
			"SELECT student_name FROM students WHERE student_id = " + get_id()
		;
		try {
			java.sql.ResultSet sql_result =
				CourseSelectionSystem.get_statement().executeQuery(sql)
			;
			try {
				if (sql_result.next()) {
					result = sql_result.getString(1);
				}
			} finally {
				sql_result.close();
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		return result;
	}
	
	public ArrayList<Course> get_courses() {
		ArrayList<Course> result = new ArrayList<Course>();
		String sql =
			"SELECT course_id FROM selections WHERE student_id = " + get_id()
		;
		try {
			java.sql.ResultSet sql_result =
				CourseSelectionSystem.get_statement().executeQuery(sql)
			;
			try {
				while (sql_result.next()) {
					result.add(new Course(sql_result.getInt(1)));
				}
			} finally {
				sql_result.close();
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		return result;
	}
	
}
