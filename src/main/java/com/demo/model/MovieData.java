package com.demo.model;

import java.io.Serializable;

public class MovieData implements Serializable {

	private static final long serialVersionUID = 1689155567512543394L;
	
	private String name;
	private String director;
	private int length;
	private int year;
	private double score;
	
	public MovieData(String name, String director, int length, int year, double score) {
		this.name = name;
		this.director = director;
		this.length = length;
		this.year = year;
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "\'" + name + "\', \'" + director + "\', " + length + ", " + year + ", " + score;
	}
}
