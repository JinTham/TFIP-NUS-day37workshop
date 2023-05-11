package tfip.csf.day37workshop.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tfip.csf.day37workshop.models.Post;
import tfip.csf.day37workshop.repositories.FileUploadRepository;

@Service
public class FileUploadService {
    @Autowired
    private FileUploadRepository repo;

    public void upload(MultipartFile file, String title, String complain) throws SQLException, IOException{
        this.repo.upload(file, title, complain);
    }

    public Optional<Post> getPostById(Integer postId){
        return this.repo.getPostById(postId);
    }
}
