package shop.s5g.front.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.Test;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.exception.AlreadyExistsException;
import shop.s5g.front.exception.AuthenticationException;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.ResourceNotFoundException;

import java.io.ByteArrayInputStream;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class FeignErrorDecoderTest {

    private final FeignErrorDecoder decoder = new FeignErrorDecoder();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * 헬퍼 메서드: MessageDto를 JSON으로 직렬화한 후 Response를 생성
     */
    private Response createResponse(int status, MessageDto messageDto, String url) throws Exception {
        byte[] body = mapper.writeValueAsBytes(messageDto);
        Request request = Request.create(
            Request.HttpMethod.GET,
            url,
            Collections.emptyMap(),
            null,
            new RequestTemplate());

        return Response.builder()
            .status(status)
            .request(request)
            .headers(Collections.emptyMap())
            .body(new ByteArrayInputStream(body), body.length)
            .build();
    }

    @Test
    void testDecode_BadRequest() throws Exception {
        Response response = createResponse(400, new MessageDto("Bad Request Error"), "http://test.com");
        Exception ex = decoder.decode("testMethod", response);
        assertTrue(ex instanceof BadRequestException);
        assertEquals("Bad Request Error", ex.getMessage());
    }

    @Test
    void testDecode_Forbidden() throws Exception {
        // 403 -> AuthenticationException("해당 리소스에 접근할 수 없습니다.")
        Response response = createResponse(403, new MessageDto("Forbidden"), "http://test.com");
        Exception ex = decoder.decode("testMethod", response);
        assertInstanceOf(AuthenticationException.class, ex);
        assertEquals("해당 리소스에 접근할 수 없습니다.", ex.getMessage());
    }

    @Test
    void testDecode_NotFound() throws Exception {
        // 404 -> ResourceNotFoundException
        Response response = createResponse(404, new MessageDto("Not Found Resource"), "http://test.com");
        Exception ex = decoder.decode("testMethod", response);
        assertInstanceOf(ResourceNotFoundException.class, ex);
        assertEquals("Not Found Resource", ex.getMessage());
    }

    @Test
    void testDecode_AlreadyExists() throws Exception {
        // 409 -> AlreadyExistsException
        Response response = createResponse(409, new MessageDto("Already Exists"), "http://test.com");
        Exception ex = decoder.decode("testMethod", response);
        assertTrue(ex instanceof AlreadyExistsException);
        assertEquals("Already Exists", ex.getMessage());
    }

    @Test
    void testDecode_Unauthorized() throws Exception {
        // 401 -> AuthenticationException(messageDto.message())
        Response response = createResponse(401, new MessageDto("Unauthorized"), "http://test.com");
        Exception ex = decoder.decode("testMethod", response);
        assertInstanceOf(AuthenticationException.class, ex);
        assertEquals("Unauthorized", ex.getMessage());
    }

    @Test
    void testDecode_UnknownError() throws Exception {
        // default branch -> IllegalArgumentException
        Response response = createResponse(418, new MessageDto("I'm a teapot"), "http://test.com");
        Exception ex = decoder.decode("testMethod", response);
        assertInstanceOf(IllegalArgumentException.class, ex);
        assertEquals("418", ex.getMessage());
    }
}
