package fi.muni.cz.reliability.tool.dataprovider.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.dozer.DozerBeanMapper;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class BeanMappingImpl implements BeanMapping {
    
    private static final String MAPPING_FILE = "dozer_mapping.xml";
    private final DozerBeanMapper dozer;

    /**
     * Initialize <code>dozer</code> with <code>MAPPING_FILE</code> 
     */
    public BeanMappingImpl() {
        dozer = new DozerBeanMapper(Arrays.asList(MAPPING_FILE));
    }
    
    @Override
    public  <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass) {
        List<T> mappedCollection = new ArrayList<>();
        for (Object object : objects) {
            mappedCollection.add(dozer.map(object, mapToClass));
        }
        return mappedCollection;
    }
}
