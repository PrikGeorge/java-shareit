package ru.practicum.shareit.exception;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
