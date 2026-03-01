package com.example.twixie.service;

import com.example.twixie.dto.ResponseDTO;
import com.example.twixie.service.mapper.ResponseMapper;
import com.example.twixie.service.repository.ResponseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final ResponseMapper responseMapper;

    public ResponseService(ResponseRepository responseRepository, ResponseMapper responseMapper) {
        this.responseRepository = responseRepository;
        this.responseMapper = responseMapper;
    }

    public List<ResponseDTO> buildResponseTree(Long parentPostId) {
        List<ResponseDTO> flatResponses =
                responseMapper.responsesToDto(
                        responseRepository.findAllResponsesByPostId(parentPostId));

        if (flatResponses == null || flatResponses.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, ResponseDTO> mapByResponseId = flatResponses.stream()
                .collect(Collectors.toMap(
                        ResponseDTO::getResponseId, // המפתח: responseId
                        Function.identity()          // הערך: אובייקט ResponseDTO עצמו
                ));

        List<ResponseDTO> rootResponses = new ArrayList<>();

        for (ResponseDTO currentDto : flatResponses) {
            Long parentId = currentDto.getParentResponseID();
            if (parentId == 0) {
                rootResponses.add(currentDto);
            }
            else if (mapByResponseId.containsKey(parentId)) {
                ResponseDTO parentDto = mapByResponseId.get(parentId);
                parentDto.getChildren().add(currentDto);
            }
        }
        // return only the parent responses
        return rootResponses;
    }

}
