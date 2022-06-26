package com.phuxuan.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.phuxuan.model.Country;
import com.phuxuan.model.User;

public class CountryDAO implements ICountryDAO{
	
	 private static final String INSERT_COUNTRY_SQL = "INSERT INTO country (name) VALUES (?);";
	private static final String SELECT_COUNTRY_BY_ID = "select * from country where id = ?;";
	private static final String SELECT_ALL_COUNTRY = "select * from country";
	private static final String DELETE_COUNTRY_SQL = "delete from country where id = ?;";
	private static final String UPDATE_COUNTRY_SQL = "update country set name = ? where id = ?;";
	private String jdbcURL = "jdbc:mysql://localhost:3306/px_usermanager?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "Quangdv180729!!";
	public CountryDAO() {
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }

	@Override
	public void insertCountry(Country country) throws SQLException {
		System.out.println(INSERT_COUNTRY_SQL);
		// try-with-resource statement will auto close the connection.
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COUNTRY_SQL)) {
			
//			Statement // Không có tham so
//			PreparedStatement // Co tham số, bắt đầu từ 1, 2 , 3...
//			CallableStatement // Làm việc với store procedure
//			
			
			preparedStatement.setString(1, country.getName());
			
			
			System.out.println(this.getClass() + " insertCountry " + preparedStatement);
			preparedStatement.executeUpdate();
			
			

		} catch (SQLException e) {
			printSQLException(e);
		}
		
	}

	@Override
	public Country selectCountry(int id) {
		
		Country country = null;
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNTRY_BY_ID);) {
            preparedStatement.setInt(1, id);
            
            System.out.println(this.getClass() + " selectCountry: " + preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                String name = rs.getString("name");
                
               
                country = new Country(id, name);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return country;
	}

	@Override
	public List<Country> selectAllCountry() {
		
		 List<Country> listCountry = new ArrayList<>();
	        // Step 1: Establishing a Connection
	        try (Connection connection = getConnection();

	             // Step 2:Create a statement using connection object
	             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_COUNTRY);) {
	        	
	        	
	            System.out.println(this.getClass() + " selectAllCountry: " + preparedStatement);
	            // Step 3: Execute the query or update query
	            ResultSet rs = preparedStatement.executeQuery();

	            // Step 4: Process the ResultSet object.
	            while (rs.next()) {
	                int id = rs.getInt("id");
	                String name = rs.getString("name");
	                
	                
	                listCountry.add(new Country(id, name));
	            }
	        } catch (SQLException e) {
	            printSQLException(e);
	        }
	        return listCountry;
	}

	@Override
	public boolean deleteCountry(int id) throws SQLException {
		boolean rowDeleted;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_COUNTRY_SQL);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
	}

	@Override
	public boolean updateCountry(Country country) throws SQLException {
		 boolean rowUpdated;
			try (Connection connection = getConnection();
					PreparedStatement statement = connection.prepareStatement(UPDATE_COUNTRY_SQL);) {
	            statement.setString(1, country.getName());
	            statement.setInt(2, country.getId());
	            

	            System.out.println(this.getClass() + " updateCountry: " + statement);
	            rowUpdated = statement.executeUpdate() > 0;
	        }
	        return rowUpdated;
	}
	
	private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    
    
}
