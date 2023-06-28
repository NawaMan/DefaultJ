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

import defaultj.annotations.DefaultInterface;

/**
 * This annotation process ensures that {@link DefaultInterface} is only annotated
 *   to an interface with all default methods.
 * 
 * @author NawaMan -- nawa@nawaman.net
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
    
    private class InterfaceChecker {
        
        private Element orgInterface;
        
        private final Map<String, String> abstracts = new TreeMap<String, String>();
        
        private final Set<String> defaults = new TreeSet<String>();
        
        public InterfaceChecker(Element orgInterface) {
            super();
            this.orgInterface = orgInterface;
        }
        
        private void ensureDefaultInterface() {
            ensureDefaultInterface(orgInterface);
            defaults.forEach(m -> abstracts.remove(m));
            abstracts.forEach((mthd, clzz)->{
                error(orgInterface, format( "Interface %s is annotated with @%s but has a non-default method: %s (%s)!",
                        orgInterface, DEFAULT_INTERFACE, mthd, clzz));
            });
        }
        
        private void ensureDefaultInterface(Element element) {
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