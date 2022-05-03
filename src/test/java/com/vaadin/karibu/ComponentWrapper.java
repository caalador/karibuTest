package com.vaadin.karibu;

import com.vaadin.flow.component.Component;

@FunctionalInterface
public interface ComponentWrapper {

    public Component getComponent();
}
