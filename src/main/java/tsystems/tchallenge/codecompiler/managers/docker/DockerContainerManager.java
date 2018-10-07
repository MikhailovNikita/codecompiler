package tsystems.tchallenge.codecompiler.managers.docker;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import org.springframework.stereotype.Service;
import tsystems.tchallenge.codecompiler.api.dto.ContainerExecutionResult;
import tsystems.tchallenge.codecompiler.domain.models.CodeLanguage;
import tsystems.tchallenge.codecompiler.managers.resources.DockerfileType;
import tsystems.tchallenge.codecompiler.managers.resources.ResourceManager;

import java.nio.file.Path;

import static com.spotify.docker.client.DockerClient.LogsParam.stderr;
import static com.spotify.docker.client.DockerClient.LogsParam.stdout;
import static com.spotify.docker.client.messages.HostConfig.Bind.from;
import static tsystems.tchallenge.codecompiler.managers.docker.ContainerOption.VOLUME_READ_ONLY;
import static tsystems.tchallenge.codecompiler.managers.docker.ContainerOption.VOLUME_WRITABLE;

@Service
public class DockerContainerManager {

    private final DockerImageManager dockerImageManager;
    private final DockerClient docker;
    private final ResourceManager resourceManager;

    public DockerContainerManager(DockerImageManager dockerImageManager, DockerClient docker,
                                  ResourceManager resourceManager) {
        this.dockerImageManager = dockerImageManager;
        this.docker = docker;
        this.resourceManager = resourceManager;
    }

    public ContainerExecutionResult startContainer(Path workDir, DockerfileType dockerfileType,
                                                   Option... options)
            throws Exception {

        ContainerOptionsState optionsState = ContainerOptionsState.applyOptions(options);
        CodeLanguage language = languageByWorkDir(workDir);
        HostConfig hostConfig = getHostConfig(workDir, optionsState.volumeReadOnly);


        final ContainerConfig containerConfig = ContainerConfig.builder()
                .hostConfig(hostConfig)
                .image(dockerImageManager.getImageId(language, dockerfileType))
                .build();

        ContainerCreation creation = docker.createContainer(containerConfig);
        String id = creation.id();

        docker.startContainer(id);
        docker.waitContainer(id);

        return buildResult(id);
    }

    private ContainerExecutionResult buildResult(String id) throws Exception {

        String stdout = collectLogs(id, stdout());
        String stderr = collectLogs(id, stderr());
        Long exitCode = docker.inspectContainer(id).state().exitCode();

        return ContainerExecutionResult.builder()
                .stdout(stdout)
                .stderr(stderr)
                .exitCode(exitCode)
                .build();

    }

    private String collectLogs(String id, DockerClient.LogsParam... params) throws Exception {
        try (LogStream stream = docker.logs(id, params)) {
            return stream.readFully();
        }

    }

    private CodeLanguage languageByWorkDir(Path workDir) {
        Path codeDir = workDir.getParent();
        Path langDir = codeDir.getParent();
        String langDirName = langDir.getFileName().toString();
        return CodeLanguage.valueOf(langDirName.toUpperCase());
    }


    // Create bind between container file system and host file system (see docker volumes)
    private HostConfig getHostConfig(Path workDir, boolean readOnly) {
        return HostConfig.builder()
                .appendBinds(from(workDir.toAbsolutePath().toString())
                        .to(resourceManager.codeSuffix)
                        .readOnly(readOnly)
                        .build())
                .build();
    }


    public static class Option {

        ContainerOption option;
        Object value;

        private Option(ContainerOption option, Object value) {
            this.option = option;
            this.value = value;
        }

        public static Option volumeReadOnly() {
            return new Option(VOLUME_READ_ONLY, true);
        }

        public static Option volumeWritable() {
            return new Option(VOLUME_WRITABLE, true);
        }
    }


}
