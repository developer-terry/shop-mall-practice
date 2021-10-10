pipeline {
  agent {
    node {
      label 'maven'
    }
  }
  environment {
      DOCKER_CREDENTIAL_ID = 'dockerhub-id'
      GITHUB_CREDENTIAL_ID = 'github-id'
      KUBECONFIG_CREDENTIAL_ID = 'demo-kubeconfig'
      REGISTRY = 'docker.io'
      DOCKERHUB_NAMESPACE = 'developterrylee'
      GITHUB_ACCOUNT = 'developer-terry'
      SONAR_CREDENTIAL_ID = 'sonar-qube'
      BRANCH_NAME = 'master'
  }
  stages {
    stage('拉取代碼') {
      steps {
        git(credentialsId: 'github-id', url: 'https://github.com/developer-terry/shop-mall-practice.git', branch: 'master', changelog: true, poll: false)
        sh 'echo $PROJECT_VERSION'
        sh 'echo $PROJECT_NAME'
      }
    }
    stage('sonarqube analysis') {
      steps {
        container ('maven') {
          withCredentials([string(credentialsId: "$SONAR_CREDENTIAL_ID", variable: 'SONAR_TOKEN')]) {
            withSonarQubeEnv('sonar') {
              sh "mvn sonar:sonar -gs `pwd`/maven-settings.xml -Dsonar.branch=$BRANCH_NAME -Dsonar.login=$SONAR_TOKEN"
            }
          }
        }
        timeout(time: 1, unit: 'HOURS') {
          waitForQualityGate abortPipeline: true
        }
      }
    }
  }
  parameters {
      string(name:'PROJECT_VERSION',defaultValue: 'v0.0Beta',description:'')
      string(name:'PROJECT_NAME',defaultValue: '',description:'')
  }
}