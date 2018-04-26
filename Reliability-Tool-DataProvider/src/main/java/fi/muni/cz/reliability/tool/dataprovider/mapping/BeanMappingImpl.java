package fi.muni.cz.reliability.tool.dataprovider.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.dozer.DozerBeanMapper;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public abstract class BeanMappingImpl implements BeanMapping {
    
    @Override
    public  <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass) {
        List<T> mappedCollection = new ArrayList<>();
        for (Object object : objects) {
            mappedCollection.add(getDozerBeanMapper().map(object, mapToClass));
        }
        return mappedCollection;
    }
    
    /**
     * Get {@link org.dozer.DozerBeanMapper DozerBeanMapper} with specified
     * mapping configuration file .
     * 
     * @return {@link org.dozer.DozerBeanMapper DozerBeanMapper}
     */
    abstract DozerBeanMapper getDozerBeanMapper();
}
