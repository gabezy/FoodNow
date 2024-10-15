package com.gabezy.foodnow.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DataSource dataSource;

	private Connection connection;

	public void clearTables() {
		try (Connection connection = dataSource.getConnection()) {
			this.connection = connection;

			checkTestDatabase();
			tryToClearTables();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			this.connection = null;
		}
	}

	private void checkTestDatabase() throws SQLException {
		String catalog = connection.getCatalog();

		if (catalog == null || !catalog.endsWith("test")) {
			throw new RuntimeException(
					"Cannot clear database tables because '" + catalog + "' is not a test database (suffix 'test' not found).");
		}
	}

	private void tryToClearTables() throws SQLException {
		List<String> tableNames = getTableNames();
		clear(tableNames);
		resetTablesPrimarykeySequences(tableNames);
	}

	private List<String> getTableNames() throws SQLException {
		List<String> tableNames = new ArrayList<>();

		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet rs = metaData.getTables(connection.getCatalog(), null, null, new String[] { "TABLE" });

		while (rs.next()) {
			tableNames.add(rs.getString("TABLE_NAME"));
		}

		// Remove Flyway history table from truncation
		tableNames.remove("flyway_schema_history");

		return tableNames;
	}

	private void clear(List<String> tableNames) throws SQLException {
		Statement statement = buildSqlStatement(tableNames);

		logger.debug("Executing SQL");
		statement.executeBatch();
	}

	private Statement buildSqlStatement(List<String> tableNames) throws SQLException {
		Statement statement = connection.createStatement();

		// Disable triggers (foreign key checks) in PostgreSQL
		statement.addBatch(sql("SET session_replication_role = 'replica'"));
		addTruncateStatements(tableNames, statement);

		// Re-enable triggers (foreign key checks) in PostgreSQL
		statement.addBatch(sql("SET session_replication_role = 'origin'"));



		return statement;
	}

	private void addTruncateStatements(List<String> tableNames, Statement statement) {
		tableNames.forEach(tableName -> {
			try {
				// Use TRUNCATE CASCADE to remove records and handle dependencies
				statement.addBatch(sql("TRUNCATE TABLE " + tableName + " CASCADE"));
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
	}

	private void resetTablesPrimarykeySequences(List<String> tableNames) throws SQLException {
		Statement statement = connection.createStatement();

		tableNames.forEach(tableName -> {
            try {
				if (tableHasPrimaryKey(tableName)) {
					String sequenceName = tableName + "_id_seq";
					statement.addBatch(sql("ALTER SEQUENCE " + sequenceName + " RESTART WITH 1"));
				}
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

		statement.executeBatch();
	}

	private boolean tableHasPrimaryKey(String tableName) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();

		try (ResultSet rs = metaData.getPrimaryKeys(null, null, tableName)) {
			return rs.next();
		}
	}

	private String sql(String sql) {
		logger.debug("Adding SQL: {}", sql);
		return sql;
	}
}

// Based: https://brightinventions.pl/blog/clear-database-in-spring-boot-tests/
