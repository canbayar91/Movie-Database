package com.demo.postgre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.demo.DatabaseManager;
import com.demo.model.MovieData;

@Component
@Qualifier("postgreManager")
public class PostgreManager implements DatabaseManager {
	
	private static final Logger LOGGER = Logger.getLogger(PostgreManager.class.getName());
	
	private static final String URL = "jdbc:postgresql://localhost/movies";
	private static final String USER = "postgres";
	private static final String PASSWORD = "qwErty8lp";
	
	private Optional<Connection> connection = Optional.empty();

	@PostConstruct
	@Override
	public void connect() {
		
		if (!connection.isPresent()) {
			
			try {
				
				connection = Optional.ofNullable(DriverManager.getConnection(URL, USER, PASSWORD));
				LOGGER.log(Level.FINE, "PostgreSQL: Successfully connected");
				
				String query = "CREATE TABLE IF NOT EXISTS movies ("
						+ "id SERIAL PRIMARY KEY,"
						+ "name VARCHAR(30) NOT NULL,"
						+ "director VARCHAR(30) NOT NULL,"
						+ "length INT,"
						+ "year INT,"
						+ "score REAL)";
				
				PreparedStatement statement = connection.get().prepareStatement(query);
				statement.executeUpdate();
				statement.close();
				
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, "PostgreSQL: Failed to connect", e);
			}
		}
	}
	
	@PreDestroy
	@Override
	public void disconnect() {
		
		if (connection.isPresent()) {
			
			try {
				connection.get().close();
				LOGGER.log(Level.FINE, "PostgreSQL: Connection closed");
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, "PostgreSQL: Failed to disconnect", e);
			}
		}
	}

	@Override
	public void addMovie(MovieData movie) {
		
		if (connection.isPresent()) {
			
			try {
				
				String query = "INSERT INTO movies(name, director, length, year, score) VALUES(?,?,?,?,?)";
				PreparedStatement statement = connection.get().prepareStatement(query);
				
				statement.setString(1, movie.getName());
				statement.setString(2, movie.getDirector());
				statement.setInt(3, movie.getLength());
				statement.setInt(4, movie.getYear());
				statement.setDouble(5, movie.getScore());
				
				statement.executeUpdate();
				statement.close();
				
				LOGGER.log(Level.FINE, "PostgreSQL: " + movie.getName() + " successfully inserted");
				
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, "PostgreSQL: Failed to insert " + movie.getName(), e);
			}
		}
	}

	@Override
	public void updateMovie(MovieData movie) {
		
		if (connection.isPresent()) {
			
			try {
				
				String query = "UPDATE movies SET director = ?, length = ?, year = ?, score = ? WHERE name = ?";
				PreparedStatement statement = connection.get().prepareStatement(query);
				
				statement.setString(1, movie.getDirector());
				statement.setInt(2, movie.getLength());
				statement.setInt(3, movie.getYear());
				statement.setDouble(4, movie.getScore());
				statement.setString(5, movie.getName());
				
				statement.executeUpdate();
				statement.close();
				
				LOGGER.log(Level.FINE, "PostgreSQL: " + movie.getName() + " successfully updated");
				
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, "PostgreSQL: Failed to update " + movie.getName(), e);
			}
		}
	}

	@Override
	public void deleteMovie(MovieData movie) {
		
		if (connection.isPresent()) {
			
			String query = "DELETE FROM movies WHERE name = ?";
			try {
				
				PreparedStatement statement = connection.get().prepareStatement(query);
				statement.setString(1, movie.getName());
				
				statement.executeUpdate();
				statement.close();
				
				LOGGER.log(Level.FINE, "PostgreSQL: " + movie.getName() + " successfully deleted");
				
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, "PostgreSQL: Failed to delete " + movie.getName(), e);
			}
		}
	}

	@Override
	public List<MovieData> queryByName(String text) {
		
		List<MovieData> movieList = new ArrayList<MovieData>();
		if (connection.isPresent()) {
			
			String query = "SELECT * FROM movies WHERE LOWER(name) LIKE LOWER(?)";
			try {
				
				PreparedStatement statement = connection.get().prepareStatement(query);
				statement.setString(1, "%" + text + "%");
				statement.setFetchSize(50);
				
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					
					String name = resultSet.getString(2);
					String director = resultSet.getString(3);
					int length = resultSet.getInt(4);
					int year = resultSet.getInt(5);
					double score = resultSet.getDouble(6);
					
					MovieData movie = new MovieData(name, director, length, year, score);
					movieList.add(movie);
				}
				
				resultSet.close();
				statement.close();
				
				LOGGER.log(Level.FINE, "PostgreSQL: Query successful");
				
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, "PostgreSQL: Query failed", e);
			}
		}
		
		return movieList;
	}
	
	@Override
	public List<MovieData> queryAll() {
		
		List<MovieData> movieList = new ArrayList<MovieData>();
		if (connection.isPresent()) {
			
			String query = "SELECT * FROM movies";
			try {
				
				PreparedStatement statement = connection.get().prepareStatement(query);
				statement.setFetchSize(50);
				
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					
					String name = resultSet.getString(2);
					String director = resultSet.getString(3);
					int length = resultSet.getInt(4);
					int year = resultSet.getInt(5);
					double score = resultSet.getDouble(6);
					
					MovieData movie = new MovieData(name, director, length, year, score);
					movieList.add(movie);
				}
				
				resultSet.close();
				statement.close();
				
				LOGGER.log(Level.FINE, "PostgreSQL: Query successful");
				
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, "PostgreSQL: Query failed", e);
			}
		}
		
		return movieList;
	}
}
