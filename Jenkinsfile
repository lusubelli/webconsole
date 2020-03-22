
pipeline {
    agent any
    stages  {
        stage('Clone repository') {
            steps {
                checkout scm
            }
        }
        stage('Build Docker Image') {
            steps {
                sh 'chmod +x accounting.sh'
                sh 'accounting.sh'
            }
        }
    }
}
