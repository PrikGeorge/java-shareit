package ru.practicum.shareit.exception;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
