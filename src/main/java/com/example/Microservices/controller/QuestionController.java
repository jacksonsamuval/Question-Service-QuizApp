package com.example.Microservices.controller;
import com.example.Microservices.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.Microservices.services.QuestionService;
import java.util.*;

@RestController
@RequestMapping("question")
public class QuestionController 
{
	@Autowired
	QuestionService questionService;
	
	@Autowired
	Environment enviroment;
	
	@GetMapping("allQuestion")
	public ResponseEntity<List<Question>> getAllQuestions()
	{
		System.out.println(enviroment.getProperty("local.server.port"));
		return questionService.getAllQuestions();
	}
	
	@GetMapping("category/{category}")
	public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category)
	{
		return questionService.getQuestionsByCategory(category);
	}
	
	@PostMapping("add")
	public ResponseEntity<String> addQuestion(@RequestBody Question question)
	{
		return questionService.addQuestion(question);
	}
	
	
	// generate questions if request from quiz
	// give questions based on question id 
	// GetScore
	
	@GetMapping("generate")
	public ResponseEntity<List<Integer>> getQuestionForQuiz(@RequestParam String categoryName, @RequestParam Integer numQuestions)
	{
		return questionService.getQuestionForQuiz(categoryName,numQuestions);
	}
	
	@PostMapping("getQuestions")
	public ResponseEntity<List<QuestionWrapper>> getQuestionFromId(@RequestBody List<Integer> questionIds)
	{
		return questionService.getQuestionsFromId(questionIds);
	}
	
	@PostMapping("getScore")
	public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses)
	{
		return questionService.getScore(responses);
	}
}
