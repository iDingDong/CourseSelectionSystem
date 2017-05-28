/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionsystem;

import java.sql.SQLException;

/**
 *
 * @author 堃
 */
public class TeacherEntry {
	public static class Info {
		int id;
		String password;
	};
	
	public static interface LoginHandler {
		public abstract Info handle();
	}
	
	private Teacher m_user;
	private static LoginHandler s_login_handler;
	
	public Teacher get_user() {
		return m_user;
	}
	
	public void run() {
		Info info;
		for (int count = 0; ; ++count) {
			if (count >= 3) {
				CourseSelectionSystem.send_message(
					"3 attempt failed. Returning."
				);
				return;
			}
			if (s_login_handler != null) {
				info = s_login_handler.handle();
			} else {
				info = new Info();
				CourseSelectionSystem.send_cmd_message("ID: ");
				info.id = Integer.valueOf(
					CourseSelectionSystem.get_cmd_input_string()
				);
				CourseSelectionSystem.send_cmd_message("Password: ");
				info.password = CourseSelectionSystem.get_cmd_input_string();
			}
			if (login(info)) {
				break;
			}
			CourseSelectionSystem.send_message("Failed to login.");
		}
		CourseSelectionSystem.send_cmd_message(
			"Welcome," + m_user.get_name() + "\n"
		);
	}
	
	private boolean login(Info info) {
		String sql =
			"SELECT password FROM teachers WHERE teacher_id = " + info.id
		;
		try {
			java.sql.ResultSet sql_result =
				CourseSelectionSystem.get_statement().executeQuery(sql)
			;
			try {
				if (sql_result.next()) {
					if (info.password.equals(sql_result.getString(1))) {
						m_user = new Teacher(info.id);
						return true;
					}
				}
			} finally {
				sql_result.close();
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		return false;
	}
	
}
