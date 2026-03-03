def FULL_IMAGE_NAME = ""
pipeline {
    agent any
    environment {
        REPO_NAME = "ranjithasu"
        IMAGE_NAME = "java-mysql-app"
    }
    stages {
        stage('Build image') {
            steps {
                script {
                    echo "Building image"
                    env.TAG = sh(script: "./gradlew -q getVersion", returnStdout: true).trim()
                    FULL_IMAGE_NAME = REPO_NAME + "/" + IMAGE_NAME + ":" + env.TAG
                    sh "docker build --build-arg BUILDKIT_INLINE_CACHE=1 --cache-from ${FULL_IMAGE_NAME} --tag ${FULL_IMAGE_NAME} ."
                }
            }
        }
        stage('Push Image') {
            environment {
                DOCKER_CREDENTIALS = credentials('DOCKERHUB_CREDS')
            }
            steps {
                script {
                    echo "Push image to private repository"
                    sh 'echo "$DOCKER_CREDENTIALS_PSW" | docker login -u "$DOCKER_CREDENTIALS_USR" --password-stdin'
                    sh "docker push $FULL_IMAGE_NAME"
                }
            }
        }
        stage('Deploy') {
            steps{
                script {
                    withAWS(
                            credentials: 'jenkins-user-access-keys',
                            region: 'us-west-2',
                            role: 'arn:aws:iam::460469850064:role/jenkins-user-deploy-role'
                    ) {
                        withCredentials([file(credentialsId: 'DEV_EKS_KUBECONFIG', variable: 'KUBECONFIG')]) {
                            sh "kubectl apply -f kubernetes/mysql/secret.yaml"
                            sh "kubectl apply -f kubernetes/mysql/config.yaml"
                            sh "kubectl apply -f kubernetes/mysql/deployment.yaml"
                            sh "kubectl apply -f kubernetes/mysql/service.yaml"
                            sh "envsubst < kubernetes/app/deployment.yaml | kubectl apply -f -"
                            sh "envsubst < kubernetes/app/service.yaml | kubectl apply -f -"
                        }
                    }
                }
            }
        }
    }
}