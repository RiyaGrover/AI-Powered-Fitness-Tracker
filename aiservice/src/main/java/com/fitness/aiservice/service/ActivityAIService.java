package com.fitness.aiservice.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("Response from ai: {}" , aiResponse);
        return processAiResponse(activity,aiResponse);
    }

    private Recommendation processAiResponse(Activity activity, String aiResponse){
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);
            JsonNode textNode = rootNode.path("candidates").get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n" , "")
                    .replaceAll("\\n```" ,"")
                    .trim();

//            log.info("Parsed Response from ai: {}" , jsonContent);

            JsonNode analysisJson = mapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis,analysisNode,"overall", "Overall:");
            addAnalysisSection(fullAnalysis,analysisNode,"pace", "Pace:");
            addAnalysisSection(fullAnalysis,analysisNode,"heartRate", "HeartRate:");
            addAnalysisSection(fullAnalysis,analysisNode,"CaloriesBurned", "CaloriesBurned:");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return createDefaultRecommendation(activity);
    }

    private Recommendation createDefaultRecommendation(Activity activity) {

        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .recommendation("Unable to generate detailed anaysis")
                .improvements(Collections.singletonList("Continue with your current routine"))
                .suggestions(Collections.singletonList("Conside consulting a fitness professional"))
                .safety(Arrays.asList(
                        "Always warm up before excercise",
                        "Listen to your body"
                ))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safetyList = new ArrayList<>();
        if (safetyNode.isArray()) {
            safetyNode.forEach(safety -> safetyList.add(safety.asText()));
        }

        return safetyList.isEmpty()
                ? Collections.singletonList("Follow general safety guidelines")
                : safetyList;

    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {

        List<String> suggestions = new ArrayList<>();
        if (suggestionsNode.isArray()) {
            suggestionsNode.forEach(suggestion -> {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestions.add(String.format("%s : %s", workout, description));
            });

        }
        return suggestions.isEmpty()
                ? Collections.singletonList("No specific suggestions provided")
                : suggestions;

    }

    private List<String> extractImprovements(JsonNode improvements) {

        List<String> improvementsList = new ArrayList<>();
        if (improvements.isArray()) {
            improvements.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();
                improvementsList.add(String.format("%s : %s", area, detail));
            });

        }
        return improvementsList.isEmpty()
                ? Collections.singletonList("No specific improvements provided")
                : improvementsList;
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {

        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format(""" 
            Analyze this fitness activity and provide detailed recommendations in the following format
            {
                "analysis" : {
                    "overall": "Overall analysis here",
                    "pace": "Pace analysis here",
                    "heartRate": "Heart rate analysis here",
                    "CaloriesBurned": "Calories Burned here"
                },
                "improvements": [
                    {
                        "area": "Area name",
                        "recommendation": "Detailed Recommendation"
                    }
                ],
                "suggestions" : [
                    {
                        "workout": "Workout name",
                        "description": "Detailed workout description"
                    }
                ],
                "safety": [
                    "Safety point 1",
                    "Safety point 2"
                ]
            }
      
            Analyze this activity:
            Activity Type: %s
            Duration: %d minutes
            calories Burned: %d
            Additional Metrics: %s
      
            provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines
            Ensure the response follows the EXACT JSON format shown above.
        """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }
}
