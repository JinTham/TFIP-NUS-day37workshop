package tfip.csf.day37workshop.repositories;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import tfip.csf.day37workshop.models.Post;


@Repository
public class FileUploadRepository {
    private static final String INSERT_POST_SQL = "INSERT INTO posts( blobc, title, complain) VALUES (?,?,?)";
    
    private static final String SQL_GET_POST_BY_POSTID = "select id , title, complain, blobc from posts where id=?";
    
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate template;

    public void upload(MultipartFile file, String title, String complain) throws SQLException, IOException{
        try(Connection con = dataSource.getConnection(); 
            PreparedStatement prstmt = con.prepareStatement(INSERT_POST_SQL)){
            InputStream is = file.getInputStream();
            prstmt.setBinaryStream(1, is, file.getSize());
            prstmt.setString(2, title);
            prstmt.setString(3, complain);
            prstmt.executeUpdate();
        }
    }

    public Optional<Post> getPostById(Integer postId){
        return template.query(SQL_GET_POST_BY_POSTID,
            (ResultSet rs)->{
                if(!rs.next())
                    return Optional.empty();
                final Post post = Post.populate(rs);
                return Optional.of(post);
            }
        , postId);
    }
}
