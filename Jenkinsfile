
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
                sh 'docker-compose -f accounting-infrastructure.yml rm -f'
                sh 'docker-compose -f accounting-infrastructure.yml up &'
                sh 'docker-compose -f accounting-infrastructure.yml logs --no-color >& logs.txt'
            }
        }
    }
}
