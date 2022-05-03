package com.vaadin.karibu.locator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.github.mvysny.kaributesting.v10.HasValueUtilsKt;
import com.github.mvysny.kaributesting.v10.LocatorKt;
import com.github.mvysny.kaributesting.v10.SearchSpec;
import kotlin.Unit;
import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.karibu.ComponentWrapper;

public class ComponentLocator<T extends Component> implements ComponentWrapper {

    private LocatorSpec<T> locatorSpec = new LocatorSpec<>();

    private T component;
    private Class<T> clazz;
    protected Component parentComponent;

    public ComponentLocator(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ComponentLocator<T> first() {
        final List<T> all = all();
        Assert.assertFalse("No component of type " + getClazz() + " was found.",
                all.isEmpty());
        component = all.get(0);//find(getClazz());
        return this;
    }

    public ComponentLocator<T> id(String id) {
        locatorSpec.id = id;
        find(getClazz());
        return this;
    }

    public ComponentLocator<T> withCaption(String caption) {
        locatorSpec.caption = caption;
        first();
        return this;
    }

    public <V, E extends HasValue.ValueChangeEvent<V>> ComponentLocator setValue(
            @Nullable V value) {
        HasValueUtilsKt.set_value((HasValue<E, V>) getComponent(), value);
        return this;
    }

    public List<T> all() {
        if (parentComponent != null) {
            return LocatorKt._find(parentComponent, clazz,
                    spec -> locatorSpec.populate(spec));
        }
        return LocatorKt._find(clazz, spec -> locatorSpec.populate(spec));
    }

    /**
     * Get new locator that will search from this locator class and down.
     *
     * @param clazz
     *         class to find
     * @param <Z>
     *         class type
     * @return ComponentLocator with results from this locator component
     */
    public <Z extends ComponentLocator> Z $(Class<Z> clazz) {
        Z z = null;
        try {
            z = clazz.getDeclaredConstructor().newInstance();
            z.parentComponent = component;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return z;
    }

    protected void find(Class<T> clazz) {
        if (parentComponent != null) {
            find(parentComponent, clazz);
        } else {
            component = LocatorKt._get(clazz,
                    spec -> locatorSpec.populate(spec));
        }
    }

    protected void find(@NotNull Component receiver, @NotNull Class<T> clazz) {
        component = LocatorKt._get(receiver, clazz,
                spec -> locatorSpec.populate(spec));
    }

    @Override
    public T getComponent() {
        if (component == null) {
            throw new RuntimeException(
                    "No component has been gotten use first() or find() to find a component");
        }
        return component;
    }

    protected Class<T> getClazz() {
        return clazz;
    }

    protected boolean isUsable() {
        Component component = getComponent();
        return component.getElement().isEnabled() && component.isAttached()
                && isEffectivelyVisible(component);
    }

    protected boolean isEffectivelyVisible(Component component) {
        return component.isVisible() && (!component.getParent().isPresent()
                || isEffectivelyVisible(component.getParent().get()));
    }

    private static class LocatorSpec<T extends Component> {

        public String id;
        public String caption;
        public String placeholder;
        public String text;
        public IntRange count = new IntRange(0, Integer.MAX_VALUE);
        public Object value;
        public String classes;
        public String withoutClasses;
        public List<Predicate<T>> predicates = new ArrayList<>(0);

        public Unit populate(SearchSpec<T> spec) {
            if (id != null)
                spec.setId(id);
            if (caption != null)
                spec.setCaption(caption);
            if (placeholder != null)
                spec.setPlaceholder(placeholder);
            if (text != null)
                spec.setText(text);
            if (value != null)
                spec.setValue(value);
            if (classes != null)
                spec.setClasses(classes);
            if (withoutClasses != null)
                spec.setWithoutClasses(withoutClasses);
            spec.setCount(count);
            spec.getPredicates().addAll(predicates);

            return Unit.INSTANCE;
        }

    }
}
