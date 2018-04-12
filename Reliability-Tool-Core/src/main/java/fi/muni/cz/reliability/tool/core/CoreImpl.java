package fi.muni.cz.reliability.tool.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class CoreImpl implements Core {
    
    private final ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    @Override
    public void calculateReliabilityForUrl(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
        //TODO
    }

}
