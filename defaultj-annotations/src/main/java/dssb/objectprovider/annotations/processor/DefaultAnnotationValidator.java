//Copyright (c) 2017 Nawapunth Manusitthipol (NawaMan).
//------------------------------------------------------------------------
//All rights reserved. This program and the accompanying materials
//are made available under the terms of the Eclipse Public License v1.0
//and Apache License v2.0 which accompanies this distribution.
//
//  The Eclipse Public License is available at
//  http://www.eclipse.org/legal/epl-v10.html
//
//  The Apache License v2.0 is available at
//  http://www.opensource.org/licenses/apache2.0.php
//
//You may elect to redistribute this code under either of these licenses.
//========================================================================
package dssb.objectprovider.annotations.processor;

import static java.lang.String.format;
import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.lang.model.element.ElementKind.ENUM_CONSTANT;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.ElementKind.METHOD;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import dssb.objectprovider.annotations.Default;

/**
 * This annotation process ensures that {@link Default} is only annotated to public, static, final fields or methods.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class DefaultAnnotationValidator extends AbstractProcessor {
    
    private static final String               DEFAULT           = Default.class.getSimpleName();
    private static final EnumSet<ElementKind> FIELDS_OR_METHODS = EnumSet.of(FIELD, METHOD);
    
    private Messager messager;
    private boolean hasError;
    
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        messager = processingEnv.getMessager();
    }
    
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<String>();
        annotations.add(Default.class.getCanonicalName());
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
        for (Element element : roundEnv.getElementsAnnotatedWith(Default.class)) {
            if (FIELDS_OR_METHODS.contains(element.getKind())) {
                ensureModifier(element, Modifier.PUBLIC);
                ensureModifier(element, Modifier.STATIC);
                ensureModifier(element, Modifier.FINAL);
            } else if(CONSTRUCTOR.equals(element.getKind())) {
                ensureModifier(element, Modifier.PUBLIC);
            } else if (!element.getKind().equals(ENUM_CONSTANT)) {
                error(element, format("Only constructors, enum constants, fields or methods can be annotated with @%s!", DEFAULT));
            }
        }
        return hasError;
    }

    private void ensureModifier(Element element, Modifier modifier) {
        if (element.getModifiers().contains(modifier))
            return;
        
        error(element, format( "Only %s element can be annotated with @%s!", modifier, DEFAULT));
    }
    
}