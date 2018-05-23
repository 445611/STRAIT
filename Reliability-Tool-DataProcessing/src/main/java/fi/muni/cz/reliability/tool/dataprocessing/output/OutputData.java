package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.models.ModelOutputData;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class OutputData implements Serializable {
    
    private String url;
    private String userName;
    private String repositoryName;
    private Date createdAt;
    private int totalNumberOfDefects;
    private Date startOfTesting;
    private Date endOfTesting;
    private List<Pair<Integer, Integer>> weeksAndDefects;
    
    private Map<String, Double> modelParameters;
    private String modelName;
    private String modelFunction;
    private ModelOutputData modelData;

    public Date getStartOfTesting() {
        return startOfTesting;
    }

    public void setStartOfTesting(Date startOfTesting) {
        this.startOfTesting = startOfTesting;
    }

    public Date getEndOfTesting() {
        return endOfTesting;
    }

    public void setEndOfTesting(Date endOfTesting) {
        this.endOfTesting = endOfTesting;
    }

    public String getModelFunction() {
        return modelFunction;
    }

    public void setModelFunction(String modelFunction) {
        this.modelFunction = modelFunction;
    }
    
    public ModelOutputData getModelData() {
        return modelData;
    }

    public void setModelData(ModelOutputData modelData) {
        this.modelData = modelData;
    }

    public Map<String, Double> getModelParameters() {
        return modelParameters;
    }

    public void setModelParameters(Map<String, Double> parameters) {
        this.modelParameters = parameters;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getTotalNumberOfDefects() {
        return totalNumberOfDefects;
    }

    public void setTotalNumberOfDefects(int totalNumberOfDefects) {
        this.totalNumberOfDefects = totalNumberOfDefects;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Pair<Integer, Integer>> getWeeksAndDefects() {
        return weeksAndDefects;
    }

    public void setWeeksAndDefects(List<Pair<Integer, Integer>> weeksAndDefects) {
        this.weeksAndDefects = weeksAndDefects;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userName, this.repositoryName, this.createdAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof OutputData) {
            return false;
        }
        final OutputData other = (OutputData) obj;
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        if (!Objects.equals(this.repositoryName, other.repositoryName)) {
            return false;
        }
        if (!Objects.equals(this.createdAt, other.createdAt)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OutputData for repository{" + "userName=" 
                + userName + ", repositoryName=" + repositoryName 
                + ", createdAt=" + createdAt + ", totalNumberOfDefects=" 
                + totalNumberOfDefects + '}';
    }
}
