package ma.zrad.system.batch.common.annotations;

import ma.zrad.system.batch.common.pojo.TripValidationResult;

@FunctionalInterface
public interface TripValidationRule<T> {
    //Pair<Boolean, String> validate(T item);

    TripValidationResult validate(T item);
}
