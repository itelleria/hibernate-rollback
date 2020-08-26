package eu.arima.hibernaterollback;

import eu.arima.hibernaterollback.users.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class GlobalServiceTest {

    @Autowired GlobalService globalService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void cleanTable() {
        this.userRepository.deleteAll();
    }

    @Test
    void doSomethingSpringDataJpaNotRecoveringFromException() {

        assertThrows(UnexpectedRollbackException.class,
                () ->  this.globalService.doSomethingSpringDataJpaNotRecoveringFromException());

    }

    @Test
    void doSomethingPlainHibernateNotRecoveringFromException() {

        assertThrows(UnexpectedRollbackException.class,
                () ->  this.globalService.doSomethingPlainHibernateNotRecoveringFromException());

    }

    @Test
    void doSomethingRecoveringFromException() {

        this.globalService.doSomethingRecoveringFromException();

        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    void doSomethingNotRecoveringFromException() {

        assertThrows(UnexpectedRollbackException.class,
                () ->  this.globalService.doSomethingNotRecoveringFromException());

    }

}
