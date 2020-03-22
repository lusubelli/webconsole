
pipeline {
    stages  {
        stage('Clone repository') {
            steps {
                checkout scm
            }
        }
        stage('Build Docker Image') {
            steps {
                sh './accounting.sh'
            }
        }
    }
}
