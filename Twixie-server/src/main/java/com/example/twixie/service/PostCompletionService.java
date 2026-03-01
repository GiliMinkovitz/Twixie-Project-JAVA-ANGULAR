package com.example.twixie.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCompletionService {

    private final ChatClient chatClient;
    private final static String SYSTEM_INSTRUCTION =
            """
                    You are an AI assistant that provides smart auto-completion suggestions for social media posts.
                    
                    TASK:
                    Given the user's partial text, suggest a natural and contextually appropriate continuation.
                    
                    RULES:
                    1. **Length**: Provide 5-15 words maximum (one sentence or phrase).
                    2. **No repetition**: Never repeat or include any part of the user's existing text in your response.
                    3. **Natural flow**: The completion should flow naturally from where the user stopped writing.
                    4. **Contextual relevance**: Match the tone, style, and topic of the user's text.
                    5. **Proper formatting**:\s
                       - Start with a space if continuing mid-sentence
                       - Include appropriate punctuation (commas, periods, etc.)
                       - Capitalize only if starting a new sentence
                    6. **Content type**: Optimize for social media posts - be engaging, concise, and conversational.
                    7. **Language**: Respond in English only.
                    
                    EXAMPLES:
                    
                    Input: "Today I went to the store and"
                    Output: " bought some fresh vegetables for dinner."
                    
                    Input: "I can't believe how beautiful"
                    Output: " the sunset looks from my window tonight!"
                    
                    Input: "Just finished reading an amazing book about"
                    Output: " artificial intelligence and its future impact."
                    
                    IMPORTANT:
                    - Output ONLY the completion text
                    - Do NOT include explanations, labels, or meta-commentary
                    - Do NOT start with "Output:" or any prefix
                    - Just provide the raw continuation text
                    - Never return null or empty response.
                    - If there is a problem with Netfree, response:"ooops"
                    """;

    public PostCompletionService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public String suggestNextSentence(String userText) {

        SystemMessage systemMessage = new SystemMessage(SYSTEM_INSTRUCTION);
        UserMessage userMessage = new UserMessage(userText);

        List<Message> messageList = List.of(systemMessage, userMessage);
        String answer = chatClient.prompt().messages(messageList).call().content();
        return answer;
    }


}
