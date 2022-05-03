package com.vaadin.karibu;

import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.karibu.locator.ComponentLocator;

@ExtendWith(PrettyTreeExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class KaribuTest {

    private Routes routes;
    private ClassInfoList handlesListeners;

    @BeforeAll
    public void discover() {
        routes = new Routes().autoDiscoverViews(scanPackage());
        final ClassGraph classGraph = new ClassGraph().enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages("com.vaadin.karibu.locator");
        handlesListeners = classGraph.scan(2)
                .getClassesWithAnnotation(HandlesTypes.class.getName());
    }

    @BeforeEach
    public void init() {
        MockVaadin.setup(getRoutes(), UI::new);
    }

    @AfterEach
    public void clean() {
        MockVaadin.tearDown();
    }

    public Routes getRoutes() {
        return routes;
    }

    public String scanPackage() {
        return "";
    }

    public void navigate(String location) {
        UI.getCurrent().navigate(location);
    }

    public void navigate(Class<? extends Component> navigationTarget) {
        UI.getCurrent().navigate(navigationTarget);
    }

    /**
     * Get an applicable ComponentLocator based on the component class.
     *
     * @param clazz
     *         component class to get locator for
     * @param <T>
     *         component
     * @return ComponentLocator for given component
     */
    public <T extends Component> ComponentLocator<T> xx(Class<T> clazz) {
        Optional<Class<?>> aClass = handlesListeners.filter(
                        classInfo -> classInfo.getAnnotationInfo(HandlesTypes.class)
                                .getParameterValues().toString()
                                .contains(clazz.getCanonicalName())).loadClasses()
                .stream().findFirst();
        if(aClass.isPresent()) {
            return $((Class<? extends ComponentLocator<T>>) aClass.get());
        }
        if(!Object.class.equals(clazz.getSuperclass())) {
            aClass = handlesListeners.filter(
                            classInfo -> classInfo.getAnnotationInfo(HandlesTypes.class)
                                    .getParameterValues().toString()
                                    .contains(clazz.getSuperclass().getCanonicalName())).loadClasses()
                    .stream().findFirst();
        }
        return $((Class<? extends ComponentLocator<T>>) aClass.get());
    }

    public <T extends ComponentLocator> T $(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
