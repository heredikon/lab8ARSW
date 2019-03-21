package edu.eci.persistences;

import edu.eci.models.User;
import edu.eci.persistences.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
@Qualifier("UserMemoryRepository")
public class UserMemoryRepository implements IUserRepository{

    public static List<User> usersContainer;
    public static List<User> getContainer(){
        if(UserMemoryRepository.usersContainer == null)
            UserMemoryRepository.usersContainer = new ArrayList<>();
        return UserMemoryRepository.usersContainer;
    }

    @Override
    public User getUserByUserName(String userName) {
        return UserMemoryRepository.getContainer()
                .stream()
                .filter(u -> userName.equals(u.getName()))
                .findFirst()
                .get();
    }

    @Override
    public List<User> findAll() {
        return UserMemoryRepository.getContainer();
    }

    @Override
    public User find(UUID id) {
        Optional<User> answer = UserMemoryRepository.getContainer()
                .stream()
                .filter(u -> id.equals(u.getId()))
                .findFirst();
        return answer.isPresent() ? answer.get() : null;
    }

    @Override
    public UUID save(User entity) {
        UserMemoryRepository.getContainer().add(entity);
        return entity.getId();
    }

    @Override
    public void update(User entity) {
        UserMemoryRepository.usersContainer = UserMemoryRepository.getContainer()
                .stream()
                .map(u -> u.getId().equals(entity.getId()) ? entity : u)
                .collect(toList());
    }

    @Override
    public void delete(User o) {
        UserMemoryRepository.usersContainer = UserMemoryRepository.getContainer()
                .stream()
                .filter(u -> !u.getId().equals(o.getId()))
                .collect(toList());
    }

    @Override
    public void remove(Long id) {
        UserMemoryRepository.usersContainer = UserMemoryRepository.getContainer()
                .stream()
                .filter(u -> !u.getId().equals(id))
                .collect(toList());
    }
}
