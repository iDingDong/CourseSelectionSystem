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
	private long m_id;
	
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
	
	public String get_password() {
		String result = "[Unknown]";
		String sql =
			"SELECT password FROM students WHERE student_id = " +
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
	
	public static boolean exist_id(long id) {
		String sql =
			"SELECT student_id FROM students WHERE student_id = " + id + ";"
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
	
	public void select_course(Course course) {
		if (!course.exist()) {
			CourseSelectionSystem.send_message("Course doesn't exist.");
			return;
		}
		if (course.is_full()) {
			CourseSelectionSystem.send_message("Course already full.");
			return;
                }
		String sql =
			"SELECT * FROM selections WHERE course_id = " +
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
                                if (!selectable(course)) {
                                        CourseSelectionSystem.send_message("Schedule already occupied.");
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
	
	public void deselect_course(Course course) {
		String sql =
			"SELECT * FROM selections WHERE course_id = " +
			course.get_id() +
			" AND student_id = " +
			get_id() + ";"
		;
		try {
			try (
				java.sql.ResultSet sql_result =
				CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				if (!sql_result.next()) {
					CourseSelectionSystem.send_message(
						"Course not selected."
					);
					return;
				}
				sql =
					"DELETE FROM selections WHERE course_id = " +
					course.get_id() +
					" and student_id = " +
					get_id() +
					";"
				;
				CourseSelectionSystem.get_statement().execute(sql);
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to deselect course.");
		}
	}
	
	public void delete_student() {
		String sql1 = "DELETE FROM students WHERE student_id = " + get_id() + ";";
		String sql2 =
			"DELETE FROM selections WHERE student_id = " + get_id() + ";"
		;
		try {
			CourseSelectionSystem.get_statement().execute(sql1);
			CourseSelectionSystem.get_statement().execute(sql2);
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to delete.");
			System.exit(-1);
		}
		m_id = -1;
	}
	
	public Course find_course_by_time(
		int week_of_term, int day_of_week, int lesson_of_day
	) {
		Course result = null;
		String sql =
			"SELECT courses.course_id FROM courses, lessons, selections WHERE" +
			" selections.student_id = " + get_id() +
			" AND courses.course_id = selections.course_id"+
			" AND courses.course_id = lessons.course_id" +
			" AND courses.begin_week <= " + week_of_term +
			" AND courses.end_week >= " + week_of_term +
			" AND lessons.day_of_week = " + day_of_week +
			" AND lessons.lesson_of_day = " + lesson_of_day + ";"
		;
		try {
			try (
				java.sql.ResultSet sql_result =
					CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				if (sql_result.next()) {
					result = new Course(sql_result.getLong(1));
				}
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		return result;
	}
	
	public boolean selectable(Course course) {
		List<Course.Lesson> lessons = course.get_lessons();
		for (int i = course.get_begin_week(); i <= course.get_end_week(); ++i) {
			for (Course.Lesson lesson : lessons) {
				if (
					find_course_by_time(
						i, lesson.day_of_week, lesson.lesson_of_day
					) != null
				) {
					return false;
				}
			}
		}
		return true;
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
