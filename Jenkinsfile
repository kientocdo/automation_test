pipeline {
	agent any
    environment {
		DOCKER_IMAGE = 'my-app:auto'
        DOCKER_REGISTRY = 'my-docker-registry'
        DOCKER_CREDENTIALS = credentials('docker-credentials-id')
    }
    stages {
		stage('Checkout') {
			steps {
				git 'https://github.com/kientocdo/automation_test.git'
            }
        }
        stage('Build Docker Image') {
			steps {
				script {
					docker.build(DOCKER_IMAGE)
                }
            }
        }
        stage('Push to Docker Registry') {
			steps {
				script {
					docker.withRegistry('https://${DOCKER_REGISTRY}', DOCKER_CREDENTIALS) {
						docker.image(DOCKER_IMAGE).push()
                    }
                }
            }
        }
        stage('Deploy') {
			steps {
				script {
					sh 'docker-compose up -d'  // Example deploy command using Docker Compose
                }
            }
        }
    }
    post {
		success {
			echo 'Deployment successful!'
        }
        failure {
			echo 'Deployment failed!'
        }
    }
}
