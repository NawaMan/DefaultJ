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

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import dssb.objectprovider.annotations.Default;
import dssb.objectprovider.annotations.DefaultInterface;
import lombok.AllArgsConstructor;

/**
 * This annotation process ensures that {@link Default} is only annotated to public, static, final fields or methods.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class DefaultInterfaceAnnotationValidator extends AbstractProcessor {
    
    private static final String DEFAULT_INTERFACE = DefaultInterface.class.getSimpleName();
    
    private Messager messager;
    private boolean hasError;
    
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        messager = processingEnv.getMessager();
    }
    
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<String>();
        annotations.add(DefaultInterface.class.getCanonicalName());
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
        for (Element element : roundEnv.getElementsAnnotatedWith(DefaultInterface.class)) {
            if(ElementKind.INTERFACE.equals(element.getKind())) {
                new InterfaceChecker(element).ensureDefaultInterface();
            } else {
                error(element, format("Only interfaces can be annotated with @%s!", DEFAULT_INTERFACE));
            }
        }
        return hasError;
    }
    
    @AllArgsConstructor
    class InterfaceChecker {
        
        private Element orgInterface;
        
        private final Map<String, String> abstracts = new TreeMap<String, String>();
        
        private final Set<String> defaults = new TreeSet<String>();
        
        void ensureDefaultInterface() {
            ensureDefaultInterface(orgInterface);
            defaults.forEach(m -> abstracts.remove(m));
            abstracts.forEach((mthd, clzz)->{
                error(orgInterface, format( "Interface %s with @%s but has a non-default method: %s in interface %s!",
                        orgInterface, DEFAULT_INTERFACE, mthd, clzz));
            });
        }
        
        void ensureDefaultInterface(Element element) {
            for (Element enclosedElement : element.getEnclosedElements()) {
                if (enclosedElement.getModifiers().contains(Modifier.DEFAULT))
                     defaults.add(enclosedElement.toString().replaceAll(" default ", " "));
                else abstracts.put(enclosedElement.toString().replaceAll(" abstract ", " "), element.toString());
            }
            
            for (TypeMirror typeMirror : ((TypeElement)element).getInterfaces()) {
                Element interfaceElement = ((DeclaredType)typeMirror).asElement();
                ensureDefaultInterface(interfaceElement);
            }
        }
    }
    
}