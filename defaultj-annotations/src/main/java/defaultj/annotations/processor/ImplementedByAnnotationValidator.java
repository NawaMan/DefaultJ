//  MIT License
//  
//  Copyright (c) 2017-2019 Nawa Manusitthipol
//  
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//  
//  The above copyright notice and this permission notice shall be included in all
//  copies or substantial portions of the Software.
//  
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//  SOFTWARE.
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
            var elementType = element.toString();
            
            var implementedBy = element.getAnnotation(ImplementedBy.class);
            var implementingTypeName = implementedBy.toString().replaceAll("^.*\\(value = (.*)\\.class\\)$", "$1");
            if (elementType.equals(implementingTypeName))
                error(element, format("%s is not a valid implementation of itself.", elementType));
            
            var implementingType   = processingEnv.getElementUtils().getTypeElement(implementingTypeName);
            var allCompatibleTypes = getAllCompatibleTypes(implementingType);
            
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