package com.vaadin.karibu.locator;

import com.vaadin.flow.component.notification.Notification;

public class NotificationLocator<T extends Notification> extends ComponentLocator<T>{

    public NotificationLocator() {
        this((Class<T>) Notification.class);
    }

    public NotificationLocator(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public NotificationLocator<T> first() {
        super.first();
        return this;
    }

    public String getText() {
        return getComponent().getElement().getChild(0).getProperty("innerHTML");
    }
}
