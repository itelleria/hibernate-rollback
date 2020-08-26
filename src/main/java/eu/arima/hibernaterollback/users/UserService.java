package eu.arima.hibernaterollback.users;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Service
public class UserService {

    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(noRollbackFor = DataIntegrityViolationException.class)
    public void failSavingExistingUserWithSpringData(User user) {
        this.userRepository.saveAndFlush(user);
    }

    @Transactional(noRollbackFor = PersistenceException.class)
    public void failSavingExistingUserWithPlainHibernate(User user) {
        this.entityManager.persist(user);
        this.entityManager.flush();
    }

    @Transactional
    public void fail() {
        if (true) {
            throw new IllegalStateException("This is painful");
        }
    }

    @Transactional(noRollbackFor = IllegalStateException.class)
    public void failWithNoRollbackFor() {
        if (true) {
            throw new IllegalStateException("This is painful");
        }
    }


}
