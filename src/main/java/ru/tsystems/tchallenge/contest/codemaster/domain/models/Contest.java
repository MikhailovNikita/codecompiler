package ru.tsystems.tchallenge.contest.codemaster.domain.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.tsystems.tchallenge.contest.codemaster.api.dto.IdAware;

import java.util.List;

@Document(collection = "contests")
@Data
@EqualsAndHashCode(callSuper = true)
public class Contest extends AbstractDocument implements IdAware {
    @ApiModelProperty("Name of codemaster")
    private String name;
    @ApiModelProperty("Set of tests")
    private List<Test> tests;
    @ApiModelProperty("Time limit per test in millis")
    private Long timeLimit;
    @ApiModelProperty("Memory limit (RAM) per test in bytes")
    private Long memoryLimit;
}