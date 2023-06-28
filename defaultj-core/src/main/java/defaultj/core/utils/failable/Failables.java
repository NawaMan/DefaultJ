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
package defaultj.core.utils.failable;

import java.util.function.Predicate;

import defaultj.core.utils.failable.Failable.Consumer;
import defaultj.core.utils.failable.Failable.Function;
import defaultj.core.utils.failable.Failable.Function2;
import defaultj.core.utils.failable.Failable.Function3;
import defaultj.core.utils.failable.Failable.Function4;
import defaultj.core.utils.failable.Failable.Function5;
import defaultj.core.utils.failable.Failable.Runnable;
import defaultj.core.utils.failable.Failable.Supplier;

/**
 * Utility methods to create Failable from lambda.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class Failables {
    
    private Failables() {
    }
    
    //== of ==
    
    /**
     * Convenient factory method to allow lambda 
     * 
     * @param runnable  the runnable.
     * @return the failable runnable.
     * 
     * @param <T>  the type of the thrown exception.
     **/
    public static <T extends Throwable> Runnable<T> of(defaultj.core.utils.failable.Failable.Runnable<T> runnable) {
        return runnable;
    }
    
    /**
     * Convenient factory method to allow lambda
     *  
     * @param supplier the failable supplier
     * @return a failable supplier.
     * 
     * @param <V> the input data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V, T extends Throwable> Supplier<V, T> of(Supplier<V, T> supplier) {
        return supplier;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param consumer  the failable consumer.
     * @return  the failable consumer.
     * 
     * @param <V> the input data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V, T extends Throwable> Consumer<V, T> of(Consumer<V, T> consumer) {
        return consumer;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable function that return value and thorow nothing.
     * @return  the predicate.
     * 
     * @param <V> the input data type.
     * @param <T>  the type of the thrown runtime exception.
     **/
    public static <V, T extends Throwable> Predicate<V> predicate(Function<V, Boolean, T> function) {
        return predicate(function.gracefully());
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable function that return value and thorow nothing.
     * @return  the predicate.
     * 
     * @param <V> the input data type.
     **/
    public static <V> Predicate<V> predicate(java.util.function.Function<V, Boolean> function) {
        return input -> function.apply(input);
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable function.
     * @return  the failable function.
     * 
     * @param <V> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V, R, T extends Throwable> Function<V, R, T> of(Function<V, R, T> function) {
        return function;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable bifunction.
     * @return  the failable bifunction.
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V1, V2, R, T extends Throwable> Function2<V1, V2, R, T> of(Function2<V1, V2, R, T> function) {
        return function;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable bifunction.
     * @return  the failable bifunction.
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <V3> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V1, V2, V3, R, T extends Throwable> Function3<V1, V2, V3, R, T> of(Function3<V1, V2, V3, R, T> function) {
        return function;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable bifunction.
     * @return  the failable bifunction.
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <V3> the input data type.
     * @param <V4> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V1, V2, V3, V4, R, T extends Throwable> Function4<V1, V2, V3, V4, R, T> of(Function4<V1, V2, V3, V4, R, T> function) {
        return function;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable bifunction.
     * @return  the failable bifunction.
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <V3> the input data type.
     * @param <V4> the input data type.
     * @param <V5> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V1, V2, V3, V4, V5, R, T extends Throwable> Function5<V1, V2, V3, V4, V5, R, T> of(Function5<V1, V2, V3, V4, V5, R, T> function) {
        return function;
    }
    
    //== full name ==
    
    /**
     * Convenient factory method to allow lambda 
     * 
     * @param runnable  the runnable.
     * @return the failable runnable.
     * 
     * @param <T>  the type of the thrown exception.
     **/
    public static <T extends Throwable> Runnable<T> runnable(Runnable<T> runnable) {
        return runnable;
    }
    
    /**
     * Convenient factory method to allow lambda
     *  
     * @param supplier the failable supplier
     * @return a failable supplier.
     * 
     * @param <V> the input data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V, T extends Throwable> Supplier<V, T> supplier(Supplier<V, T> supplier) {
        return supplier;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param consumer  the failable consumer.
     * @return  the failable consumer.
     * 
     * @param <V> the input data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V, T extends Throwable> Consumer<V, T> consumer(Consumer<V, T> consumer) {
        return consumer;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable function.
     * @return  the failable function.
     * 
     * @param <V> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V, R, T extends Throwable> Function<V, R, T> function(Function<V, R, T> function) {
        return function;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable bifunction.
     * @return  the failable bifunction.
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V1, V2, R, T extends Throwable> Function2<V1, V2, R, T> function2(Function2<V1, V2, R, T> function) {
        return function;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable bifunction.
     * @return  the failable bifunction.
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <V3> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V1, V2, V3, R, T extends Throwable> Function3<V1, V2, V3, R, T> function3(Function3<V1, V2, V3, R, T> function) {
        return function;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable bifunction.
     * @return  the failable bifunction.
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <V3> the input data type.
     * @param <V4> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V1, V2, V3, V4, R, T extends Throwable> Function4<V1, V2, V3, V4, R, T> function4(Function4<V1, V2, V3, V4, R, T> function) {
        return function;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable bifunction.
     * @return  the failable bifunction.
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <V3> the input data type.
     * @param <V4> the input data type.
     * @param <V5> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V1, V2, V3, V4, V5, R, T extends Throwable> Function5<V1, V2, V3, V4, V5, R, T> function5(Function5<V1, V2, V3, V4, V5, R, T> function) {
        return function;
    }
    
    //== short name == -- "You can all sleep sound tonight ... I am not crazy." -- or not :-p
    
    /**
     * Convenient factory method to allow lambda 
     * 
     * @param runnable  the runnable.
     * @return the failable runnable.
     * 
     * @param <T>  the type of the thrown exception.
     **/
    public static <T extends Throwable> Runnable<T> r(Runnable<T> runnable) {
        return runnable;
    }
    
    /**
     * Convenient factory method to allow lambda
     *  
     * @param supplier the failable supplier
     * @return a failable supplier.
     * 
     * @param <V> the input data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V, T extends Throwable> Supplier<V, T> s(Supplier<V, T> supplier) {
        return supplier;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param consumer  the failable consumer.
     * @return  the failable consumer.
     * 
     * @param <V> the input data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V, T extends Throwable> Consumer<V, T> c(Consumer<V, T> consumer) {
        return consumer;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable function that return value and thorow nothing.
     * @return  the predicate.
     * 
     * @param <V> the input data type.
     * @param <T>  the type of the thrown runtime exception.
     **/
    public static <V, T extends RuntimeException> Predicate<V> p(Function<V, Boolean, T> function) {
        return input -> function.apply(input);
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable function.
     * @return  the failable function.
     * 
     * @param <V> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V, R, T extends Throwable> Function<V, R, T> f(Function<V, R, T> function) {
        return function;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable bifunction.
     * @return  the failable bifunction.
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V1, V2, R, T extends Throwable> Function2<V1, V2, R, T> f2(Function2<V1, V2, R, T> function) {
        return function;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable bifunction.
     * @return  the failable bifunction.
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <V3> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V1, V2, V3, R, T extends Throwable> Function3<V1, V2, V3, R, T> f3(Function3<V1, V2, V3, R, T> function) {
        return function;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable bifunction.
     * @return  the failable bifunction.
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <V3> the input data type.
     * @param <V4> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V1, V2, V3, V4, R, T extends Throwable> Function4<V1, V2, V3, V4, R, T> f4(Function4<V1, V2, V3, V4, R, T> function) {
        return function;
    }
    
    /**
     * Convenient factory method to allow lambda.
     * 
     * @param function  the failable bifunction.
     * @return  the failable bifunction.
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <V3> the input data type.
     * @param <V4> the input data type.
     * @param <V5> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    public static <V1, V2, V3, V4, V5, R, T extends Throwable> Function5<V1, V2, V3, V4, V5, R, T> f5(Function5<V1, V2, V3, V4, V5, R, T> function) {
        return function;
    }
    
    //== namespace classes ==
    
    public static final class Runnables {
        
        private Runnables() {}
        
        /**
         * Convenient factory method to allow lambda 
         * 
         * @param runnable  the runnable.
         * @return the failable runnable.
         * 
         * @param <T>  the type of the thrown exception.
         **/
        public static <T extends Throwable> Runnable<T> of(Runnable<T> runnable) {
            return runnable;
        }
        
    }
    
    public static final class Suppliers {
        
        private Suppliers() {}
        
        /**
         * Convenient factory method to allow lambda
         *  
         * @param supplier the failable supplier
         * @return a failable supplier.
         * 
         * @param <V> the input data type.
         * @param <T>  the type of the thrown exception.
         **/
        public static <V, T extends Throwable> Supplier<V, T> of(Supplier<V, T> supplier) {
            return supplier;
        }
        
    }
    
    public static final class Consumers {
        
        private Consumers() {}
        
        /**
         * Convenient factory method to allow lambda.
         * 
         * @param consumer  the failable consumer.
         * @return  the failable consumer.
         * 
         * @param <V> the input data type.
         * @param <T>  the type of the thrown exception.
         **/
        public static <V, T extends Throwable> Consumer<V, T> of(Consumer<V, T> consumer) {
            return consumer;
        }
        
    }
    
    public static final class Functions {
        
        private Functions() {}
        
        /**
         * Convenient factory method to allow lambda.
         * 
         * @param function  the failable function.
         * @return  the failable function.
         * 
         * @param <V> the input data type.
         * @param <R>  the returned data type.
         * @param <T>  the type of the thrown exception.
         **/
        public static <V, R, T extends Throwable> Function<V, R, T> of(Function<V, R, T> function) {
            return function;
        }
    }
    
    public static final class Function2s {
        
        private Function2s() {}
        
        /**
         * Convenient factory method to allow lambda.
         * 
         * @param function  the failable bifunction.
         * @return  the failable bifunction.
         * 
         * @param <V1> the input data type.
         * @param <V2> the input data type.
         * @param <R>  the returned data type.
         * @param <T>  the type of the thrown exception.
         **/
        public static <V1, V2, R, T extends Throwable> Function2<V1, V2, R, T> of(Function2<V1, V2, R, T> function) {
            return function;
        }
    }
    
    public static final class Function3s {
        
        private Function3s() {}
        
        /**
         * Convenient factory method to allow lambda.
         * 
         * @param function  the failable bifunction.
         * @return  the failable bifunction.
         * 
         * @param <V1> the input data type.
         * @param <V2> the input data type.
         * @param <V3> the input data type.
         * @param <R>  the returned data type.
         * @param <T>  the type of the thrown exception.
         **/
        public static <V1, V2, V3, R, T extends Throwable> Function3<V1, V2, V3, R, T> of(Function3<V1, V2, V3, R, T> function) {
            return function;
        }
    }
    
    public static final class Function4s {
        
        private Function4s() {}
        
        /**
         * Convenient factory method to allow lambda.
         * 
         * @param function  the failable bifunction.
         * @return  the failable bifunction.
         * 
         * @param <V1> the input data type.
         * @param <V2> the input data type.
         * @param <V3> the input data type.
         * @param <V4> the input data type.
         * @param <R>  the returned data type.
         * @param <T>  the type of the thrown exception.
         **/
        public static <V1, V2, V3, V4, R, T extends Throwable> Function4<V1, V2, V3, V4, R, T> of(Function4<V1, V2, V3, V4, R, T> function) {
            return function;
        }
    }
    
    public static final class Function5s {
        
        private Function5s() {}
        
        /**
         * Convenient factory method to allow lambda.
         * 
         * @param function  the failable bifunction.
         * @return  the failable bifunction.
         * 
         * @param <V1> the input data type.
         * @param <V2> the input data type.
         * @param <V3> the input data type.
         * @param <V4> the input data type.
         * @param <V5> the input data type.
         * @param <R>  the returned data type.
         * @param <T>  the type of the thrown exception.
         **/
        public static <V1, V2, V3, V4, V5, R, T extends Throwable> Function5<V1, V2, V3, V4, V5, R, T> of(Function5<V1, V2, V3, V4, V5, R, T> function) {
            return function;
        }
    }
    
}