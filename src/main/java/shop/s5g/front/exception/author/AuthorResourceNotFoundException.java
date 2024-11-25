package shop.s5g.front.exception.author;

import shop.s5g.front.exception.ResourceNotFoundException;

public class AuthorResourceNotFoundException extends ResourceNotFoundException {
    public AuthorResourceNotFoundException(String message) {
        super(message);
    }
}
