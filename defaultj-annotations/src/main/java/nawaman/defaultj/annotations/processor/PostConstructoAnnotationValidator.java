//  ========================================================================
//  Copyright (c) 2017-2018 Nawapunth Manusitthipol (NawaMan).
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
package nawaman.defaultj.annotations.processor;

import static java.lang.String.format;
import static javax.lang.model.element.ElementKind.METHOD;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import nawaman.defaultj.annotations.PostConstruct;

/**
 * This annotation process ensures that {@link PostConstruct} is only annotated to a method that take to parameter.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class PostConstructoAnnotationValidator extends AbstractProcessor {
    
    private static final String POST_CONSTRCT = PostConstruct.class.getSimpleName();
    
    private Messager messager;
    private boolean hasError;
    
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        messager = processingEnv.getMessager();
    }
    
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<String>();
        annotations.add(PostConstruct.class.getCanonicalName());
        return annotations;
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    
    private void error(Element e, String msg) {
        hasError = true;
        messager.printMessage(Diagnostic.Kind.ERROR, msg, e);
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        hasError = false;
        for (Element element : roundEnv.getElementsAnnotatedWith(PostConstruct.class)) {
            if (METHOD.equals(element.getKind())) {
                System.out.println(((ExecutableElement)element).getParameters());
                if (0 != ((ExecutableElement)element).getParameters().size())
                    error(element, format("Only methods with no parameter can be annotated with @%s!", POST_CONSTRCT));
            } else {
                error(element, format("Only methods can be annotated with @%s!", POST_CONSTRCT));
            }
        }
        return hasError;
    }
    
}