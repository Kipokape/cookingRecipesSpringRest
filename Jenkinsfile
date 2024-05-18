pipeline {
  agent any
  options {
    buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '3')
  }    
  environment {
    TOMCAT_SERVER="172.31.72.160"
    ROOT_WAR_LOCATION="/opt/tomcat/webapps"
    LOCAL_WAR_DIR="build/dist"
    WAR_FILE="cookingRecipesSpringRest-1.0-SNAPSHOT.war"
  }
  stages {
    stage('verify tooling') {
      steps {
        bat '''
          java -version
          ./bld version
        '''
      }
    }
    stage('download') {
      steps {
        bat './bld download purge'
      }
    }
    stage('compile') {
      steps {
        bat './bld clean compile'
      }
    }
    stage('precompile') {
      steps {
        bat './bld precompile'
      }
    }
    stage('test') {
      steps {
        bat './bld test'
      }
    }
    stage('war') {
      steps {
        bat './bld war'
      }
    }  
    stage('copy the war file to the Tomcat server') {
      steps {
        bat '''
          ssh -i $TOMCAT_CREDS_USR@$TOMCAT_SERVER "/home/pi/tools/apache-tomcat-10.1.18/bin/catalina.bat stop"
          ssh -i $TOMCAT_CREDS_USR@$TOMCAT_SERVER "rm -rf $ROOT_WAR_LOCATION/ROOT; rm -f $ROOT_WAR_LOCATION/ROOT.war"
          scp -i $LOCAL_WAR_DIR/$WAR_FILE $TOMCAT_CREDS_USR@$TOMCAT_SERVER:$ROOT_WAR_LOCATION/ROOT.war
          ssh -i $TOMCAT_CREDS_USR@$TOMCAT_SERVER "chown $TOMCAT_CREDS_USR:$TOMCAT_CREDS_USR $ROOT_WAR_LOCATION/ROOT.war"
          ssh -i $TOMCAT_CREDS_USR@$TOMCAT_SERVER "/home/pi/tools/apache-tomcat-10.1.18/bin/catalina.bat start"
        '''
      }
    }
  }
}
