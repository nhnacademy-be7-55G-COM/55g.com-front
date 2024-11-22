package shop.s5g.front.exception.tag;

import shop.s5g.front.exception.ResourceNotFoundException;

public class TagResourceNotFoundException extends ResourceNotFoundException {
    public TagResourceNotFoundException(String message) {
        super(message);
    }
}
