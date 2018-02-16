package com.bdpanajoto.examples.db.initialize;

import java.util.ArrayList;
import java.util.List;

import com.bdpanajoto.examples.db.DatabaseInterface;

public final class SetupDatabase {

	private SetupDatabase() {
	}

	public static void init(DatabaseInterface db) {
		initAppVersion(db);
		cleanup(db);
		initDept(db);
		initEmp(db);
	}

	private static void initAppVersion(DatabaseInterface db) {
		db.executeSQL("DROP TABLE app_version PURGE");
		db.executeSQL("CREATE TABLE app_version (app_version VARCHAR2(100), valid_from DATE, valid_to DATE)");
		db.executeSQL("INSERT INTO app_version (app_version, valid_from, valid_to) "
				+ "SELECT '0.0.1', sysdate, TO_DATE('December 31, 9999, 11:59 P.M.',"
				+ "'Month dd, YYYY, HH:MI A.M.','NLS_DATE_LANGUAGE = American') FROM dual");
	}

	private static void cleanup(DatabaseInterface db) {
		db.executeSQL("DROP TABLE employee");
		db.executeSQL("DROP TABLE department");
	}

	private static void initDept(DatabaseInterface db) {
		db.executeSQL("CREATE TABLE department (id NUMBER CONSTRAINT department_pk PRIMARY KEY,"
				+ "name VARCHAR2(50),location VARCHAR2(50))");

		List<String> sqlList = new ArrayList<>();
		sqlList.add("INSERT INTO department VALUES (1,'Business','London')");
		sqlList.add("INSERT INTO department VALUES (2,'IT','India')");
		sqlList.add("INSERT INTO department VALUES (3,'Sales','New York')");
		sqlList.add("INSERT INTO department VALUES (4,'Operations','New York')");
		sqlList.add("INSERT INTO department VALUES (5,'IT','Bulgaria')");
		db.executeBatchSQL(sqlList);
	}

	private static void initEmp(DatabaseInterface db) {
		db.executeSQL("CREATE TABLE " + "employee (id NUMBER CONSTRAINT employee_pk PRIMARY KEY," + "name VARCHAR2(50),"
				+ "job VARCHAR2(50)," + "manager_id NUMBER," + "hiredate DATE," + "salary NUMBER(7,2),"
				+ "department_id NUMBER CONSTRAINT department_fk REFERENCES department)");

		List<String> sqlList = new ArrayList<>();
		sqlList.add(
				"INSERT INTO employee VALUES (1,'John Stein','Manager',NULL,to_date('17-12-1980','dd-mm-yyyy'),5000,1)");
		sqlList.add(
				"INSERT INTO employee VALUES (2,'Viktor Frankel','Salesman',1,to_date('20-2-1981','dd-mm-yyyy'),1600,3)");
		sqlList.add(
				"INSERT INTO employee VALUES (3,'Sessilia Smith','Salesman',1,to_date('22-2-1981','dd-mm-yyyy'),1250,3)");
		sqlList.add(
				"INSERT INTO employee VALUES (4,'John Doe','Manager',NULL,to_date('2-4-1981','dd-mm-yyyy'),2975,1)");
		sqlList.add(
				"INSERT INTO employee VALUES (5,'Peter Liskowitz','Programmer',4,to_date('28-9-1981','dd-mm-yyyy'),1250,2)");
		sqlList.add(
				"INSERT INTO employee VALUES (6,'Daniel Radclif','Clerk',4,to_date('1-5-1981','dd-mm-yyyy'),2850,4)");
		sqlList.add(
				"INSERT INTO employee VALUES (7,'Maria Sharapova','Clerk',1,to_date('9-6-1981','dd-mm-yyyy'),2450,4)");
		sqlList.add(
				"INSERT INTO employee VALUES (8,'Albert Einstein','Analyst',1,to_date('9-6-2006','dd-mm-yyyy'),3000,1)");
		sqlList.add(
				"INSERT INTO employee VALUES (9,'Manoj Dutta','Programmer',1,to_date('17-11-1981','dd-mm-yyyy'),500,2)");
		sqlList.add(
				"INSERT INTO employee VALUES (10,'Debasissa Gupta','Programmer',1,to_date('8-9-1981','dd-mm-yyyy'),1500,2)");
		sqlList.add(
				"INSERT INTO employee VALUES (11,'Victor Adams','Clerk',1,to_date('30-3-2016','dd-mm-yyyy'),1100,4)");
		sqlList.add(
				"INSERT INTO employee VALUES (12,'Barzana Panayotova','Programmer',4,to_date('3-12-2016','dd-mm-yyyy'),950,5)");
		sqlList.add(
				"INSERT INTO employee VALUES (13,'Debora Styles','Analyst',4,to_date('3-12-1981','dd-mm-yyyy'),3000,1)");
		sqlList.add("INSERT INTO employee VALUES (14,'Mark Twain','Clerk',1,to_date('23-1-1982','dd-mm-yyyy'),1300,4)");
		db.executeBatchSQL(sqlList);
	}
}
