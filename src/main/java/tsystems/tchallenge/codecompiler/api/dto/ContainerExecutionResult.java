package tsystems.tchallenge.codecompiler.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.Duration;

@Data
@Builder
@ToString(exclude = {"stdout", "stderr"})
public class ContainerExecutionResult {
    private String stdout;
    private String stderr;
    private Duration executionTime;
    private Long exitCode;
    private Boolean oomKilled;
    private Long memoryUsage;
}
