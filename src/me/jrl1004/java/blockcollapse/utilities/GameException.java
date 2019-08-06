package me.jrl1004.java.blockcollapse.utilities;

public class GameException extends Exception {
    private static final long serialVersionUID = 7093333955229151536L;
    final String error;

    public GameException() {
	this.error = "";
    }

    public GameException(String error) {
	this.error = error;
    }

    @Override
    public String getMessage() {
	return error;
    }
}
