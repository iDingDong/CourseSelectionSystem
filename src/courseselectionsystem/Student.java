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
public class Student {
	private final long m_id;
	
	public Student(long id) {
		m_id = id;
	}
	
	public long get_id() {
		return m_id;
	}
	
	public String get_name() {
		String result = "[Unknown]";
		String sql =
			"SELECT student_name FROM students WHERE student_id = " +
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
			"SELECT course_id FROM selections WHERE student_id = " +
			get_id() +
			";"
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
	
	public void select_course(Course course) {
		if (course.is_full()) {
			CourseSelectionSystem.send_message("Course already full.");
			return;
		}
		String sql =
			"SELECT couse_id, student_id FROM selections WHERE course_id = " +
			course.get_id() +
			" AND student_id = " +
			get_id() + ";"
		;
		try {
			try (
				java.sql.ResultSet sql_result =
				CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				if (sql_result.next()) {
					CourseSelectionSystem.send_message(
						"Course already selected."
					);
					return;
				}
				sql =
					"INSERT INTO selections VALUES(" +
					course.get_id() +
					", " +
					get_id() +
					");"
				;
				CourseSelectionSystem.get_statement().execute(sql);
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to select course.");
		}
	}
	
}
