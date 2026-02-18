package com.example.twixie.controller;

import com.example.twixie.dto.ResponseDTO;
import com.example.twixie.model.Response;
import com.example.twixie.security.CustomUserDetails;
import com.example.twixie.service.ResponseService;
import com.example.twixie.service.mapper.ResponseMapper;
import com.example.twixie.service.repository.ResponseRepository;
import com.example.twixie.service.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequestMapping({"api/response"})
@RestController
public class ResponseController {


    private final UsersRepository usersRepository;
    private final ResponseMapper responseMapper;
    private final ResponseRepository responseRepository;
    private final ResponseService responseService;

    public ResponseController(UsersRepository usersRepository, ResponseMapper responseMapper, ResponseRepository responseRepository, ResponseService responseService) {
        this.usersRepository = usersRepository;
        this.responseMapper = responseMapper;
        this.responseRepository = responseRepository;
        this.responseService = responseService;
    }

    @GetMapping({"/getResponseById/{id}"})
    public ResponseEntity<ResponseDTO> getResponseById(@PathVariable Long id) {
        try {
            Optional<Response> optionalResponse = responseRepository.findById(id);
            if (optionalResponse.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            Response response = optionalResponse.get();
            ResponseDTO rDto = responseMapper.responseToResponseDto(response);
            return new ResponseEntity<>(rDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/getResponsesByParentPostID/{parentPostId}"})
    public ResponseEntity<List<ResponseDTO>> getResponsesByParentPostID(@PathVariable Long parentPostId) {
        try {
            List<ResponseDTO> responses = responseService.buildResponseTree(parentPostId);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/addResponse"})
    @Transactional
    public ResponseEntity<ResponseDTO> addResponse(@Valid @RequestBody Response r, Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            Long id = ((CustomUserDetails) principal).getId();
            r.setResponser(usersRepository.findById(id).get());
            r.setDeleted(false);
            r.setDateCreated(LocalDateTime.now());
            Response response = responseRepository.save(r);
            return new ResponseEntity<>(responseMapper.responseToResponseDto(response), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateResponse/{id}")
    @PreAuthorize("hasRole('ADMIN') or @responseRepository.findById(#id).orElse(null)?.parentPostResponse.poster.userId == authentication.principal.id")
    public ResponseEntity<ResponseDTO> updateResponse(@PathVariable Long id, @RequestBody String newContent) {
        try {
            Optional<Response> optionalResponse = responseRepository.findById(id);
            if (optionalResponse.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            Response r = optionalResponse.get();
            r.setContent(newContent);
            Response newResponse = responseRepository.save(r);
            return new ResponseEntity<>(responseMapper.responseToResponseDto(newResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteResponse/{id}")
    @PreAuthorize("hasRole('ADMIN') or @responseRepository.findById(#id).orElse(null)?.responser.userId == authentication.principal.id")
    @Transactional
    public ResponseEntity<ResponseDTO> deleteResponse(@PathVariable Long id) {
        try {
            Optional<Response> optionalResponse = responseRepository.findById(id);
            if (optionalResponse.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            Response response = optionalResponse.get();

            Long currentUserId = ((CustomUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getId();

            // update the content - deleted
            if (response.getResponser().getUserId().equals(currentUserId)) {
                response.setContent("This response has been deleted by its author");
            } else {
                response.setContent("This response has been deleted by an admin");
            }
            response.setDeleted(true);
            Response saved = responseRepository.save(response);
            return ResponseEntity.ok(responseMapper.responseToResponseDto(saved));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getChildrenResponses/{parentResponseId}")
    public ResponseEntity<List<ResponseDTO>> getChildResponses(@PathVariable Long parentResponseId) {
        try {
            List<Response> responses = responseRepository.findResponsesByParentResponseID(parentResponseId);
            return new ResponseEntity<>(responseMapper.responsesToDto(responses), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
