package tw.com.aidenmade.rescuehero.domain.rescue.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupTaskDto;
import tw.com.aidenmade.rescuehero.domain.rescue.mapstruct.RescueGroupTaskProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.repository.RescueGroupTaskRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RescueGroupTaskService {
    private final RescueGroupTaskRepository rescueGroupTaskRepository;
    private final RescueGroupTaskProjectionMapper rescueGroupTaskProjectionMapper;

    public Page<RescueGroupTaskDto> getUserAvailable(Long groupId, Long disasterId, String nameLike,
                                                     Long statusId, Integer priority, Pageable pageable) {
        return rescueGroupTaskRepository
                .findPageByConditions(groupId, disasterId, nameLike, statusId, priority, pageable);
    }
}