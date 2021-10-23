package fi.muni.cz.dataprocessing.output;

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
    private int initialNumberOfIssues;
    private int totalNumberOfDefects;
    private List<Pair<Integer, Integer>> cumulativeDefects;
    private List<Pair<Integer, Integer>> timeBetweenDefects;
    private String timeBetweenDefectsUnit;

    private String repositoryDescription;
    private int repositorySize;
    private int repositoryContributors;
    private int repositoryWatchers;
    private int repositoryForks;
    private Date repositoryCreatedAt;
    private Date repositoryLastPushedAt;
    private long developmentDays;

    private Map<String, Double> modelParameters;
    private List<Pair<Integer, Integer>> estimatedIssuesPrediction;
    private Map<String, String> goodnessOfFit;
    private boolean existTrend;
    private double trend;
    private String modelName;
    private String modelFunction;
    private String testingPeriodsUnit;
    private List<String> filtersUsed;
    private List<String> processorsUsed;
    private String solver;

    /**
     * Default constructor.
     */
    public OutputData() {
    }
    
    private OutputData(OutputDataBuilder builder) {
        this.url = builder.url;
        this.userName = builder.userName;
        this.repositoryName = builder.repositoryName;
        this.createdAt = builder.createdAt;
        this.initialNumberOfIssues = builder.initialNumberOfIssues;
        this.totalNumberOfDefects = builder.totalNumberOfDefects;
        this.cumulativeDefects = builder.cumulativeDefects;
        this.timeBetweenDefects = builder.timeBetweenDefects;
        this.timeBetweenDefectsUnit = builder.timeBetweenDefectsUnit;
        this.modelParameters = builder.modelParameters;
        this.estimatedIssuesPrediction = builder.estimatedIssuesPrediction;
        this.goodnessOfFit = builder.goodnessOfFit;
        this.existTrend = builder.existTrend;
        this.trend = builder.trend;
        this.modelName = builder.modelName;
        this.modelFunction = builder.modelFunction;
        this.testingPeriodsUnit = builder.testingPeriodsUnit;
        this.filtersUsed = builder.filtersUsed;
        this.processorsUsed = builder.processorsUsed;
        this.solver = builder.solver;
        this.repositoryContributors = builder.repositoryContributors;
        this.repositoryCreatedAt = builder.repositoryCreatedAt;
        this.repositoryDescription = builder.repositoryDescription;
        this.repositoryForks = builder.repositoryForks;
        this.repositoryLastPushedAt = builder.repositoryLastPushedAt;
        this.repositorySize = builder.repositorySize;
        this.repositoryWatchers = builder.repositoryWatchers;
        this.developmentDays = builder.developmentDays;
    }

    public String getSolver() {
        return solver;
    }

    public void setSolver(String solver) {
        this.solver = solver;
    }

    
    
    public String getTimeBetweenDefectsUnit() {
        return timeBetweenDefectsUnit;
    }

    public void setTimeBetweenDefectsUnit(String timeBetweenDefectsUnit) {
        this.timeBetweenDefectsUnit = timeBetweenDefectsUnit;
    }
    
    public String getTestingPeriodsUnit() {
        return testingPeriodsUnit;
    }

    public void setTestingPeriodsUnit(String testPeriodUnit) {
        this.testingPeriodsUnit = testPeriodUnit;
    }
    
    public int getInitialNumberOfIssues() {
        return initialNumberOfIssues;
    }

    public void setInitialNumberOfIssues(int initialNumberOfIssues) {
        this.initialNumberOfIssues = initialNumberOfIssues;
    }

    public List<String> getFiltersUsed() {
        return filtersUsed;
    }

    public void setFiltersUsed(List<String> filtersUsed) {
        this.filtersUsed = filtersUsed;
    }

    public List<String> getProcessorsUsed() {
        return processorsUsed;
    }

    public void setProcessorsUsed(List<String> processorsUsed) {
        this.processorsUsed = processorsUsed;
    }
    
    public boolean isExistTrend() {
        return existTrend;
    }

    public void setExistTrend(boolean existTrend) {
        this.existTrend = existTrend;
    }

    public List<Pair<Integer, Integer>> getTimeBetweenDefects() {
        return timeBetweenDefects;
    }

    public void setTimeBetweenDefects(List<Pair<Integer, Integer>> timeBetweenDefects) {
        this.timeBetweenDefects = timeBetweenDefects;
    }

    public double getTrend() {
        return trend;
    }

    public void setTrend(double trend) {
        this.trend = trend;
    }

    public List<Pair<Integer, Integer>> getEstimatedIssuesPrediction() {
        return estimatedIssuesPrediction;
    }

    public void setEstimatedIssuesPrediction(List<Pair<Integer, Integer>> estimatedIssuesPrediction) {
        this.estimatedIssuesPrediction = estimatedIssuesPrediction;
    }

    public Map<String, String> getGoodnessOfFit() {
        return goodnessOfFit;
    }

    public void setGoodnessOfFit(Map<String, String> goodnessOfFit) {
        this.goodnessOfFit = goodnessOfFit;
    }

    public String getModelFunction() {
        return modelFunction;
    }

    public void setModelFunction(String modelFunction) {
        this.modelFunction = modelFunction;
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

    public List<Pair<Integer, Integer>> getCumulativeDefects() {
        return cumulativeDefects;
    }

    public void setCumulativeDefects(List<Pair<Integer, Integer>> cumulativeDefects) {
        this.cumulativeDefects = cumulativeDefects;
    }

    public String getRepositoryDescription() {
        return repositoryDescription;
    }

    public void setRepositoryDescription(String repositoryDescription) {
        this.repositoryDescription = repositoryDescription;
    }

    public int getRepositorySize() {
        return repositorySize;
    }

    public void setRepositorySize(int repositorySize) {
        this.repositorySize = repositorySize;
    }

    public int getRepositoryContributors() {
        return repositoryContributors;
    }

    public void setRepositoryContributors(int repositoryContributors) {
        this.repositoryContributors = repositoryContributors;
    }

    public int getRepositoryWatchers() {
        return repositoryWatchers;
    }

    public void setRepositoryWatchers(int repositoryWatchers) {
        this.repositoryWatchers = repositoryWatchers;
    }

    public int getRepositoryForks() {
        return repositoryForks;
    }

    public void setRepositoryForks(int repositoryForks) {
        this.repositoryForks = repositoryForks;
    }

    public Date getRepositoryCreatedAt() {
        return repositoryCreatedAt;
    }

    public void setRepositoryCreatedAt(Date repositoryCreatedAt) {
        this.repositoryCreatedAt = repositoryCreatedAt;
    }

    public Date getRepositoryLastPushedAt() {
        return repositoryLastPushedAt;
    }

    public void setRepositoryLastPushedAt(Date repositoryLastPushedAt) {
        this.repositoryLastPushedAt = repositoryLastPushedAt;
    }

    public long getDevelopmentDays() {
        return developmentDays;
    }

    public void setDevelopmentDays(long developmentDays) {
        this.developmentDays = developmentDays;
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
    
    /**
     * Builder.
     */
    public static class OutputDataBuilder {
        private String url;
        private String userName;
        private String repositoryName;
        private Date createdAt;
        private int initialNumberOfIssues;
        private int totalNumberOfDefects;
        private List<Pair<Integer, Integer>> cumulativeDefects;
        private List<Pair<Integer, Integer>> timeBetweenDefects;
        private String timeBetweenDefectsUnit;

        private String repositoryDescription;
        private int repositorySize;
        private int repositoryContributors;
        private int repositoryWatchers;
        private int repositoryForks;
        private Date repositoryCreatedAt;
        private Date repositoryLastPushedAt;
        private long developmentDays;

        private Map<String, Double> modelParameters;
        private List<Pair<Integer, Integer>> estimatedIssuesPrediction;
        private Map<String, String> goodnessOfFit;
        private boolean existTrend;
        private double trend;
        private String modelName;
        private String modelFunction;
        private String testingPeriodsUnit;
        private List<String> filtersUsed;
        private List<String> processorsUsed;
        private String solver;
        
        /**
         * Set solver of models parameters.
         * 
         * @param solver models parameters.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setSolver(String solver) {
            this.solver = solver;
            return this;
        }
        
        /**
         * Set url of repository.
         * 
         * @param url url of repository.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * Set user name of repository.
         * 
         * @param userName url of repository.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        /**
         * Set repository name.
         * 
         * @param repositoryName url of repository.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setRepositoryName(String repositoryName) {
            this.repositoryName = repositoryName;
            return this;
        }

        /**
         * Set date when data was created.
         * 
         * @param createdAt date.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        /**
         * Set initial number of issues.
         * 
         * @param initialNumberOfIssues initial number.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setInitialNumberOfIssues(int initialNumberOfIssues) {
            this.initialNumberOfIssues = initialNumberOfIssues;
            return this;
        }

        /**
         * Set total number of defects.
         * 
         * @param totalNumberOfDefects total number.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setTotalNumberOfDefects(int totalNumberOfDefects) {
            this.totalNumberOfDefects = totalNumberOfDefects;
            return this;
        }

        /**
         * Set cumulative defects.
         * 
         * @param cumulativeDefects calculated cumulative data.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setCumulativeDefects(List<Pair<Integer, Integer>> cumulativeDefects) {
            this.cumulativeDefects = cumulativeDefects;
            return this;
        }

        /**
         * Set calculated time between defects.
         * 
         * @param timeBetweenDefects calculated time between defects.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setTimeBetweenDefects(List<Pair<Integer, Integer>> timeBetweenDefects) {
            this.timeBetweenDefects = timeBetweenDefects;
            return this;
        }

        /**
         * Set time unit between defects.
         * 
         * @param timeBetweenDefectsUnit time unit.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setTimeBetweenDefectsUnit(String timeBetweenDefectsUnit) {
            this.timeBetweenDefectsUnit = timeBetweenDefectsUnit;
            return this;
        }

        /**
         * Set model parameters.
         * 
         * @param modelParameters model parameters.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setModelParameters(Map<String, Double> modelParameters) {
            this.modelParameters = modelParameters;
            return this;
        }

        /**
         * Set estimated ssues prediction.
         * 
         * @param estimatedIssuesPrediction estimated issues prediction.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setEstimatedIssuesPrediction(List<Pair<Integer, Integer>> estimatedIssuesPrediction) {
            this.estimatedIssuesPrediction = estimatedIssuesPrediction;
            return this;
        }

        /**
         * Set goodness of fit data.
         * 
         * @param goodnessOfFit goodness of fit data.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setGoodnessOfFit(Map<String, String> goodnessOfFit) {
            this.goodnessOfFit = goodnessOfFit;
            return this;
        }

        /**
         * Set if exists trend.
         * 
         * @param existTrend true if exist trend, false otherwise.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setExistTrend(boolean existTrend) {
            this.existTrend = existTrend;
            return this;
        }

        /**
         * Set trend value.
         * 
         * @param trend trend value.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setTrend(double trend) {
            this.trend = trend;
            return this;
        }

        /**
         * Set name of mode.
         * 
         * @param modelName model name.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setModelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        /**
         * Set model function.
         * 
         * @param modelFunction model function.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setModelFunction(String modelFunction) {
            this.modelFunction = modelFunction;
            return this;
        }

        /**
         * Set testing periods unit.
         * 
         * @param testingPeriodsUnit testing periods unit.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setTestingPeriodsUnit(String testingPeriodsUnit) {
            this.testingPeriodsUnit = testingPeriodsUnit;
            return this;
        }

        /**
         * Set filters used.
         * 
         * @param filtersUsed list of filters used.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setFiltersUsed(List<String> filtersUsed) {
            this.filtersUsed = filtersUsed;
            return this;
        }

        /**
         * Set processors used.
         * 
         * @param processorsUsed list of processors used.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setProcessorsUsed(List<String> processorsUsed) {
            this.processorsUsed = processorsUsed;
            return this;
        }

        /**
         * Set repository description.
         *
         * @param repositoryDescription repository description.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setRepositoryDescription(String repositoryDescription) {
            this.repositoryDescription = repositoryDescription;
            return this;
        }

        /**
         * Set repository size.
         *
         * @param repositorySize repository size.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setRepositorySize(int repositorySize) {
            this.repositorySize = repositorySize;
            return this;
        }

        /**
         * Set repository contributors count.
         *
         * @param repositoryContributors contributors count.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setRepositoryContributors(int repositoryContributors) {
            this.repositoryContributors = repositoryContributors;
            return this;
        }

        /**
         * Set repository watchers count.
         *
         * @param repositoryWatchers watchers count.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setRepositoryWatchers(int repositoryWatchers) {
            this.repositoryWatchers = repositoryWatchers;
            return this;
        }

        /**
         * Set repository forks count.
         *
         * @param repositoryForks forks count.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setRepositoryForks(int repositoryForks) {
            this.repositoryForks = repositoryForks;
            return this;
        }

        /**
         * Set repository created at date.
         *
         * @param repositoryCreatedAt created at date.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setRepositoryCreatedAt(Date repositoryCreatedAt) {
            this.repositoryCreatedAt = repositoryCreatedAt;
            return this;
        }

        /**
         * Set repository last updated at date.
         *
         * @param repositoryLastPushedAt last updated at date.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setRepositoryLastPushedAt(Date repositoryLastPushedAt) {
            this.repositoryLastPushedAt = repositoryLastPushedAt;
            return this;
        }

        /**
         * Set repository development dates count.
         *
         * @param developmentDays development dates count.
         * @return this builder, to allow method chaining.
         */
        public OutputDataBuilder setDevelopmentDays(long developmentDays) {
            this.developmentDays = developmentDays;
            return this;
        }

        /**
         * Constructs an OutputData with the values declared by this OutputData.OutputDataBuilder
         * 
         * @return the new OutputData.
         */
        public OutputData build() {
            return new OutputData(this);
        }
    }
}
