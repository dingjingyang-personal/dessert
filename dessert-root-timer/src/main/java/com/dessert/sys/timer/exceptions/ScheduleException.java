package com.dessert.sys.timer.exceptions;

@SuppressWarnings("serial")
public class ScheduleException extends DexcoderException {

	/**
	 * Constructor
	 */
	public ScheduleException() {
		super();
	}

	/**
	 * Instantiates a new ScheduleException.
	 *
	 * @param e the e
	 */
	public ScheduleException(Throwable e) {
		super(e);
	}

	/**
	 * Constructor
	 *
	 * @param message the message
	 */
	public ScheduleException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 *
	 * @param code the code
	 * @param message the message
	 */
	public ScheduleException(String code, String message) {
		super(code, message);
	}
}
