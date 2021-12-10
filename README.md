# STRAIT
A tool for software reliability analysis using GitHub repositories, developed as part of my [**Bachelor's thesis**](https://is.muni.cz/th/a2htp/) and extended in [**Master's thesis**](TODO). It was proposed in paper http://dx.doi.org/10.1109/MSR.2019.00025 on *2021 IEEE/ACM 18th International Conference on Mining Software Repositories (MSR)*


A practical part of the thesis will consist of a tool that would allow automated reliability analysis of the projects hosted on GitHub using the selected Software Reliability Growth Models (SRGMs). The input data for the model will be collected from the issue tracker of the GitHub projects.

Tool Requirements
* Java, version 8+: [https://www.java.com/en/](https://www.java.com/en/)
* R Project, version 3.5.0+: [https://cloud.r-project.org/](https://cloud.r-project.org/)
* Apache Derby DB, version 10.14.2.0+: [https://db.apache.org/derby](https://db.apache.org/derby/papers/DerbyTut/install_software.html#derby)
* Define properties in *git_hub_authentication_file.properties*

# Installation for MS Windows

1. If Java is not installed, run the Java installer
2. Append Java bin directory to the environment PATH variable (e.g., C:\Program FilesJava\jdk1.8.0\_171\bin)
3. If R Project is not installed, run the installer
4. Run the R.exe and in the console, install the rJava packages: 
    * install.packages("rJava")
    * install.packages("nls2")
    * install.packages("broom")
5. Set the environment variables for R Project:
    *  R_HOME=Path-to-R-install-directory (e.g., R_HOME=C:\Program Files\R-3.5.)
    *  path=R_HOME\bin\x64
    *  path=R_HOME\library\rJava\libs\x64
    *  path=R_HOME\library\rJava\jri\x64
6. Make sure Apache Derby client server is running or run - *startNetworkServer.bat*

If 32-bit operating system is used the *\x64* part should be replaced with *\i386*.

# Installation for Unix

The Unix installation assumes that the Aptitude package
manager is available. The following commands should be
executed via terminal:

1. sudo apt-get install default-jdk
2. sudo apt install dirmngr apt-transport-https ca-certificates software-properties-common gnupg2
3. sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-key 'E19F5F87128899B192B1A2C2AD5F960A256A04AF'
4. sudo add-apt-repository 'deb https://cloud.r-project.org/bin/linux/debian stretch-cran35/'
5. sudo apt update
6. sudo apt install r-base
7. sudo -i R
    * install.packages("rJava")
    * install.packages("nls2")
    * install.packages("broom")
8. Set the R_HOME variable
9. Make sure Apache Derby client server is running or run - *startNetworkServer*

# Usage

The tool can be executed from command-line by running:

```java -jar strait.jar [OPTIONS]```

An overview of command-line options is in Table - options. The tool also prints a
list of all options if no argument is provided. The help can be accessed by running:

```java -jar strait.jar --help```

A simple execution of the tool to evaluate the testify
project hosted at Github may look like:

```java -jar strait.jar -url https://github.com/stretchr/testify -ns testify -e -fde -fc -fdu -ft 2018-01-01T00:00:00 2021-01-01T00:00:00```

> With the *-url* option, it specifies the location of the project. The option *-ns* specify the name of the
snapshot for storing the gathered issues. At the second run, the
snapshot name with *-sn* can be provided instead of the *-url* - the local data stored in the database will be used for the analysis. The option *-e* starts the execution of the SRGM analysis. No specific models are selected, so all of the available SRGMs will be applied. The *-fde* will filter only defects from issue reports. With the option *-fc*, closed issues are only concidered. The *-fdu* option filters out duplicated issues. Furthermore, with *-ft* it limits the time period for which issue reports will be considered.

# Table - options
| Short option | Long option | Arguments |
| :---: | :---: | :---: |
| -h | --help | - |
| -url | - | [Repository URL] |
| -asl | --allSnapshotsList | - |
| -sn | --snapshotName | - |
| -cf | - |  [Path to config file]|
| -sl | --snapshotsList | - |
| -s | --save | [Data format] |
| -e | --evaluate | [Output file name] |
| -p | --predict | [Number of time units for prediction] |
| -ns | --newSnapshot | [Name of the new snapshot] |
| -fl | --filterLabel | [Label names] |
| -fc | --filterClosed | - |
| -ft | --filterTime | [From] [To] |
| -fde | --filterDefects | - |
| -fdu | --filterDuplications | - |
| -ms | --models | [Models] |
| -pt | --periodOfTesting | [Testing period time unit] |
| -tb | --timBetweenIssues | [Time unit for TBF] |
| -gm | --graphMultiple | - |
| -so | --solver | [Solver] |
| -out | - | [Output type] |
