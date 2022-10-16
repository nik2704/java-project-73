package hexlet.code.app.service;

import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    /**
     * The method of adding label.
     * @param       labelDto - label is being created
     * @return       new label
     */
    @Override
    public Label createNewLabel(LabelDto labelDto) {
        final Label label = new Label();
        label.setName(labelDto.getName());
        return labelRepository.save(label);
    }

    /**
     * The base method of updating a label.
     * @param       id - status id
     * @param       labelDto - new label data
     * @return       updated label
     */
    @Override
    public Label updateLabel(long id, LabelDto labelDto) {
        final Label labelToUpdate = labelRepository.findById(id).get();
        labelToUpdate.setName(labelDto.getName());
        return labelRepository.save(labelToUpdate);
    }
}
