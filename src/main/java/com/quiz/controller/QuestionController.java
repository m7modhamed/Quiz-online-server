package com.quiz.controller;


import com.quiz.model.Question;
import com.quiz.service.IQuestionService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/quizzes")
@CrossOrigin(origins = "http://localhost:5173/")
public class QuestionController {

    private final IQuestionService  questionService;

    @PostMapping("/create-new-question")
    public ResponseEntity<Question> createQuestion(@Valid @RequestBody Question question){
        Question createdQuestion=questionService.createQuestion(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @GetMapping("/all-questions")
    public ResponseEntity<List<Question>> getAllQuestions(){
        List<Question> questions=questionService.getAllQuestion();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/question/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        Optional<Question> theQuestion = questionService.getQuestionById(id);
        if (theQuestion.isPresent()) {
            return ResponseEntity.ok(theQuestion.get());
        }else{
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    @PutMapping("/question/{id}")
    public ResponseEntity<Question> updateQuestionById(@PathVariable Long id,@RequestBody Question question) throws ChangeSetPersister.NotFoundException {
            Question updatedQuestion=questionService.updateQuestion(id,question);
            return ResponseEntity.ok(updatedQuestion);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
            questionService.deleteQuestion(id);

            return ResponseEntity.noContent().build();

    }

    @GetMapping("/subjects")
    public ResponseEntity<List<String>> getAllSubjects(){
        List<String> subjects=questionService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("fetch-questions-for-user")
    public ResponseEntity<List<Question>> getQuestionsForUser(
            @RequestParam Integer numOfQuestion
            ,@RequestParam String subject){

        List<Question> questions=questionService
                .getQuestionForUser(numOfQuestion,subject);

        List<Question> mutableQuestions=new ArrayList<>(questions);
        Collections.shuffle(mutableQuestions);
        int availableQuestions=Math.min(numOfQuestion,mutableQuestions.size());
        List<Question> randomQuestions=mutableQuestions.subList(0,availableQuestions);

        return ResponseEntity.ok(randomQuestions);
    }

}
