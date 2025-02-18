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
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "user-with-posts",
                attributeNodes = {
                        @NamedAttributeNode("name"),
                        @NamedAttributeNode(value = "posts", subgraph = "posts-subgraph"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "posts-subgraph",
                                attributeNodes = {
                                        @NamedAttributeNode("comments")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "user-with-comments",
                attributeNodes = {
                        @NamedAttributeNode("name"),
                        @NamedAttributeNode(value = "comments", subgraph = "comments-subgraph"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "comments-subgraph",
                                attributeNodes = {
                                        @NamedAttributeNode("post")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "user-with-posts-and-comments",
                attributeNodes = {
                        @NamedAttributeNode("name"),
                        @NamedAttributeNode(value = "posts", subgraph = "posts-subgraph"),
                        @NamedAttributeNode(value = "comments", subgraph = "comments-subgraph")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "posts-subgraph",
                                attributeNodes = {
                                        @NamedAttributeNode("comments")
                                }
                        ),
                        @NamedSubgraph(
                                name = "comments-subgraph",
                                attributeNodes = {
                                        @NamedAttributeNode("post")
                                }
                        )
                }
        ),

})
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String email;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Post> posts = new ArrayList<>();

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Comment> comments = new ArrayList<>();

  public User(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public void addPost(Post post) {
    posts.add(post);
  }

  public void addComment(Comment comment) {
    comments.add(comment);
  }


}
