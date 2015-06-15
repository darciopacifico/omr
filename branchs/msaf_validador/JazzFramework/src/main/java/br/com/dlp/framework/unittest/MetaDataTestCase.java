package br.com.dlp.framework.unittest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MetaDataTestCase {
	public static void main(String args[]) throws ClassNotFoundException,
			SQLException {

		Class.forName("org.gjt.mm.mysql.Driver");
		Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost/grapi", "root", "");

		PreparedStatement preparedStatement = connection
				.prepareStatement("Select * from empresavo");

		// ResultSet resultSet = preparedStatement.executeQuery();

		ResultSetMetaData resultSetMetaData = preparedStatement.getMetaData();

		System.out.println("retornou algo?:"
				+ resultSetMetaData.getColumnName(1));

	}
}
