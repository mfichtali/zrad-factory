package ma.zrad.system.trip.batch.api.feign.common;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

@Slf4j
public class FeignClientRetryer implements Retryer {

    private final int retryMaxAttempts;
    private final Long retryInterval;
    /**
     * In MS (2000ms = 2s)
     **/
    private int attempt;

    public FeignClientRetryer(int retryMaxAttempt, Long retryInterval) {
        this.retryMaxAttempts = retryMaxAttempt;
        this.retryInterval = retryInterval;
        this.attempt = 1;
    }

    @Override
    public void continueOrPropagate(RetryableException e) {

        log.warn("🔄 Retrying HTTP request...");
        if (attempt++ >= retryMaxAttempts) {
            throw e;
        }
        if (e.getCause() instanceof ConnectException
                || e.getCause() instanceof SocketTimeoutException
                || e.getCause() instanceof UnknownHostException) {
            try {
                Thread.sleep(retryInterval);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        } else {
            throw e;
        }
        log.info("✅ Retrying: {} attempt {}", e.request().url(), attempt);

    }

    @Override
    public Retryer clone() {
        return new FeignClientRetryer(retryMaxAttempts, retryInterval);
    }

}
