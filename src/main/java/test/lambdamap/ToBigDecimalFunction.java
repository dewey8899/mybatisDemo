package test.lambdamap;

import java.math.BigDecimal;

/**
 * Created by deweydu
 * Date on 2019/9/29 16:49
 */
@FunctionalInterface
public interface ToBigDecimalFunction<T> {
    /**
     * Applies this function to the given argument.
     *
     * @param value
     *            the function argument
     * @return the function result
     */
    BigDecimal applyAsBigDecimal(T value);
}
