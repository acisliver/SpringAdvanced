package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloTraceV1 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X--";

    public TraceStatus begin(String message) {
        TraceId id = new TraceId();
        long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", id.getId(), addSpace(START_PREFIX, id.getLevel()), message);
        return new TraceStatus(id, startTimeMs, message);
    }
    public void end(TraceStatus status) {
        complete(status, null);
    }
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(),
                    resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs,
                    e.toString());
        } }


    private String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append(fun(prefix, i, level));
        }
        return sb.toString();
    }

    private String fun(String prefix, int i, int level) {
        return (i == level - 1) ? String.format("|%s", prefix) : "|  ";
    }
}
