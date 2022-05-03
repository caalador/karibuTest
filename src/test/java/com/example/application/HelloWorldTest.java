package com.example.application;

import com.example.application.views.helloworld.HelloWorldView;

import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.karibu.locator.ButtonLocator;
import com.vaadin.karibu.locator.ComponentLocator;
import com.vaadin.karibu.KaribuTest;
import com.vaadin.karibu.locator.NotificationLocator;
import com.vaadin.karibu.locator.TextFieldLocator;

import org.junit.Assert;
import org.junit.Test;

public class HelloWorldTest extends KaribuTest {

    @Override
    public String scanPackage() {
        return "com.example.application";
    }

    @Test
    public void typeName_clickButton() {
        new TextFieldLocator().withCaption("Your name").setValue("test");
        new ButtonLocator().withCaption("Say hello").click();

        Assert.assertEquals(1, new NotificationLocator().all().size());
        Assert.assertEquals("Hello test", new NotificationLocator().first().getText());

        Assert.assertEquals(3, new ButtonLocator<>().all().size());

        Assert.assertEquals(1, new TextFieldLocator<>(EmailField.class).all().size());
    }

    @Test
    public void typeName_clickButton_locators() {
        System.out.println(xx(MyTextField.class).getClass());
        $(TextFieldLocator.class).withCaption("Your name").setValue("test");
        $(ButtonLocator.class).withCaption("Say hello").click();

        Assert.assertEquals(1, $(NotificationLocator.class).all().size());
        Assert.assertEquals("Hello test", $(NotificationLocator.class).first().getText());

        // 2 buttons in HelloWorldLayout
        Assert.assertEquals(2, new ComponentLocator<>(HelloWorldView.class).first().$(ButtonLocator.class).all().size());
        // 3 buttons in full layout
        Assert.assertEquals("DrawerToggle extends Button", 3, $(ButtonLocator.class).all().size());

        Assert.assertEquals(1, new TextFieldLocator<>(EmailField.class).all().size());
    }

    @Test
    public void failingTest_seeMessagesForComponentTree() {
        $(TextFieldLocator.class).withCaption("Your name").setValue("test");
        $(ButtonLocator.class).withCaption("Say hello").click();

        Assert.assertEquals(2, $(NotificationLocator.class).all().size());
    }

//    @Test
//    public void typeName_clickButton_existing() {
//        TextField name = LocatorJ._get(TextField.class,
//                spec -> spec.withCaption("Your name"));
//        LocatorJ._setValue(name, "test");
//        LocatorJ._click(
//                LocatorJ._get(Button.class, s -> s.withCaption("Say hello")));
//        LocatorJ._assert(Notification.class, 1);
//    }
}
