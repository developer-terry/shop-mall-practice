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
        container('maven') {
          sh "mvn clean install -Dmaven.test.skip=true -gs `pwd`/maven-settings.xml"
        }
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
    stage ('構建鏡像-推送鏡像') {
      steps {
        container ('maven') {
          sh 'mvn -Dmaven.test.skip=true -gs `pwd`/maven-settings.xml clean package'
          sh 'echo `pwd` 顯示當前目錄'
          sh 'cd $PROJECT_NAME && docker build -f Dockerfile -t $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER .'
          withCredentials([usernamePassword(passwordVariable : 'DOCKER_PASSWORD' ,usernameVariable : 'DOCKER_USERNAME' ,credentialsId : "$DOCKER_CREDENTIAL_ID" ,)]) {
              sh 'echo "$DOCKER_PASSWORD" | docker login $REGISTRY -u "$DOCKER_USERNAME" --password-stdin'
              sh 'docker tag  $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:latest'
              sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:latest'
          }
        }
      }
    }
    stage('部署到k8s') {
      when{
        branch 'master'
      }
      steps {
        input(id: 'deploy-to-dev-$PROJECT_NAME', message: '是否將 $PROJECT_NAME 部署到集群中?')
        kubernetesDeploy(configs: '$PROJECT_NAME/deploy/**', enableConfigSubstitution: true, kubeconfigId: "$KUBECONFIG_CREDENTIAL_ID")
      }
    }
    stage('push with tag') {
      when{
        expression{
          return params.PROJECT_VERSION =~ /v.*/
        }
      }
      steps {
        container ('maven') {
          input(id: 'release-image-with-tag', message: '是否發布當前版本鏡像嗎?')
            withCredentials([usernamePassword(credentialsId: "$GITHUB_CREDENTIAL_ID", passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
              sh 'git config --global user.email "p134030772@gmail.com" '
              sh 'git config --global user.name "terrylee" '
              sh 'git tag -a $PROJECT_VERSION -m "$PROJECT_VERSION" '
              sh 'git push http://$GIT_USERNAME:$GIT_PASSWORD@github.com/$GITHUB_ACCOUNT/shop-mall-practice.git --tags --ipv4'
            }
            sh 'docker tag  $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:$PROJECT_NAME '
          sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:$PROJECT_VERSION '
        }
      }
    }
  }
  parameters {
      string(name:'PROJECT_VERSION',defaultValue: 'v0.0Beta',description:'')
      string(name:'PROJECT_NAME',defaultValue: '',description:'')
  }
}