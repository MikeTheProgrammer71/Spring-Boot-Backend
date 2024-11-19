package com.superherobackend.superhero.exceptions;

public class DuplicateSuperheroException extends Exception{

	public String message;

	public DuplicateSuperheroException(String message) {
		super(message);
	}

	public DuplicateSuperheroException() {
		super("Superhero already in database!");
	}
}
