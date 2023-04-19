package org.elmiriyounes.javabackend.repository;

import com.fasterxml.jackson.databind.PropertyName;
import org.elmiriyounes.javabackend.entity.Role;
import org.elmiriyounes.javabackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

	/*
		JPA has a naming convention "conventionBy{PropertyName = column's name of DB}
		examples:
			* findBy{PropertyName}: findByTitle(String title) = return a value
			* findBy{PropertyName}In: findByTitleIn(List<String> titles) = return a Collection
			* findBy{PropertyName}Like: findByTitleLike(String title) = return a Collection
			* findBy{PropertyName}OrderBy{PropertyName}: findByTitleOrderByDateAsc(String title)
			= return a Collection or findByTitleOrderByDateDesc(String title)
			* countBy{PropertyName}: countByTitle(String title) = return count of rows
			* existsBy{PropertyName}: existsByTitle(String title) = return true if this title exists in the DB
	*/

	Optional<User> findByEmail(String email);

	Optional<List<User>> findByRole(Role role);

	boolean existsByEmail(String email);

	Long countByRole(Role role);
}
