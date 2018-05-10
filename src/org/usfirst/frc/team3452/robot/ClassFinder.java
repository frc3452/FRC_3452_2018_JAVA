package org.usfirst.frc.team3452.robot;

/**
 * @author max
 *
 */
public class ClassFinder {

	public static Class classFinder(String classname) {
		Class m_clazz;

		try {
			m_clazz = Class.forName(classname);
			return m_clazz;
		} catch (ClassNotFoundException e) {
		}

		return null;
	}
}
