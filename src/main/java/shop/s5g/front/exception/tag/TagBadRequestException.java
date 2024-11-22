package shop.s5g.front.exception.tag;

import shop.s5g.front.exception.BadRequestException;

public class TagBadRequestException extends BadRequestException {
    public TagBadRequestException(String message) {
        super(message);
    }
}
