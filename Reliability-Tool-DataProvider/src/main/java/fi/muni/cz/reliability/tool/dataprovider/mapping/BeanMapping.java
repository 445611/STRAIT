package fi.muni.cz.reliability.tool.dataprovider.mapping;

import java.util.Collection;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface BeanMapping {
    
    /**
     * Method for mapping collection
     * @param objects collection of objects
     * @param mapToClass desired class
     * @param <T> Type
     * @return mapped collection
     */
    <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass);
}
