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
	
	public enum ScheduleType {
		every_week,
		uneven_only,
		even_only
	}
	
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
	
	public Teacher get_teacher() {
		int result = -1;
		String sql =
			"SELECT teacher_id FROM courses WHERE course_id = " + get_id()
		;
		try {
			java.sql.ResultSet sql_result =
				CourseSelectionSystem.get_statement().executeQuery(sql)
			;
			try {
				if (sql_result.next()) {
					result = sql_result.getInt(1);
				}
			} finally {
				sql_result.close();
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		if (result == -1) {
			System.exit(-1);
		}
		return new Teacher(result);
	}
	
	public ArrayList<Student> get_students() {
		ArrayList<Student> result = new ArrayList<Student>();
		String sql =
			"SELECT student_id FROM selections WHERE course_id = " + get_id()
		;
		try {
			java.sql.ResultSet sql_result =
				CourseSelectionSystem.get_statement().executeQuery(sql)
			;
			try {
				while (sql_result.next()) {
					result.add(new Student(sql_result.getInt(1)));
				}
			} finally {
				sql_result.close();
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		return result;
	}
	
	public static boolean exist_id(int id) {
		String sql = "SELECT course_id FROM courses WHERE course_id = " + id;
		try {
			java.sql.ResultSet sql_result =
				CourseSelectionSystem.get_statement().executeQuery(sql)
			;
			try {
				return sql_result.next();
			} finally {
				sql_result.close();
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
			System.exit(-1);
		}
		return true;
	}
	
}
