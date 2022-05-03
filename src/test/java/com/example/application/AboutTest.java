package com.example.application;

import com.example.application.views.about.AboutView;
import org.junit.Assert;
import org.junit.Test;

import com.vaadin.flow.component.html.Image;
import com.vaadin.karibu.locator.ComponentLocator;
import com.vaadin.karibu.KaribuTest;

public class AboutTest extends KaribuTest {


    @Override
    public String scanPackage() {
        return "com.example.application";
    }

    @Test
    public void aboutView_containsImage() {
        navigate("about");
        final Image image = new ComponentLocator<>(Image.class).first()
                .getComponent();
        Assert.assertEquals("images/empty-plant.png", image.getSrc());
        Assert.assertTrue("Alt text should be set", image.getAlt().isPresent());
        Assert.assertEquals("placeholder plant", image.getAlt().get());
        Assert.assertEquals("200px", image.getWidth());
    }

    @Test
    public void getWithID() {
        navigate(AboutView.class);

        final Image image = new ComponentLocator<>(Image.class).id(
                "second").getComponent();

        Assert.assertEquals("second plant", image.getAlt().get());
    }
}
