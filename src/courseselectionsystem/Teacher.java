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
public class Teacher {
	private long m_id;
	
	public Teacher(long id) {
		m_id = id;
	}
	
	public long get_id() {
		return m_id;
	}
	
	public String get_name() {
		String result = "[Unknown]";
		String sql =
			"SELECT teacher_name FROM teachers WHERE teacher_id = " +
			get_id() +
			";"
		;
		try {
			try (
				java.sql.ResultSet sql_result =
					CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				if (sql_result.next()) {
					result = sql_result.getString(1);
				}
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		return result;
	}
	
	public String get_password() {
		String result = "[Unknown]";
		String sql =
			"SELECT password FROM teachers WHERE teacher_id = " +
			get_id() +
			";"
		;
		try {
			try (
				java.sql.ResultSet sql_result =
					CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				if (sql_result.next()) {
					result = sql_result.getString(1);
				}
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		return result;
	}
	
	public List<Course> get_courses() {
		ArrayList<Course> result = new ArrayList<Course>();
		String sql =
			"SELECT course_id FROM courses WHERE teacher_id = " + get_id() + ";"
		;
		try {
			try (
				java.sql.ResultSet sql_result =
					CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				while (sql_result.next()) {
					result.add(new Course(sql_result.getLong(1)));
				}
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		return result;
	}
	
	public static boolean exist_id(long id) {
		String sql =
			"SELECT teacher_id FROM teachers WHERE teacher_id = " + id + ";"
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
	
	public boolean exist() {
		return exist_id(get_id());
	}
	
	public void delete_teacher() {
		for (Course course : get_courses()) {
			course.delete_course();
		}
		String sql =
			"DELETE FROM teachers WHERE teacher_id = " + get_id() + ";"
		;
		try {
			CourseSelectionSystem.get_statement().execute(sql);
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to delete.");
			System.exit(-1);
		}
		m_id = -1;
	}
	
	public static String display_info_header(boolean show_password) {
		String result =
			String.format("%1$-13s", "ID") +
			String.format("%1$-13s", "Name")
		;
		if (show_password) {
			result += String.format("%1$-17s", "Password");
		}
		return result;
	}
	
	public String display_info_on_cmd(boolean show_password) {
		String result =
			String.format("%1$-13s", String.valueOf(get_id())) +
			String.format("%1$-13s", get_name())
		;
		if (show_password) {
			result += String.format("%1$-17s", String.valueOf(get_password()));
		}
		return result;
	}
	
}
