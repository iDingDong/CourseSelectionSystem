/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionsystem;

/**
 *
 * @author 堃
 */
public class TimeTable {
	private Student m_student;
	
	public Student get_student() {
		return m_student;
	}
	
	public void set_student(Student student) {
		m_student = student;
	}
	
	public Course inquire_course_by_time(
		int week_of_term,
		int day_of_week,
		int lesson_of_day
	) {
		if (
			week_of_term >= 0 &&
			day_of_week >= 0 &&
			day_of_week <= 6 &&
			lesson_of_day >= 0 &&
			lesson_of_day <= 5
		) {
		}
		return null;
	}
	
}
