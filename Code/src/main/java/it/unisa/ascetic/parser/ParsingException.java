package it.unisa.ascetic.parser;

public class ParsingException extends Exception {

    public ParsingException() {
        super("Error occurred Parsing Exception");
    }

    public ParsingException(String message) {
        super(message);
    }
}
