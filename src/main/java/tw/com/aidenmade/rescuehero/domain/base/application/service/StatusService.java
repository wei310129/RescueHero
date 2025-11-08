package tw.com.aidenmade.rescuehero.domain.base.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.base.enums.StatusType;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.StatusProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.repository.StatusRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusProjectionMapper statusProjectionMapper;
    private final StatusRepository statusRepository;

    @Transactional(readOnly = true)
    public List<StatusDto> getTaskStatuses() {
        return statusRepository.findByType_Name(StatusType.TASK.name()).stream()
                .map(statusProjectionMapper::toDto)
                .toList();
    }
}
