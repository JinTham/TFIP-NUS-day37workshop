package tfip.csf.day37workshop.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@Service
public class S3Service {
    
    @Autowired
    private AmazonS3 s3Client;

    @Value("${DO_STORAGE_BUCKETNAME}")
    private String bucketName;

    public String upload(MultipartFile file, String title, String complain) throws IOException{
        // Need to prepare 2 items: metadata, key (i.e. the name of the image)
        // prepare userData for metadata
        Map<String, String> userData = new HashMap<>();
        userData.put("name", "Kenneth");
        userData.put("uploadDateTime", LocalDateTime.now().toString());
        userData.put("originalFilename", file.getOriginalFilename());
        userData.put("title", title);
        userData.put("complain", complain);
        
        // prepare metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(userData);

        // prepare key
        String key = UUID.randomUUID().toString().substring(0, 8);
        System.out.println(">>>> " + file.getOriginalFilename());
        StringTokenizer tk = new StringTokenizer(file.getOriginalFilename(), ".");
        int count = 0;
        String filenameExt = "";
        while(tk.hasMoreTokens()){
            if(count == 1){
                filenameExt = tk.nextToken();
                break;
            }else{
                filenameExt = tk.nextToken();
                count++;
            }
        }
        if(filenameExt.equals("blob"))
            filenameExt = filenameExt + ".png";
        
        // Use metadata and key to create PutObjectRequest
        PutObjectRequest putRequest = 
            new PutObjectRequest(bucketName, "myobject%s.%s".formatted(key, filenameExt), file.getInputStream(), metadata);
        putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        s3Client.putObject(putRequest);
        return "myobject%s.%s".formatted(key, filenameExt);
    }

    public List<String> listAll() throws JsonProcessingException{
        ListObjectsRequest listReq = new ListObjectsRequest().withBucketName(bucketName).withPrefix("");
        ObjectListing objList = s3Client.listObjects(listReq);

        List<S3ObjectSummary> objSummary = new ArrayList<>();

        while (true) {
            objSummary.addAll(objList.getObjectSummaries());
            if (objList.isTruncated()) {
                objList = s3Client.listNextBatchOfObjects(objList);
            } else {
                break;
            }
        }

        List<String> allItems = objSummary.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
        return allItems;
    }

    public JsonObject download(String id) throws IOException{
        GetObjectRequest getReq =  new GetObjectRequest(bucketName, id);
            S3Object result = s3Client.getObject(getReq);

            //! get object metadata
            ObjectMetadata metaData = result.getObjectMetadata();
            Map<String,String> userData = metaData.getUserMetadata();
            
            //! get object content as byte[]
            try(S3ObjectInputStream s3is = result.getObjectContent()){
                byte[] buffer = s3is.readAllBytes();
                String encodedString = Base64.getEncoder().encodeToString(buffer);
        
                JsonObject payload = Json.createObjectBuilder()
                    .add("image", "data:"+metaData.getContentType()+";base64,"+ encodedString)
                    .build();
                return payload;
            }

    }

}
