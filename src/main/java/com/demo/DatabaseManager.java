package com.demo;

import java.util.List;

import com.demo.model.MovieData;

public interface DatabaseManager {

	void connect();
	void disconnect();
	
	void addMovie(MovieData movie);
	void updateMovie(MovieData movie);
	void deleteMovie(MovieData movie);
	
	List<MovieData> queryByName(String text);
	List<MovieData> queryAll();
}
