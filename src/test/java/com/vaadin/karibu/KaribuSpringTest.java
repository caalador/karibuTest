package com.vaadin.karibu;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.spring.MockSpringServlet;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public abstract class KaribuSpringTest extends KaribuTest {

    @Autowired
    private ApplicationContext ctx;

    @Override
    public void init() {
        SpringServlet servlet = new MockSpringServlet(getRoutes(), ctx,
                UI::new);
        MockVaadin.setup(UI::new, servlet);
    }
}
