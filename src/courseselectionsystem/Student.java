/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionsystem;

import java.util.ArrayList;
import java.util.List;

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
	
	public List<Course> get_courses() {
		List<Course> result = new ArrayList<Course>();
		String sql = "SELECT course_id FROM selections WHERE student_id = " + get_id();
		
		return result;
	}
	
}
