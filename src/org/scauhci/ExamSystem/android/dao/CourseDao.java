package org.scauhci.ExamSystem.android.dao;

import java.util.HashMap;

import org.scauhci.ExamSystem.android.pojo.CoursePojo;
import org.scauhci.ExamSystem.android.tool.ExecuteResultFlag;
import org.scauhci.ExamSystem.android.tool.HashValue;

import android.database.Cursor;
import android.text.format.Time;

public class CourseDao {

	DaoHelper daoHelper = new DaoHelper(null, "exam_online.db", null, 0);
	String tableName = "course";
	private CoursePojo latestCoursePojo = null;

	public CourseDao() {
		// update();
	}

	public int add(CoursePojo coursePojo) {
		int executeResult = ExecuteResultFlag.ERROR;

		long courseId;
		for (courseId = HashValue.getDJBHashValue(coursePojo.getCourseName()); getCoursePojoByCourseId(Long
				.toHexString(courseId)) != null; courseId++)
			;
		coursePojo.setCourseId(Long.toHexString(courseId));

		String[] keys = { "courseId", "courseName", "courseType" };
		String[] values = { coursePojo.getCourseId(),
				coursePojo.getCourseName(), coursePojo.getCourseType() + "" };

		daoHelper.insert(tableName, keys, values);

		return executeResult;
	}

	public int delete(CoursePojo coursePojo) {
		int executeResult = ExecuteResultFlag.ERROR;

		String[] keys = { "courseId" };
		String[] values = { coursePojo.getCourseId() };

		daoHelper.delete(tableName, keys, values);

		return executeResult;
	}

	public int change(CoursePojo coursePojo) {
		int executeResult = ExecuteResultFlag.ERROR;

		HashMap<String, String> keyValueMap = getKeyValueMapByCoursePojo(coursePojo);

		String[] keys = new String[keyValueMap.size()];
		keyValueMap.keySet().toArray(keys);
		String[] newValues = new String[keys.length];
		keyValueMap.values().toArray(newValues);
		String[] whereConditionKeys = { "courseId" };
		String[] whereConditionValues = { coursePojo.getCourseId() };

		daoHelper.update(tableName, keys, newValues, whereConditionKeys,
				whereConditionValues);

		return executeResult;
	}

	public HashMap<String, String> getKeyValueMapByCoursePojo(
			CoursePojo coursePojo) {
		HashMap<String, String> keyValueMap = new HashMap<String, String>();

		if (coursePojo.getCourseName() != null) {
			keyValueMap.put("courseName", coursePojo.getCourseName());
		}
		if (coursePojo.getCourseType() != -1) {
			keyValueMap.put("courseType", coursePojo.getCourseType() + "");
		}

		return keyValueMap;
	}

	public CoursePojo getCoursePojoByCourseId(String courseId) {
		CoursePojo coursePojo = new CoursePojo();

		String[] keys = { "*" };
		String[] whereConditionKeys = { "courseId" };
		String[] whereConditionValues = { courseId };

		Cursor courseCursor = daoHelper.select(tableName, keys,
				whereConditionKeys, whereConditionValues);
		while (courseCursor.moveToNext()) {
			coursePojo.setCourseId(courseCursor.getString(courseCursor
					.getColumnIndex("courseId")));
			coursePojo.setCourseName(courseCursor.getString(courseCursor
					.getColumnIndex("courseName")));
			coursePojo.setCourseType(courseCursor.getInt(courseCursor
					.getColumnIndex("courseType")));
		}

		return coursePojo;
	}

	public CoursePojo completeCoursePojo(CoursePojo coursePojo) {
		HashMap<String, String> keyValueMap = getKeyValueMapByCoursePojo(coursePojo);

		String[] keys = { "*" };
		String[] whereConditionKeys = new String[keyValueMap.size()];
		keyValueMap.keySet().toArray(whereConditionKeys);
		String[] whereConditionValues = new String[whereConditionKeys.length];
		keyValueMap.values().toArray(whereConditionValues);

		Cursor courseCursor = daoHelper.select(tableName, keys,
				whereConditionKeys, whereConditionValues);
		while (courseCursor.moveToNext()) {
			coursePojo.setCourseId(courseCursor.getString(courseCursor
					.getColumnIndex("courseId")));
			coursePojo.setCourseName(courseCursor.getString(courseCursor
					.getColumnIndex("courseName")));
			coursePojo.setCourseType(courseCursor.getInt(courseCursor
					.getColumnIndex("courseType")));
		}

		return coursePojo;
	}
}