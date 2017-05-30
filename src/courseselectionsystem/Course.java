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
	private final long m_id;
	
	public enum ScheduleType {
		every_week,
		uneven_only,
		even_only
	}
	
	public Course(long id) {
		m_id = id;
	}
	
	public long get_id() {
		return m_id;
	}
	
	public String get_name() {
		String result = "[Unknown]";
		String sql =
			"SELECT course_name FROM courses WHERE course_id = " +
			get_id() +
			";"
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
	
	public Teacher get_teacher() {
		long result = -1;
		String sql =
			"SELECT teacher_id FROM courses WHERE course_id = " + get_id() + ";"
		;
		try {
			try (
				java.sql.ResultSet sql_result =
					CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				if (sql_result.next()) {
					result = sql_result.getInt(1);
				}
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		if (result == -1) {
			System.exit(-1);
		}
		return new Teacher(result);
	}
	
	public int get_capacity() {
		int result = -1;
		String sql =
			"SELECT capacity FROM courses WHERE course_id = " + get_id() + ";"
		;
		try {
			try (
				java.sql.ResultSet sql_result =
					CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				if (sql_result.next()) {
					result = sql_result.getInt(1);
				}
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		if (result == -1) {
			System.exit(-1);
		}
		return result;
	}
	
	public int get_begin_week() {
		int result = -1;
		String sql =
			"SELECT begin_week FROM courses WHERE course_id = " + get_id() + ";"
		;
		try {
			try (
				java.sql.ResultSet sql_result =
					CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				if (sql_result.next()) {
					result = sql_result.getInt(1);
				}
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		if (result == -1) {
			System.exit(-1);
		}
		return result;
	}
	
	public int get_end_week() {
		int result = -1;
		String sql =
			"SELECT end_week FROM courses WHERE course_id = " + get_id() + ";"
		;
		try {
			try (
				java.sql.ResultSet sql_result =
					CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				if (sql_result.next()) {
					result = sql_result.getInt(1);
				}
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		if (result == -1) {
			System.exit(-1);
		}
		return result;
	}
	
	public int get_student_count() {
		int result = -1;
		String sql =
			"SELECT COUNT(*) FROM selections WHERE course_id = " +
			get_id() +
			";"
		;
		try {
			try (
				java.sql.ResultSet sql_result =
					CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				if (sql_result.next()) {
					result = sql_result.getInt(1);
				}
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		if (result == -1) {
			System.exit(-1);
		}
		return result;
	}
	
	public boolean is_full() {
		return get_student_count() >= get_capacity();
	}
	
	public List<Student> get_students() {
		ArrayList<Student> result = new ArrayList<Student>();
		String sql =
			"SELECT student_id FROM selections WHERE course_id = " +
			get_id() +
			";"
		;
		try {
			try (
				java.sql.ResultSet sql_result =
					CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				while (sql_result.next()) {
					result.add(new Student(sql_result.getLong(1)));
				}
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		return result;
	}
	
	public static boolean exist_id(long id) {
		String sql =
			"SELECT course_id FROM courses WHERE course_id = " + id + ";"
		;
		try {
			try (
				java.sql.ResultSet sql_result =
					CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				return sql_result.next();
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
			System.exit(-1);
		}
		return true;
	}
	
	public void delete_course() {
		String sql1 = "DELETE FROM courses WHERE course_id = " + get_id() + ";";
		String sql2 =
			"DELETE FROM selections WHERE course_id = " + get_id() + ";"
		;
		String sql3 = "DELETE FROM lessons WHERE course_id = " + get_id() + ";";
		try {
			CourseSelectionSystem.get_statement().execute(sql1);
			CourseSelectionSystem.get_statement().execute(sql2);
			CourseSelectionSystem.get_statement().execute(sql3);
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to delete.");
			System.exit(-1);
		}
	}
	
}
