package com.demo.postgre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
				
				Statement statement = connection.get().createStatement();
				statement.executeUpdate(query);
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
			
			String query = "INSERT INTO movies(name, director, length, year, score) VALUES(" + movie.toString() + ")";
			try {
				Statement statement = connection.get().createStatement();
				statement.executeUpdate(query);
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
			
			String query = "UPDATE movies"
					+ " SET director = \'" + movie.getDirector() + "\'"
					+ ", length = " + movie.getLength()
					+ ", year = " + movie.getYear()
					+ ", score = " + movie.getScore()
					+ " WHERE name = \'" + movie.getName() + "\'";
			
			try {
				Statement statement = connection.get().createStatement();
				statement.executeUpdate(query);
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
			
			String query = "DELETE FROM movies WHERE name = \'" + movie.getName() + "\'";
			try {
				Statement statement = connection.get().createStatement();
				statement.executeUpdate(query);
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
			
			String query = "SELECT * FROM movies WHERE name LIKE \'%" + text + "%\'";
			try {
				
				Statement statement = connection.get().createStatement();
				statement.setFetchSize(50);
				
				ResultSet resultSet = statement.executeQuery(query);
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
				
				Statement statement = connection.get().createStatement();
				statement.setFetchSize(50);
				
				ResultSet resultSet = statement.executeQuery(query);
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
