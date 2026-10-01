pipeline {
    agent any

    environment {
        PATH = "/opt/homebrew/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"

        SPRING_DATASOURCE_PASSWORD =
            credentials('civicpulse-db-password')

        SPRING_MAIL_USERNAME =
            credentials('civicpulse-mail-username')

        SPRING_MAIL_PASSWORD =
            credentials('civicpulse-mail-password')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh '''
                    echo "Building NotificationService..."

                    ./mvnw clean compile
                '''
            }
        }

        stage('Test') {
            steps {
                sh '''
                    echo "Running NotificationService tests..."

                    ./mvnw test
                '''
            }
        }

        stage('Secret Scan') {
            steps {
                sh '''
                    echo "Running Trivy secret scan..."

                    trivy fs \
                      --scanners secret \
                      --skip-dirs target \
                      --exit-code 1 \
                      .
                '''
            }
        }

        stage('Verify Docker') {
            steps {
                sh '''
                    echo "Docker location:"
                    which docker

                    echo "Docker version:"
                    docker --version
                '''
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    echo "Building NotificationService Docker image..."

                    docker build \
                      -t civicpulse-notification-service:${BUILD_NUMBER} .
                '''
            }
        }

        stage('Docker Image Scan') {
            steps {
                sh '''
                    echo "Scanning NotificationService Docker image..."

                    trivy image \
                    --timeout 15m \
                    --severity HIGH,CRITICAL \
                    civicpulse-notification-service:${BUILD_NUMBER}
                '''
            }
        }

        stage('Docker Push') {
            steps {

                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-civicpulse',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {

                    sh '''
                        echo "Logging in to Docker Hub..."

                        echo "$DOCKER_PASSWORD" | docker login \
                          -u "$DOCKER_USERNAME" \
                          --password-stdin

                        echo "Tagging Docker image..."

                        docker tag \
                          civicpulse-notification-service:${BUILD_NUMBER} \
                          ${DOCKER_USERNAME}/civicpulse-notification-service:${BUILD_NUMBER}

                        echo "Pushing Docker image..."

                        docker push \
                          ${DOCKER_USERNAME}/civicpulse-notification-service:${BUILD_NUMBER}

                        echo "Logging out from Docker Hub..."

                        docker logout
                    '''
                }
            }
        }
    }

    post {

        success {
            echo '''
            ==========================================
            NotificationService CI SUCCESS
            ==========================================
            '''
        }

        failure {
            echo '''
            ==========================================
            NotificationService CI FAILED
            ==========================================
            Check the failed stage and Jenkins logs.
            ==========================================
            '''
        }

        always {
            echo "Build Number: ${BUILD_NUMBER}"
        }
    }
}