package eu.arima.hibernaterollback;

import eu.arima.hibernaterollback.users.User;
import eu.arima.hibernaterollback.users.UserRepository;
import eu.arima.hibernaterollback.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;

@Service
public class GlobalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalService.class);

    private final UserService userService;

    private final UserRepository userRepository;

    public GlobalService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Transactional
    public void doSomethingSpringDataJpaNotRecoveringFromException() {
        createUser();

        try {
            User user = new User();
            user.setName("test");
            userService.failSavingExistingUserWithSpringData(user);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("Exception catched. Trying to recover...", e);
        }

    }

    @Transactional
    public void doSomethingPlainHibernateNotRecoveringFromException() {
        createUser();

        try {
            User user = new User();
            user.setName("test");
            this.userService.failSavingExistingUserWithPlainHibernate(user);
        } catch (PersistenceException e) {
            LOGGER.info("Exception catched. Trying to recover...", e);
        }

    }

    @Transactional
    public void doSomethingRecoveringFromException() {
        createUser();

        try {
            this.userService.failWithNoRollbackFor();
        } catch (IllegalStateException e) {
            LOGGER.info("Exception catched. Trying to recover...", e);
        }

    }

    @Transactional
    public void doSomethingNotRecoveringFromException() {
        createUser();

        try {
            this.userService.fail();
        } catch (IllegalStateException e) {
            LOGGER.info("Exception catched. Trying to recover...", e);
        }

    }

    private void createUser() {
        User user1 = new User();
        user1.setName("test");
        this.userRepository.saveAndFlush(user1);
    }

}
