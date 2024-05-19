pipeline {
  agent any
  options {
    buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '3')
  }
  environment {
    TOMCAT_SERVER="172.31.72.160"
    ROOT_WAR_LOCATION="C:\\path\\to\\tomcat\\webapps"
    LOCAL_WAR_DIR="target"
    WAR_FILE="cookingRecipesSpringRest-1.0-SNAPSHOT.war"
    MAVEN_CMD="C:\\apache-maven-3.8.6\\bin\\mvn.cmd"
    MAVEN_OPTS="-Dmaven.repo.local=/root/.m2/repository"
  }
  stages {
    stage('verify tooling') {
      steps {
        bat 'java -version'
      }
    }
    stage('download') {
      steps {
        bat '%MAVEN_CMD% clean'
      }
    }
    stage('compile') {
      steps {
        bat '%MAVEN_CMD% compile'
      }
    }
    stage('test') {
      steps {
        bat '%MAVEN_CMD% test'
      }
    }
    stage('package') {
      steps {
        bat '%MAVEN_CMD% package'
      }
    }
    stage('copy the war file to the Tomcat server') {
      steps {
        bat '''
          psexec \\\\$TOMCAT_SERVER -u $TOMCAT_CREDS_USR -p $TOMCAT_CREDS_PSWD "C:\\path\\to\\tomcat\\bin\\catalina.bat stop"
          psexec \\\\$TOMCAT_SERVER -u $TOMCAT_CREDS_USR -p $TOMCAT_CREDS_PSWD "cmd /c del $ROOT_WAR_LOCATION\\ROOT /Q"
          psexec \\\\$TOMCAT_SERVER -u $TOMCAT_CREDS_USR -p $TOMCAT_CREDS_PSWD "cmd /c del $ROOT_WAR_LOCATION\\ROOT.war /Q"
          copy $LOCAL_WAR_DIR\\$WAR_FILE \\\\$TOMCAT_SERVER\\$ROOT_WAR_LOCATION\\ROOT.war
          psexec \\\\$TOMCAT_SERVER -u $TOMCAT_CREDS_USR -p $TOMCAT_CREDS_PSWD "C:\\path\\to\\tomcat\\bin\\catalina.bat start"
        '''
      }
    }
  }
}
