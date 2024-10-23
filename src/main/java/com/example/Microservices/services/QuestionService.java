package com.example.Microservices.services;
import com.example.Microservices.Dao.QuestionDao;
import com.example.Microservices.model.*;
import com.example.Microservices.model.Question;
import com.example.Microservices.model.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class QuestionService {
	
	@Autowired
	QuestionDao questionDao;
	
	public ResponseEntity<List<Question>> getAllQuestions() 
	{
		try 
		{
			return new ResponseEntity<>(questionDao.findAll(),HttpStatus.OK);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<List<Question>> getQuestionsByCategory(String category) 
	{
		try
		{
			return new ResponseEntity<>(questionDao.findByCategory(category),HttpStatus.OK);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<String> addQuestion(Question question) 
	{	
		try
		{
			questionDao.save(question);	
			return new ResponseEntity<>("Success",HttpStatus.CREATED);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ResponseEntity<>("Question Not Added",HttpStatus.BAD_REQUEST);	
	}

	//Microservices Methods
	
	public ResponseEntity<List<Integer>> getQuestionForQuiz(String categoryName, Integer numQuestions) {
		List<Integer> questions = questionDao.findRandomQuestionsByCategory(categoryName);
		return new ResponseEntity<>(questions, HttpStatus.OK);
	}

	public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
		List<QuestionWrapper> wrappers = new ArrayList<>();
		List<Question> questions = new ArrayList<>();
		
		for(Integer id : questionIds)
		{
			questions.add(questionDao.findById(id).get());
		}
		for(Question question : questions)
		{
			QuestionWrapper wrapper = new QuestionWrapper();
			wrapper.setId(question.getId());
			wrapper.setQuestionTitle(question.getQuestionTitle());
			wrapper.setOption1(question.getOption1());
			wrapper.setOption2(question.getOption2());
			wrapper.setOption3(question.getOption3());
			wrapper.setOption4(question.getOption4());
			wrappers.add(wrapper);
		}		
		return new ResponseEntity<>(wrappers, HttpStatus.OK);
	}

	public ResponseEntity<Integer> getScore(List<Response> responses) 
	{		
		int right =0;	
		for(Response response: responses)
		{
			Question question = questionDao.findById(response.getId()).get();
			if(response.getResponse().equals(question.getRightAnswer()))
				right++;
		}		
		return new ResponseEntity<>(right, HttpStatus.OK);
	}
}
