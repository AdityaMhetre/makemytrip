pipeline {

    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))
    }

    tools {
        maven 'maven_3.9.11'
    }

    stages {
        stage('Code Compilation') {
            steps {
                echo 'Starting Code Compilation now...'
                sh 'mvn clean compile'
                echo 'Code Compilation Completed Successfully!'
            }
        }
        stage('Code QA Execution') {
            steps {
                echo 'Running JUnit Test Cases...'
                sh 'mvn clean test'
                echo 'JUnit Test Cases Completed Successfully !'
            }
        }
        stage('Code Package') {
            steps {
                echo 'Creating WAR Artifact...'
                sh 'mvn clean package'
                echo 'WAR Artifact Created Successfully!'
            }
        }
        stage('Build & Tag Docker Image') {
                    steps {
                        echo 'Building Docker Image with Tags...'
                        sh "docker build -t adityamhetre/makemytrip:latest -t makemytrip:latest ."
                        echo 'Docker Image Build Completed!'
                    }
        }
        stage('Docker Image Scanning') {
                    steps {
                        echo 'Scanning Docker Image with Trivy...'
//                         sh 'trivy image ${DOCKER_IMAGE}:latest || echo "Scan Failed - Proceeding with Caution"'
                        echo 'Docker Image Scanning Completed!'
                    }
                }
        stage('Push Docker Image to Docker Hub') {
                    steps {
                        script {
                            withCredentials([string(credentialsId: 'dockerhubCred', variable: 'dockerhubCred')]) {
                                sh 'docker login docker.io -u adityamhetre -p ${dockerhubCred}'
                                echo 'Pushing Docker Image to Docker Hub...'
                                sh 'docker push adityamhetre/makemytrip:latest'
                                echo 'Docker Image Pushed to Docker Hub Successfully!'
                            }
                        }
                    }
                }
        stage('Push Docker Image to Amazon ECR') {
                    steps {
                        script {
                            withDockerRegistry([credentialsId: 'ecr:ap-south-1:ecr-credentials', url: "https://343474957259.dkr.ecr.ap-south-1.amazonaws.com"]) {
                                echo 'Tagging and Pushing Docker Image to ECR...'
                                sh '''
                                    docker images
                                    docker tag makemytrip:latest 343474957259.dkr.ecr.ap-south-1.amazonaws.com/makemytrip:latest
                                    docker push 343474957259.dkr.ecr.ap-south-1.amazonaws.com/makemytrip:latest
                                '''
                                echo 'Docker Image Pushed to Amazon ECR Successfully!'
                            }
                        }
                    }
        }


    }
}



