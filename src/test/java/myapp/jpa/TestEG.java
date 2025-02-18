package myapp.jpa;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import myapp.jpa.dao.CommentRepository;
import myapp.jpa.dao.PostRepository;
import myapp.jpa.dao.UserReposity;
import myapp.jpa.model.Comment;
import myapp.jpa.model.Post;
import myapp.jpa.model.User;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Rollback(value = false)
@SpringBootTest
public class TestEG {

  @Autowired PostRepository pr;

  @Autowired UserReposity ur;

  @Autowired CommentRepository cr;

  @Autowired EntityManager em;

  public void populate() {
    var u1 = new User("Thomas", "thomas.celeschi@gmail.com");
    ur.save(u1);
    var u2 = new User("", "");
    ur.save(u2);
    var u3 = new User("", "");
    ur.save(u3);

    var p10 = new Post("Il fait beau", u1);
    pr.save(p10);
    var p11 = new Post("Su-per", u1);
    pr.save(p11);
    var p12 = new Post("", u1);
    pr.save(p12);

    var p20 = new Post("", u2);
    pr.save(p20);
    var p21 = new Post("", u2);
    pr.save(p21);
    var p22 = new Post("", u2);
    pr.save(p22);

    var p30 = new Post("", u3);
    pr.save(p30);
    var p31 = new Post("", u3);
    pr.save(p31);
    var p32 = new Post("", u3);
    pr.save(p32);

    var c100 = new Comment("Pas mal", p10, u2);
    cr.save(c100);
    var c101 = new Comment("La vidéo", p10, u2);
    cr.save(c101);
    var c102 = new Comment("Je like", p10, u3);
    cr.save(c102);

    var c110 = new Comment("Super", p11, u1);
    cr.save(c110);
    var c111 = new Comment("", p11, u2);
    cr.save(c111);
    var c112 = new Comment("", p11, u2);
    cr.save(c112);

    var c120 = new Comment("", p12, u3);
    cr.save(c120);
    var c121 = new Comment("", p12, u2);
    cr.save(c121);
    var c122 = new Comment("", p12, u3);
    cr.save(c122);

    var c200 = new Comment("", p20, u1);
    cr.save(c200);
    var c201 = new Comment("", p20, u3);
    cr.save(c201);
    var c202 = new Comment("", p20, u3);
    cr.save(c202);

    var c210 = new Comment("", p21, u1);
    cr.save(c210);
    var c211 = new Comment("", p21, u1);
    cr.save(c211);
    var c212 = new Comment("", p21, u3);
    cr.save(c212);

    var c220 = new Comment("", p22, u2);
    cr.save(c220);
    var c221 = new Comment("", p22, u2);
    cr.save(c221);
    var c222 = new Comment("", p22, u3);
    cr.save(c222);

    var c300 = new Comment("", p30, u2);
    cr.save(c300);
    var c301 = new Comment("", p30, u2);
    cr.save(c301);
    var c302 = new Comment("", p30, u2);
    cr.save(c302);

    var c310 = new Comment("", p31, u1);
    cr.save(c310);
    var c311 = new Comment("", p31, u1);
    cr.save(c311);
    var c312 = new Comment("", p31, u1);
    cr.save(c312);

    var c320 = new Comment("", p32, u3);
    cr.save(c320);
    var c321 = new Comment("", p32, u3);
    cr.save(c321);
    var c322 = new Comment("", p32, u1);
    cr.save(c322);
  }

  @Test
  public void testFindUser() {
    populate();
    User user = em.find(User.class, 1);
    assertEquals(user.getName(), "Thomas");
    assertEquals(user.getEmail(), "thomas.celeschi@gmail.com");
  }

  @Test
  public void testFindComment() {
    populate();
    Comment comment = em.find(Comment.class, 1);
    assertEquals(comment.getReply(), "Pas mal");
    User user = comment.getUser();
    assertEquals(user.getId(), 2);
    assertThrows(LazyInitializationException.class, () -> user.getName());
    assertThrows(LazyInitializationException.class, () -> user.getEmail());
    Post post = comment.getPost();
    assertEquals(post.getId(), 1);
    assertThrows(LazyInitializationException.class, () -> post.getSubject());
    assertThrows(LazyInitializationException.class, () -> post.getComments());
  }

  @Test
  public void testFindPost() {
    populate();
    Post post = em.find(Post.class, 1);
    Comment comment = post.getComments().get(0);
    assertEquals(post.getComments().size(), 3);
    assertEquals(comment.getReply(), "Pas mal");
    assertEquals(comment.getUser().getId(), 2);
    assertEquals(comment.getPost(), post);
    User user = post.getUser();
    assertEquals(user.getId(), 1);
    assertThrows(LazyInitializationException.class, () -> user.getName());
    assertThrows(LazyInitializationException.class, () -> user.getEmail());
  }

  @Test
  public void testFindPostUsingEG() {
    populate();
    EntityGraph<?> entityGraph = em.getEntityGraph("post-entity-graph-with-comment-users");
    Map<String, Object> properties = new HashMap<>();
    properties.put("jakarta.persistence.fetchgraph", entityGraph);
    Post post = em.find(Post.class, 1, properties);

    Comment comment = post.getComments().get(0);
    assertEquals(post.getComments().size(), 3);
    assertEquals(comment.getReply(), "Pas mal");
    assertEquals(comment.getUser().getId(), 2);
    assertEquals(comment.getPost(), post);
    User user = post.getUser();
    assertEquals(user.getId(), 1);
    assertEquals(user.getName(), "Thomas");
    assertEquals(user.getEmail(), "thomas.celeschi@gmail.com");
  }

  @Test
  public void testFindPostUsingJPQL() {
    populate();
    EntityGraph entityGraph = em.getEntityGraph("post-entity-graph-with-comment-users");
    Post post =
        em.createQuery("select p from Post p where p.id = :id", Post.class)
            .setParameter("id", 1)
            .setHint("jakarta.persistence.fetchgraph", entityGraph)
            .getSingleResult();

    Comment comment = post.getComments().get(0);
    assertEquals(post.getComments().size(), 3);
    assertEquals(comment.getReply(), "Pas mal");
    assertEquals(comment.getUser().getId(), 2);
    assertEquals(comment.getPost(), post);
    User user = post.getUser();
    assertEquals(user.getId(), 1);
    assertEquals(user.getName(), "Thomas");
    assertEquals(user.getEmail(), "thomas.celeschi@gmail.com");
  }

  @Test
  public void testFindUserNew() {
    populate();
    User user = em.find(User.class, 1);

    assertThrows(LazyInitializationException.class, () -> user.getPosts().get(0).getSubject());
    assertThrows(LazyInitializationException.class, () -> user.getComments().get(0).getReply());
  }
  @Test
  public void testFindUserPostsUsingEG() {
    populate();
    EntityGraph<?> entityGraph = em.getEntityGraph("user-with-posts");
    Map<String, Object> properties = new HashMap<>();
    properties.put("jakarta.persistence.fetchgraph", entityGraph);
    User user = em.find(User.class, 1, properties);

    Post post = user.getPosts().get(0);
    assertEquals(post.getSubject(), "Il fait beau");
    assertEquals(post.getComments().get(0).getReply(), "Pas mal");

    assertThrows(LazyInitializationException.class, () -> user.getComments().get(0).getReply());
  }

  @Test
  public void testFindUserCommentsUsingEG() {
    populate();
    EntityGraph<?> entityGraph = em.getEntityGraph("user-with-comments");
    Map<String, Object> properties = new HashMap<>();
    properties.put("jakarta.persistence.fetchgraph", entityGraph);
    User user = em.find(User.class, 1, properties);

    Comment comment = user.getComments().get(0);
    assertEquals(comment.getReply(), "Super");
    assertEquals(comment.getPost().getSubject(), "Su-per");

    assertThrows(LazyInitializationException.class, () -> user.getPosts().get(0).getSubject());
  }

  @Test
  public void testFindUserPostsAndCommentsUsingEG() {
    populate();
    EntityGraph<?> entityGraph = em.getEntityGraph("user-with-posts-and-comments");
    Map<String, Object> properties = new HashMap<>();
    properties.put("jakarta.persistence.fetchgraph", entityGraph);
    User user = em.find(User.class, 1, properties);

    Post post = user.getPosts().get(0);
    assertEquals(post.getSubject(), "Il fait beau");
    assertEquals(post.getComments().get(0).getReply(), "Pas mal");

    Comment comment = user.getComments().get(0);
    assertEquals(comment.getReply(), "Super");
    assertEquals(comment.getPost().getSubject(), "Su-per");
  }
}
