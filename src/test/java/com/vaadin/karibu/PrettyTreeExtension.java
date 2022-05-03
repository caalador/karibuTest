package com.vaadin.karibu;

import com.github.mvysny.kaributesting.v10.PrettyPrintTree;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.UI;

public class PrettyTreeExtension implements AfterTestExecutionCallback {
    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        boolean testFailed = extensionContext.getExecutionException()
                .isPresent();
        if (testFailed) {
            final String prettyPrintTree = PrettyPrintTree.Companion.ofVaadin(
                    UI.getCurrent()).print();
            LoggerFactory.getLogger(PrettyTreeExtension.class)
                    .error("Test {}::{} failed with the tree:\n{}",
                            extensionContext.getTestClass().get()
                                    .getSimpleName(),
                            extensionContext.getTestMethod().get().getName(),
                            prettyPrintTree);
        }
    }
}
// Junit4 same functionality?
//    Logger logger = LoggerFactory.getLogger(TestCase.class);
//    @Rule public MethodRule watchman = new TestWatchman() {
//        public void failed(Throwable e, FrameworkMethod method) {
//          final String prettyPrintTree = PrettyPrintTree.Companion.ofVaadin(
//        UI.getCurrent()).print();
//            LoggerFactory.getLogger(PrettyTreeExtension.class)
//        .error("Test {}::{} failed with the tree:\n{}",
//        extensionContext.getTestClass().get()
//        .getSimpleName(),
//        extensionContext.getTestMethod().get().getName(),
//        prettyPrintTree);
//        }
//    };
