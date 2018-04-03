package fi.muni.cz.reliability.tool.dataprovider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class BeanMappingImpl implements BeanMapping {
    
    @Autowired
    private DozerBeanMapper dozer;
    
    @Override
    public  <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass) {
        List<T> mappedCollection = new ArrayList<>();
        for (Object object : objects) {
            mappedCollection.add(dozer.map(object, mapToClass));
        }
        return mappedCollection;
    }
}
