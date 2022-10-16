package hexlet.code.app.service;

import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.model.Label;

public interface LabelService {
    Label createNewLabel(LabelDto labelDto);

    Label updateLabel(long id, LabelDto labelDto);
}
