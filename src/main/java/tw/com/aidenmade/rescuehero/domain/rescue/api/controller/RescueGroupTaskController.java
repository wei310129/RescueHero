package tw.com.aidenmade.rescuehero.domain.rescue.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.com.aidenmade.rescuehero.domain.base.api.controller.AbstractBaseController;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.base.application.service.StatusService;
import tw.com.aidenmade.rescuehero.domain.base.definition.StatusDefinition;
import tw.com.aidenmade.rescuehero.domain.rescue.api.request.RescueGroupTaskAcceptedRequest;
import tw.com.aidenmade.rescuehero.domain.rescue.api.request.RescueGroupTaskAvailableRequest;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupTaskDto;
import tw.com.aidenmade.rescuehero.domain.rescue.application.service.RescueGroupTaskService;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/group-task")
@RequiredArgsConstructor
public class RescueGroupTaskController extends AbstractBaseController {
    private final StatusService statusService;
    private final RescueGroupTaskService rescueGroupTaskService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/available")
    public ResponseEntity<Object> getUserAvailable(@RequestBody RescueGroupTaskAvailableRequest request) {
        Page<RescueGroupTaskDto> page = rescueGroupTaskService.getUserAvailable(
                request.getGroupId(),
                request.getDisasterId(),
                request.getNameLike(),
                request.getStatusId(),
                request.getPriority(),
                request.getPageable()
        );
        if (!page.hasContent()) {
            return notFoundResponse();
        }
        return okResponse(page);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/accepted")
    public ResponseEntity<Object> getUserAccepted(@RequestBody RescueGroupTaskAcceptedRequest request) {
        Optional<StatusDto> statusDtoOptional = statusService.getTaskStatuses().stream()
                .filter(s -> s.name().equals(StatusDefinition.TASK_ACCEPTED.getStatusName())).findFirst();
        if (statusDtoOptional.isEmpty()) {
            log.error("找不到任務類型: {}", StatusDefinition.TASK_ACCEPTED.getStatusName());
            return notFoundResponse();
        }
        StatusDto statusDto = statusDtoOptional.get();
        Page<RescueGroupTaskDto> page = rescueGroupTaskService.getUserAvailable(
                request.getGroupId(),
                request.getDisasterId(),
                request.getNameLike(),
                statusDto.id(),
                request.getPriority(),
                request.getPageable()
        );
        if (!page.hasContent()) {
            return notFoundResponse();
        }
        return okResponse(page);
    }
}
