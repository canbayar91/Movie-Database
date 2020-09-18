package com.demo.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.demo.DatabaseManager;
import com.demo.model.MovieData;

@Component
public class MovieSearchPanel extends JFrame {

	private static final long serialVersionUID = -1899109344737885692L;
	
	private JTextField searchBar = new JTextField("Search", 50);
	private MovieTableModel tableModel = new MovieTableModel();
	private JTable movieTable = new JTable(tableModel);
	
	@Autowired
	private MovieDetailsPanel detailsPanel;
	
	@Autowired
	@Qualifier("postgreManager")
	private DatabaseManager dbManager;
	
	public MovieSearchPanel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Movie Search Engine Demo");
		setLayout(new GridBagLayout());
		initialize();
	}
	
	private void initialize() {
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 0, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(searchBar, gbc);
		
		JScrollPane scrollPane = new JScrollPane(movieTable);
		
		gbc.gridy = 1;
		add(scrollPane, gbc);
		
		JPanel buttonsPanel = createButtonsPanel();
		
		gbc.gridy = 2;
		add(buttonsPanel, gbc);
		
		searchBar.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if (searchBar.getText().equals("")) {
					searchBar.setText("Search");
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if (searchBar.getText().equals("Search")) {
					searchBar.setText("");
				}
			}
		});
		
		searchBar.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateVisibleMovies();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateVisibleMovies();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateVisibleMovies();
			}
		});
		
		scrollPane.requestFocus();
	}
	
	private JPanel createButtonsPanel() {

		JPanel buttonsPanel = new JPanel();
		
		Icon addIcon = new ImageIcon("src/main/resources/icons/icons8-add-property-16.png");
		Icon editIcon = new ImageIcon("src/main/resources/icons/icons8-edit-property-16.png");
		Icon deleteIcon = new ImageIcon("src/main/resources/icons/icons8-delete-property-16.png");
		
		JButton addButton = new JButton(addIcon);
		JButton editButton = new JButton(editIcon);
		JButton deleteButton = new JButton(deleteIcon);
		
		addButton.addActionListener(e -> openDetailsPanel(false));
		editButton.addActionListener(e -> openDetailsPanel(true));
		deleteButton.addActionListener(e -> deleteSelectedMovie());
		
		buttonsPanel.add(addButton);
		buttonsPanel.add(editButton);
		buttonsPanel.add(deleteButton);
		
		return buttonsPanel;
	}

	private void openDetailsPanel(boolean editMode) {
		
		detailsPanel.clear();
		if (editMode) {
			
			int row = movieTable.getSelectedRow();
			if (row >= 0) {
				MovieData movie = tableModel.getSelectedMovie(row);
				detailsPanel.fill(movie);
			}
		}
		
		detailsPanel.setEditMode(editMode);
		detailsPanel.setVisible(true);
	}
	
	private void deleteSelectedMovie() {
		int row = movieTable.getSelectedRow();
		if (row >= 0) {
			MovieData movie = tableModel.getSelectedMovie(row);
			dbManager.deleteMovie(movie);
			updateVisibleMovies();
		}
	}
	
	public void updateVisibleMovies() {
		String text = searchBar.getText();
		if (!text.equals("Search") && !text.equals("")) {
			List<MovieData> movieList = dbManager.queryByName(text);
			tableModel.setVisibleMovies(movieList);
		} else {
			List<MovieData> movieList = dbManager.queryAll();
			tableModel.setVisibleMovies(movieList);
		}
		
		movieTable.repaint();
	}
}
