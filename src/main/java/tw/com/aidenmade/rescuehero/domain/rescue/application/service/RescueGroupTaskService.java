package tw.com.aidenmade.rescuehero.domain.rescue.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupTaskDto;
import tw.com.aidenmade.rescuehero.domain.rescue.mapstruct.RescueGroupTaskProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.repository.RescueGroupTaskRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RescueGroupTaskService {
    private final RescueGroupTaskRepository rescueGroupTaskRepository;
    private final RescueGroupTaskProjectionMapper rescueGroupTaskProjectionMapper;

    public Page<RescueGroupTaskDto> getUserAvailable(String nameLike,
                                                     Long statusId, Integer priority, Pageable pageable) {
        return rescueGroupTaskRepository
                .findPageByConditions(nameLike, List.of(statusId), priority, pageable);
    }

    public Page<RescueGroupTaskDto> getUserAccepted(String nameLike, Integer priority, Pageable pageable) {
        return rescueGroupTaskRepository
                .findPageByConditions(nameLike, null, priority, pageable);
    }
}