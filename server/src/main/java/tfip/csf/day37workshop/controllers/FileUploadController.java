package tfip.csf.day37workshop.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import tfip.csf.day37workshop.models.Post;
import tfip.csf.day37workshop.services.FileUploadService;
import tfip.csf.day37workshop.services.S3Service;


@Controller
public class FileUploadController {
    @Autowired
    private S3Service s3Svc;

    @Autowired
    private FileUploadService ffSvc;

    private static final String BASE64_PREFIX = "data:image/png;base64,";

    @PostMapping(path="/upload",consumes=MediaType.MULTIPART_FORM_DATA_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> upload(
        @RequestPart MultipartFile file,
        @RequestPart String title,
        @RequestPart String complain){
        String key = "";
        try{
            key = s3Svc.upload(file, title, complain);
            this.ffSvc.upload(file, title, complain);
        }catch(IOException | SQLException e){
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
        }
        JsonObject payload = Json.createObjectBuilder()
            .add("imageKey", key)
            .build();
            
        return ResponseEntity.ok(payload.toString());
    }

    @GetMapping(path="/get-image/{postId}")
    public ResponseEntity<String> retrieveImage(@PathVariable Integer postId){
        Optional<Post> r = this.ffSvc.getPostById(postId);
        Post p = r.get();
        String encodedString  = Base64.getEncoder().encodeToString(p.getImage());
        JsonObject payload = Json.createObjectBuilder()
                                .add("image", BASE64_PREFIX + encodedString)
                                .build();
        return ResponseEntity.ok(payload.toString());
    }

    @GetMapping(path="/get-all-images")
    public ResponseEntity<String> retrieveAllImagesFromS3() throws JsonProcessingException {
        List<String> allItems = s3Svc.listAll();
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(allItems);
        return ResponseEntity.status(HttpStatus.OK).body(jsonStr);
    }

    @GetMapping(path="/get-image-s3/{postId}")
    public ResponseEntity<String> retrieveImageFromS3(@PathVariable String postId){
        try {
            JsonObject payload = s3Svc.download(postId);
            return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(payload.toString());
        } catch (AmazonS3Exception ex){
            // object not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{'error':'Object not found'}");
        } catch (Exception ex){
            // For S3ObjectInputStream
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{'error':'Inputstream error'}");
        }
    }
   
}
