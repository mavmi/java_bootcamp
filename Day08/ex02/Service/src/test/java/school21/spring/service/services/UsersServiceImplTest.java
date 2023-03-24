package school21.spring.service.services;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import school21.spring.service.config.TestApplicationConfig;

public class UsersServiceImplTest {
    ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestApplicationConfig.class);
    private UsersServiceImpl usersService = (UsersServiceImpl)applicationContext.getBean("usersService");

    @Test
    public void testFailedSingUp(){
        Assert.assertNull(usersService.signUp("email3"));
    }

    @Test
    public void testSucceededSingUp(){
        Assert.assertNotNull(usersService.signUp("email6"));
    }
}
