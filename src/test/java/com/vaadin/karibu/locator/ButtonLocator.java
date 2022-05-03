package com.vaadin.karibu.locator;

import com.github.mvysny.kaributesting.v10.ButtonKt;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;

public class ButtonLocator<T extends Button> extends ComponentLocator<T>{

    public ButtonLocator() {
        this((Class<T>) Button.class);
    }

    public ButtonLocator(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public ButtonLocator<T> withCaption(String caption) {
        super.withCaption(caption);
        return this;
    }

    public ButtonLocator<T> click() {
        if(!isUsable()) {
            throw new IllegalArgumentException("Button is not usable");
        }
//        ButtonKt._click(getComponent());
        getComponent().click();
        return this;
    }
}
