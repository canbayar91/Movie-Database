package com.demo.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.demo.DatabaseManager;
import com.demo.model.MovieData;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

@Lazy
@Component
@Qualifier("mongoManager")
public class MongoManager implements DatabaseManager {

	private static final Logger LOGGER = Logger.getLogger(MongoManager.class.getName());

	private MongoClient client;
	private MongoCollection<Document> collection;

	@PostConstruct
	@Override
	public void connect() {
		client = new MongoClient("localhost", 27017);
		MongoDatabase database = client.getDatabase("movies");
		collection = database.getCollection("movies");
		LOGGER.log(Level.FINE, "MongoDB: Successfully connected");
	}

	@PreDestroy
	@Override
	public void disconnect() {
		client.close();
		LOGGER.log(Level.FINE, "MongoDB: Successfully disconnected");
	}

	@Override
	public void addMovie(MovieData movie) {

		Document document = new Document();
		document.put("name", movie.getName());
		document.put("director", movie.getDirector());
		document.put("length", movie.getLength());
		document.put("year", movie.getYear());
		document.put("score", movie.getScore());
		
		collection.insertOne(document);
		LOGGER.log(Level.FINE, "MongoDB: " + movie.getName() + " successfully inserted");
	}

	@Override
	public void updateMovie(MovieData movie) {
		
		Document searchDocument = new Document();
		searchDocument.put("name", movie.getName());
		
		Document document = new Document();
		document.put("director", movie.getDirector());
		document.put("length", movie.getLength());
		document.put("year", movie.getYear());
		document.put("score", movie.getScore());
		
		Document updateDocument = new Document();
		updateDocument.append("$set", document);
		
		collection.updateOne(searchDocument, updateDocument);
		LOGGER.log(Level.FINE, "MongoDB: " + movie.getName() + " successfully updated");
	}

	@Override
	public void deleteMovie(MovieData movie) {

		Document document = new Document();
		document.put("name", movie.getName());
		
		collection.deleteOne(document);
		LOGGER.log(Level.FINE, "MongoDB: " + movie.getName() + " successfully deleted");
	}

	@Override
	public List<MovieData> queryByName(String text) {
		
		Document regexDocument = new Document();
		regexDocument.put("name", Pattern.compile(".*" + text + ".*", Pattern.CASE_INSENSITIVE));

		List<MovieData> movieList = new ArrayList<MovieData>();
		MongoCursor<Document> cursor = collection.find(regexDocument).iterator();
		while (cursor.hasNext()) {
			
			Document document = cursor.next();
			String name = document.getString("name");
			String director = document.getString("director");
			int length = document.getInteger("length");
			int year = document.getInteger("year");
			double score = document.getDouble("score");
			
			MovieData movie = new MovieData(name, director, length, year, score);
			movieList.add(movie);
		}

		return movieList;
	}

	@Override
	public List<MovieData> queryAll() {
		
		List<MovieData> movieList = new ArrayList<MovieData>();
		MongoCursor<Document> cursor = collection.find().iterator();
		while (cursor.hasNext()) {
			
			Document document = cursor.next();
			String name = document.getString("name");
			String director = document.getString("director");
			int length = document.getInteger("length");
			int year = document.getInteger("year");
			double score = document.getDouble("score");
			
			MovieData movie = new MovieData(name, director, length, year, score);
			movieList.add(movie);
		}

		return movieList;
	}
}
