package com.rafael.mediasocial.user;

import com.rafael.mediasocial.jpa.PostRepository;
import com.rafael.mediasocial.jpa.UserRepository;
import com.rafael.mediasocial.user.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserJpaResource {


    private UserRepository userRepository;

    private PostRepository postRepository;

    public UserJpaResource(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // GET /users
    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    // GET /users
    @GetMapping("/jpa/users/{id}")
    //EntityModel defines entity classes, primary keys, secondary keys, and relationships between entities.
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()){
            throw new UserNotFoundException("id: "+id);
        }
        //para add links na resposta do metodo
        EntityModel<User> entityModel = EntityModel.of(user.get());
        //para adicionar links ao entityModel
        WebMvcLinkBuilder linkBuilder = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(linkBuilder.withRel("all-users"));

        return entityModel;
    }

    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrievePostsForUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty())
            throw new UserNotFoundException("id: "+id);

        return user.get().getPosts();
    }

    @PostMapping("/jpa/users")
    public ResponseEntity<User> createUsers(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        //metodo para devolver o URI do novo usuario
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Object> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty())
            throw new UserNotFoundException("id: "+id);

        //usa o .get() pq eh um Optional
        post.setUser(user.get());
        Post savedPost = postRepository.save(post);

        //metodo para devolver o URI do novo usuario
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();    }
}
