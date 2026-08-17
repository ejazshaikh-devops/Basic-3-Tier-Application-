pipeline {
    agent any

    tools {
        jdk 'jdk'
        maven 'Maven3'      
        nodejs 'Node20'   
    }

    environment {
        PATH = "/usr/local/bin:${env.PATH}"
        DOCKERHUB_USER  = 'your-dockerhub-username'
        BACKEND_IMAGE   = "${DOCKERHUB_USER}/attendance-backend"
        FRONTEND_IMAGE  = "${DOCKERHUB_USER}/attendance-frontend"
        IMAGE_TAG       = "${BUILD_NUMBER}"
    }

    stages {

        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/your-username/Student-Attendance-v1.git'
            }
        }

        stage('Build Backend') {
            steps {
                dir('AttendanceApp/backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('AttendanceApp/frontend/attendance-frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                dir('AttendanceApp/backend') {
                    sh "docker build -t ${BACKEND_IMAGE}:${IMAGE_TAG} ."
                }
                dir('AttendanceApp/frontend/attendance-frontend') {
                    sh "docker build -t ${FRONTEND_IMAGE}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh "docker push ${BACKEND_IMAGE}:${IMAGE_TAG}"
                    sh "docker push ${FRONTEND_IMAGE}:${IMAGE_TAG}"
                }
            }
        }

        stage('Deploy Containers') {
            steps {
                sh "docker pull ${BACKEND_IMAGE}:${IMAGE_TAG}"
                sh "docker pull ${FRONTEND_IMAGE}:${IMAGE_TAG}"

                sh 'docker rm -f attendance-backend attendance-frontend attendance-db || true'
                sh 'docker network create attendance-net || true'

                // DB tier - local MariaDB container
                sh '''
                    docker run -d --name attendance-db \
                      --network attendance-net \
                      -e MYSQL_ROOT_PASSWORD=rootpass \
                      -e MYSQL_DATABASE=attendance \
                      -e MYSQL_USER=attendance_user \
                      -e MYSQL_PASSWORD=attendance_pass \
                      -p 3306:3306 \
                      mariadb:10.11
                '''
                sh 'sleep 20' //wait time for db

                sh """
                    docker run -d --name attendance-backend \
                      --network attendance-net \
                      -e DB_HOST=attendance-db \
                      -e DB_PORT=3306 \
                      -e DB_NAME=attendance \
                      -e DB_USER=attendance_user \
                      -e DB_PASS=attendance_pass \
                      -p 8080:8080 \
                      ${BACKEND_IMAGE}:${IMAGE_TAG}
                """

                sh "docker run -d --name attendance-frontend --network attendance-net -p 80:80 ${FRONTEND_IMAGE}:${IMAGE_TAG}"
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed. Check console output above.'
        }
    }
}
