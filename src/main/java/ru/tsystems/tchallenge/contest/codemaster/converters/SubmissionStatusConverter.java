package ru.tsystems.tchallenge.contest.codemaster.converters;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import ru.tsystems.tchallenge.contest.codemaster.domain.models.CodeRunStatus;
import ru.tsystems.tchallenge.contest.codemaster.domain.models.SubmissionStatus;

@Service
@Mapper(componentModel = "spring")
public interface SubmissionStatusConverter {

    default SubmissionStatus fromCodeRunStatus(CodeRunStatus status) {
        switch (status) {
            case OK:
                return SubmissionStatus.OK;
            case TIME_LIMIT:
                return SubmissionStatus.TIME_LIMIT;
            case MEMORY_LIMIT:
                return SubmissionStatus.MEMORY_LIMIT;
            case SERVER_ERROR:
                return SubmissionStatus.SERVER_ERROR;
            case RUNTIME_ERROR:
                return SubmissionStatus.RUNTIME_ERROR;
        }
        return null;
    }
}