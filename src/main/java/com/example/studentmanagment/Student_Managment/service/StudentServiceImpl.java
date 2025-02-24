package com.example.studentmanagment.Student_Managment.service;

import com.example.studentmanagment.Student_Managment.model.Student;
import com.example.studentmanagment.Student_Managment.repo.StudentRepository;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.springframework.ai.document.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroqAiHelperService groqAiHelperService;



    @Autowired
    @Qualifier("customVectorStore")
    VectorStore milvusStore;


    @Override
    public Student saveStudent(Student student) {
        Student savedStudent = studentRepository.save(student);
        List<Document> documents = List.of(
                new Document(student.getName()+" "+student.getEmail()+" "+student.getMeaning())
        );

        milvusStore.add(documents);

        System.out.println("Student added to Milvus!");

        return savedStudent;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student updateStudent(Long id, Student updatedStudent) {
        return studentRepository.findById(id).map(student -> {
            student.setName(updatedStudent.getName());
            student.setEmail(updatedStudent.getEmail());
            // ✅ Fetch updated meaning
            String newMeaning = groqAiHelperService.getNameMeaning(updatedStudent.getName());
            student.setMeaning(newMeaning);
            return studentRepository.save(student);
        }).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public String fetchNameMeaning(String name) {
        return groqAiHelperService.getNameMeaning(name); // Fetch the meaning
    }



   @Override
    public List<Document> searchSimilarText(String info) {
        return milvusStore.similaritySearch(SearchRequest.builder()
                .query(info)
                .topK(1)
                .build());
    }


//    public List<Document> convertStudentToDocuments(Student student, EmbeddingModel embeddingModel) {
//        String studentText = student.toString();
//        float[] embedding = embeddingModel.embed(studentText);  // Generate embeddings
//
//        Document doc = new Document(
//                studentText,   // The content to store
//                Map.of(        // Metadata (optional)
//                        "id", student.getId().toString(),
//                        "name", student.getName(),
//                        "email", student.getEmail(),
//                        "vector", embedding
//                )
//        );
//
//        return List.of(doc);
//    }

    // private List<Float> convertStudentToVector(Student student) {
    //     // Example: Convert name and email to a vector (this is just a placeholder)
    //     List<Float> vector = new ArrayList<>();
    //     vector.add((float) student.getName().length()); // Example feature
    //     vector.add((float) student.getEmail().length()); // Example feature
    //     // Add more features as needed
    //     return vector;
    // }

    // public List<Float> createEmbedding(Student student) {
    //     // Convert student data to a vector (this is a placeholder for your actual embedding logic)
    //     List<Float> embedding = new ArrayList<>();
    //     embedding.add((float) student.getId());
    //     embedding.add(student.getName().length() * 0.1f); // Example transformation
    //     // Add more features as needed
    //     return embedding;
    // }

//    public List<Student> searchSimilarStudents(List<Float> queryVector) {
//        // Perform a vector search in Milvus
//        List<Float> searchResults = vectorStore. // k is the number of nearest neighbors to return
//
//        // Convert search results back to Student objects (this is a placeholder)
//        List<Student> similarStudents = new ArrayList<>();
//        for (Float result : searchResults) {
//            // Retrieve the student based on the result (you may need to map IDs or similar)
//            similarStudents.add(retrieveStudentById(result)); // Implement this method
//        }
//        return similarStudents;
//    }
}
