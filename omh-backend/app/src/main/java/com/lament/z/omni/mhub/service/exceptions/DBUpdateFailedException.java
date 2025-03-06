package com.lament.z.omni.mhub.service.exceptions;


import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;

/**
 * like {@link JdbcUpdateAffectedIncorrectNumberOfRowsException}
 * <p>
 *  Exception thrown when affected row isn't 1.
 * */
public class DBUpdateFailedException extends RuntimeException{
	public DBUpdateFailedException(Integer row) {
		super("Affected row is expected 1, but found " + row + " ." );
	}
}
