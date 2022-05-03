package com.vaadin.karibu;

import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.PrettyPrintTree;
import com.github.mvysny.kaributesting.v10.Routes;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.karibu.locator.ComponentLocator;



public abstract class KaribuTest {

    private Routes routes;
    private ClassInfoList handlesListeners;

    @Rule
    public final ExternalResource resource = new ExternalResource() {
        @Override
        protected void before() {
            routes = new Routes().autoDiscoverViews(scanPackage());
            final ClassGraph classGraph = new ClassGraph().enableClassInfo()
                    .enableAnnotationInfo()
                    .acceptPackages("com.vaadin.karibu.locator");
            handlesListeners = classGraph.scan(2)
                    .getClassesWithAnnotation(HandlesTypes.class.getName());
        };

    };

    @Before
    public void init() {
        MockVaadin.setup(getRoutes(), UI::new);
    }

    // this needs to be in testWatcher finished else we will clean the UI before
    // we get the change to generate the tree.
//    @After
//    public void clean() {
//        MockVaadin.tearDown();
//    }

    @Rule
    public TestRule watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            final String prettyPrintTree = PrettyPrintTree.Companion.ofVaadin(
                    UI.getCurrent()).print();
            LoggerFactory.getLogger(PrettyTreeExtension.class)
                    .error("Test {}::{} failed with the tree:\n{}",
                            description.getTestClass(),
                            description.getMethodName(), prettyPrintTree);
        }

        @Override
        protected void finished(Description description) {
            MockVaadin.tearDown();
        }
    };

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
