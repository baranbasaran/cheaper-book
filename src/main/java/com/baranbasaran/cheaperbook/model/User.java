package com.baranbasaran.cheaperbook.model;

import com.baranbasaran.cheaperbook.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "users")
@Table(name = "users")
@Where(clause = "deleted = false")
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String profilePicture;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    @Builder.Default
    private Set<User> followers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private Set<User> following = new HashSet<>();

    public void follow(User userToFollow) {
        if (following.contains(userToFollow)) {
            return;
        }
        following.add(userToFollow);
        userToFollow.getFollowers().add(this);
    }

    public void unfollow(User userToUnfollow) {
        following.remove(userToUnfollow);
        userToUnfollow.getFollowers().remove(this);
    }

    public void addBook(Book book) {
        this.books.add(book);
        book.setOwner(this);
    }

    public void removeBook(Book book) {
        this.books.remove(book);
        book.setOwner(null);
    }

    public boolean isValid() {
        return this.getUsername() != null && this.getPassword() != null && this.getEmail() != null;
    }
}
