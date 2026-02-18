//package com.example.twixie.controller;
//
//import com.example.twixie.service.PostCompletionService;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("api/completion")
//public class CompletionController {
//
//    private final PostCompletionService service;
//
//    public CompletionController(PostCompletionService service) {
//        this.service = service;
//    }
//
//    @GetMapping("/completePost")
//    public String complete(@RequestParam String text) {
//        return service.suggestNextSentence(text);
//    }
//}

package com.example.twixie.controller;

import com.example.twixie.dto.CompletionRequest;
import com.example.twixie.dto.CompletionResponse;
import com.example.twixie.service.PostCompletionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/completion")
public class CompletionController {

    private final PostCompletionService postCompletionService;

    public CompletionController(PostCompletionService postCompletionService) {
        this.postCompletionService = postCompletionService;
    }

    @PostMapping("/complete")
    public ResponseEntity<CompletionResponse> complete(@RequestBody CompletionRequest request) {
        // Validation
        if (request.text() == null || request.text().isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(new CompletionResponse(null, "Text cannot be empty"));
        }

        if (request.text().length() > 1000) {
            return ResponseEntity
                    .badRequest()
                    .body(new CompletionResponse(null, "Text is too long (max 1000 characters)"));
        }

        try {
            String completion = postCompletionService.suggestNextSentence(request.text());
            return ResponseEntity.ok(new CompletionResponse(completion, null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CompletionResponse(null, "Error generating completion: " + e.getMessage()));
        }
    }


}
