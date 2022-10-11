package hexlet.code.app.service;

import hexlet.code.app.dto.StatusDto;
import hexlet.code.app.model.Status;
import hexlet.code.app.repository.StatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    /**
     * The base method of adding status.
     * @param       statusDto - status is being created
     * @return       new status
     */
    @Override
    public Status createNewStatus(StatusDto statusDto) {
        final Status status = new Status();
        status.setName(statusDto.getName());
        return statusRepository.save(status);
    }

    /**
     * The base method of updating status.
     * @param       id - status id
     * @param       statusDto - new status data
     * @return       updated status
     */
    @Override
    public Status updateStatus(long id, StatusDto statusDto) {
        final Status statusToUpdate = statusRepository.findById(id).get();
        statusToUpdate.setName(statusDto.getName());
        return statusRepository.save(statusToUpdate);
    }
}

