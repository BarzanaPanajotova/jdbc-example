package com.bdpanajoto.examples.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import com.bdpanajoto.examples.dto.EmployeeDTO;

import oracle.jdbc.pool.OracleDataSource;

public class OracleDatabaseInterfaceImpl implements DatabaseInterface {

	private DataSource ds;

	OracleDatabaseInterfaceImpl(String driver, String serverName, String databaseName, int port, String user,
			char[] password) throws SQLException {

		OracleDataSource ds = new OracleDataSource();
		ds.setDriverType(driver);
		ds.setServerName(serverName);
		ds.setDatabaseName(databaseName);
		ds.setPortNumber(port);
		ds.setUser(user);
		ds.setPassword(String.copyValueOf(password));
		this.ds = ds;

		try (Connection conn = ds.getConnection()) {
			System.out.println(
					"Connection to : " + serverName + ":" + port + "/" + databaseName + " tested successfully");
		}
	}

	@Override
	public void executeSQL(String sql) {
		System.out.println("Executing: " + sql);
		try (Connection conn = ds.getConnection(); PreparedStatement psmt = conn.prepareStatement(sql)) {
			psmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	@Override
	public void executeBatchSQL(List<String> sqlList) {
		try (Connection conn = ds.getConnection(); Statement smt = conn.createStatement()) {
			conn.setAutoCommit(false);
			for (String sql : sqlList) {
				System.out.println("Adding to batch executor: " + sql);
				smt.addBatch(sql);
			}
			smt.executeBatch();
			conn.commit();
			System.out.println("Batch executed!");
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	@Override
	public List<EmployeeDTO> getEmployees() {
		String sql = "SELECT id,name,job,manager_id,TO_CHAR(hiredate, 'YYYY-MM-DD') AS hiredate,salary,department_id FROM employee";
		System.out.println("Executing: " + sql);
		try (Connection conn = ds.getConnection(); PreparedStatement psmt = conn.prepareStatement(sql)) {
			ResultSet rs = psmt.executeQuery();
			List<EmployeeDTO> employeeList = new ArrayList<>();
			while (rs.next()) {
				EmployeeDTO employee = new EmployeeDTO();
				employee.setId(rs.getInt(1));
				employee.setName(rs.getString(2));
				employee.setJob(rs.getString(3));
				employee.setManagerId(rs.getInt(4));
				employee.setHiredate(LocalDate.parse(rs.getString(5)));
				employee.setSalary(Double.parseDouble(rs.getString(6)));
				employee.setDepartmentId(rs.getInt(7));
				employeeList.add(employee);
			}
			return employeeList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
