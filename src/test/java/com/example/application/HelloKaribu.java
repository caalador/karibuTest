package com.example.application;

import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.github.mvysny.kaributesting.v10.LocatorKt;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.karibu.locator.ComponentLocator;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//@SpringBootTest
//@DirtiesContext
public class HelloKaribu {

    private static Routes routes;

    @BeforeAll
    public static void discover() {
        routes = new Routes().autoDiscoverViews("com.example.application");
    }

    //    @Autowired
    //    private ApplicationContext ctx;

    @BeforeEach
    public void init() {
        //        SpringServlet servlet = new MockSpringServlet(routes, ctx, UI::new);
        //        MockVaadin.setup(UI::new, servlet);
        MockVaadin.setup(routes, UI::new);
    }

    @AfterEach
    public void clean() {
        MockVaadin.tearDown();
    }

    @Test
    public void typeName_clickButton_existing() {
        TextField name = LocatorJ._get(TextField.class,
                spec -> spec.withCaption("Your name"));
        LocatorJ._setValue(name, "test");
        LocatorJ._click(
                LocatorJ._get(Button.class, s -> s.withCaption("Say hello")));
        LocatorJ._assert(Notification.class, 2);
    }

    @Test
    public void aboutView_containsImage() {
        UI.getCurrent().navigate("about");

        // ?? Can we target image explicits to only get the first item using the spec?
//        final Image image = LocatorJ._get(Image.class, spec -> spec.withCount(1));

        Image image = LocatorJ._find(Image.class).get(0);

        Assert.assertEquals("images/empty-plant.png", image.getSrc());
        Assert.assertTrue("Alt text set", image.getAlt().isPresent());
        Assert.assertEquals("placeholder plant", image.getAlt().get());
        Assert.assertEquals("200px", image.getWidth());

    }

}
