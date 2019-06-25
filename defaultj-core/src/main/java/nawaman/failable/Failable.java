//  Copyright (c) 2017 Nawapunth Manusitthipol (NawaMan).
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
package nawaman.failable;

import lombok.val;

/**
 * Failable actions.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class Failable {
    
    private Failable() {
    }
    
    /**
     * Failable runnable.
     *   
     * @param <T> the throwable type.
     **/
    @FunctionalInterface
    public static interface Runnable<T extends Throwable> {
        
        /**
         * Run this runnable.
         * 
         * @throws T the thrown exception.
         **/
        public void run() throws T;
        
        /**
         * Run this runnable. 
         * 
         * @throws T  the thrown exception.
         **/
        public default void perform() throws T {
            run();
        }
        
        /**
         * Run this runnable. 
         * 
         * @throws T  the thrown exception.
         **/
        public default void process() throws T {
            run();
        }
        
        
        /**
         * Change to regular runnable.
         * 
         * @return Java's Runnable.
         **/
        public default java.lang.Runnable toRunnable() {
            return gracefully();
        }
        
        /**
         * Convert to a regular runnable and throw FailableException if there is an exception.
         * 
         * @return Java's Runnable.
         **/
        public default java.lang.Runnable gracefully() {
            return () -> {
                try {
                    run();
                } catch (FailableException t) {
                    throw t;
                } catch (Throwable t) {
                    throw new FailableException(t);
                }
            };
        }
        
        /**
         * Convert to a regular runnable that completely ignore the exception throw from it.
         * 
         * @return Java's Runnable.
         **/
        public default java.lang.Runnable carelessly() {
            return () -> {
                try {
                    run();
                } catch (Throwable t) {
                }
            };
        }
        
    }
    
    /**
     * Failable consumer.
     * 
     * @param <V>  the type of the return value.
     * @param <T>  the type of the thrown exception.
     **/
    @FunctionalInterface
    public static interface Supplier<V, T extends Throwable> {
        
        /**
         * Run this supplier. 
         * 
         * @return  the result value.
         * @throws T the thrown exception.
         **/
        public V get() throws T;
        
        /**
         * Run this supplier. 
         * 
         * @return  the result value.
         * @throws T  the thrown exception.
         **/
        public default V value() throws T {
            return get();
        }
        
        /**
         * Convert to a regular supplier and throw FailableException if there is an exception.
         *  
         * @return  Java Supplier.
         **/
        public default java.util.function.Supplier<V> toSupplier() {
            return gracefully();
        }
        
        /**
         * Convert to a runnable.
         *  
         * @return  a Failable Runnable.
         **/
        public default Runnable<T> asRunnable() {
            return ()->this.get();
        }
        
        /**
         * Convert to a regular supplier and throw FailableException if there is an exception. 
         * 
         * @return   Java Supplier.
         **/
        public default java.util.function.Supplier<V> gracefully() {
            return () -> {
                try {
                    return get();
                } catch (FailableException t) {
                    throw t;
                } catch (Throwable t) {
                    throw new FailableException(t);
                }
            };
        }
        
        /**
         * Convert to a regular supplier that completely ignore the exception throw from it. 
         * 
         * @return   Java Supplier.
         **/
        public default java.util.function.Supplier<V> carelessly() {
            return () -> {
                try {
                    return get();
                } catch (Throwable t) {
                    return null;
                }
            };
        }
    }
    
    /**
     * Failable consumer. 
     * 
     * @param <V>  the value data type.
     * @param <T>  the type of the thrown exception.
     **/
    @FunctionalInterface
    public static interface Consumer<V, T extends Throwable> {
        
        /**
         * Run this consumer. 
         * 
         * @param value the accepted value.
         * @throws T  the thrown exception.
         **/
        public void accept(V value) throws T;
        
        /**
         * Run this consumer. 
         * 
         * @param value  the first input value.
         * @throws T  the thrown exception.
         **/
        public default void perform(V value) throws T {
            accept(value);
        }
        
        /**
         * Run this consumer. 
         * 
         * @param value  the first input value.
         * @throws T  the thrown exception.
         **/
        public default void process(V value) throws T {
            accept(value);
        }
        
        /**
         * Run this consumer. 
         * 
         * @param value  the first input value.
         * @throws T  the thrown exception.
         **/
        public default void take(V value) throws T {
            accept(value);
        }
        
        /**
         * Convert to a regular consumer and throw FailableException if there is an exception.
         * 
         * @return  Java Consumer.
         **/
        public default java.util.function.Consumer<V> toConsumer() {
            return gracefully();
        }
        
        /**
         * Convert to a runnable by currying the given value.
         * 
         * @param value 
         *          the value to be used.
         * @return  Failable runnable.
         **/
        public default Runnable<T> asRunnableFor(V value) {
            return ()->this.accept(value);
        }
        
        /**
         * Convert to a runnable by currying the given value.
         * 
         * @param supplier 
         *          the supplier for the value to be used.
         * @return  Failable runnable.
         **/
        public default Runnable<T> asRunnableFor(java.util.function.Supplier<V> supplier) {
            return ()->{
                val value = supplier.get();
                this.accept(value);
            };
        }
        
        /**
         * Convert to a regular consumer and throw FailableException if there is an exception. 
         * 
         * @return  Java Consumer.
         **/        
        public default java.util.function.Consumer<V> gracefully() {
            return v -> {
                try {
                    accept(v);
                } catch (FailableException t) {
                    throw t;
                } catch (Throwable t) {
                    throw new FailableException(t);
                }
            };
        }
        
        /**
         * Convert to a regular consumer that completely ignore the exception throw from it. 
         * 
         * @return  Java Consumer.
         **/
        public default java.util.function.Consumer<V> carelessly() {
            return v -> {
                try {
                    accept(v);
                } catch (Throwable t) {
                }
            };
        }
    }
    
    /**
     * Failable function.
     * 
     * @param <V>  the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    @FunctionalInterface
    public static interface Function<V, R, T extends Throwable> {
        
        /**
         * Run this function. 
         * 
         * @param value  the input value.
         * @return  the returned value.
         * @throws T  the thrown exception.
         **/
        public R apply(V value) throws T;
        
        /**
         * Run this function. 
         * 
         * @param value  the first input value.
         * @return  the returned value.
         * @throws T  the thrown exception.
         **/
        public default R of(V value) throws T {
            return apply(value);
        }
        
        /**
         * Convert to a regular function and throw FailableException if there is an exception. 
         * 
         * @return  Java Function.
         **/
        public default java.util.function.Function<V, R> toFunction() {
            return gracefully();
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param value 
         *          the value to be used.
         * @return  Failable runnable.
         **/
        public default Supplier<R, T> asSupplierFor(V value) {
            return ()->this.apply(value);
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param supplier 
         *          the supplier for the value to be used.
         * @return  Failable runnable.
         **/
        public default Supplier<R, T> asSupplierFor(java.util.function.Supplier<V> supplier) {
            return ()->{
                val value = supplier.get();
                return this.apply(value);
            };
        }
        
        /**
         * Convert to a consumer by currying the given value.
         * 
         * @return  Failable runnable.
         **/
        public default Consumer<V, T> asConsumerFor() {
            return value->{
                this.apply(value);
            };
        }
        
        /**
         * Create a new function that take the result of this function as input of the given function.
         * 
         * @param tail  the function to pipe to.
         * @return  the new function.
         * 
         * @param <R2> the return type of the tail function.
         * @param <T2> the exception type of the tail function.
         */
        public default <R2, T2 extends Throwable> Function<V, R2, RuntimeException> pipeTo(Function<R, R2, T2> tail) {
            return value-> {
                val mid = this.gracefully().apply(value);
                val end = tail.gracefully().apply(mid);
                return end;
            };
        }
        
        /**
         * Create a new function that take the result of this function as input of the given function.
         * 
         * @param tail  the function to pipe to.
         * @return  the new function.
         * 
         * @param <V2> the second input type of the tail function.
         * @param <R2> the return type of the tail function.
         * @param <T2> the exception type of the tail function.
         */
        public default <V2, R2, T2 extends Throwable> Function2<V, V2, R2, RuntimeException> pipeTo(Function2<R, V2, R2, T2> tail) {
            return (value, value2)-> {
                val mid = this.gracefully().apply(value);
                val end = tail.gracefully().apply(mid, value2);
                return end;
            };
        }
        
        /**
         * Convert to a regular function and throw FailableException if there is an exception. 
         * 
         * @return  Java Function.
         **/        
        public default java.util.function.Function<V, R> gracefully() {
            return v -> {
                try {
                    return apply(v);
                } catch (FailableException t) {
                    throw t;
                } catch (Throwable t) {
                    throw new FailableException(t);
                }
            };
        }
        
        /**
         * Convert to a regular function that completely ignore the exception throw from it. 
         * 
         * @return  Java Function.
         **/
        public default java.util.function.Function<V, R> carelessly() {
            return v -> {
                try {
                    return apply(v);
                } catch (Throwable t) {
                    return null;
                }
            };
        }
    }
    
    /**
     * Failable bi-function.
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    @FunctionalInterface
    public static interface Function2<V1, V2, R, T extends Throwable> {
        
        /**
         * Run this function. 
         * 
         * @param value1  the first input value.
         * @param value2  the second input value.
         * @return  the returned value.
         * @throws T  the thrown exception.
         **/
        public R apply(V1 value1, V2 value2) throws T;
        
        /**
         * Run this function. 
         * 
         * @param value1  the first input value.
         * @param value2  the second input value.
         * @return  the returned value.
         * @throws T  the thrown exception.
         **/
        public default R of(V1 value1, V2 value2) throws T {
            return apply(value1, value2);
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param value1
         *          the first value to be used.
         * @return  Failable function.
         **/
        public default Function<V2, R, T> of(V1 value1) {
            return asFunctionFor(value1);
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param value2
         *          the second value to be used.
         * @return  Failable function.
         **/
        public default Function<V1, R, T> of2(V2 value2) {
            return flip().asFunctionFor(value2);
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param supplier1
         *          the supplier of the first value to be used.
         * @return  Failable function.
         **/
        public default Function<V2, R, T> of(java.util.function.Supplier<V1> supplier1) {
            return asFunctionFor(supplier1);
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param supplier1
         *          the supplier of the first value to be used.
         * @return  Failable function.
         **/
        public default Function<V1, R, T> of2(java.util.function.Supplier<V2> supplier1) {
            return flip().asFunctionFor(supplier1);
        }
        
        /**
         * Run this function. 
         * 
         * @param value1  the first input value.
         * @param value2  the second input value.
         * @return  the returned value.
         * @throws T  the thrown exception.
         **/
        public default R between(V1 value1, V2 value2) throws T {
            return apply(value1, value2);
        }
        
        /**
         * Convert to a regular bi-function and throw FailableException if there is an exception. 
         * 
         * @return  Java Function.
         **/
        public default java.util.function.BiFunction<V1, V2, R> toBiFunction() {
            return gracefully();
        }
        
        /**
         * Swap the location of the parameters.
         * 
         * @return the new BiFunction with the parameters position swapped.
         */
        public default Function2<V2, V1, R, T> flip() {
            return (value2, value1)->{
                return this.apply(value1, value2);
            };
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param value1
         *          the first value to be used.
         * @return  Failable runnable.
         **/
        public default Function<V2, R, T> asFunctionFor(V1 value1) {
            return value2->{
                return this.apply(value1, value2);
            };
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param supplier1 
         *          the supplier for the value1 to be used.
         * @return  Failable runnable.
         **/
        public default Function<V2, R, T> asFunctionFor(java.util.function.Supplier<V1> supplier1) {
            return value2->{
                val value1 = supplier1.get();
                return this.apply(value1, value2);
            };
        }
        
        /**
         * Convert to a regular function and throw FailableException if there is an exception. 
         * 
         * @return  Java Function.
         **/        
        public default java.util.function.BiFunction<V1, V2, R> gracefully() {
            return (v1,v2) -> {
                try {
                    return apply(v1, v2);
                } catch (FailableException t) {
                    throw t;
                } catch (Throwable t) {
                    throw new FailableException(t);
                }
            };
        }
        
        /**
         * Convert to a regular function that completely ignore the exception throw from it. 
         * 
         * @return  Java Function.
         **/
        public default java.util.function.BiFunction<V1, V2, R> carelessly() {
            return (v1,v2) -> {
                try {
                    return apply(v1, v2);
                } catch (Throwable t) {
                    return null;
                }
            };
        }
    }
    
    /**
     * Failable tri-function (function with 3 parameters).
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <V3> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    @FunctionalInterface
    public static interface Function3<V1, V2, V3, R, T extends Throwable> {
        
        /**
         * Run this function. 
         * 
         * @param value1  the first input value.
         * @param value2  the second input value.
         * @param value3  the third input value.
         * @return  the returned value.
         * @throws T  the thrown exception.
         **/
        public R apply(V1 value1, V2 value2, V3 value3) throws T;
        
        /**
         * Run this function. 
         * 
         * @param value1  the first input value.
         * @param value2  the second input value.
         * @param value3  the third input value.
         * @return  the returned value.
         * @throws T  the thrown exception.
         **/
        public default R of(V1 value1, V2 value2, V3 value3) throws T {
            return apply(value1, value2, value3);
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param value1
         *          the first value to be used.
         * @return  Failable function.
         * @throws T  the thrown exception.
         **/
        public default Function2<V2, V3, R, T> of(V1 value1) throws T {
            return asFunctionFor(value1);
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param supplier1
         *          the supplier of the first value to be used.
         * @return  Failable function.
         * @throws T  the thrown exception.
         **/
        public default Function2<V2, V3, R, T> of(java.util.function.Supplier<V1> supplier1) throws T {
            return asFunctionFor(supplier1);
        }
        
        /**
         * Run this function. 
         * 
         * @param value1  the first input value.
         * @param value2  the second input value.
         * @param value3  the third input value.
         * @return  the returned value.
         * @throws T  the thrown exception.
         **/
        public default R between(V1 value1, V2 value2, V3 value3) throws T {
            return apply(value1, value2, value3);
        }
        
        /**
         * Swap the location of the first and second parameters.
         * 
         * @return the new BiFunction with the parameters position swapped.
         */
        public default Function3<V2, V1, V3, R, T> flip12() {
            return (value2, value1, value3)->{
                return this.apply(value1, value2, value3);
            };
        }
        
        /**
         * Swap the location of the first and third parameters.
         * 
         * @return the new BiFunction with the parameters position swapped.
         */
        public default Function3<V3, V2, V1, R, T> flip13() {
            return (value3, value2, value1)->{
                return this.apply(value1, value2, value3);
            };
        }
        
        /**
         * Swap the location of the first and third parameters.
         * 
         * @return the new BiFunction with the parameters position swapped.
         */
        public default Function3<V1, V3, V2, R, T> flip23() {
            return (value1, value3, value2)->{
                return this.apply(value1, value2, value3);
            };
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param value1
         *          the first value to be used.
         * @return  Failable runnable.
         **/
        public default Function2<V2, V3, R, T> asFunctionFor(V1 value1) {
            return (value2, value3)->{
                return this.apply(value1, value2, value3);
            };
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param supplier1 
         *          the supplier for the value1 to be used.
         * @return  Failable runnable.
         **/
        public default Function2<V2, V3, R, T> asFunctionFor(java.util.function.Supplier<V1> supplier1) {
            return (value2, value3)->{
                val value1 = supplier1.get();
                return this.apply(value1, value2, value3);
            };
        }
    }
    
    /**
     * Failable quadri-function (function with 4 parameters).
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <V3> the input data type.
     * @param <V4> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    @FunctionalInterface
    public static interface Function4<V1, V2, V3, V4, R, T extends Throwable> {
        
        /**
         * Run this function. 
         * 
         * @param value1  the first input value.
         * @param value2  the second input value.
         * @param value3  the third input value.
         * @param value4  the forth input value.
         * @return  the returned value.
         * @throws T  the thrown exception.
         **/
        public R apply(V1 value1, V2 value2, V3 value3, V4 value4) throws T;
        
        /**
         * Run this function. 
         * 
         * @param value1  the first input value.
         * @param value2  the second input value.
         * @param value3  the third input value.
         * @param value4  the forth input value.
         * @return  the returned value.
         * @throws T  the thrown exception.
         **/
        public default R of(V1 value1, V2 value2, V3 value3, V4 value4) throws T {
            return apply(value1, value2, value3, value4);
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param value1
         *          the first value to be used.
         * @return  Failable function.
         * @throws T  the thrown exception.
         **/
        public default Function3<V2, V3, V4, R, T> of(V1 value1) throws T {
            return asFunctionFor(value1);
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param supplier1
         *          the supplier of the first value to be used.
         * @return  Failable function.
         * @throws T  the thrown exception.
         **/
        public default Function3<V2, V3, V4, R, T> of(java.util.function.Supplier<V1> supplier1) throws T {
            return asFunctionFor(supplier1);
        }
        
        /**
         * Run this function. 
         * 
         * @param value1  the first input value.
         * @param value2  the second input value.
         * @param value3  the third input value.
         * @param value4  the forth input value.
         * @return  the returned value.
         * @throws T  the thrown exception.
         **/
        public default R between(V1 value1, V2 value2, V3 value3, V4 value4) throws T {
            return apply(value1, value2, value3, value4);
        }
        
        /**
         * Swap the location of the first and second parameters.
         * 
         * @return the new BiFunction with the parameters position swapped.
         */
        public default Function4<V2, V1, V3, V4, R, T> flip12() {
            return (value2, value1, value3, value4)->{
                return this.apply(value1, value2, value3, value4);
            };
        }
        
        /**
         * Swap the location of the first and third parameters.
         * 
         * @return the new BiFunction with the parameters position swapped.
         */
        public default Function4<V3, V2, V1, V4, R, T> flip13() {
            return (value3, value2, value1, value4)->{
                return this.apply(value1, value2, value3, value4);
            };
        }
        
        /**
         * Swap the location of the first and third parameters.
         * 
         * @return the new BiFunction with the parameters position swapped.
         */
        public default Function4<V1, V3, V2, V4, R, T> flip23() {
            return (value1, value3, value2, value4)->{
                return this.apply(value1, value2, value3, value4);
            };
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param value1
         *          the first value to be used.
         * @return  Failable runnable.
         **/
        public default Function3<V2, V3, V4, R, T> asFunctionFor(V1 value1) {
            return (value2, value3, value4)->{
                return this.apply(value1, value2, value3, value4);
            };
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param supplier1 
         *          the supplier for the value1 to be used.
         * @return  Failable runnable.
         **/
        public default Function3<V2, V3, V4, R, T> asFunctionFor(java.util.function.Supplier<V1> supplier1) {
            return (value2, value3, value4)->{
                val value1 = supplier1.get();
                return this.apply(value1, value2, value3, value4);
            };
        }
    }
    
    /**
     * Failable quinque-function (function with 5 parameters).
     * 
     * @param <V1> the input data type.
     * @param <V2> the input data type.
     * @param <V3> the input data type.
     * @param <V4> the input data type.
     * @param <V5> the input data type.
     * @param <R>  the returned data type.
     * @param <T>  the type of the thrown exception.
     **/
    @FunctionalInterface
    public static interface Function5<V1, V2, V3, V4, V5, R, T extends Throwable> {
        
        /**
         * Run this function. 
         * 
         * @param value1  the first input value.
         * @param value2  the second input value.
         * @param value3  the third input value.
         * @param value4  the forth input value.
         * @param value5  the fifth input value.
         * @return  the returned value.
         * @throws T  the thrown exception.
         **/
        public R apply(V1 value1, V2 value2, V3 value3, V4 value4, V5 value5) throws T;
        
        /**
         * Run this function. 
         * 
         * @param value1  the first input value.
         * @param value2  the second input value.
         * @param value3  the third input value.
         * @param value4  the forth input value.
         * @param value5  the fifth input value.
         * @return  the returned value.
         * @throws T  the thrown exception.
         **/
        public default R of(V1 value1, V2 value2, V3 value3, V4 value4, V5 value5) throws T {
            return apply(value1, value2, value3, value4, value5);
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param value1
         *          the first value to be used.
         * @return  Failable function.
         * @throws T  the thrown exception.
         **/
        public default Function4<V2, V3, V4, V5, R, T> of(V1 value1) throws T {
            return asFunctionFor(value1);
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param supplier1
         *          the supplier of the first value to be used.
         * @return  Failable function.
         * @throws T  the thrown exception.
         **/
        public default Function4<V2, V3, V4, V5, R, T> of(java.util.function.Supplier<V1> supplier1) throws T {
            return asFunctionFor(supplier1);
        }
        
        /**
         * Run this function. 
         * 
         * @param value1  the first input value.
         * @param value2  the second input value.
         * @param value3  the third input value.
         * @param value4  the forth input value.
         * @param value5  the fifth input value.
         * @return  the returned value.
         * @throws T  the thrown exception.
         **/
        public default R between(V1 value1, V2 value2, V3 value3, V4 value4, V5 value5) throws T {
            return apply(value1, value2, value3, value4, value5);
        }
        
        /**
         * Swap the location of the first and second parameters.
         * 
         * @return the new BiFunction with the parameters position swapped.
         */
        public default Function5<V2, V1, V3, V4, V5, R, T> flip12() {
            return (value2, value1, value3, value4, value5)->{
                return this.apply(value1, value2, value3, value4, value5);
            };
        }
        
        /**
         * Swap the location of the first and third parameters.
         * 
         * @return the new BiFunction with the parameters position swapped.
         */
        public default Function5<V3, V2, V1, V4, V5, R, T> flip13() {
            return (value3, value2, value1, value4, value5)->{
                return this.apply(value1, value2, value3, value4, value5);
            };
        }
        
        /**
         * Swap the location of the first and third parameters.
         * 
         * @return the new BiFunction with the parameters position swapped.
         */
        public default Function5<V1, V3, V2, V4, V5, R, T> flip23() {
            return (value1, value3, value2, value4, value5)->{
                return this.apply(value1, value2, value3, value4, value5);
            };
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param value1
         *          the first value to be used.
         * @return  Failable runnable.
         **/
        public default Function4<V2, V3, V4, V5, R, T> asFunctionFor(V1 value1) {
            return (value2, value3, value4, value5)->{
                return this.apply(value1, value2, value3, value4, value5);
            };
        }
        
        /**
         * Convert to a supplier by currying the given value.
         * 
         * @param supplier1 
         *          the supplier for the value1 to be used.
         * @return  Failable runnable.
         **/
        public default Function4<V2, V3, V4, V5, R, T> asFunctionFor(java.util.function.Supplier<V1> supplier1) {
            return (value2, value3, value4, value5)->{
                val value1 = supplier1.get();
                return this.apply(value1, value2, value3, value4, value5);
            };
        }
    }
    
}
