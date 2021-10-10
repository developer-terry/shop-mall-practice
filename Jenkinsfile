pipeline {
  agent {
    node {
      label 'maven'
    }
  }

  parameters {
      string(name:'PROJECT_VERSION',defaultValue: 'v0.0Beta',description:'')
      string(name:'PROJECT_NAME',defaultValue: '',description:'')
  }

  environment {
      DOCKER_CREDENTIAL_ID = 'dockerhub-id'
      GITHUB_CREDENTIAL_ID = 'github-id'
      KUBECONFIG_CREDENTIAL_ID = 'demo-kubeconfig'
      REGISTRY = 'docker.io'
      DOCKERHUB_NAMESPACE = 'developterrylee'
      GITHUB_ACCOUNT = 'developer-terry'
      SONAR_CREDENTIAL_ID = 'sonar-qube'
  }

  stages {
    stage('拉取代碼') {
      steps {
        git(credentialsId: 'github-id', url: 'https://github.com/developer-terry/shop-mall-practice.git', branch: 'master', changelog: true, poll: false)
        sh 'echo 正在構建 $PROJECT_NAME 版本號：$PROJECT_VERSION'
      }
    }

    stage('sonarqube analysis') {
      steps {
        container ('maven') {
          withCredentials([string(credentialsId: "$SONAR_CREDENTIAL_ID", variable: 'SONAR_TOKEN')]) {
            withSonarQubeEvent('sonar') {
              sh "mvn sonar:sonar -gs `pwd`/mvn-settings.xml -Dsonar.branch=$BRANCH_NAME -Dsonar.login=$SONAR_TOKEN"
            }
          }
        }
        timeout(time: 1, unit: 'HOURS') {
          waitForQualityGate abortPipeline: true
        }
      }
    }
  }
}