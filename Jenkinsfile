pipeline {
  agent any
  options {
    buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '3')
  }    
  environment {
    TOMCAT_SERVER="172.31.72.160"
    ROOT_WAR_LOCATION="C:\\path\\to\\tomcat\\webapps"
    LOCAL_WAR_DIR="build\\dist"
    WAR_FILE="cookingRecipesSpringRest-1.0-SNAPSHOT.war"
  }
  stages {
    stage('verify tooling') {
      steps {
        bat '''
          java -version
          bld.bat version
        '''
      }
    }
    stage('download') {
      steps {
        bat 'bld.bat download purge'
      }
    }
    stage('compile') {
      steps {
        bat 'bld.bat clean compile'
      }
    }
    stage('precompile') {
      steps {
        bat 'bld.bat precompile'
      }
    }
    stage('test') {
      steps {
        bat 'bld.bat test'
      }
    }
    stage('war') {
      steps {
        bat 'bld.bat war'
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
