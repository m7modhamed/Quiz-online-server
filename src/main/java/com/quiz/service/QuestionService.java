package com.quiz.service;

import com.quiz.model.Question;
import com.quiz.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService{

    private final QuestionRepository repo;
    @Override
    public Question createQuestion(Question question) {
        return repo.save(question);
    }

    @Override
    public List<Question> getAllQuestion() {
        return repo.findAll();
    }

    @Override
    public Optional<Question> getQuestionById(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<String> getAllSubjects() {
        return repo.findDistinctSubject();
    }

    @Override
    public Question updateQuestion(Long id, Question question) throws ChangeSetPersister.NotFoundException {
        Optional<Question> theQuestion=this.getQuestionById(id);
        if(theQuestion.isPresent()){
            Question updatedQuestion=theQuestion.get();
            updatedQuestion.setQuestion(question.getQuestion());
            updatedQuestion.setChoices(question.getChoices());
            updatedQuestion.setCorrectAnswers(question.getCorrectAnswers());
            return repo.save(updatedQuestion);
        }else{
            throw new ChangeSetPersister.NotFoundException();
        }

    }

    @Override
    public void deleteQuestion(Long id) {

        repo.deleteById(id);

    }

    @Override
    public List<Question> getQuestionForUser(Integer numberOfQuestions, String subject) {
        Pageable pageable= PageRequest.of(0,numberOfQuestions);
        return repo.findBySubject(subject,pageable).getContent();
    }
}
