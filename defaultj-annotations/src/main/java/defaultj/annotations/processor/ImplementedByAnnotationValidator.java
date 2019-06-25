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
package defaultj.annotations.processor;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import defaultj.annotations.ImplementedBy;

import static java.util.stream.Collectors.toList;

import lombok.val;

/**
 * This annotation process ensures that {@link ImplementedBy} parameter is compatible with the annotated class.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class ImplementedByAnnotationValidator extends AbstractProcessor {
    
    private ProcessingEnvironment processingEnv;
    private Messager messager;
    private boolean hasError;
    
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        messager = processingEnv.getMessager();
        this.processingEnv = processingEnv;
    }
    
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<String>();
        annotations.add(ImplementedBy.class.getCanonicalName());
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
        for (Element element : roundEnv.getElementsAnnotatedWith(ImplementedBy.class)) {
            val elementType = element.toString();
            
            val implementedBy = element.getAnnotation(ImplementedBy.class);
            val implementingTypeName = implementedBy.toString().replaceAll("^.*\\(value = (.*)\\.class\\)$", "$1");
            if (elementType.equals(implementingTypeName))
                error(element, format("%s is not a valid implementation of itself.", elementType));
            
            val implementingType   = processingEnv.getElementUtils().getTypeElement(implementingTypeName);
            val allCompatibleTypes = getAllCompatibleTypes(implementingType);
            
            if (!allCompatibleTypes.contains(elementType))
                error(element, format("%s is not compatible with %s", implementingTypeName, elementType));
        }
        return hasError;
    }
    
    private List<String> getAllCompatibleTypes(TypeElement implementingType) {
        List<String> allCompatibleTypes = new ArrayList<>();
        allCompatibleTypes.add(implementingType.getSuperclass().toString());
        allCompatibleTypes.addAll(implementingType.getInterfaces().stream().map(String::valueOf).collect(toList()));
        return allCompatibleTypes;
    }
    
}