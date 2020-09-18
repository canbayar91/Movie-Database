package com.demo.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.demo.DatabaseManager;
import com.demo.model.MovieData;

@Component
public class MovieDetailsPanel extends JDialog {

	private static final long serialVersionUID = -3556503447643319033L;
	
	private JTextField movieField = new JTextField(20);
	private JTextField directorField = new JTextField(20);
	private JTextField lengthField = new JTextField(20);
	private JTextField yearField = new JTextField(20);
	private JTextField scoreField = new JTextField(20);
	
	private boolean editMode = false;
	
	@Autowired
	private MovieSearchPanel searchPanel;
	
	@Autowired
	@Qualifier("postgreManager")
	private DatabaseManager dbManager;
	
	public MovieDetailsPanel() {
		setTitle("Movie Data");
		setLayout(new GridBagLayout());
		initialize();
		pack();
	}
	
	private void initialize() {
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 0, 10);
		
		JLabel movieLabel = new JLabel("Movie");
		JLabel directorLabel = new JLabel("Director");
		JLabel lengthLabel = new JLabel("Length");
		JLabel yearLabel = new JLabel("Year");
		JLabel scoreLabel = new JLabel("Score");
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		add(movieLabel, gbc);
		
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		add(movieField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		add(directorLabel, gbc);
		
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		add(directorField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		add(lengthLabel, gbc);
		
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		add(lengthField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.EAST;
		add(yearLabel, gbc);
		
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		add(yearField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 10, 10, 10);
		add(scoreLabel, gbc);
		
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		add(scoreField, gbc);
		
		JPanel buttonsPanel = createButtonsPanel();
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		add(buttonsPanel, gbc);
	}
	
	private JPanel createButtonsPanel() {

		JPanel buttonsPanel = new JPanel();
		
		JButton saveButton = new JButton("Save");
		JButton cancelButton = new JButton("Cancel");
		
		saveButton.addActionListener(e -> saveMovie());
		cancelButton.addActionListener(e -> setVisible(false));
		
		buttonsPanel.add(saveButton);
		buttonsPanel.add(cancelButton);
		
		return buttonsPanel;
	}
	
	private void saveMovie() {

		String name = movieField.getText();
		String director = directorField.getText();
		int length = Integer.parseInt(lengthField.getText());
		int year = Integer.parseInt(yearField.getText());
		double score = Double.parseDouble(scoreField.getText());
		
		if (editMode) {
			MovieData movie = new MovieData(name, director, length, year, score);
			dbManager.updateMovie(movie);
		} else {
			MovieData movie = new MovieData(name, director, length, year, score);
			dbManager.addMovie(movie);
		}
		
		setVisible(false);
		searchPanel.updateVisibleMovies();
	}

	public void fill(MovieData movie) {
		movieField.setText(movie.getName());
		directorField.setText(movie.getDirector());
		lengthField.setText(Integer.toString(movie.getLength()));
		yearField.setText(Integer.toString(movie.getYear()));
		scoreField.setText(Double.toString(movie.getScore()));
	}
	
	public void clear() {
		movieField.setText("");
		directorField.setText("");
		lengthField.setText("");
		yearField.setText("");
		scoreField.setText("");
	}
	
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
		movieField.setEnabled(!editMode);
	}
}
