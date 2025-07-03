package ma.zrad.system.trip.batch.api.feign.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.exceptions.BusinessException;
import ma.zrad.system.batch.common.exceptions.TechnicalException;
import ma.zrad.system.batch.common.pojo.ErrorWrapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FeignClientErrorDecoder implements ErrorDecoder {

    @Autowired
    private ObjectMapper objectMapper;

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        String message = null;
        Reader reader = null;
        try {
            reader = response.body().asReader(StandardCharsets.UTF_8);
            String result = IOUtils.toString(reader);
            var exceptionError = objectMapper.readValue(result, ErrorWrapper.class);
            message = exceptionError.getMessage();
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        if (HttpStatus.valueOf(response.status()).is4xxClientError()) {
            log.error("Client-Side : Error while executing {} Error code {}", methodKey, response.status());
            return new BusinessException(message);
        } else if (HttpStatus.valueOf(response.status()).is5xxServerError()) {
            log.error("Server-Side : Error while executing {} Error code {}", methodKey, response.status());
            return new TechnicalException(message);
        }
        return defaultErrorDecoder.decode(methodKey, response);

    }

}
