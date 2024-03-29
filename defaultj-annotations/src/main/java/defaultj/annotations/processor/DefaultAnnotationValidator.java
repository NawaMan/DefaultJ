//  MIT License
//  
//  Copyright (c) 2017-2023 Nawa Manusitthipol
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

import defaultj.annotations.Default;

/**
 * This annotation process ensures that {@link Default} is only annotated to public, static, final fields or methods.
 * 
 * @author NawaMan -- nawa@nawaman.net
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