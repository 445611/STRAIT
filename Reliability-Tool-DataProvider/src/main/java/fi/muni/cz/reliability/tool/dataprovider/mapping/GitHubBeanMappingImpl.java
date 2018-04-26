package fi.muni.cz.reliability.tool.dataprovider.mapping;

import java.util.Arrays;
import org.dozer.DozerBeanMapper;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GitHubBeanMappingImpl extends BeanMappingImpl {
    
    private static final String MAPPING_FILE = "github_dozer_mapping.xml";

    @Override
    DozerBeanMapper getDozerBeanMapper() {
        return new DozerBeanMapper(Arrays.asList(MAPPING_FILE));
    }
}
