package ru.spbau.mit.sd;

import java.io.IOException;

/**
 * Exception when we want to read file but our parameters is directory
 */
class FileIsDirectoryException extends IOException {
    FileIsDirectoryException(String message) {
        super(message);
    }
}

