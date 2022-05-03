package com.vaadin.karibu.locator;

import javax.servlet.annotation.HandlesTypes;

import com.github.mvysny.kaributesting.v10.HasValueUtilsKt;
import org.jetbrains.annotations.Nullable;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.GeneratedVaadinTextField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

@HandlesTypes({TextField.class, EmailField.class, PasswordField.class })
public class TextFieldLocator<T extends GeneratedVaadinTextField> extends ComponentLocator<T> {

    public TextFieldLocator() {
        this((Class<T>) TextField.class);
    }

    public TextFieldLocator(Class<T> clazz) {
        super(clazz);
    }

//    @Override
    public <V, E extends HasValue.ValueChangeEvent<V>> TextFieldLocator setValue(@Nullable V value) {
        if(!isUsable()) {
            throw new RuntimeException("Button is not usable");
        }
        getComponent().setValue(value);
        HasValueUtilsKt.set_value((HasValue<E, V>) getComponent(), value);
        return this;
    }

    @Override
    public TextFieldLocator withCaption(String caption) {
        super.withCaption(caption);
        return this;
    }

    @Override
    public T getComponent() {
        return super.getComponent();
    }
}
