package com.quizservice.service;


import com.quizservice.dao.QuizDao;
import com.quizservice.feign.QuizInterface;
import com.quizservice.model.QuestionWrapper;
import com.quizservice.model.Quiz;
import com.quizservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {


    @Autowired
    QuizDao quizDao;
//
    @Autowired
    QuizInterface quizInterface;



    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();

        Quiz quiz = new Quiz();
        quiz.setTitle(title);

        quiz.setQuestionIDs(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);

    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIDs();


        ResponseEntity<List<QuestionWrapper>> questions= quizInterface.getQuestionsFromId(questionIds);

        return questions;
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {

        ResponseEntity<Integer> score = quizInterface.getScore(responses);

        return score;
    }
}
