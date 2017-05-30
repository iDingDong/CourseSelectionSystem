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
	private long m_id;
	
	public static class Lesson {
		int day_of_week;
		int lesson_of_day;
	}
	
	public enum ScheduleType {
		every_week,
		uneven_only,
		even_only
	}
	
	public static List<Course> get_all_courses() {
		ArrayList<Course> result = new ArrayList<Course>();
		String sql =
			"SELECT course_id FROM courses;";
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
	
	public List<Lesson> get_lessons() {
		ArrayList<Lesson> result = new ArrayList<Lesson>();
		String sql =
			"SELECT day_of_week, lesson_of_day FROM lessons " +
			"WHERE course_id = " +
			get_id() +
			";"
		;
		try {
			try (
				java.sql.ResultSet sql_result =
					CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				while (sql_result.next()) {
					Lesson lesson = new Lesson();
					lesson.day_of_week = sql_result.getInt(1);
					lesson.lesson_of_day = sql_result.getInt(2);
					result.add(lesson);
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
	
	public boolean exist() {
		return exist_id(get_id());
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
		m_id = -1;
	}
	
	public boolean check_new_id(long new_id) {
		if (exist_id(new_id) && new_id != get_id()) {
			CourseSelectionSystem.send_message("ID already occupied.");
			return false;
		}
		return true;
	}
	
	public void change_id(long new_id) {
		if (get_id() == new_id) {
			return;
		}
		if (!check_new_id(new_id)) {
			return;
		}
		String sql1 =
			"UPDATE courses SET course_id = " +
			new_id +
			" WHERE course_id = " +
			get_id() +
			";"
		;
		String sql2 =
			"UPDATE lessons SET course_id = " +
			new_id +
			" WHERE course_id = " +
			get_id() +
			";"
		;
		String sql3 =
			"UPDATE selections SET course_id = " +
			new_id +
			" WHERE course_id = " +
			get_id() +
			";"
		;
		try {
			CourseSelectionSystem.get_statement().execute(sql1);
			CourseSelectionSystem.get_statement().execute(sql2);
			CourseSelectionSystem.get_statement().execute(sql3);
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to modify.");
			System.exit(-1);
		}
		m_id = new_id;
	}
	
	public boolean check_new_name(String new_name) {
		return true;
	}
	
	public void change_name(String new_name) {
		if (!check_new_name(new_name)) {
			return;
		}
		String sql =
			"UPDATE courses SET course_name = '" +
			new_name +
			"' WHERE course_id = " +
			get_id() +
			";"
		;
		try {
			CourseSelectionSystem.get_statement().execute(sql);
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to modify.");
			System.exit(-1);
		}
	}
	
	public boolean check_new_capacity(int new_capacity) {
		if (get_student_count() > new_capacity) {
			CourseSelectionSystem.send_message(
				"New capacity cannot fit existing students."
			);
			return false;
		}
		return true;
	}
	
	public void change_capacity(int new_capacity) {
		if (!check_new_capacity(new_capacity)) {
			return;
		}
		String sql =
			"UPDATE courses SET capacity = " +
			new_capacity +
			" WHERE course_id = " +
			get_id() +
			";"
		;
		try {
			CourseSelectionSystem.get_statement().execute(sql);
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to modify.");
			System.exit(-1);
		}
	}
	
	public boolean check_new_schedule(int new_begin_week, int new_end_week) {
		if (new_begin_week > new_end_week) {
			CourseSelectionSystem.send_message(
				"Begin week should be no later than end week."
			);
			return false;
		}
		return true;
	}
	
	public void change_schedule(int new_begin_week, int new_end_week) {
		if (!check_new_schedule(new_begin_week, new_end_week)) {
			return;
		}
		String sql =
			"UPDATE courses SET begin_week = " +
			new_begin_week +
			", end_week = " +
			new_end_week +
			" WHERE course_id = " +
			get_id() +
			";"
		;
		try {
			CourseSelectionSystem.get_statement().execute(sql);
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to modify.");
			System.exit(-1);
		}
	}
	
	public void set_lessons(List<Lesson> lessons) {
		clear_lessons();
		try {
			for (Lesson lesson : lessons) {
				String sql =
					"INSERT INTO lessons VALUES(" +
					get_id() +
					", " +
					lesson.day_of_week +
					", " +
					lesson.lesson_of_day +
					");"
				;
				CourseSelectionSystem.get_statement().execute(sql);
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to set.");
			System.exit(-1);
		}
		
	}
	
	public void clear_lessons() {
		String sql = "DELETE FROM lessons WHERE course_id = " + get_id() + ";";
		try {
			CourseSelectionSystem.get_statement().execute(sql);
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to delete.");
			System.exit(-1);
		}
	}
	
	public static String display_info_header() {
		return
			String.format("%1$-9s", "C-ID") +
			String.format("%1$-33s", "Name") +
			String.format("%1$-13s", "T-ID") +
			String.format("%1$-8s", "Cap") +
			String.format("%1$-6s", "Sche")
		;
	}
	
	public String display_info_on_cmd() {
		return
			String.format("%1$-9s", String.valueOf(get_id())) +
			String.format("%1$-33s", get_name()) +
			String.format("%1$-13s", String.valueOf(get_teacher().get_id())) +
			String.format(
				"%1$-8s", get_student_count() + "/" + get_capacity()
			) +
			String.format("%1$-6s", get_begin_week() + "-" + get_end_week())
		;
	}
	
}
