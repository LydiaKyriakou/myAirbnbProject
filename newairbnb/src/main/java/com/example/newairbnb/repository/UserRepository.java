package com.example.newairbnb.repository;
import com.example.newairbnb.exception.UserNotFoundException;
import com.example.newairbnb.user.Rental;
import com.example.newairbnb.user.RoleName;
import com.example.newairbnb.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{

    void deleteUserById(Long id);

    Optional<User> findUserById(Long id);

    Optional<User> findByUsername( String username);

    Optional<User> findByEmail( String email);

    Boolean existsByUsername( String username);

    Boolean existsByEmail( String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    @Query(value = "SELECT * " +
            "FROM users u " ,
            nativeQuery = true)
    Page<User> getUsers(Pageable pageable);



    default User getUserByName(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found" + username + "username"));
    }


    @Query("SELECT u.id FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<Long> findUserIdsByRole(@Param("roleName") RoleName roleName);


}