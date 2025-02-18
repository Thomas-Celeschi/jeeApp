package myapp.jpa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "post-entity-graph-with-comment-users",
        attributeNodes = {
                @NamedAttributeNode("subject"),
                @NamedAttributeNode("user"),
                @NamedAttributeNode(value = "comments", subgraph = "comments-subgraph"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "comments-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("user")
                        }
                )
        }
)
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String subject;
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    public Post(String subject, User user) {
        this.subject = subject;
        this.user = user;
        this.user.addPost(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
