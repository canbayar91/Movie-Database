package com.demo.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.demo.model.MovieData;

public class MovieTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 5300972367435379275L;
	
	private String[] columnNames = {"Movie Name", "Director", "Length", "Release Year", "IMDB Score"};
	private List<MovieData> visibleMovieList = new ArrayList<MovieData>();
	
	public String getColumnName(int col) {
        return columnNames[col];
    }

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return visibleMovieList.size();
	}

	public Object getValueAt(int row, int col) {
		
		if (row > visibleMovieList.size()) {
			return null;
		}
		
		MovieData movie = visibleMovieList.get(row);
		switch (col) {
		case 0:
			return movie.getName();
		case 1:
			return movie.getDirector();
		case 2:
			return movie.getLength();
		case 3:
			return movie.getYear();
		case 4:
			return movie.getScore();
		default:
			return null;
		}
	}
	
	public MovieData getSelectedMovie(int index) {
		return visibleMovieList.get(index);
	}

	public void setVisibleMovies(List<MovieData> movieList) {
		visibleMovieList.clear();
		visibleMovieList.addAll(movieList);
	}
}
